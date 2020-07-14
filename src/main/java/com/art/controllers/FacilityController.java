package com.art.controllers;

import com.art.model.Facility;
import com.art.model.InvestorsCash;
import com.art.model.Users;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.SearchSummary;
import com.art.service.FacilityService;
import com.art.service.InvestorsCashService;
import com.art.service.UnderFacilitiesService;
import com.art.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Controller
@Transactional
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

        Facility facility = new Facility();
        model.addAttribute("newFacility", facility);
        model.addAttribute("edit", false);
        return "facility_edit";
    }

    @RequestMapping(value = {"/newfacility"}, method = RequestMethod.POST)
    public String saveFacility(@ModelAttribute("newFacility") Facility newFacility, BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "facility_edit";
        }

        String ret = "списку объектов.";
        String redirectUrl = "/admin_facility";
        facilityService.create(newFacility);

        model.addAttribute("success", "Объект " + newFacility.getName() + " успешно добавлен.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @RequestMapping(value = "/admin_facility", method = RequestMethod.GET)
    public String adminFacility(ModelMap model) {

        List<Facility> facilities = facilityService.findAllWithUnderFacilities();

        model.addAttribute("facilities", facilities);

        return "admin_facility";
    }

    /**
     * Создание страницы с объектом редактирования
     */
    @RequestMapping(value = {"/edit-facility-{id}"}, method = RequestMethod.GET)
    public String editFacility(@PathVariable BigInteger id, ModelMap model) {

        Facility facility = facilityService.findById(id);

        model.addAttribute("newFacility", facility);
        model.addAttribute("edit", true);
        return "facility_edit";
    }

    /**
     * Обновление объекта в базе данных
     */
    @RequestMapping(value = {"/edit-facility-{id}"}, method = RequestMethod.POST)
    public String updateFacility(@ModelAttribute("newFacility") Facility facility, BindingResult result, ModelMap model) {
        String ret = "списку объектов.";
        String redirectUrl = "/admin_facility";
        if (result.hasErrors()) {
            return "facility_edit";
        }
        facilityService.update(facility);

        model.addAttribute("success", "Данные объекта " + facility.getName() + " успешно обновлены.");
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
        Facility facility = facilityService.findById(new BigInteger(searchSummary.getFacilityStr()));
        List<InvestorsCash> investorsCashes = investorsCashService.findByFacilityId(facility.getId());
        investorsCashes.forEach(c -> investorsCashService.deleteById(c.getId()));
        try {
            facility.getUnderFacilities().forEach(f -> underFacilitiesService.deleteById(f.getId()));
            facilityService.deleteById(facility.getId());
            response.setMessage("Объект " + facility
                    .getName() + " успешно удалён.");
        } catch (Exception e) {
            response.setError("При удалении объекта " + facility
                    .getName() + " произошла ошибка.");
        }
        return response;
    }

    @ModelAttribute("investors")
    public List<Users> initializeInvestors() {
        return userService.findAll();
    }

}
