package com.art.controllers;

import com.art.model.CashSources;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.SearchSummary;
import com.art.service.CashSourcesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Controller
public class CashSourcesController {

    @Resource(name = "cashSourcesService")
    private CashSourcesService cashSourcesService;

    @GetMapping(value = "/viewcashsources")
    public String cashSourcesPage(ModelMap model) {

        List<CashSources> cashSources = cashSourcesService
                .findAll();
        model.addAttribute("cashSources", cashSources);

        return "viewcashsources";
    }

    @GetMapping(value = {"/newcashsource"})
    public String newCashSource(ModelMap model) {
        String title = "Добавление источника денег";
        CashSources cashSources = new CashSources();
        model.addAttribute("cashSources", cashSources);
        model.addAttribute("edit", false);
        model.addAttribute("title", title);
        return "addcashsource";
    }

    @PostMapping(value = {"/savecashsource"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse saveUser(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        CashSources cashSources = new CashSources();
        cashSources.setCashSource(searchSummary.getCashSource());
        cashSourcesService.create(cashSources);
        response.setMessage("Источник денег <b>" + cashSources.getCashSource() + "</b> успешно добавлен.");
        return response;
    }


    @GetMapping(value = {"/edit-cashsource-{id}"})
    public String editCashSource(@PathVariable BigInteger id, ModelMap model) {
        String title = "Обновление источника получения денег";
        CashSources cashSources = cashSourcesService.findById(id);

        model.addAttribute("cashSources", cashSources);
        model.addAttribute("edit", true);
        model.addAttribute("title", title);
        return "addcashsource";
    }

    @PostMapping(value = {"/editcashsource"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse updateSource(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        CashSources cashSources = cashSourcesService.findById(new BigInteger(searchSummary.getCashSourceId()));
        cashSources.setCashSource(searchSummary.getCashSource());
        cashSourcesService.update(cashSources);
        response.setMessage("Источник денег <b>" + cashSources.getCashSource() + "</b> успешно изменён.");
        return response;
    }

    @PostMapping(value = {"/deletesource"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse deleteSource(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        cashSourcesService.deleteById(new BigInteger(searchSummary.getCashSourceId()));
        response.setMessage("Источник успешно удалён.");
        return response;
    }
}
