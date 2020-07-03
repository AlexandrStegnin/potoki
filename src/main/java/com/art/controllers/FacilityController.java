package com.art.controllers;

import com.art.model.*;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.SearchSummary;
import com.art.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller
public class FacilityController {

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "underFacilitiesService")
    private UnderFacilitiesService underFacilitiesService;

    @Resource(name = "investorsCashService")
    private InvestorsCashService investorsCashService;

    /**
     * Создание объекта
     */
    @RequestMapping(value = {"/newfacility"}, method = RequestMethod.GET)
    public String newFacility(ModelMap model) {

        Facilities facility = new Facilities();
        model.addAttribute("newFacility", facility);
        model.addAttribute("edit", false);
        return "facility_edit";
    }

    @RequestMapping(value = {"/newfacility"}, method = RequestMethod.POST)
    public String saveFacility(@ModelAttribute("newFacility") Facilities newFacility, BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "facility_edit";
        }

        String ret = "списку объектов.";
        String redirectUrl = "/admin_facility";
        facilityService.create(newFacility);

        model.addAttribute("success", "Объект " + newFacility.getFacility() + " успешно добавлен.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @RequestMapping(value = "/admin_facility", method = RequestMethod.GET)
    public String adminFacility(ModelMap model) {

        List<Facilities> facilityes = facilityService.findAllWithUnderFacilitiesRentorsInvestorsManagers();

        model.addAttribute("facilities", facilityes);

        return "admin_facility";
    }

    /**
     * Создание страницы с объектом редактирования
     */
    @RequestMapping(value = {"/edit-facility-{id}"}, method = RequestMethod.GET)
    public String editFacility(@PathVariable BigInteger id, ModelMap model) {

        Facilities facility = facilityService.findByIdWithRentorsInvestorsManagers(id);

        model.addAttribute("newFacility", facility);
        model.addAttribute("edit", true);
        return "facility_edit";
    }

    /**
     * Обновление объекта в базе данных
     */
    @RequestMapping(value = {"/edit-facility-{id}"}, method = RequestMethod.POST)
    public String updateFacility(@ModelAttribute("newFacility") Facilities facility, BindingResult result, ModelMap model) {
        String ret = "списку объектов.";
        String redirectUrl = "/admin_facility";
        if (result.hasErrors()) {
            return "facility_edit";
        }
        facilityService.update(facility);

        model.addAttribute("success", "Данные объекта " + facility.getFacility() + " успешно обновлены.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    /**
     * Удаление объекта по ID.
     */

    @PostMapping(value = "delete", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse deleteFacility(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        Facilities facility = facilityService.findByIdWithInvestors(new BigInteger(searchSummary.getFacility()));
        Set<Users> investors = facility.getInvestors();
        List<InvestorsCash> cash = new ArrayList<>(0);
        investors.forEach(i -> cash.addAll(investorsCashService.findByInvestorId(i.getId())));
        cash.forEach(c -> {
            if (Objects.equals(facility, c.getFacility())) {
                investorsCashService.deleteById(c.getId());
            }
        });
        try {
            facility.getUnderFacilities().forEach(f -> underFacilitiesService.deleteById(f.getId()));
            facilityService.deleteById(facility.getId());
            response.setMessage("Объект " + facility
                    .getFacility() + " успешно удалён.");
        } catch (Exception e) {
            response.setError("При удалении объекта " + facility
                    .getFacility() + " произошла ошибка.");
        }
        return response;
    }

    @ModelAttribute("managers")
    public List<Users> initializeManagers() {
        List<Users> usersList = new ArrayList<>(0);
        usersList.add(new Users("0", "Выберите управляющего"));
        usersList.addAll(userService.findAll());
        return usersList;
    }

    @ModelAttribute("rentors")
    public List<Users> initializeRentors() {
        return userService.findAll();
    }

    @ModelAttribute("investors")
    public List<Users> initializeInvestors() {
        return userService.findAll();
    }

}
