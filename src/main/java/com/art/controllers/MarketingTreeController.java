package com.art.controllers;

import com.art.model.MarketingTree;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.KinEnum;
import com.art.repository.MarketingTreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class MarketingTreeController {

    private final MarketingTreeRepository marketingTreeRepository;

    @Autowired
    public MarketingTreeController(MarketingTreeRepository marketingTreeRepository) {
        this.marketingTreeRepository = marketingTreeRepository;
    }

    @GetMapping(value = {"/marketingTree"})
    public String marketingTreePage(ModelMap model) {
        String title = "Маркетинговое дерево";
        List<KinEnum> kins = new ArrayList<>(
                Arrays.asList(KinEnum.values()));
        List<MarketingTree> trees = marketingTreeRepository.findAll();

        model.addAttribute("trees", trees);
        model.addAttribute("kins", kins);
        model.addAttribute("title", title);
        return "viewmarketingtree";
    }

    @PostMapping(value = {"/updateMarketingTree"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse updateMarketingTree() {
        GenericResponse response = new GenericResponse();
        marketingTreeRepository.updateMarketingTree(BigInteger.ZERO);
        response.setMessage("Данные маркетингового дерева успешно обновлены");
        return response;
    }
}
