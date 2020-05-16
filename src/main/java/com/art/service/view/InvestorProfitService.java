package com.art.service.view;

import com.art.model.supporting.view.InvestorProfit;
import com.art.repository.view.InvestorProfitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Service
public class InvestorProfitService {

    private final InvestorProfitRepository investorProfitRepository;

    public InvestorProfitService(InvestorProfitRepository investorProfitRepository) {
        this.investorProfitRepository = investorProfitRepository;
    }

    public List<InvestorProfit> findByLogin(String login) {
        return investorProfitRepository.findByLoginOrderByYearSale(login);
    }

}
