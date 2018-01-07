package com.art.controllers;

import com.art.func.GetPrincipalFunc;
import com.art.model.CashTypes;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.SearchSummary;
import com.art.service.CashTypesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Controller
public class CashTypesController {
    @Resource(name = "getPrincipalFunc")
    private GetPrincipalFunc getPrincipalFunc;

    @Resource(name = "cashTypesService")
    private CashTypesService cashTypesService;

    @GetMapping(value = "/viewcashtypes")
    public String cashTypesPage(ModelMap model) {

        List<CashTypes> cashTypes = cashTypesService
                .findAll();
        model.addAttribute("cashTypes", cashTypes);
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());

        return "viewcashtypes";
    }

    @GetMapping(value = { "/newcashtype" })
    public String newCashSource(ModelMap model) {
        String title = "Добавление вида денег";
        CashTypes cashTypes = new CashTypes();
        model.addAttribute("cashTypes", cashTypes);
        model.addAttribute("edit", false);
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());
        model.addAttribute("title", title);
        return "addcashtypes";
    }

    @PostMapping(value = { "/savecashtype" }, produces="application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse saveCashType(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        CashTypes cashTypes = new CashTypes();
        cashTypes.setCashType(searchSummary.getCashType());
        cashTypesService.create(cashTypes);
        response.setMessage("Вид денег <b>" + cashTypes.getCashType() + "</b> успешно добавлен.");
        return response;
    }


    @GetMapping(value = { "/edit-cashtype-{id}" })
    public String editCashType(@PathVariable BigInteger id, ModelMap model) {
        String title = "Обновление вида денег";
        CashTypes cashTypes = cashTypesService.findById(id);

        model.addAttribute("cashTypes", cashTypes);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());
        model.addAttribute("title", title);
        return "addcashtypes";
    }

    /**
     * This method will be called on form submission, handling POST request for
     * updating user in database. It also validates the user input
     */
    @PostMapping(value = { "/editcashtype" }, produces="application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse updateSource(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        CashTypes cashTypes = cashTypesService.findById(new BigInteger(searchSummary.getCashTypeId()));
        cashTypes.setCashType(searchSummary.getCashType());
        cashTypesService.update(cashTypes);
        response.setMessage("Вид денег <b>" + cashTypes.getCashType() + "</b> успешно изменён.");
        return response;
    }

    @PostMapping(value = { "/deletetype" }, produces="application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse deleteSource(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        cashTypesService.deleteById(new BigInteger(searchSummary.getCashTypeId()));
        response.setMessage("Вид денег успешно удалён.");
        return response;
    }
}
