package com.art.controllers;

import com.art.model.Facilities;
import com.art.model.RentorsDetails;
import com.art.model.Users;
import com.art.service.FacilityService;
import com.art.service.RentorsDetailsService;
import com.art.service.StuffService;
import com.art.service.UserService;
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
public class RentorsDetailsController {

    @Resource(name = "rentorsDetailsService")
    private RentorsDetailsService rentorsDetailsService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "stuffService")
    private StuffService stuffService;

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @GetMapping(value = "/rentorsdetails")
    public String rentorsDetails(ModelMap model) {

        List<RentorsDetails> rentorsDetails = rentorsDetailsService.findAll();

        model.addAttribute("rentorsDetails", rentorsDetails);

        return "viewrentorsdetails";
    }

    @GetMapping(value = {"/newdetails"})
    public String newDetails(ModelMap model) {

        RentorsDetails rentorsDetails = new RentorsDetails();
        model.addAttribute("rentorsDetails", rentorsDetails);
        model.addAttribute("edit", false);
        return "adddetails";
    }

    @PostMapping(value = {"/newdetails"})
    public String saveDetails(@ModelAttribute("rentorsDetails") RentorsDetails rentorsDetails,
                              BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "adddetails";
        }

        String ret = "реквизитам арендаторов.";
        String redirectUrl = "/rentorsdetails";
        rentorsDetailsService.create(rentorsDetails);

        model.addAttribute("success", "Реквизиты арендатора по объекту " + rentorsDetails.getFacility()
                .getFacility() + " успешно добавлены.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @GetMapping(value = {"/edit-details-{id}"})
    public String editDetails(@PathVariable BigInteger id, ModelMap model) {

        RentorsDetails rentorsDetails = rentorsDetailsService.findById(id);

        model.addAttribute("rentorsDetails", rentorsDetails);
        model.addAttribute("edit", true);
        return "adddetails";
    }

    @PostMapping(value = {"/edit-details-{id}"})
    public String updateDetails(@ModelAttribute("rentorsDetails") RentorsDetails rentorsDetails,
                                BindingResult result, ModelMap model) {
        String ret = "реквизитам арендаторов.";
        String redirectUrl = "/rentorsdetails";
        if (result.hasErrors()) {
            return "adddetails";
        }

        rentorsDetailsService.update(rentorsDetails);

        model.addAttribute("success", "Реквизиты арендатора по объекту " + rentorsDetails.getFacility()
                .getFacility() + " успешно обновлены.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    /**
     * Удаление объекта по ID.
     */
    @GetMapping(value = {"/delete-details-{id}"})
    public String deleteDetails(@PathVariable BigInteger id) {
        rentorsDetailsService.deleteById(id);
        return "redirect:/rentorsdetails";
    }

    @ModelAttribute("rentors")
    public List<Users> initializeRentors() {
        return userService.findRentors(stuffService.findByStuff("Арендатор").getId());
    }

    @ModelAttribute("facilities")
    public List<Facilities> initializeFacilityes() {
        return facilityService.findAll();
    }
}
