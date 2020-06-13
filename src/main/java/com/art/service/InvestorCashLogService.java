package com.art.service;

import com.art.model.InvestorCashLog;
import com.art.model.InvestorsCash;
import com.art.model.TransactionLog;
import com.art.repository.InvestorCashLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с историей операций над деньгами
 *
 * @author Alexandr Stegnin
 */

@Service
public class InvestorCashLogService {

    private final InvestorCashLogRepository investorCashLogRepository;

    public InvestorCashLogService(InvestorCashLogRepository investorCashLogRepository) {
        this.investorCashLogRepository = investorCashLogRepository;
    }

    /**
     * Найти сумму в истории по id
     *
     * @param id суммы
     * @return найденная сумма
     */
    public InvestorCashLog findById(Long id) {
        return investorCashLogRepository.findOne(id);
    }

    /**
     * Найти сумму в логе по id суммы
     *
     * @param cashId id суммы
     * @return найденная запись
     */
    public InvestorCashLog findByCashId(Long cashId) {
        List<InvestorCashLog> cashLogs = investorCashLogRepository.findByCashIdOrderByIdDesc(cashId);
        if (cashLogs.size() > 0) {
            return cashLogs.get(0);
        }
        return null;
    }

    /**
     * Найти сумму в логе по id транзакции
     *
     * @param txId id транзакции
     * @return найденная запись
     */
    public InvestorCashLog findByTxId(Long txId) {
        return investorCashLogRepository.findByTransactionLogId(txId);
    }

    /**
     * Создать сумму в истории и в логе на основании суммы инвестора
     *
     * @param cash сумма инвестора
     * @param log транзакция
     */
    public void create(InvestorsCash cash, TransactionLog log) {
        InvestorCashLog cashLog = new InvestorCashLog(cash, log);
        investorCashLogRepository.save(cashLog);
    }

    /**
     * Создать суммы в истории и в логе на основании списка сумм
     *
     * @param cashes список денег
     */
    public void update(List<InvestorsCash> cashes, TransactionLog log) {
        cashes.forEach(cash -> {
            InvestorCashLog cashLog = new InvestorCashLog(cash, log);
            investorCashLogRepository.save(cashLog);
        });
    }

    /**
     * Удалить запись из истории операций по id суммы
     *
     * @param cash сумма
     */
    public void delete(InvestorsCash cash) {
        InvestorCashLog cashLog = findByCashId(cash.getId().longValue());
        if (null != cashLog) {
            investorCashLogRepository.delete(cashLog);
        }
    }

    /**
     * Удалить запись из истории операций
     *
     * @param cashLog запись к удалению
     */
    public void delete(InvestorCashLog cashLog) {
        investorCashLogRepository.delete(cashLog);
    }

}