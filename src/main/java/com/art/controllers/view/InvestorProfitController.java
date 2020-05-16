package com.art.controllers.view;

import com.art.model.supporting.view.InvestorProfit;
import com.art.service.view.InvestorProfitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@RestController
public class InvestorProfitController {

    private final InvestorProfitService investorProfitService;

    public InvestorProfitController(InvestorProfitService investorProfitService) {
        this.investorProfitService = investorProfitService;
    }

    @GetMapping(path = "/investor-profit")
    public List<InvestorProfit> findAll() {
        return investorProfitService.findByLogin("investor007");
    }

}
