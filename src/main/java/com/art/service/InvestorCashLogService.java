package com.art.service;

import com.art.model.*;
import com.art.model.supporting.enums.CashType;
import com.art.repository.InvestorCashLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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
    public List<InvestorCashLog> findByTxId(Long txId) {
        return investorCashLogRepository.findByTransactionLogId(txId);
    }

    /**
     * Создать сумму в истории и в логе на основании суммы инвестора
     *
     * @param cash сумма инвестора
     * @param log  транзакция
     */
    public void create(InvestorCash cash, TransactionLog log) {
        InvestorCashLog cashLog = new InvestorCashLog(cash, log, CashType.INVESTOR_CASH);
        investorCashLogRepository.save(cashLog);
    }

    /**
     * Создать сумму в истории и в логе на основании списка сумм инвестора
     *
     * @param cashes список сумм инвестора
     * @param log    транзакция
     */
    public void create(Set<InvestorCash> cashes, TransactionLog log) {
        cashes.forEach(cash -> create(cash, log));
    }

    /**
     * Создать суммы в истории и в логе на основании списка сумм
     *
     * @param cashes список денег
     */
    public void update(List<InvestorCash> cashes, TransactionLog log) {
        cashes.forEach(cash -> {
            InvestorCashLog cashLog = new InvestorCashLog(cash, log, CashType.INVESTOR_CASH);
            investorCashLogRepository.save(cashLog);
        });
    }

    /**
     * Создать суммы в истории и в логе на основании сумм с продажи
     *
     * @param flowsSales суммы с продажи
     * @param log        лог
     */
    public void reinvestmentSale(List<InvestorsFlowsSale> flowsSales, TransactionLog log) {
        flowsSales.forEach(flowsSale -> {
            InvestorCashLog cashLog = new InvestorCashLog(flowsSale, log, CashType.SALE_CASH);
            investorCashLogRepository.save(cashLog);
        });
    }

    /**
     * Создать суммы в истории и в логе на основании сумм с аренды
     *
     * @param flows суммы с аренды
     * @param log   лог
     */
    public void reinvestmentRent(List<InvestorsFlows> flows, TransactionLog log) {
        flows.forEach(sum -> {
            InvestorCashLog cashLog = new InvestorCashLog(sum, log, CashType.RENT_CASH);
            investorCashLogRepository.save(cashLog);
        });
    }

    /**
     * Удалить запись из истории операций по id суммы
     *
     * @param cash сумма
     */
    public void delete(InvestorCash cash) {
        InvestorCashLog cashLog = findByCashId(cash.getId());
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
