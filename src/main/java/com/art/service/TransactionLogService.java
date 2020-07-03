package com.art.service;

import com.art.model.*;
import com.art.model.supporting.enums.TransactionType;
import com.art.model.supporting.dto.InvestorCashDTO;
import com.art.model.supporting.dto.TransactionLogDTO;
import com.art.model.supporting.filters.TxLogFilter;
import com.art.repository.TransactionLogRepository;
import com.art.specifications.TransactionLogSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Alexandr Stegnin
 */

@Service
@Transactional
public class TransactionLogService {

    private final TransactionLogSpecification specification;

    private final TransactionLogRepository transactionLogRepository;

    private final InvestorCashLogService investorCashLogService;

    private final InvestorsCashService investorsCashService;

    private final InvestorsFlowsSaleService investorsFlowsSaleService;

    private final InvestorsFlowsService investorsFlowsService;

    @Autowired
    public TransactionLogService(TransactionLogSpecification specification,
                                 TransactionLogRepository transactionLogRepository,
                                 InvestorCashLogService investorCashLogService,
                                 InvestorsCashService investorsCashService,
                                 InvestorsFlowsSaleService investorsFlowsSaleService,
                                 InvestorsFlowsService investorsFlowsService) {
        this.specification = specification;
        this.transactionLogRepository = transactionLogRepository;
        this.investorCashLogService = investorCashLogService;
        this.investorsCashService = investorsCashService;
        this.investorsFlowsSaleService = investorsFlowsSaleService;
        this.investorsFlowsService = investorsFlowsService;
    }

    /**
     * Создать запись об операции с деньгами
     *
     * @param transactionLog - операция
     */
    public void create(TransactionLog transactionLog) {
        transactionLogRepository.save(transactionLog);
    }

    /**
     * Обновить запись о транзакции
     *
     * @param log запись
     */
    public void update(TransactionLog log) {
        transactionLogRepository.save(log);
    }

    /**
     * Найти список транзакций, которые содержат переданные деньги инвестора
     *
     * @param cash деньги инвестора
     * @return список транзакций
     */
    public List<TransactionLog> findByCash(InvestorsCash cash) {
        return transactionLogRepository.findByInvestorsCashesContains(cash);
    }

    /**
     * Получить список денег, затронутых в транзакции
     *
     * @param txLogId id транзакции
     * @return список денег
     */
    @Transactional(readOnly = true)
    public List<InvestorCashDTO> getCashByTxId(Long txLogId) {
        TransactionLog log = findById(txLogId);
        List<InvestorCashLog> cashLogs = investorCashLogService.findByTxId(txLogId);
        List<InvestorCashDTO> cashDTOS = new ArrayList<>();
        cashLogs.forEach(cashLog -> {
            InvestorCashDTO cashDTO = new InvestorCashDTO(cashLog);
            cashDTOS.add(cashDTO);
        });

        log.getInvestorsCashes()
                .forEach(cash -> {
                    InvestorCashDTO dto = new InvestorCashDTO(cash);
                    cashDTOS.add(dto);
                });
        return cashDTOS;
    }

    /**
     * Найти операцию по id
     *
     * @param id id операции
     * @return найденная операция
     */
    public TransactionLog findById(Long id) {
        TransactionLog transactionLog = transactionLogRepository.findOne(id);
        if (null == transactionLog) {
            throw new RuntimeException("Операция с id = [" + id + "] не найдена");
        }
        return transactionLog;
    }

    /**
     * Получить список операций по фильтрам
     *
     * @param filter   фильтр
     * @param pageable для постраничного отображения
     * @return список операций
     */
    public List<TransactionLog> findAll(TxLogFilter filter, Pageable pageable) {
        return transactionLogRepository.findAll(specification.getFilter(filter), pageable).getContent();
    }

    /**
     * Откат операции
     *
     * @param logDTO DTO транзакции
     * @return сообщение об успешном/не успешном выполнении
     */
    public String rollbackTransaction(TransactionLogDTO logDTO) {
        TransactionLog log = findById(logDTO.getId());
        switch (log.getType()) {
            case CREATE:
                return rollbackCreate(log);

            case UPDATE:
                return rollbackUpdate(log);

            case CLOSING:
                return rollbackClosing(log);

            case CLOSING_RESALE:
                return rollbackResale(log);

            case DIVIDE:
                return "Операция [DIVIDE] не реализована";

            case REINVESTMENT_SALE:
                return rollbackReinvestmentSale(log);

            case REINVESTMENT_RENT:
                return rollbackReinvestmentRent(log);

            case UNDEFINED:
                return "Операция [UNDEFINED] не реализована";

            default:
                return "Что-то не то выбрали: " + log.getType().name();
        }
    }

    /**
     * Создать запись в логе по операции создания денег инвестора
     *
     * @param cash деньги инвестора
     * @param type тип операции
     */
    public void create(InvestorsCash cash, TransactionType type) {
        TransactionLog log = new TransactionLog();
        log.setInvestorsCashes(Collections.singleton(cash));
        log.setType(type);
        log.setRollbackEnabled(true);
        create(log);
    }

    /**
     * Создать запись категории "обновление" в логе
     *
     * @param cash деньги инвестора
     */
    public void update(InvestorsCash cash) {
        TransactionLog log = new TransactionLog();
        log.setInvestorsCashes(Collections.singleton(cash));
        log.setType(TransactionType.UPDATE);
        log.setRollbackEnabled(true);
        create(log);
        investorCashLogService.create(cash, log);
        blockLinkedLogs(cash, log);
    }

    /**
     * Создать запись категории "Закрытие. Вывод" в логе
     *
     * @param cashes список сумм инвестора
     */
    public void close(Set<InvestorsCash> cashes) {
        TransactionLog log = new TransactionLog();
        log.setInvestorsCashes(cashes);
        log.setType(TransactionType.CLOSING);
        log.setRollbackEnabled(true);
        create(log);
        investorCashLogService.create(cashes, log);
        cashes.forEach(cash -> blockLinkedLogs(cash, log));
    }

    /**
     * Создать запись категории "Закрытие. Перепродажа доли" в логе
     *
     * @param cashes суммы инвесторов
     */
    public void resale(Set<InvestorsCash> oldCashes, Set<InvestorsCash> cashes) {
        oldCashes.forEach(oldCash -> {
            oldCash.setDateClosingInvest(null);
            oldCash.setTypeClosingInvest(null);
            oldCash.setRealDateGiven(null);
        });

        TransactionLog log = new TransactionLog();
        log.setInvestorsCashes(cashes);
        log.setType(TransactionType.CLOSING_RESALE);
        log.setRollbackEnabled(true);
        create(log);
        investorCashLogService.create(oldCashes, log);
        cashes.forEach(cash -> blockLinkedLogs(cash, log));
    }

    /**
     * Создать запись категории "Реинвестирование с продажи" в логе
     *
     * @param flowsSaleList список денег с продажи
     * @param cashList список денег, основанных на деньгах с продажи
     */
    public void reinvestmentSale(List<InvestorsFlowsSale> flowsSaleList, Set<InvestorsCash> cashList) {
        TransactionLog log = new TransactionLog();
        log.setInvestorsCashes(cashList);
        log.setType(TransactionType.REINVESTMENT_SALE);
        log.setRollbackEnabled(true);
        create(log);
        investorCashLogService.reinvestmentSale(flowsSaleList, log);
    }

    /**
     * Создать запись категории "Реинвестирование с аренды" в логе
     *
     * @param flowsList список денег с аренды
     * @param cashList список денег, основанных на деньгах с продажи
     */
    public void reinvestmentRent(List<InvestorsFlows> flowsList, Set<InvestorsCash> cashList) {
        TransactionLog log = new TransactionLog();
        log.setInvestorsCashes(cashList);
        log.setType(TransactionType.REINVESTMENT_RENT);
        log.setRollbackEnabled(true);
        create(log);
        investorCashLogService.reinvestmentRent(flowsList, log);
    }

    @Transactional
    public String rollbackCreate(TransactionLog log) {
        Set<InvestorsCash> cashes = log.getInvestorsCashes();
        try {
            cashes.forEach(cash -> {
                investorCashLogService.delete(cash);
                investorsCashService.deleteById(cash.getId());
            });
            transactionLogRepository.delete(log);
            return "Откат сумм прошёл успешно";
        } catch (Exception e) {
            return String.format("При откате операций произошла ошибка: [%s]", e.getLocalizedMessage());
        }
    }

    @Transactional
    public String rollbackUpdate(TransactionLog log) {
        Set<InvestorsCash> cashes = log.getInvestorsCashes();
        try {
            cashes.forEach(cash -> {
                List<InvestorCashLog> cashLogs = investorCashLogService.findByTxId(log.getId());
                cashLogs.forEach(cashLog -> {
                    if (cash.getId().longValue() == cashLog.getCashId()) {
                        mergeCash(cash, cashLog);
                        investorCashLogService.delete(cashLog);
                        investorsCashService.update(cash);
                    }
                });
            });
            unblockTransactions(log.getId());
            transactionLogRepository.delete(log);
            return "Откат операции прошёл успешно";
        } catch (Exception e) {
            return String.format("При удалении транзакции произошла ошибка [%s]", e.getLocalizedMessage());
        }
    }

    @Transactional
    public String rollbackResale(TransactionLog log) {
        Set<InvestorsCash> cashes = log.getInvestorsCashes();
        try {
            Set<InvestorsCash> cashToDelete = new HashSet<>(cashes);
            List<InvestorCashLog> cashLogs = investorCashLogService.findByTxId(log.getId());
            cashes.forEach(cash -> cashLogs.forEach(cashLog -> {
                if (cash.getId().longValue() == cashLog.getCashId()) {
                    cashToDelete.remove(cash);
                    mergeCash(cash, cashLog);
                    investorCashLogService.delete(cashLog);
                    investorsCashService.update(cash);
                }
            }));

            cashToDelete.forEach(cash -> investorsCashService.deleteById(cash.getId()));

            unblockTransactions(log.getId());
            transactionLogRepository.delete(log);
            return "Откат операции прошёл успешно";
        } catch (Exception e) {
            return String.format("При удалении транзакции произошла ошибка [%s]", e.getLocalizedMessage());
        }
    }

    @Transactional
    public String rollbackClosing(TransactionLog log) {
        return rollbackUpdate(log);
    }

    @Transactional
    public String rollbackReinvestmentSale(TransactionLog log) {
        Set<InvestorsCash> cashes = log.getInvestorsCashes();
        List<InvestorCashLog> cashLogs = investorCashLogService.findByTxId(log.getId());
        List<BigInteger> flowsCashIdList = cashLogs
                .stream()
                .map(cashLog -> BigInteger.valueOf(cashLog.getCashId()))
                .collect(Collectors.toList());
        List<InvestorsFlowsSale> flowsSales = investorsFlowsSaleService.findByIdIn(flowsCashIdList);
        try {
            cashes.forEach(investorsCashService::delete);
            flowsSales.forEach(flowSale -> {
                flowSale.setIsReinvest(0);
                investorsFlowsSaleService.update(flowSale);
            });
            cashLogs.forEach(investorCashLogService::delete);
            transactionLogRepository.delete(log);
            return "Откат операции прошёл успешно";
        } catch (Exception e) {
            return String.format("При удалении транзакции произошла ошибка [%s]", e.getLocalizedMessage());
        }
    }

    @Transactional
    public String rollbackReinvestmentRent(TransactionLog log) {
        Set<InvestorsCash> cashes = log.getInvestorsCashes();
        List<InvestorCashLog> cashLogs = investorCashLogService.findByTxId(log.getId());
        List<BigInteger> flowsCashIdList = cashLogs
                .stream()
                .map(cashLog -> BigInteger.valueOf(cashLog.getCashId()))
                .collect(Collectors.toList());
        List<InvestorsFlows> flowsRent = investorsFlowsService.findByIdIn(flowsCashIdList);
        try {
            cashes.forEach(investorsCashService::delete);
            flowsRent.forEach(flowRent -> {
                flowRent.setIsReinvest(0);
                investorsFlowsService.update(flowRent);
            });
            cashLogs.forEach(investorCashLogService::delete);
            transactionLogRepository.delete(log);
            return "Откат операции прошёл успешно";
        } catch (Exception e) {
            return String.format("При удалении транзакции произошла ошибка [%s]", e.getLocalizedMessage());
        }
    }

    /**
     * Изменяем значения суммы на значения суммы из лога
     *
     * @param cash    сумма
     * @param cashLog сумма из лога
     */
    @Transactional
    public void mergeCash(InvestorsCash cash, InvestorCashLog cashLog) {
        cash.setGivedCash(cashLog.getGivenCash());
        cash.setFacility(cashLog.getFacility());
        cash.setUnderFacility(cashLog.getUnderFacility());
        cash.setInvestor(cashLog.getInvestor());
        cash.setDateGivedCash(cashLog.getDateGivenCash());
        cash.setCashSource(cashLog.getCashSource());
        cash.setNewCashDetails(cashLog.getNewCashDetail());
        cash.setShareType(cashLog.getShareType());
        cash.setTypeClosingInvest(cashLog.getTypeClosingInvest());
        cash.setDateClosingInvest(cashLog.getDateClosingInvest());
        cash.setRealDateGiven(cashLog.getRealDateGiven());
    }

    /**
     * Метод для блокирования отката операций, если в них участвовала сумма инвестора
     *
     * @param cash сумма инвестора
     * @param log  текущая операция логирования
     */
    private void blockLinkedLogs(InvestorsCash cash, TransactionLog log) {
        List<TransactionLog> linkedLogs = findByCash(cash);
        linkedLogs.forEach(linkedLog -> {
            if (null == linkedLog.getBlockedFrom()) {
                if (!linkedLog.getId().equals(log.getId())) {
                    linkedLog.setRollbackEnabled(false);
                    linkedLog.setBlockedFrom(log);
                    update(linkedLog);
                }
            }
        });
    }

    /**
     * Разблокировать транзакции по id лога
     *
     * @param logId id лога
     */
    private void unblockTransactions(Long logId) {
        List<TransactionLog> blockedLogs = transactionLogRepository.findByBlockedFromId(logId);
        blockedLogs.forEach(blockedLog -> {
            blockedLog.setRollbackEnabled(true);
            blockedLog.setBlockedFrom(null);
            transactionLogRepository.save(blockedLog);
        });
    }

    /**
     * Получить список видов операции для использования в фильтрах
     *
     * @return список видов операций
     */
    public Map<Integer, String> getTypes() {
        Map<Integer, String> map = new HashMap<>();
        map.put(0, "Вид операции");
        TransactionType[] types = TransactionType.values();
        for (TransactionType type : types) {
            if (!type.equals(TransactionType.UNDEFINED)) {
                map.put(type.getId(), type.getTitle());
            }
        }
        return map;
    }

    /**
     * Получить список "кем создавалась транзакция" для использования в фильтре
     *
     * @return список "кем создавалась транзакция"
     */
    public List<String> getCreators() {
        List<TransactionLog> logs = transactionLogRepository.findAll();
        List<String> creators = new ArrayList<>();
        creators.add("Кем создана");
        creators.addAll(logs
                .stream()
                .map(TransactionLog::getCreatedBy)
                .distinct()
                .sorted()
                .collect(Collectors.toList()));
        return creators;
    }

}
