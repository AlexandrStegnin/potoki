package com.art.controllers;

import com.art.model.Facilities;
import com.art.model.FacilitiesBuySales;
import com.art.model.UnderFacilities;
import com.art.model.supporting.enums.BuySalesEnum;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class FacilitiesBuySalesController {
    @Resource(name = "facilitiesBuySalesService")
    private FacilitiesBuySalesService facilitiesBuySalesService;

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @Resource(name = "underFacilitiesService")
    private UnderFacilitiesService underFacilitiesService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "stuffService")
    private StuffService stuffService;

    @GetMapping(value = "/facilitiesbuysales")
    public String facilitiesBuySalesPage(ModelMap model) {

        List<FacilitiesBuySales> facilitiesBuySales = facilitiesBuySalesService.findAll();
        model.addAttribute("facilitiesBuySales", facilitiesBuySales);

        return "viewfacilitiesbuysales";
    }

    @GetMapping(value = {"/edit-buysales-{id}"})
    public String editFacilitiesBuySales(@PathVariable BigInteger id, ModelMap model) {
        String title = "Обновление данных по продаже/покупке объекта";
        FacilitiesBuySales facilitiesBuySales = facilitiesBuySalesService.findById(id);
        List<BuySalesEnum> enums = new ArrayList<BuySalesEnum>(
                Arrays.asList(BuySalesEnum.values()));
        model.addAttribute("enums", enums);
        model.addAttribute("facilitiesBuySales", facilitiesBuySales);
        model.addAttribute("edit", true);
        model.addAttribute("title", title);
        return "addfacilitiesbuysales";
    }

    @PostMapping(value = {"/edit-buysales-{id}"})
    public String updateFacilitiesBuySales(@ModelAttribute("facilitiesBuySales") FacilitiesBuySales facilitiesBuySales,
                                           BindingResult result, ModelMap model) {
        String ret = "списку продаж/покупок объектов.";
        String redirectUrl = "/facilitiesbuysales";
        if (result.hasErrors()) {
            return "addfacilitiesbuysales";
        }

        facilitiesBuySalesService.update(facilitiesBuySales);

        model.addAttribute("success", "Данные по покупке/продаже объекта " +
                facilitiesBuySales.getFacility().getFacility() +
                " успешно обновлены.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @GetMapping(value = {"/delete-buysales-{id}"})
    public String deleteFacilitiesBuySales(@PathVariable BigInteger id) {
        facilitiesBuySalesService.deleteById(id);
        return "redirect:/facilitiesbuysales";
    }

    @GetMapping(value = {"/newbuysales"})
    public String newFacilitiesBuySales(ModelMap model) {
        String title = "Добавление покупки/продажи объекта";
        FacilitiesBuySales facilitiesBuySales = new FacilitiesBuySales();
        model.addAttribute("facilitiesBuySales", facilitiesBuySales);
        List<BuySalesEnum> enums = new ArrayList<BuySalesEnum>(
                Arrays.asList(BuySalesEnum.values()));
        model.addAttribute("enums", enums);
        model.addAttribute("edit", false);
        model.addAttribute("title", title);
        return "addfacilitiesbuysales";
    }

    @PostMapping(value = {"/newbuysales"})
    public String saveFacilitiesBuySales(@ModelAttribute("facilitiesBuySales") FacilitiesBuySales facilitiesBuySales,
                                         BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "addfacilitiesbuysales";
        }
        String ret = "списку покупки/продажи объектов";
        String redirectUrl = "/facilitiesbuysales";

        facilitiesBuySalesService.create(facilitiesBuySales);

        model.addAttribute("success", "Покупка/продажа объекта " + facilitiesBuySales.getFacility().getFacility() +
                " успешно добавлена.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @ModelAttribute("facilities")
    public List<Facilities> initializeFacilities() {
        return facilityService.findAll();
    }

    @ModelAttribute("underFacilities")
    public List<UnderFacilities> initializeUnderFacilities() {
        return underFacilitiesService.findAll();
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
