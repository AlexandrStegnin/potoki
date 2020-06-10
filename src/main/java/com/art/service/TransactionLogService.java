package com.art.service;

import com.art.config.SecurityUtils;
import com.art.model.InvestorsCash;
import com.art.model.TransactionLog;
import com.art.model.supporting.TransactionType;
import com.art.model.supporting.filters.TxLogFilter;
import com.art.repository.TransactionLogRepository;
import com.art.specifications.TransactionLogSpecification;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Alexandr Stegnin
 */

@Service
public class TransactionLogService {

    private final TransactionLogSpecification specification;

    private final TransactionLogRepository transactionLogRepository;

    public TransactionLogService(TransactionLogSpecification specification,
                                 TransactionLogRepository transactionLogRepository) {
        this.specification = specification;
        this.transactionLogRepository = transactionLogRepository;
    }

    /**
     * Создать запись об операции с деньгами
     *
     * @param transactionLog - операция
     * @return - созданная запись
     */
    public TransactionLog forCreateCash(TransactionLog transactionLog) {
        return transactionLogRepository.save(transactionLog);
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
        return transactionLogRepository.findOne(id);
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
     * @param filter фильтр
     * @param pageable для постраничного отображения
     * @return список операций
     */
    public List<TransactionLog> findAll(TxLogFilter filter, Pageable pageable) {
        return transactionLogRepository.findAll(specification.getFilter(filter), pageable).getContent();
    }

    /**
     * Откат операции
     *
     * @param transactionId id транзакции
     * @return сообщение об успешном/не успешном выполнении
     */
    public String rollbackTransaction(Long transactionId) {
        return null;
    }

    public List<String> getInvestors() {
        List<TransactionLog> logs = transactionLogRepository.findAll();
        List<String> investors = new ArrayList<>();
        investors.add("Выберите инвестора");
        investors.addAll(logs
                .stream()
                .map(TransactionLog::getInvestor)
                .distinct()
                .sorted()
                .collect(Collectors.toList()));
        return investors;
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

    public void forCreateCash(InvestorsCash cash) {
        create(cash, TransactionType.CREATE);
    }

    public void forUpdateCash(InvestorsCash cash) {
        create(cash, TransactionType.UPDATE);
    }

    private void create(InvestorsCash cash, TransactionType type) {
        TransactionLog log = new TransactionLog();
        log.setCreatedBy(SecurityUtils.getUsername());
        log.setInvestor(cash.getInvestor().getLogin());
        log.setInvestorsCashes(Collections.singleton(cash));
        log.setTxDate(new Date());
        log.setType(type);
        forCreateCash(log);
    }

}