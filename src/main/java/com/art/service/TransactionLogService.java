package com.art.service;

import com.art.config.SecurityUtils;
import com.art.model.InvestorsCash;
import com.art.model.TransactionLog;
import com.art.model.supporting.TransactionType;
import com.art.model.supporting.dto.InvestorCashDTO;
import com.art.model.supporting.filters.TxLogFilter;
import com.art.repository.TransactionLogRepository;
import com.art.specifications.TransactionLogSpecification;
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
    public TransactionLog create(TransactionLog transactionLog) {
        return transactionLogRepository.save(transactionLog);
    }

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
     * @param transactionId id транзакции
     * @return сообщение об успешном/не успешном выполнении
     */
    public String rollbackTransaction(Long transactionId) {
        return null;
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

    public void create(InvestorsCash cash) {
        create(cash, TransactionType.CREATE);
    }

    public void forUpdateCash(InvestorsCash cash) {
        create(cash, TransactionType.UPDATE);
    }

    public void create(InvestorsCash cash, TransactionType type) {
        TransactionLog log = new TransactionLog();
        log.setCreatedBy(SecurityUtils.getUsername());
        log.setInvestorsCashes(Collections.singleton(cash));
        log.setTxDate(new Date());
        log.setType(type);
        log.setRollbackEnabled(true);
        create(log);
    }

    public void create(List<InvestorsCash> cashes, TransactionType type) {
        TransactionLog log = new TransactionLog();
        log.setCreatedBy(SecurityUtils.getUsername());
        log.setInvestorsCashes(new HashSet<>(cashes));
        log.setTxDate(new Date());
        log.setType(type);
        log.setRollbackEnabled(true);
        create(log);
    }

}
