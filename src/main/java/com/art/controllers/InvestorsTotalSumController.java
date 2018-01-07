package com.art.controllers;

import com.art.func.GetPrincipalFunc;
import com.art.model.supporting.InvestorsTotalSum;
import com.art.service.InvestorsCashService;
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
public class InvestorsTotalSumController {

    @Resource(name = "investorsCashService")
    private InvestorsCashService investorsCashService;

    @Resource(name = "getPrincipalFunc")
    private GetPrincipalFunc getPrincipalFunc;

    @RequestMapping(value = "/investorstotalsum", method = RequestMethod.GET)
    public String investorsTotalSumPage(ModelMap model) {
        /*
        List<InvestorsTotalSum> investorsTotalSums = investorsCashService.getInvestorsTotalSum(
                getPrincipalFunc.getPrincipalId()
        );
        model.addAttribute("investorsTotalSums", investorsTotalSums);
        */
        List<InvestorsTotalSum> investorsTotalSums = investorsCashService.getInvestorsCashSums(getPrincipalFunc.getPrincipalId()
        );
        model.addAttribute("investorsTotalSums", investorsTotalSums);
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());

        return "investorstotalsums";
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
