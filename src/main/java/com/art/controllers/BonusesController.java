package com.art.controllers;

import com.art.func.GetPrincipalFunc;
import com.art.model.BonusTypes;
import com.art.model.Bonuses;
import com.art.model.Facilities;
import com.art.model.Users;
import com.art.service.*;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class BonusesController {
    @Resource(name = "bonusesService")
    private BonusesService bonusesService;

    @Resource(name = "bonusTypesService")
    private BonusTypesService bonusTypesService;

    @Resource(name = "getPrincipalFunc")
    private GetPrincipalFunc getPrincipalFunc;

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @Resource(name = "stuffService")
    private StuffService stuffService;

    @Resource(name = "userService")
    private UserService userService;

    @GetMapping(value = "/bonuses")
    public String bonusesPage(ModelMap model) {

        List<Bonuses> bonuses = bonusesService.findAll();
        model.addAttribute("bonuses", bonuses);

        return "viewbonuses";
    }

    @GetMapping(value = { "/edit-bonuses-{id}" })
    public String editBonuses(@PathVariable BigInteger id, ModelMap model) {
        String title = "Обновление данных по бонусам";
        Bonuses bonuses = bonusesService.findById(id);
        model.addAttribute("bonuses", bonuses);
        model.addAttribute("edit", true);
        model.addAttribute("title", title);
        return "addbonuses";
    }

    @PostMapping(value = { "/edit-bonuses-{id}" })
    public String updateBonuses(@ModelAttribute("bonuses") Bonuses bonuses,
                                           BindingResult result, ModelMap model) {
        String ret = "списку бонусов.";
        String redirectUrl = "/bonuses";
        if (result.hasErrors()) {
            return "addbonuses";
        }

        bonusesService.update(bonuses);

        model.addAttribute("success", "Данные по бонусам объекта " +
                bonuses.getFacility().getFacility() +
                " успешно обновлены.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @GetMapping(value = { "/delete-bonuses-{id}" })
    public String deleteBonuses(@PathVariable BigInteger id) {
        bonusesService.deleteById(id);
        return "redirect:/bonuses";
    }

    @GetMapping(value = { "/newbonuses" })
    public String newBonuses(ModelMap model) {
        String title = "Добавление бонуса";
        Bonuses bonuses = new Bonuses();
        model.addAttribute("bonuses", bonuses);
        model.addAttribute("edit", false);
        model.addAttribute("title", title);
        return "addbonuses";
    }

    @PostMapping(value = { "/newbonuses" })
    public String saveBonuses(@ModelAttribute("bonuses") Bonuses bonuses,
                                         BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "addbonuses";
        }
        String ret = "списку бонусов";
        String redirectUrl = "/bonuses";

        bonusesService.create(bonuses);

        model.addAttribute("success", "Бонус по объекту " + bonuses.getFacility().getFacility() +
                " успешно добавлен.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @ModelAttribute("facilities")
    public List<Facilities> initializeFacilities() {
        return facilityService.findAll();
    }

    @ModelAttribute("rentors")
    public List<Users> initializeRentors() {
        return userService.findRentors(stuffService.findByStuff("Арендатор").getId());
    }

    @ModelAttribute("managers")
    public List<Users> initializeManagers() {
        return userService.findRentors(stuffService.findByStuff("Управляющий").getId());
    }

    @ModelAttribute("bonusTypes")
    public List<BonusTypes> initializeBonusTypes() {
        return bonusTypesService.findAll();
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
