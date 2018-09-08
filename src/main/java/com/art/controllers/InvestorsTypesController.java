package com.art.controllers;

import com.art.model.InvestorsTypes;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.SearchSummary;
import com.art.service.InvestorsTypesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Controller
public class InvestorsTypesController {

    @Resource(name = "investorsTypesService")
    private InvestorsTypesService investorsTypesService;

    @GetMapping(value = "/viewinvestorstypes")
    public String investorsTypesPage(ModelMap model) {

        List<InvestorsTypes> investorsTypes = investorsTypesService
                .findAll();
        model.addAttribute("investorsTypes", investorsTypes);

        return "viewinvestorstypes";
    }

    @GetMapping(value = {"/newinvtype"})
    public String newInvType(ModelMap model) {
        String title = "Добавление типа инвесторов";
        InvestorsTypes investorsTypes = new InvestorsTypes();
        model.addAttribute("investorsTypes", investorsTypes);
        model.addAttribute("edit", false);
        model.addAttribute("title", title);
        return "addinvestorstype";
    }

    @PostMapping(value = {"/saveinvtype"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse saveUser(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        InvestorsTypes investorsTypes = new InvestorsTypes();
        investorsTypes.setInvestorsType(searchSummary.getInvType());
        investorsTypesService.create(investorsTypes);
        response.setMessage("Тип инвестора <b>" + investorsTypes.getInvestorsType() + "</b> успешно добавлен.");
        return response;
    }


    @GetMapping(value = {"/edit-invtype-{id}"})
    public String editInvType(@PathVariable BigInteger id, ModelMap model) {
        String title = "Обновление типов инвесторов";
        InvestorsTypes investorsTypes = investorsTypesService.findById(id);

        model.addAttribute("investorsTypes", investorsTypes);
        model.addAttribute("edit", true);
        model.addAttribute("title", title);
        return "addinvestorstype";
    }

    @PostMapping(value = {"/editinvtype"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse updateSource(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        InvestorsTypes investorsTypes = investorsTypesService.findById(new BigInteger(searchSummary.getInvTypeId()));
        investorsTypes.setInvestorsType(searchSummary.getInvType());
        investorsTypesService.update(investorsTypes);
        response.setMessage("Тип инвестора <b>" + investorsTypes.getInvestorsType() + "</b> успешно изменён.");
        return response;
    }

    @PostMapping(value = {"/deleteinvtype"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse deleteSource(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        investorsTypesService.deleteById(new BigInteger(searchSummary.getInvTypeId()));
        response.setMessage("Тип инвестора успешно удалён.");
        return response;
    }
}
