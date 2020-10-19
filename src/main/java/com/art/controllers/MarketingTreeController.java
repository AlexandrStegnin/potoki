package com.art.controllers;

import com.art.config.application.Location;
import com.art.model.AppUser;
import com.art.model.MarketingTree;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.enums.KinEnum;
import com.art.model.supporting.filters.MarketingTreeFilter;
import com.art.service.MarketingTreeService;
import com.art.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class MarketingTreeController {

    private final UserService userService;
    private final MarketingTreeService marketingTreeService;
    private final MarketingTreeFilter filter = new MarketingTreeFilter();

    @Autowired
    public MarketingTreeController(MarketingTreeService marketingTreeService, UserService userService) {
        this.userService = userService;
        this.marketingTreeService = marketingTreeService;
    }

    @GetMapping(path = Location.MARKETING_TREE)
    public ModelAndView marketingTreePage(@PageableDefault(size = 100) @SortDefault Pageable pageable) {
        return prepareModel(filter);
    }

    @PostMapping(path = Location.MARKETING_TREE)
    public ModelAndView marketingTreeWithFilter(@ModelAttribute("filter") MarketingTreeFilter filter) {
        return prepareModel(filter);
    }

    /**
     * Подготовить модель для страницы
     *
     * @param filter фильтры
     */
    private ModelAndView prepareModel(MarketingTreeFilter filter) {
        ModelAndView model = new ModelAndView("marketing-tree");
        Pageable pageable = new PageRequest(filter.getPageNumber(), filter.getPageSize());
        Page<MarketingTree> page = marketingTreeService.findAll(filter, pageable);
        model.addObject("page", page);
        model.addObject("filter", filter);
        return model;
    }

    @ResponseBody
    @PostMapping(path = Location.MARKETING_TREE_UPDATE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public GenericResponse updateMarketingTree() {
        GenericResponse response = new GenericResponse();
        response.setMessage(marketingTreeService.updateMarketingTreeFromApp());
        return response;
    }

    @ModelAttribute("investors")
    public List<AppUser> initializeInvestors() {
        return userService.initializeInvestors();
    }

    @ModelAttribute("partners")
    public List<AppUser> initializePartners() {
        return userService.initializePartners();
    }

    @ModelAttribute("kins")
    public List<KinEnum> initKins() {
        return new ArrayList<>(
                Arrays.asList(KinEnum.values()));
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

}
