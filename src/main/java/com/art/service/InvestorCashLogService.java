package com.art.service;

import com.art.model.InvestorCashLog;
import com.art.model.InvestorsCash;
import com.art.model.supporting.TransactionType;
import com.art.repository.InvestorCashLogRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Service
public class InvestorCashLogService {

    private final InvestorCashLogRepository investorCashLogRepository;

    private final TransactionLogService transactionLogService;

    public InvestorCashLogService(InvestorCashLogRepository investorCashLogRepository,
                                  TransactionLogService transactionLogService) {
        this.investorCashLogRepository = investorCashLogRepository;
        this.transactionLogService = transactionLogService;
    }

    public InvestorCashLog findById(Long id) {
        InvestorCashLog cashLog = investorCashLogRepository.findOne(id);
        if (null == cashLog) {
            throw new EntityNotFoundException("Не найдена сумма из лога операций с id = [" + id + "]");
        }
        return cashLog;
    }

    public void create(InvestorsCash cash) {
        InvestorCashLog cashLog = new InvestorCashLog(cash);
        investorCashLogRepository.save(cashLog);
        transactionLogService.create(cash, TransactionType.CREATE);
    }

    public void update(List<InvestorsCash> cashes) {
        cashes.forEach(cash -> {
            InvestorCashLog cashLog = new InvestorCashLog(cash);
            investorCashLogRepository.save(cashLog);
        });
        transactionLogService.create(cashes, TransactionType.UPDATE);
    }
}
