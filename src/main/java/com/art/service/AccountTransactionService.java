package com.art.service;

import com.art.config.exception.ApiException;
import com.art.config.exception.EntityNotFoundException;
import com.art.model.*;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.dto.*;
import com.art.model.supporting.enums.CashType;
import com.art.model.supporting.enums.OperationType;
import com.art.model.supporting.enums.OwnerType;
import com.art.model.supporting.enums.ShareType;
import com.art.model.supporting.filters.AccTxFilter;
import com.art.repository.AccountTransactionRepository;
import com.art.repository.MoneyRepository;
import com.art.repository.RentPaymentRepository;
import com.art.repository.SalePaymentRepository;
import com.art.specifications.AccountTransactionSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.art.config.application.Constant.NEW_CASH_DETAIL_REINVEST;

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

    public Page<AccountTransaction> findAll(AccTxFilter filter, Pageable pageable) {
        if (filter.getPageSize() == 0) pageable = new PageRequest(filter.getPageNumber(), filter.getTotal() + 1);
        return accountTransactionRepository.findAll(
                transactionSpecification.getFilter(filter),
                pageable
        );
    }

    /**
     * Получить список владельцев счетов для фильтрации
     *
     * @return список владельцев
     */
    public List<Account> initOwners() {
        return accountTransactionRepository.getAllOwners(OwnerType.INVESTOR);
    }

    /**
     * Получить список плательщиков (ПОДОБЪЕКТОВ) для фильтрации
     *
     * @return список плательщиков
     */
    public List<Account> initPayers() {
        return accountTransactionRepository.getAllPayers();
    }

    /**
     * Получить список плательщиков (ОБЪЕКТОВ) для фильтрации
     *
     * @return список плательщиков
     */
    public List<Account> initParentPayers() {
        return accountTransactionRepository.getAllParentPayers();
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
        Set<AccountTransaction> transactions = new HashSet<>();
        try {
            dto.getTxIds().forEach(id -> {
                AccountTransaction transaction = findParent(id);
                if (transaction != null) {
                    transactions.add(transaction);
                }
            });
            transactions.forEach(transaction -> {
                List<AccountTransaction> allTx = new ArrayList<>(getAllChild(transaction));
                allTx.add(transaction);
                deleteMonies(allTx);
                releaseMonies(allTx);
                deleteSalePayments(allTx);
                deleteRentPayments(allTx);
                deleteByParent(transaction);
            });
        } catch (Exception e) {
            return new ApiResponse(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST.value());
        }
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
     * Удалить суммы из денег инвесторов, если они были реинвестированы
     *
     * @param transactions список транзакций
     */
    private void deleteMonies(List<AccountTransaction> transactions) {
        List<Money> toDelete = new ArrayList<>();
        transactions.forEach(tx -> tx.getMonies()
                .stream()
                .filter(Objects::nonNull)
                .forEach(money -> {
                    NewCashDetail newCashDetail = money.getNewCashDetail();
                    if (Objects.nonNull(newCashDetail)) {
                        if (newCashDetail.getName().equalsIgnoreCase(NEW_CASH_DETAIL_REINVEST)) {
                            tx.removeMoney(money);
                            toDelete.add(money);
                        }
                    }
                }));
        moneyRepository.delete(toDelete);
    }

    private Set<AccountTransaction> getAllChild(AccountTransaction transaction) {
        Set<AccountTransaction> transactions = transaction.getChild();
        if (transactions != null && !transactions.isEmpty()) {
            for (AccountTransaction tx : transactions) {
                transactions.addAll(getAllChild(tx));
            }
        }
        return transactions;
    }

    /**
     * Получить список счетов с итоговым балансом
     *
     * @param filter список фильтров
     * @param pageable для постраничного отображения
     * @return список счетов с суммарным балансом
     */
    public Page<AccountDTO> getSummary(AccTxFilter filter, Pageable pageable) {
        if (filter.getPageSize() == 0) pageable = new PageRequest(filter.getPageNumber(), filter.getTotal() + 1);
        List<Account> owners = filter.getOwners();
        List<Account> payers = filter.getPayers();
        List<Account> parentPayers = filter.getParentPayers();

        switch (filter.getType()) {
            case IS_CLEAR:
                return accountTransactionRepository.getSummary(OwnerType.INVESTOR, pageable);
            case BY_OWNERS:
                return accountTransactionRepository.fetchSummaryByOwners(OwnerType.INVESTOR, owners, pageable);
            case BY_PAYERS:
                return accountTransactionRepository.fetchSummaryByPayers(OwnerType.INVESTOR, payers, pageable);
            case BY_PARENT_PAYERS:
                return accountTransactionRepository.fetchSummaryByParentPayers(OwnerType.INVESTOR, parentPayers, pageable);
            case BY_OWNERS_AND_PAYERS:
                return accountTransactionRepository.fetchSummaryByOwnersAndPayers(OwnerType.INVESTOR, owners, payers, pageable);
            case BY_OWNERS_AND_PARENT_PAYERS:
                return accountTransactionRepository.fetchSummaryByOwnersAndParentPayers(OwnerType.INVESTOR, owners, parentPayers, pageable);
            case BY_PAYERS_AND_PARENT_PAYERS:
                return accountTransactionRepository.fetchSummaryByPayersAndParentPayers(OwnerType.INVESTOR, payers, parentPayers, pageable);
            case BY_OWNERS_AND_PAYERS_AND_PARENT_PAYERS:
                return accountTransactionRepository.fetchSummaryByOwnersAndPayersAndParentPayers(OwnerType.INVESTOR, owners, payers, parentPayers, pageable);
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
        AccTxFilter filter = getFilter(dto);
        List<AccountTransaction> transactions = accountTransactionRepository.findAll(transactionSpecification.getDetailsFilter(filter));

        return transactions
                .stream()
                .map(AccountTransactionDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Составить фильтр из DTO
     *
     * @param dto DTO
     * @return фильтр
     */
    private AccTxFilter getFilter(AccountSummaryDTO dto) {
        AccTxFilter filter = new AccTxFilter();
        filter.setOwners(dto.getOwners());
        filter.setPayers(dto.getPayers());
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
        List<Long> accIds = dto.getAccountsIds().stream().distinct().collect(Collectors.toList());
        for (Long id : accIds) {
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
            AccountDTO accountDTO = accountTransactionRepository.fetchSummaryByOwners(OwnerType.INVESTOR, owner.getOwnerName());
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
        List<Long> accIds = dto.getAccountsIds().stream().distinct().collect(Collectors.toList());
        ApiResponse response = new ApiResponse();
        for (Long id : accIds) {
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
        debitTx.setCash(creditTx.getCash().negate());
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
        NewCashDetail newCashDetail = newCashDetailService.findByName(NEW_CASH_DETAIL_REINVEST);
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
        return accountTransactionRepository.fetchSummaryByOwners(OwnerType.INVESTOR, ownerName);
    }

    public BalanceDTO getBalance(Long ownerId) {
        AccountDTO accountDTO = accountTransactionRepository.fetchBalance(OwnerType.INVESTOR, ownerId);
        if (accountDTO != null) {
            return new BalanceDTO(accountDTO);
        }
        return new BalanceDTO();
    }

    /**
     * Обновить сумму транзакции
     *
     * @param transaction транзакция
     * @param newCash новая сумма
     * @return транзакция
     */
    public AccountTransaction updateCash(AccountTransaction transaction, BigDecimal newCash) {
        AccountTransaction tx = findById(transaction.getId());
        tx.setCash(newCash);
        return accountTransactionRepository.saveAndFlush(tx);
    }

    /**
     * Создать расходную транзакцию по счёту инвестора покупателя доли
     *
     * @param dto DTO для перепокупки
     * @param buyerMonies открытые суммы инвестора покупателя
     */
    public AccountTransaction reBuy(ReBuyShareDTO dto, List<Money> buyerMonies) {
        Account owner = accountService.findByInvestorId(dto.getBuyerId());
        if (Objects.isNull(owner)) {
            throw new ApiException("Не найден счёт покупателя", HttpStatus.NOT_FOUND);
        }
        Account recipient = accountService.findByOwnerId(dto.getFacilityId(), OwnerType.FACILITY);
        if (Objects.isNull(recipient)) {
            throw new ApiException("Не найден счёт объекта", HttpStatus.NOT_FOUND);
        }
        AccountTransaction transaction = new AccountTransaction(owner);
        transaction.setOperationType(OperationType.CREDIT);
        transaction.setPayer(owner);
        transaction.setRecipient(recipient);
        transaction.setCashType(CashType.RE_BUY_SHARE);
        BigDecimal summa = reduceSum(buyerMonies);
        transaction.setCash(summa.negate());
        accountTransactionRepository.save(transaction);
        buyerMonies.forEach(money -> money.setTransaction(transaction));
        moneyRepository.save(buyerMonies);
        return transaction;
    }

    /**
     * Создать приходную транзакцию по счёта инвестора продавца доли
     *  @param dto DTO для перепродажи доли
     * @param sellerMonies деньги инвестора продавца
     * @param parentTx связанная транзакция
     */
    public void resale(ReBuyShareDTO dto, List<Money> sellerMonies, AccountTransaction parentTx) {
        Account owner = getInvestorOwner(dto.getSellerId());
        Account payer = getInvestorOwner(dto.getBuyerId());
        AccountTransaction transaction = new AccountTransaction(owner);
        transaction.setOperationType(OperationType.DEBIT);
        transaction.setPayer(payer);
        transaction.setRecipient(owner);
        transaction.setCashType(CashType.RESALE_SHARE);
        BigDecimal summa = reduceSum(sellerMonies);
        transaction.setCash(summa);
        transaction.setParent(parentTx);
        accountTransactionRepository.save(transaction);
        sellerMonies.forEach(money -> money.setTransaction(transaction));
        moneyRepository.save(sellerMonies);
    }

    /**
     * Получить счёт инвестора
     *
     * @param ownerId id инвестора
     * @return найденный счёт
     */
    private Account getInvestorOwner(Long ownerId) {
        Account owner = accountService.findByInvestorId(ownerId);
        if (Objects.isNull(owner)) {
            throw new ApiException("Не найден счёт покупателя", HttpStatus.NOT_FOUND);
        }
        return owner;
    }

    /**
     * Посчитать общую сумму денег
     *
     * @param monies деньги
     * @return сумма
     */
    private BigDecimal reduceSum(List<Money> monies) {
        return monies
                .stream()
                .map(Money::getGivenCash)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getInvestorBalance(Long investorId) {
        Account investorAccount = accountService.findByInvestorId(investorId);
        if (Objects.isNull(investorAccount)) {
            throw new ApiException("Не найден счёт инвестора", HttpStatus.NOT_FOUND);
        }
        return getBalance(investorAccount.getId()).getSummary();
    }

}
