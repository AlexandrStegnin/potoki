package com.art.service;

import com.art.model.InvestorsCash;
import com.art.model.TransactionLog;
import com.art.model.supporting.TransactionType;
import com.art.model.supporting.dto.InvestorCashDTO;
import com.art.model.supporting.dto.TransactionLogDTO;
import com.art.model.supporting.filters.TxLogFilter;
import com.art.repository.TransactionLogRepository;
import com.art.specifications.TransactionLogSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    public TransactionLogService(TransactionLogSpecification specification,
                                 TransactionLogRepository transactionLogRepository,
                                 InvestorCashLogService investorCashLogService,
                                 InvestorsCashService investorsCashService) {
        this.specification = specification;
        this.transactionLogRepository = transactionLogRepository;
        this.investorCashLogService = investorCashLogService;
        this.investorsCashService = investorsCashService;
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
        List<InvestorCashDTO> cashDTOS = new ArrayList<>();
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
     * Найти все операции
     *
     * @return список операций
     */
    public List<TransactionLog> findAll() {
        return transactionLogRepository.findAll();
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
                return "Операция [CLOSING] не реализована";

            case DIVIDE:
                return "Операция [DIVIDE] не реализована";

            case REINVESTMENT:
                return "Операция [REINVESTMENT] не реализована";

            case UNDEFINED:
                return "Операция [UNDEFINED] не реализована";

            default:
                return "Что-то не то выбрали: " + log.getType().name();
        }
    }

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

    public void create(List<InvestorsCash> cashes, TransactionType type) {
        TransactionLog log = new TransactionLog();
        log.setInvestorsCashes(new HashSet<>(cashes));
        log.setType(type);
        log.setRollbackEnabled(true);
        create(log);
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
        return null;
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
     * Создать запись в логе категории обновление
     *
     * @param cash деньги инвестора
     */
    public void update(InvestorsCash cash) {
        TransactionLog log = new TransactionLog();
        log.setInvestorsCashes(Collections.singleton(cash));
        log.setType(TransactionType.UPDATE);
        log.setRollbackEnabled(true);
        investorCashLogService.create(cash);
        List<TransactionLog> linkedLogs = findByCash(cash);
        linkedLogs.forEach(linkedLog -> {
            linkedLog.setRollbackEnabled(false);
            update(linkedLog);
        });
        create(log);
    }
}
