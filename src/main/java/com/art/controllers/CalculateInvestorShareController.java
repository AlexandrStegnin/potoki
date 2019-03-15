package com.art.controllers;

import com.art.model.supporting.filters.InvShareFilter;
import com.art.repository.CalculateInvestorShareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Controller
@RequestMapping(path = "/calculateInvShare")
public class CalculateInvestorShareController {

    private final CalculateInvestorShareRepository calcInvShareRepo;

    @Autowired
    public CalculateInvestorShareController(CalculateInvestorShareRepository calcInvShareRepo) {
        this.calcInvShareRepo = calcInvShareRepo;
    }

    @GetMapping
    public ModelAndView calcInvSharePage() {
        return getModelAndView("");
    }

    @PostMapping
    public ModelAndView calculateInvestorShare(
            @RequestParam(name = "yearFrom", required = false) Integer yearFrom,
            @RequestParam(name = "yearTo", required = false) Integer yearTo) {
        calcInvShareRepo.calculateInvShare(yearFrom, yearTo);
        return getModelAndView("Данные долей инвесторов успешно обновлены");
    }

    private ModelAndView getModelAndView(String message) {
        ModelAndView modelAndView = new ModelAndView("calculateInvestorShare");
        List<Integer> years = calcInvShareRepo.getYearsFromInvCash();
        InvShareFilter invShareFilter = new InvShareFilter();
        invShareFilter.setYearFrom(years.get(0));
        invShareFilter.setYearTo(years.get(years.size() - 1));
        modelAndView.addObject("yearFrom", years);
        modelAndView.addObject("yearTo", years);
        modelAndView.addObject("invShareFilter", invShareFilter);
        modelAndView.addObject("messageResponse", message);
        return modelAndView;
    }

}
