package com.art.controllers;

import com.art.model.supporting.view.InvestorAnnex;
import com.art.service.view.InvestorAnnexService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Controller
public class AnnexController {

    private final InvestorAnnexService annexService;

    public AnnexController(InvestorAnnexService annexService) {
        this.annexService = annexService;
    }

    @GetMapping(path = "/investor/annexes")
    public String getAnnexes(ModelMap model) {
        List<InvestorAnnex> contracts = annexService.findAll();
        model.addAttribute("contracts", contracts);
        return "annex";
    }

}
