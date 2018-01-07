package com.art.controllers;

import com.art.func.GetPrincipalFunc;
import com.art.model.TypeClosingInvest;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.SearchSummary;
import com.art.service.TypeClosingInvestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Controller
public class TypeClosingInvestController {

    @Resource(name = "getPrincipalFunc")
    private GetPrincipalFunc getPrincipalFunc;

    @Resource(name = "typeClosingInvestService")
    private TypeClosingInvestService typeClosingInvestService;

    @GetMapping(value = "/viewTypesClosingInvest")
    public ModelAndView typeClosingInvestPage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("viewTypesClosingInvest");
        List<TypeClosingInvest> typeClosingInvestsList = typeClosingInvestService.findAllWithCriteriaApi();
        modelAndView.addObject("typeClosingInvestsList", typeClosingInvestsList);
        modelAndView.addObject("loggedinuser", getPrincipalFunc.getLogin());
        return modelAndView;
    }

    @GetMapping(value = { "/newTypeClosing" })
    public String newTypeClosing(ModelMap model) {
        String title = "Добавление вида закрытия";
        TypeClosingInvest typeClosingInvest = new TypeClosingInvest();
        model.addAttribute("typeClosingInvest", typeClosingInvest);
        model.addAttribute("edit", false);
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());
        model.addAttribute("title", title);
        return "addTypeClosingInvest";
    }

    @PostMapping(value = { "/saveTypeClosing" }, produces="application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse saveTypeClosing(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        TypeClosingInvest typeClosingInvest = new TypeClosingInvest();
        typeClosingInvest.setTypeClosingInvest(searchSummary.getTypeClosingInvest());
        typeClosingInvestService.create(typeClosingInvest);
        response.setMessage("Вид закрытия <b>" + typeClosingInvest.getTypeClosingInvest() + "</b> успешно добавлен.");
        return response;
    }


    @GetMapping(value = { "/edit-typeClosing-{id}" })
    public String editTypeClosing(@PathVariable BigInteger id, ModelMap model) {
        String title = "Обновление вида закрытия";
        TypeClosingInvest typeClosingInvest = typeClosingInvestService.findById(id);

        model.addAttribute("typeClosingInvest", typeClosingInvest);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());
        model.addAttribute("title", title);
        return "addTypeClosingInvest";
    }

    /**
     * This method will be called on form submission, handling POST request for
     * updating user in database. It also validates the user input
     */
    @PostMapping(value = { "/editTypeClosing" }, produces="application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse updateTypeClosing(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        TypeClosingInvest typeClosingInvest = typeClosingInvestService.findById(new BigInteger(searchSummary.getTypeClosingInvestId()));
        typeClosingInvest.setTypeClosingInvest(searchSummary.getTypeClosingInvest());
        typeClosingInvestService.update(typeClosingInvest);
        response.setMessage("Вид закрытия <b>" + typeClosingInvest.getTypeClosingInvest() + "</b> успешно изменён.");
        return response;
    }

    @PostMapping(value = { "/deleteTypeClosing" }, produces="application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse deleteTypeClosing(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        typeClosingInvestService.deleteById(new BigInteger(searchSummary.getTypeClosingInvestId()));
        response.setMessage("Вид закрытия успешно удалён.");
        return response;
    }
}
