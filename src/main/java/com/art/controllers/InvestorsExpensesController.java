package com.art.controllers;

import com.art.model.Facilities;
import com.art.model.InvestorsExpenses;
import com.art.model.TypeExpenses;
import com.art.model.Users;
import com.art.model.supporting.InvestorsExpEnum;
import com.art.service.*;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class InvestorsExpensesController {

    @Resource(name = "investorsExpensesService")
    private InvestorsExpensesService investorsExpensesService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @Resource(name = "stuffService")
    private StuffService stuffService;

    @Resource(name = "typeExpensesService")
    private TypeExpensesService typeExpensesService;

    @GetMapping(value = "/investorsexp")
    public String investorsExpensesPage(ModelMap model) {

        List<InvestorsExpenses> investorsExpenses = investorsExpensesService.findAll();
        model.addAttribute("investorsExpenses", investorsExpenses);

        return "viewinvestorsexp";
    }

    @GetMapping(value = {"/edit-investorsexp-{id}"})
    public String editInvestorsExpenses(@PathVariable BigInteger id, ModelMap model) {
        String title = "Обновление данных по расходам инвесторов";
        InvestorsExpenses investorsExpenses = investorsExpensesService.findById(id);
        List<InvestorsExpEnum> enums = new ArrayList<InvestorsExpEnum>(
                Arrays.asList(InvestorsExpEnum.values()));
        model.addAttribute("enums", enums);
        model.addAttribute("investorsExpenses", investorsExpenses);
        model.addAttribute("edit", true);
        model.addAttribute("title", title);
        return "addinvestorsexp";
    }

    @PostMapping(value = {"/edit-investorsexp-{id}"})
    public String updateInvestorsExpenses(@ModelAttribute("investorsExpenses") InvestorsExpenses investorsExpenses,
                                          BindingResult result, ModelMap model) {
        String ret = "списку расходов инвесторов.";
        String redirectUrl = "/investorsexp";
        if (result.hasErrors()) {
            return "addinvestorsexp";
        }

        investorsExpensesService.update(investorsExpenses);

        model.addAttribute("success", "Данные по расходам инвестора успешно обновлены.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @GetMapping(value = {"/delete-investorsexp-{id}"})
    public String deleteInvestorsExpenses(@PathVariable BigInteger id) {
        investorsExpensesService.deleteById(id);
        return "redirect:/investorsexp";
    }

    @GetMapping(value = {"/newinvestorsexp"})
    public String newInvestorsExpenses(ModelMap model) {
        String title = "Добавление расхода инвестора";
        InvestorsExpenses investorsExpenses = new InvestorsExpenses();
        List<InvestorsExpEnum> enums = new ArrayList<InvestorsExpEnum>(
                Arrays.asList(InvestorsExpEnum.values()));
        model.addAttribute("enums", enums);
        model.addAttribute("investorsExpenses", investorsExpenses);
        model.addAttribute("edit", false);
        model.addAttribute("title", title);
        return "addinvestorsexp";
    }

    @PostMapping(value = {"/newinvestorsexp"})
    public String saveInvestorsExp(@ModelAttribute("investorsExpenses") InvestorsExpenses investorsExpenses,
                                   BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "addinvestorsexp";
        }
        String ret = "списку расходов инвесторов";
        String redirectUrl = "/investorsexp";

        investorsExpensesService.create(investorsExpenses);

        model.addAttribute("success", "Расходы инвестора успешно добавлены.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @ModelAttribute("facilities")
    public List<Facilities> initializeFacilities() {
        return facilityService.findAll();
    }

    @ModelAttribute("investors")
    public List<Users> initializeRentors() {
        return userService.findRentors(stuffService.findByStuff("Инвестор").getId());
    }

    @ModelAttribute("typeExpenses")
    public List<TypeExpenses> initializeTypeExpenses() {
        return typeExpensesService.findAll();
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

}
