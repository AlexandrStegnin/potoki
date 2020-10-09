package com.art.service;

import com.art.config.exception.EntityNotFoundException;
import com.art.model.*;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.dto.AccountTxDTO;
import com.art.model.supporting.enums.CashType;
import com.art.model.supporting.enums.OperationType;
import com.art.model.supporting.enums.OwnerType;
import com.art.model.supporting.filters.AccountTransactionFilter;
import com.art.repository.AccountTransactionRepository;
import com.art.specifications.AccountTransactionSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Service
public class AccountTransactionService {

    private final AccountTransactionSpecification transactionSpecification;

    private final AccountTransactionRepository accountTransactionRepository;

    private final AccountService accountService;

    private final FacilityService facilityService;

    private final UnderFacilityService underFacilityService;

    private final NewCashDetailService newCashDetailService;

    private final MoneyService moneyService;

    public AccountTransactionService(AccountTransactionSpecification transactionSpecification,
                                     AccountTransactionRepository accountTransactionRepository, AccountService accountService,
                                     FacilityService facilityService, UnderFacilityService underFacilityService,
                                     NewCashDetailService newCashDetailService, MoneyService moneyService) {
        this.transactionSpecification = transactionSpecification;
        this.accountTransactionRepository = accountTransactionRepository;
        this.accountService = accountService;
        this.facilityService = facilityService;
        this.underFacilityService = underFacilityService;
        this.newCashDetailService = newCashDetailService;
        this.moneyService = moneyService;
    }

    public AccountTransaction create(AccountTransaction transaction) {
        return accountTransactionRepository.save(transaction);
    }

    public AccountTransaction update(AccountTransaction transaction) {
        if (transaction.getId() == null) {
            throw new RuntimeException("Не задан id транзакции");
        }
        AccountTransaction tx = accountTransactionRepository.findOne(transaction.getId());
        if (tx == null) {
            throw new EntityNotFoundException("Не найдена транзакция");
        }
        return accountTransactionRepository.save(transaction);
    }

    public void delete(AccountTransaction transaction) {
        accountTransactionRepository.delete(transaction);
    }

    public AccountTransaction findById(Long id) {
        AccountTransaction tx = accountTransactionRepository.findOne(id);
        if (tx == null) {
            throw new EntityNotFoundException("Не найдена транзакция");
        }
        return tx;
    }

    public Page<AccountTransaction> findAll(AccountTransactionFilter filter, Pageable pageable) {
        if (filter.getPageSize() == 0) pageable = new PageRequest(filter.getPageNumber(), filter.getTotal() + 1);
        return accountTransactionRepository.findAll(
                transactionSpecification.getFilter(filter),
                pageable
        );
    }

    public ApiResponse reinvest(AccountTxDTO dto) {
        if (dto.getId() == null) {
            return new ApiResponse("Не указан id суммы", HttpStatus.PRECONDITION_FAILED.value());
        }
        if (dto.getFacilityId() == null) {
            return new ApiResponse("Не указан id объекта", HttpStatus.PRECONDITION_FAILED.value());
        }
        if (dto.getUnderFacilityId() == null) {
            return new ApiResponse("Не указан id подобъекта", HttpStatus.PRECONDITION_FAILED.value());
        }
        AccountTransaction transaction = findById(dto.getId());
        if (transaction == null) {
            return new ApiResponse("Транзакция не найдена", HttpStatus.NOT_FOUND.value());
        }
        Account facilityAccount = accountService.findByOwnerId(dto.getFacilityId(), OwnerType.FACILITY);
        if (facilityAccount == null) {
            return new ApiResponse("Не найден счёт объекта для реинвестирования");
        }
        Facility facility = facilityService.findById(dto.getFacilityId());
        if (facility == null) {
            return new ApiResponse("Не найден объект для реинвестирования");
        }
        UnderFacility underFacility = underFacilityService.findById(dto.getUnderFacilityId());
        if (underFacility == null) {
            return new ApiResponse("Не найден подобъект для реинвестирования");
        }
        NewCashDetail newCashDetail = newCashDetailService.findByName("Реинвестирование с продажи (прибыль)");
        if (newCashDetail == null) {
            throw new EntityNotFoundException("Не найдены детали новых денег [Реинвестирование с продажи (прибыль)]");
        }
        try {
            cashingFromAccount(transaction, facilityAccount, facility, underFacility, dto.getDateReinvest(), dto.getShareType(), newCashDetail);
        } catch (Exception e) {
            return new ApiResponse(e.getLocalizedMessage());
        }
        AccountTransaction newTransaction = new AccountTransaction(facilityAccount);
        newTransaction.setPayer(transaction.getOwner());
        newTransaction.setRecipient(facilityAccount);
        newTransaction.setOperationType(OperationType.DEBIT);
        newTransaction.setCashType(CashType.INVESTOR_CASH);
        Money money = createMoney(transaction, facility, underFacility, newCashDetail, dto.getDateReinvest(), dto.getShareType());
        newTransaction.setMoney(money);
        accountTransactionRepository.save(newTransaction);
        return new ApiResponse("Реинвестирование прошло успешно");
    }

    private void cashingFromAccount(AccountTransaction oldTransaction, Account recipient, Facility facility,
                                    UnderFacility underFacility, Date dateReinvest, String shareType, NewCashDetail newCashDetail) {
        AccountTransaction newTransaction = new AccountTransaction(oldTransaction.getOwner());
        newTransaction.setPayer(oldTransaction.getOwner());
        newTransaction.setRecipient(recipient);
        newTransaction.setOperationType(OperationType.CREDIT);
        newTransaction.setCashType(CashType.INVESTOR_CASH);
        Money money = createMoney(oldTransaction, facility, underFacility, newCashDetail, dateReinvest, shareType);
        newTransaction.setMoney(money);
        newTransaction.setBlocked(true);
        accountTransactionRepository.save(newTransaction);
    }

    private Money createMoney(AccountTransaction transaction, Facility facility, UnderFacility underFacility,
                              NewCashDetail newCashDetail, Date dateReinvest, String shareType) {
        Money money;
        switch (transaction.getCashType()) {
            case SALE_CASH:
                money = new Money(transaction.getSalePayment(), facility, underFacility, newCashDetail, dateReinvest, shareType);
                break;
            default:
                throw new RuntimeException("Не реализовано");
        }
        return moneyService.createNew(money);
    }

    /**
     * Удалить данные о транзакциях, если есть связанные данные с выплатами (продажа)
     *
     * @param salePaymentId id выплаты (продажа)
     */
    public void deleteBySalePaymentId(Long salePaymentId) {
        AccountTransactionFilter filter = new AccountTransactionFilter();
        filter.setSalePaymentId(salePaymentId);
        List<AccountTransaction> transactions = accountTransactionRepository.findAll(transactionSpecification.getFilter(filter));
        accountTransactionRepository.delete(transactions);
    }
}
