package com.art.service;

import com.art.config.exception.EntityNotFoundException;
import com.art.model.*;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.dto.AccountDTO;
import com.art.model.supporting.dto.AccountSummaryDTO;
import com.art.model.supporting.dto.AccountTransactionDTO;
import com.art.model.supporting.dto.AccountTxDTO;
import com.art.model.supporting.enums.CashType;
import com.art.model.supporting.enums.OperationType;
import com.art.model.supporting.enums.OwnerType;
import com.art.model.supporting.enums.ShareType;
import com.art.model.supporting.filters.AccountTransactionFilter;
import com.art.repository.AccountTransactionRepository;
import com.art.repository.MoneyRepository;
import com.art.repository.RentPaymentRepository;
import com.art.repository.SalePaymentRepository;
import com.art.specifications.AccountTransactionSpecification;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Alexandr Stegnin
 */

@Service
public class AccountTransactionService {

    private final AccountTransactionSpecification transactionSpecification;

    private final AccountTransactionRepository accountTransactionRepository;

    private final SalePaymentRepository salePaymentRepository;

    private final FacilityService facilityService;

    private final UnderFacilityService underFacilityService;

    private final AccountService accountService;

    private final UserService userService;

    private final MoneyRepository moneyRepository;

    private final NewCashDetailService newCashDetailService;

    private final RentPaymentRepository rentPaymentRepository;

    public AccountTransactionService(AccountTransactionSpecification transactionSpecification,
                                     AccountTransactionRepository accountTransactionRepository,
                                     SalePaymentRepository salePaymentRepository, FacilityService facilityService,
                                     UnderFacilityService underFacilityService, AccountService accountService,
                                     UserService userService, MoneyRepository moneyRepository,
                                     NewCashDetailService newCashDetailService, RentPaymentRepository rentPaymentRepository) {
        this.transactionSpecification = transactionSpecification;
        this.accountTransactionRepository = accountTransactionRepository;
        this.salePaymentRepository = salePaymentRepository;
        this.facilityService = facilityService;
        this.underFacilityService = underFacilityService;
        this.accountService = accountService;
        this.userService = userService;
        this.moneyRepository = moneyRepository;
        this.newCashDetailService = newCashDetailService;
        this.rentPaymentRepository = rentPaymentRepository;
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

    /**
     * Удалить списком транзакции
     *
     * @param dto DTO со списком id транзакций
     */
    public void deleteByDTO(AccountTxDTO dto) {
        Set<AccountTransaction> transactions = new HashSet<>();
        dto.getTxIds().forEach(id -> {
            AccountTransaction transaction = findParent(id);
            if (transaction != null) {
                transactions.add(transaction);
            }
        });
        transactions.forEach(transaction -> {
            releaseMonies(new ArrayList<>(transaction.getChild()));
            deleteByParent(transaction);
        });
    }

    /**
     * Найти родительскую транзакцию
     *
     * @param id id
     * @return родительская транзакция
     */
    private AccountTransaction findParent(Long id) {
        AccountTransaction parent = findById(id);
        if (parent == null) {
            return null;
        }
        if (parent.getParent() == null) {
            return parent;
        }
        return findParent(parent.getParent().getId());
    }

    /**
     * Удалить транзакции связанные с родителем
     *
     * @param parent родитель
     */
    private void deleteByParent(AccountTransaction parent) {
        accountTransactionRepository.delete(parent);
    }

    /**
     * Удалить по id
     *
     * @param accTxId id транзакции
     */
    public void deleteById(Long accTxId) {
        AccountTransaction transaction = findById(accTxId);
        if (transaction.getParent() != null) {
            AccountTransaction parent = transaction.getParent();
            Set<AccountTransaction> children = parent.getChild();
            releaseMonies(new ArrayList<>(children));
        }
        delete(transaction);
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

    /**
     * Получить список владельцев счетов для фильтрации
     *
     * @return список владельцев
     */
    public List<String> initOwners() {
        List<String> owners = new ArrayList<>();
        owners.add("Выберите владельца");
        owners.addAll(accountTransactionRepository.getAllOwners());
        return owners;
    }

    /**
     * Получить список владельцев счетов для фильтрации
     *
     * @return список владельцев
     */
    public List<String> initRecipients() {
        List<String> owners = new ArrayList<>();
        owners.add("Выберите отправителя");
        owners.addAll(accountTransactionRepository.getAllRecipients());
        return owners;
    }

    /**
     * Получить список плательщиков (ПОДОБЪЕКТОВ) для фильтрации
     *
     * @return список плательщиков
     */
    public List<String> initPayers() {
        List<String> payers = new ArrayList<>();
        payers.add("Выберите подобъект");
        payers.addAll(accountTransactionRepository.getAllPayers());
        return payers;
    }

    /**
     * Получить список плательщиков (ОБЪЕКТОВ) для фильтрации
     *
     * @return список плательщиков
     */
    public List<String> initParentPayers() {
        List<String> payers = new ArrayList<>();
        payers.add("Выберите объект");
        payers.addAll(accountTransactionRepository.getAllParentPayers());
        return payers;
    }

    /**
     * Получить список видов денег
     *
     * @return список видов денег
     */
    public List<CashType> initCashTypes() {
        return accountTransactionRepository.getAllCashTypes();
    }

    /**
     * Удалить список транзакций
     *
     * @param dto DTO для удаления
     * @return ответ
     */
    @Transactional
    public ApiResponse delete(AccountTxDTO dto) {
        List<AccountTransaction> transactions = new ArrayList<>();
        dto.getTxIds().forEach(id -> transactions.add(findById(id)));
        accountTransactionRepository.delete(transactions);
        deleteSalePayments(transactions);
        deleteRentPayments(transactions);
        releaseMonies(transactions);
        return new ApiResponse("Данные успешно удалены");
    }

    /**
     * Удалить связанные выплаты с продажи
     *
     * @param transactions список транзакций
     */
    private void deleteSalePayments(List<AccountTransaction> transactions) {
        List<SalePayment> salePayments = new ArrayList<>();
        transactions.forEach(tx -> tx.getSalePayments().forEach(salePayment -> {
            if (salePayment != null) {
                salePayments.add(salePayment);
            }
        }));
        salePaymentRepository.delete(salePayments);
    }

    /**
     * Удалить связанные выплаты с аренды
     *
     * @param transactions список транзакций
     */
    private void deleteRentPayments(List<AccountTransaction> transactions) {
        List<RentPayment> rentPayments = new ArrayList<>();
        transactions.forEach(tx -> tx.getRentPayments().forEach(rentPayment -> {
            if (rentPayment != null) {
                rentPayments.add(rentPayment);
            }
        }));
        rentPaymentRepository.delete(rentPayments);
    }

    /**
     * Открыть закрытые суммы, если удаляется связанные транзакции
     *
     * @param transactions список транзакций
     */
    private void releaseMonies(List<AccountTransaction> transactions) {
        List<Money> monies = new ArrayList<>();
        transactions.forEach(tx -> tx.getMonies().forEach(money -> {
            if (money != null) {
                monies.add(money);
            }
        }));
        monies.forEach(money -> {
            money.setDateClosing(null);
            money.setTypeClosing(null);
            money.setIsReinvest(0);
            money.setTransaction(null);
        });
        moneyRepository.save(monies);
    }

    /**
     * Получить список счетов с итоговым балансом
     *
     * @param filter список фильтров
     * @param pageable для постраничного отображения
     * @return список счетов с суммарным балансом
     */
    public Page<AccountDTO> getSummary(AccountTransactionFilter filter, Pageable pageable) {
        if (filter.getPageSize() == 0) pageable = new PageRequest(filter.getPageNumber(), filter.getTotal() + 1);
        String ownerName = filter.getOwner();
        String payerName = filter.getPayer();
        String parentPayer = filter.getParentPayer();

        switch (filter.getFilterType()) {
            case IS_CLEAR:
                return accountTransactionRepository.getSummary(OwnerType.INVESTOR, pageable);
            case BY_OWNER:
                return accountTransactionRepository.fetchSummaryByOwner(OwnerType.INVESTOR, ownerName, pageable);
            case BY_PAYER:
                return accountTransactionRepository.fetchSummaryByPayer(OwnerType.INVESTOR, payerName, pageable);
            case BY_PARENT_PAYER:
                return accountTransactionRepository.fetchSummaryByParentPayer(OwnerType.INVESTOR, parentPayer, pageable);
            case BY_OWNER_AND_PAYER:
                return accountTransactionRepository.fetchSummaryByOwnerAndPayer(OwnerType.INVESTOR, ownerName, payerName, pageable);
            case BY_OWNER_AND_PARENT_PAYER:
                return accountTransactionRepository.fetchSummaryByOwnerAndParentPayer(OwnerType.INVESTOR, ownerName, parentPayer, pageable);
            case BY_PAYER_AND_PARENT_PAYER:
                return accountTransactionRepository.fetchSummaryByPayerAndParentPayer(OwnerType.INVESTOR, payerName, parentPayer, pageable);
            case BY_OWNER_AND_PAYER_AND_PARENT_PAYER:

                break;
        }
        return accountTransactionRepository.getSummary(OwnerType.INVESTOR, pageable);
    }

    /**
     * Получить список владельцев счетов (ИНВЕСТОРОВ) для фильтрации
     *
     * @return список владельцев
     */
    public List<String> initInvestorOwners() {
        List<String> owners = new ArrayList<>();
        owners.add("Выберите владельца");
        owners.addAll(accountTransactionRepository.getOwners(OwnerType.INVESTOR));
        return owners;
    }

    /**
     * Получить список транзакций по определённому счёту
     *
     * @param dto DTO для просмотра информации
     * @return список транзакций по счёту
     */
    public List<AccountTransactionDTO> getDetails(AccountSummaryDTO dto) {
        Long accountId = dto.getAccountId();
        if (accountId == null) {
            throw new RuntimeException("Не указан id счёта");
        }
        AccountTransactionFilter filter = getFilter(dto);
        List<AccountTransaction> transactions = accountTransactionRepository.findAll(transactionSpecification.getDetailsFilter(filter));

        return transactions
                .stream()
                .map(AccountTransactionDTO::new)
                .collect(Collectors.toList());
    }

    private AccountTransactionFilter getFilter(AccountSummaryDTO dto) {
        AccountTransactionFilter filter = new AccountTransactionFilter();
        filter.setOwner(dto.getOwnerName());
        filter.setPayer(dto.getPayerName());
        filter.setParentPayer(dto.getParentPayer());
        filter.setAccountId(dto.getAccountId());
        return filter;
    }

    /**
     * Реинвестировать суммы
     *
     * @param dto DTO для реинвестирования
     * @return ответ об исполнении операции
     */
    public ApiResponse reinvest(AccountTxDTO dto) {
        ApiResponse response = checkDTO(dto);
        if (response != null) {
            return response;
        }
        if (dto.isReinvestAll()) {
            return reinvestAll(dto);
        } else {
            response = checkAvailableCash(dto);
            if (response != null) {
                return response;
            }
            return reinvestPart(dto);
        }
    }

    /**
     * Реинвестировать всю сумму
     *
     * @param dto DTO для реинвестирования
     * @return ответ об исполнении
     */
    private ApiResponse reinvestAll(AccountTxDTO dto) {
        ApiResponse response = new ApiResponse();
        for (Long id : dto.getAccountsIds()) {
            Account owner = accountService.findByOwnerId(id, OwnerType.INVESTOR);
            if (owner == null) {
                prepareErrorResponse(response, "Не найден счёт инвестора");
                return response;
            }
            Account recipient = accountService.findByOwnerId(dto.getUnderFacilityId(), OwnerType.UNDER_FACILITY);
            if (recipient == null) {
                prepareErrorResponse(response, "Не найден счёт подобъекта");
                return response;
            }
            AccountDTO accountDTO = accountTransactionRepository.fetchSummaryByOwner(OwnerType.INVESTOR, owner.getOwnerName());
            dto.setCash(accountDTO.getSummary());
            try {
                AccountTransaction creditTx = createCreditTransaction(owner, recipient, dto);
                createDebitTransaction(creditTx, dto);
            } catch (Exception e) {
                prepareErrorResponse(response, e.getLocalizedMessage());
                return response;
            }
        }
        return new ApiResponse("Реинвестирование прошло успешно");
    }

    /**
     * Реинвестировать указанную часть суммы
     *
     * @param dto DTO для реинвестирования
     * @return ответ об исполнении
     */
    private ApiResponse reinvestPart(AccountTxDTO dto) {
        ApiResponse response = new ApiResponse();
        for (Long id : dto.getAccountsIds()) {
            Account owner = accountService.findByOwnerId(id, OwnerType.INVESTOR);
            if (owner == null) {
                prepareErrorResponse(response, "Не найден счёт инвестора");
                return response;
            }
            Account recipient = accountService.findByOwnerId(dto.getUnderFacilityId(), OwnerType.UNDER_FACILITY);
            if (recipient == null) {
                prepareErrorResponse(response, "Не найден счёт подобъекта");
                return response;
            }
            try {
                AccountTransaction creditTx = createCreditTransaction(owner, recipient, dto);
                createDebitTransaction(creditTx, dto);
            } catch (Exception e) {
                prepareErrorResponse(response, e.getLocalizedMessage());
                return response;
            }
        }
        return new ApiResponse("Реинвестирование прошло успешно");
    }

    /**
     * Проверить наличие необходимой суммы для реинвестирования
     *
     * @param dto DTO для реинвестирования
     * @return ответ
     */
    private ApiResponse checkAvailableCash(AccountTxDTO dto) {
        ApiResponse response = new ApiResponse();
        List<Long> accIds = dto.getAccountsIds();
        for (Long accId : accIds) {
            AppUser investor = userService.findById(accId);
            if (investor == null) {
                prepareErrorResponse(response, "Не найден инвестор");
                return response;
            }
            AccountDTO accountDTO = getOwnerSummary(investor.getLogin());
            if (accountDTO.getSummary().compareTo(dto.getCash()) < 0) {
                prepareErrorResponse(response, "У инвестора [" + investor.getLogin() + "] недостаточно средств для реинвестирования");
                return response;
            }
        }
        return null;
    }

    /**
     * Создать приходную транзакцию
     *
     * @param creditTx расходная транзакция
     * @param dto DTO для реинвестирования
     */
    private void createDebitTransaction(AccountTransaction creditTx, AccountTxDTO dto) {
        Account recipient = accountService.findByOwnerId(dto.getUnderFacilityId(), OwnerType.UNDER_FACILITY);
        AccountTransaction debitTx = new AccountTransaction(recipient);
        debitTx.setOperationType(OperationType.DEBIT);
        debitTx.setPayer(creditTx.getOwner());
        debitTx.setRecipient(recipient);
        debitTx.setMonies(creditTx.getMonies());
        debitTx.setCashType(CashType.INVESTOR_CASH);
        debitTx.setCash(creditTx.getMonies().stream()
                .map(Money::getGivenCash)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        accountTransactionRepository.save(debitTx);
    }

    /**
     * Создать расходную транзакцию по счёту
     *  @param owner владелец
     * @param recipient получатель
     * @param dto DTO
     */
    private AccountTransaction createCreditTransaction(Account owner, Account recipient, AccountTxDTO dto) {
        AccountTransaction creditTx = new AccountTransaction(owner);
        creditTx.setOperationType(OperationType.CREDIT);
        creditTx.setPayer(owner);
        creditTx.setRecipient(recipient);
        Money money = createMoney(owner, dto);
        creditTx.setCashType(CashType.INVESTOR_CASH);
        creditTx.setCash(money.getGivenCash().negate());
        accountTransactionRepository.save(creditTx);
        money.setTransaction(creditTx);
        moneyRepository.save(money);
        return creditTx;
    }

    /**
     * Создать сумму в деньгах инвесторов
     *
     * @param owner владелец счёта
     * @param dto DTO для реинвестирования
     * @return созданная сумма
     */
    private Money createMoney(Account owner, AccountTxDTO dto) {
        Facility facility = facilityService.findById(dto.getFacilityId());
        if (facility == null) {
            throw new EntityNotFoundException("Не найден объект для реинвестирования");
        }
        UnderFacility underFacility = underFacilityService.findById(dto.getUnderFacilityId());
        if (underFacility == null) {
            throw new EntityNotFoundException("Не найден подобъект для реинвестирования");
        }
        AppUser investor = userService.findById(owner.getOwnerId());
        if (investor == null) {
            throw new EntityNotFoundException("Не найден инвестор");
        }
        NewCashDetail newCashDetail = newCashDetailService.findByName("Реинвестирование");
        if (newCashDetail == null) {
            throw new EntityNotFoundException("Не найдены детали новых денег [Реинвестирование]");
        }
        ShareType shareType = ShareType.fromTitle(dto.getShareType());
        Date dateGiven = dto.getDateReinvest();
        BigDecimal cash = dto.getCash();
        Money money = new Money();
        money.setFacility(facility);
        money.setUnderFacility(underFacility);
        money.setShareType(shareType);
        money.setDateGiven(dateGiven);
        money.setGivenCash(cash);
        money.setInvestor(investor);
        money.setNewCashDetail(newCashDetail);
        return moneyRepository.save(money);
    }

    /**
     * Подготовить ответ с ошибкой
     *
     * @param response ответ
     * @param error сообщение
     */
    private void prepareErrorResponse(ApiResponse response, String error) {
        response.setError(error);
        response.setStatus(HttpStatus.PRECONDITION_FAILED.value());
    }

    /**
     * Проверить DTO для реинвестирования
     *
     * @param dto DTO для проверки
     * @return ответ проверки
     */
    private ApiResponse checkDTO(AccountTxDTO dto) {
        ApiResponse response = new ApiResponse();
        if (dto == null) {
            prepareErrorResponse(response, "Не указан DTO для реинвестирования");
            return response;
        }
        if (dto.getFacilityId() == null) {
            prepareErrorResponse(response, "Не указан id объекта");
            return response;
        }
        if (dto.getUnderFacilityId() == null) {
            prepareErrorResponse(response, "Не указан id подобъекта");
            return response;
        }
        if (dto.getShareType() == null) {
            prepareErrorResponse(response, "Не указана доля для реинвестирования");
            return response;
        }
        if (dto.getAccountsIds() == null || dto.getAccountsIds().isEmpty()) {
            prepareErrorResponse(response, "Не указаны счета для реинвестирования");
            return response;
        }
        if (dto.getDateReinvest() == null) {
            prepareErrorResponse(response, "Не указана дата реивестирования");
            return response;
        }
        return null;
    }

    /**
     * Получить сумму на счетах инвестора
     *
     * @param ownerName логин инвестора
     * @return сумма
     */
    private AccountDTO getOwnerSummary(String ownerName) {
        return accountTransactionRepository.fetchSummaryByOwner(OwnerType.INVESTOR, ownerName);
    }

    /**
     * Получить связанные с суммой в деньгах инвесторов транзакции
     *
     * @param moneyId id суммы в деньгах инвесторов
     */
    public List<AccountTransaction> findByMoneyId(Long moneyId) {
        AccountTransactionFilter filter = new AccountTransactionFilter();
        filter.setMoneyId(moneyId);
        List<AccountTransaction> transactions = accountTransactionRepository.findAll(transactionSpecification.getFilter(filter));
        return transactions;
    }

    public void deleteByMoneyId(Long moneyId) {
        accountTransactionRepository.delete(findByMoneyId(moneyId));
    }

}
