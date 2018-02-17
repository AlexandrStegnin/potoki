package com.art.controllers;

import com.art.model.TypeExpenses;
import com.art.service.TypeExpensesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Controller
public class TypeExpensesController {

    @Resource(name = "typeExpensesService")
    private TypeExpensesService typeExpensesService;

    @GetMapping(value = "/typeexp")
    public String typeExpensesPage(ModelMap model) {

        List<TypeExpenses> typeExpenses = typeExpensesService.findAll();
        model.addAttribute("typeExpenses", typeExpenses);

        return "viewtypeexp";
    }

    @GetMapping(value = {"/edit-typeexp-{id}"})
    public String editTypeExpenses(@PathVariable BigInteger id, ModelMap model) {
        String title = "Обновление данных по типам расходов";
        TypeExpenses typeExpenses = typeExpensesService.findById(id);
        model.addAttribute("typeExpenses", typeExpenses);
        model.addAttribute("edit", true);
        model.addAttribute("title", title);
        return "addtypeexp";
    }

    @PostMapping(value = {"/edit-typeexp-{id}"})
    public String updateTypeExpenses(@ModelAttribute("typeExpenses") TypeExpenses typeExpenses,
                                     BindingResult result, ModelMap model) {
        String ret = "списку типов расходов.";
        String redirectUrl = "/typeexp";
        if (result.hasErrors()) {
            return "addtypeexp";
        }

        typeExpensesService.update(typeExpenses);

        model.addAttribute("success", "Данные по типу расходов " +
                typeExpenses.getTypeExp() +
                " успешно обновлены.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @GetMapping(value = {"/delete-typeexp-{id}"})
    public String deleteTypeExpenses(@PathVariable BigInteger id) {
        typeExpensesService.delete(id);
        return "redirect:/typeexp";
    }

    @GetMapping(value = {"/newtypeexp"})
    public String newTypeExpenses(ModelMap model) {
        String title = "Добавление типа расходов";
        TypeExpenses typeExpenses = new TypeExpenses();
        model.addAttribute("typeExpenses", typeExpenses);
        model.addAttribute("edit", false);
        model.addAttribute("title", title);
        return "addtypeexp";
    }

    @PostMapping(value = {"/newtypeexp"})
    public String saveTypeExpenses(@ModelAttribute("typeExpenses") TypeExpenses typeExpenses,
                                   BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "addtypeexp";
        }
        String ret = "списку типов расходов";
        String redirectUrl = "/typeexp";

        typeExpensesService.create(typeExpenses);

        model.addAttribute("success", "Тип расходов " + typeExpenses.getTypeExp() +
                " успешно добавлен.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

}
