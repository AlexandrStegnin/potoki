package com.art.controllers.view;

import com.art.model.supporting.view.CompanyProfit;
import com.art.service.view.CompanyProfitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@RestController
public class CompanyProfitController {

    private final CompanyProfitService companyProfitService;

    public CompanyProfitController(CompanyProfitService companyProfitService) {
        this.companyProfitService = companyProfitService;
    }

    @GetMapping(path = "/company-profit")
    public List<CompanyProfit> findAll() {
        return companyProfitService.findAll();
    }

}
