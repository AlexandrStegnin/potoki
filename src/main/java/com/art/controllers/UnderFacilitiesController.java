package com.art.controllers;

import com.art.model.Facilities;
import com.art.model.Rooms;
import com.art.model.UnderFacilities;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class UnderFacilitiesController {

    @Resource(name = "underFacilitiesService")
    private UnderFacilitiesService underFacilitiesService;

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @Resource(name = "facilitiesBuySalesService")
    private FacilitiesBuySalesService facilitiesBuySalesService;

    @Resource(name = "roomsService")
    private RoomsService roomsService;

    @GetMapping(value = "/underfacilities")
    public String underFacilityPage(ModelMap model) {
        List<UnderFacilities> underFacilities = underFacilitiesService.findAllWithCriteriaApi();
        model.addAttribute("underFacilities", underFacilities);

        return "viewunderfacilities";
    }

    @GetMapping(value = {"/edit-underfacility-{id}"})
    public String editUnderFacility(@PathVariable BigInteger id, ModelMap model) {
        String title = "Обновление данных по подобъекту";
        UnderFacilities underFacilities = underFacilitiesService.findByIdWithCriteriaApi(id);
        model.addAttribute("underFacilities", underFacilities);
        model.addAttribute("edit", true);
        model.addAttribute("title", title);
        return "addunderfacility";
    }

    @PostMapping(value = {"/edit-underfacility-{id}"})
    public String updateUnderFacility(@ModelAttribute("underFacilities") UnderFacilities underFacilities,
                                      BindingResult result, ModelMap model) {
        String ret = "списку подобъектов.";
        String redirectUrl = "/underfacilities";
        if (result.hasErrors()) {
            return "addunderfacility";
        }
        Set<Rooms> rooms = new HashSet<>(roomsService.findByUnderFacility(underFacilities));
        underFacilities.setRooms(rooms);
        underFacilitiesService.update(underFacilities);

        model.addAttribute("success", "Данные по подобъекту " + underFacilities.getFacility().getFacility() +
                " успешно обновлены.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @GetMapping(value = {"/delete-underfacility-{id}"})
    public String deleteUnderFacility(@PathVariable BigInteger id) {
        facilitiesBuySalesService.deleteByUnderFacility(underFacilitiesService.findById(id));
        underFacilitiesService.deleteById(id);
        return "redirect:/underfacilities";
    }

    @GetMapping(value = {"/newunderfacility"})
    public String newUnderfacility(ModelMap model) {
        String title = "Добавление подобъекта";
        UnderFacilities underFacilities = new UnderFacilities();
        model.addAttribute("underFacilities", underFacilities);
        model.addAttribute("edit", false);
        model.addAttribute("title", title);
        return "addunderfacility";
    }

    @PostMapping(value = {"/newunderfacility"})
    public String saveUnderFacility(@ModelAttribute("underFacilities") UnderFacilities underFacilities,
                                    BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "addunderfacility";
        }
        String ret = "списку подобъектов";
        String redirectUrl = "/underfacilities";

        underFacilitiesService.create(underFacilities);

        model.addAttribute("success", "Подобъект " + underFacilities.getFacility().getFacility() +
                " успешно добавлен.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @ModelAttribute("facilities")
    public List<Facilities> initializeFacilities() {
        return facilityService.findAll();
    }

    @ModelAttribute("rooms")
    public List<Rooms> initializeR() {
        return roomsService.init();
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
