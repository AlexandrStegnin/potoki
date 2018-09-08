package com.art.controllers;

import com.art.model.Facilities;
import com.art.model.FacilitiesServiceContracts;
import com.art.model.PaymentsMethod;
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
public class FacilitiesServiceContractsController {
    @Resource(name = "facilitiesServiceContractsService")
    private FacilitiesServiceContractsService facilitiesServiceContractsService;

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @Resource(name = "paymentsMethodService")
    private PaymentsMethodService paymentsMethodService;

    @Resource(name = "stuffService")
    private StuffService stuffService;

    @Resource(name = "userService")
    private UserService userService;

    @GetMapping(value = "/viewfacilitiesservicecontracts")
    public String facilitiesServiceContractsPage(ModelMap model) {

        List<FacilitiesServiceContracts> facilitiesServiceContracts = facilitiesServiceContractsService
                .findAll();
        model.addAttribute("facilitiesServiceContracts", facilitiesServiceContracts);

        return "viewfacilitiesservicecontracts";
    }

    @GetMapping(value = {"/newfacilitiesservicecontracts"})
    public String newFacilitiesServiceContracts(ModelMap model) {
        String title = "Добавление договора обслуживания";
        FacilitiesServiceContracts facilitiesServiceContracts = new FacilitiesServiceContracts();
        model.addAttribute("facilitiesServiceContracts", facilitiesServiceContracts);
        model.addAttribute("edit", false);
        model.addAttribute("title", title);
        return "addfacilitiesservicecontracts";
    }

    @PostMapping(value = {"/newfacilitiesservicecontracts"})
    public String saveFacilitiesServiceContract(@ModelAttribute("facilitiesServiceContracts")
                                                        FacilitiesServiceContracts facilitiesServiceContracts,
                                                BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "addfacilitiesservicecontracts";
        }
        String ret = "списку договоров на обслуживание";
        String redirectUrl = "/viewfacilitiesservicecontracts";

        facilitiesServiceContractsService.create(facilitiesServiceContracts);

        model.addAttribute("success", "Договор на обслуживание объекта " +
                facilitiesServiceContracts.getFacility().getFacility() + " успешно добавлен.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @GetMapping(value = {"/edit-facilitiesservicecontracts-{id}"})
    public String editFacilitiesServiceContracts(@PathVariable BigInteger id, ModelMap model) {
        String title = "Обновление данных по договору обслуживания";
        FacilitiesServiceContracts facilitiesServiceContracts = facilitiesServiceContractsService.findById(id);

        model.addAttribute("facilitiesServiceContracts", facilitiesServiceContracts);
        model.addAttribute("edit", true);
        model.addAttribute("title", title);
        return "addfacilitiesservicecontracts";
    }

    /**
     * This method will be called on form submission, handling POST request for
     * updating user in database. It also validates the user input
     */
    @PostMapping(value = {"/edit-facilitiesservicecontracts-{id}"})
    public String updateFacilitiesServiceContracts(
            @ModelAttribute("facilitiesservicecontracts") FacilitiesServiceContracts facilitiesServiceContracts,
            BindingResult result, ModelMap model) {
        String ret = "списку договоров обслуживания.";
        String redirectUrl = "/viewfacilitiesservicecontracts";
        if (result.hasErrors()) {
            return "addfacilitiesservicecontracts";
        }

        facilitiesServiceContractsService.update(facilitiesServiceContracts);

        model.addAttribute("success", "Данные по договору обслуживания объекта " +
                facilitiesServiceContracts.getFacility().getFacility() + " успешно обновлены.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @RequestMapping(value = {"/delete-facilitiesservicecontracts-{id}"}, method = RequestMethod.GET)
    public String deleteFacilitiesServiceContracts(@PathVariable BigInteger id) {
        facilitiesServiceContractsService.deleteById(id);
        return "redirect:/viewfacilitiesservicecontracts";
    }

    @ModelAttribute("facilities")
    public List<Facilities> initializeFacilities() {
        return facilityService.findAll();
    }

    @ModelAttribute("paymentsMethod")
    public List<PaymentsMethod> initializePaymentsMethod() {
        return paymentsMethodService.findByManager(0);
    }

    @ModelAttribute("rentors")
    public List<Users> initializeRentors() {
        return userService.findRentors(stuffService.findByStuff("Арендатор").getId());
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }


}
