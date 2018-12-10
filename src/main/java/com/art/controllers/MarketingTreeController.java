package com.art.controllers;

import com.art.model.MarketingTree;
import com.art.model.Users;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.KinEnum;
import com.art.model.supporting.filters.MarketingTreeFilter;
import com.art.service.MarketingTreeService;
import com.art.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class MarketingTreeController {

    private final UserService userService;
    private final MarketingTreeService marketingTreeService;
    private MarketingTreeFilter filters = new MarketingTreeFilter();

    @Autowired
    public MarketingTreeController(MarketingTreeService marketingTreeService, UserService userService) {
        this.userService = userService;
        this.marketingTreeService = marketingTreeService;
    }

    @GetMapping(value = {"/marketingTree"})
    public String marketingTreePage(ModelMap model) {
        String title = "Маркетинговое дерево";
        List<KinEnum> kins = new ArrayList<>(
                Arrays.asList(KinEnum.values()));
        Pageable pageable = new PageRequest(filters.getPageNumber(), filters.getPageSize());
        Page<MarketingTree> page = marketingTreeService.findAll(filters, pageable);

        model.addAttribute("filters", filters);
        model.addAttribute("page", page);
        model.addAttribute("kins", kins);
        model.addAttribute("title", title);
        return "viewmarketingtree";
    }

    @PostMapping(value = "/marketingTree")
    public ModelAndView marketingTreeWithFilter(
            @ModelAttribute("filters") MarketingTreeFilter filters) {
        ModelAndView modelAndView = new ModelAndView("viewmarketingtree");
        String title = "Маркетинговое дерево";
        List<KinEnum> kins = new ArrayList<>(
                Arrays.asList(KinEnum.values()));
        Pageable pageable = new PageRequest(filters.getPageNumber(), filters.getPageSize());
        Page<MarketingTree> page = marketingTreeService.findAll(filters, pageable);
        modelAndView.addObject("page", page);
        modelAndView.addObject("kins", kins);
        modelAndView.addObject("title", title);
        modelAndView.addObject("filters", filters);

        return modelAndView;
    }

    @PostMapping(value = {"/updateMarketingTree"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse updateMarketingTree() {
        GenericResponse response = new GenericResponse();
        marketingTreeService.updateMarketingTree(BigInteger.ZERO);
        response.setMessage("Данные маркетингового дерева успешно обновлены");
        return response;
    }

    @ModelAttribute("investors")
    public List<Users> initializeInvestors() {
        return userService.initializeInvestors();
    }

    @ModelAttribute("partners")
    public List<Users> initializePartners() {
        return userService.initializePartners();
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

}
