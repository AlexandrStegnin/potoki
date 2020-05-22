package com.art.service.view;

import com.art.config.SecurityUtils;
import com.art.model.supporting.view.InvestorProfit;
import com.art.repository.view.InvestorProfitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
        if (Objects.isNull(login)) {
            login = SecurityUtils.getUsername();
        }
        return investorProfitRepository.findByLoginOrderByYearSale(login);
    }

}
