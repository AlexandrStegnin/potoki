package com.art.controllers;

import com.art.func.GetPrincipalFunc;
import com.art.model.NewCashDetails;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.SearchSummary;
import com.art.service.NewCashDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Controller
public class NewCashDetailsController {
    @Resource(name = "getPrincipalFunc")
    private GetPrincipalFunc getPrincipalFunc;

    @Resource(name = "newCashDetailsService")
    private NewCashDetailsService newCashDetailsService;

    @GetMapping(value = "/viewnewcashdetails")
    public String newCashDetailsPage(ModelMap model) {

        List<NewCashDetails> newCashDetails = newCashDetailsService
                .findAll();
        model.addAttribute("newCashDetails", newCashDetails);
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());

        return "viewnewcashdetails";
    }

    @GetMapping(value = { "/newcashdetail" })
    public String newCashDetail(ModelMap model) {
        String title = "Добавление деталей новых денег";
        NewCashDetails newCashDetails = new NewCashDetails();
        model.addAttribute("newCashDetails", newCashDetails);
        model.addAttribute("edit", false);
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());
        model.addAttribute("title", title);
        return "addnewcashdetail";
    }

    @PostMapping(value = { "/savenewcashdetail" }, produces="application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse saveNewCashDetail(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        NewCashDetails newCashDetails = new NewCashDetails();
        newCashDetails.setNewCashDetail(searchSummary.getNewCashDetail());
        newCashDetailsService.create(newCashDetails);
        response.setMessage("Детали новых денег <b>" + newCashDetails.getNewCashDetail() + "</b> успешно добавлены.");
        return response;
    }


    @GetMapping(value = { "/edit-newcashdetail-{id}" })
    public String editNewCashDetail(@PathVariable BigInteger id, ModelMap model) {
        String title = "Обновление деталей новых денег";
        NewCashDetails newCashDetails = newCashDetailsService.findById(id);

        model.addAttribute("newCashDetails", newCashDetails);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());
        model.addAttribute("title", title);
        return "addnewcashdetail";
    }

    /**
     * This method will be called on form submission, handling POST request for
     * updating user in database. It also validates the user input
     */
    @PostMapping(value = { "/editnewcashdetail" }, produces="application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse updateNewCashDetail(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        NewCashDetails newCashDetails = newCashDetailsService.findById(new BigInteger(searchSummary.getNewCashDetailId()));
        newCashDetails.setNewCashDetail(searchSummary.getNewCashDetail());
        newCashDetailsService.update(newCashDetails);
        response.setMessage("Детали новых денег <b>" + newCashDetails.getNewCashDetail() + "</b> успешно изменены.");
        return response;
    }

    @PostMapping(value = { "/deletenewcashdetail" }, produces="application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse deleteSource(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        newCashDetailsService.deleteById(new BigInteger(searchSummary.getNewCashDetailId()));
        response.setMessage("Детали новых денег успешно удалены.");
        return response;
    }
}
