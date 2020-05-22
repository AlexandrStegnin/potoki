package com.art.controllers.view;

import com.art.model.supporting.view.CompanyInvestorProfit;
import com.art.model.supporting.view.CompanyProfit;
import com.art.model.supporting.view.InvestorProfit;
import com.art.service.view.CompanyProfitService;
import com.art.service.view.InvestorProfitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@RestController
public class ProfitController {

    private final CompanyProfitService companyProfitService;

    private final InvestorProfitService investorProfitService;

    public ProfitController(CompanyProfitService companyProfitService,
                            InvestorProfitService investorProfitService) {
        this.companyProfitService = companyProfitService;
        this.investorProfitService = investorProfitService;
    }

    @GetMapping(path = "/union-profit")
    public List<CompanyInvestorProfit> findByLogin(@RequestParam(required = false) String login) {
        List<CompanyInvestorProfit> profitUnions = new ArrayList<>();
        List<CompanyProfit> companyProfits = companyProfitService.findAll();
        List<InvestorProfit> investorProfits = investorProfitService.findByLogin(login);
        companyProfits.forEach(companyProfit -> {
            CompanyInvestorProfit union = new CompanyInvestorProfit();
            union.setYearSale(companyProfit.getYearSale());
            union.setProfit(companyProfit.getProfit());
            profitUnions.add(union);
        });
        profitUnions.forEach(profitUnion -> investorProfits.stream()
                .filter(investorProfit -> investorProfit.getYearSale() == profitUnion.getYearSale())
                .findFirst()
                .ifPresent(profit -> {
                    profitUnion.setLogin(profit.getLogin());
                    profitUnion.setInvestorProfit(profit.getProfit());
                }));
        return profitUnions;
    }

}
