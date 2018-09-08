package com.art.controllers;

import com.art.model.Facilities;
import com.art.model.FacilitiesReserves;
import com.art.model.UnderFacilities;
import com.art.service.FacilitiesReservesService;
import com.art.service.FacilityService;
import com.art.service.StuffService;
import com.art.service.UnderFacilitiesService;
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
public class FacilitiesReservesController {

    @Resource(name = "facilitiesReservesService")
    private FacilitiesReservesService facilitiesReservesService;

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @Resource(name = "underFacilitiesService")
    private UnderFacilitiesService underFacilitiesService;

    @Resource(name = "stuffService")
    private StuffService stuffService;

    @GetMapping(value = "/facilitiesreserves")
    public String facilitiesReservesPage(ModelMap model) {

        List<FacilitiesReserves> facilitiesReserves = facilitiesReservesService.findAll();
        model.addAttribute("facilitiesReserves", facilitiesReserves);

        return "viewfacilitiesreserves";
    }

    @GetMapping(value = {"/edit-reserves-{id}"})
    public String editFacilitiesReserves(@PathVariable BigInteger id, ModelMap model) {
        String title = "Обновление данных по резервам";
        FacilitiesReserves facilitiesReserves = facilitiesReservesService.findById(id);
        model.addAttribute("facilitiesReserves", facilitiesReserves);
        model.addAttribute("edit", true);
        model.addAttribute("title", title);
        return "addfacilitiesreserves";
    }

    @PostMapping(value = {"/edit-reserves-{id}"})
    public String updateFacilitiesReserves(@ModelAttribute("facilitiesReserves") FacilitiesReserves facilitiesReserves,
                                           BindingResult result, ModelMap model) {
        String ret = "списку резервов.";
        String redirectUrl = "/facilitiesreserves";
        if (result.hasErrors()) {
            return "addfacilitiesreserves";
        }

        facilitiesReservesService.update(facilitiesReserves);

        model.addAttribute("success", "Данные по резерву на объект " +
                facilitiesReserves.getFacility().getFacility() +
                " успешно обновлены.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @GetMapping(value = {"/delete-reserves-{id}"})
    public String deleteFacilitiesReserves(@PathVariable BigInteger id) {
        facilitiesReservesService.deleteById(id);
        return "redirect:/facilitiesreserves";
    }

    @GetMapping(value = {"/newfacilitiesreserves"})
    public String newFacilitiesReserves(ModelMap model) {
        String title = "Добавление резерва";
        FacilitiesReserves facilitiesReserves = new FacilitiesReserves();
        model.addAttribute("facilitiesReserves", facilitiesReserves);
        model.addAttribute("edit", false);
        model.addAttribute("title", title);
        return "addfacilitiesreserves";
    }

    @PostMapping(value = {"/newfacilitiesreserves"})
    public String saveFacilitiesReserves(@ModelAttribute("facilitiesReserves") FacilitiesReserves facilitiesReserves,
                                         BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "addfacilitiesreserves";
        }
        String ret = "списку резервов";
        String redirectUrl = "/facilitiesreserves";

        facilitiesReservesService.create(facilitiesReserves);

        model.addAttribute("success", "Резерв по объекту " + facilitiesReserves.getFacility().getFacility() +
                " успешно добавлен.");
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
