package com.art.controllers;

import com.art.func.GetPrincipalFunc;
import com.art.model.supporting.InvestorsPlanSale;
import com.art.service.InvestorsExpensesService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class InvestorsPlanSaleController {

    @Resource(name = "investorsExpensesService")
    private InvestorsExpensesService investorsExpensesService;

    @Resource(name = "getPrincipalFunc")
    private GetPrincipalFunc getPrincipalFunc;

    @RequestMapping(value = "/investorsplansale", method = RequestMethod.GET)
    public String investorsTotalSumPage(ModelMap model) {
        List<InvestorsPlanSale> investorsPlanSales = investorsExpensesService.getInvestorsPlanSale(
                getPrincipalFunc.getPrincipalId()
        );
        model.addAttribute("investorsPlanSales", investorsPlanSales);

        return "investorsplansales";
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

}
