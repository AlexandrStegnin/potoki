package com.art.controllers;

import com.art.func.GetPrincipalFunc;
import com.art.model.AllowanceIp;
import com.art.model.Facilities;
import com.art.model.UnderFacilities;
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
public class AllowanceIpController {

    @Resource(name = "allowanceIpService")
    private AllowanceIpService allowanceIpService;

    @Resource(name = "getPrincipalFunc")
    private GetPrincipalFunc getPrincipalFunc;

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @Resource(name = "underFacilitiesService")
    private UnderFacilitiesService underFacilitiesService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "stuffService")
    private StuffService stuffService;

    @GetMapping(value = "/allowanceip")
    public String allowanceIpPage(ModelMap model) {

        List<AllowanceIp> allowanceIpList = allowanceIpService.findAll();
        model.addAttribute("allowanceIpList", allowanceIpList);

        return "viewallowanceip";
    }

    @GetMapping(value = { "/edit-allowance-{id}" })
    public String editAllowanceIp(@PathVariable BigInteger id, ModelMap model) {
        String title = "Обновление данных по надбавке ИП";
        AllowanceIp allowanceIp = allowanceIpService.findById(id);
        model.addAttribute("allowanceIp", allowanceIp);
        model.addAttribute("edit", true);
        model.addAttribute("title", title);
        return "addallowanceip";
    }

    @PostMapping(value = { "/edit-allowance-{id}" })
    public String updateAllowance(@ModelAttribute("allowanceIp") AllowanceIp allowanceIp,
                                           BindingResult result, ModelMap model) {
        String ret = "списку надбавок ИП.";
        String redirectUrl = "/allowanceip";
        if (result.hasErrors()) {
            return "addallowanceip";
        }

        allowanceIpService.update(allowanceIp);

        model.addAttribute("success", "Данные по надбавке ИП по объекту " +
                allowanceIp.getFacility().getFacility() +
                " успешно обновлены.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @GetMapping(value = { "/delete-allowance-{id}" })
    public String deleteAllowanceIp(@PathVariable BigInteger id) {
        allowanceIpService.deleteById(id);
        return "redirect:/allowanceip";
    }

    @GetMapping(value = { "/newallowanceip" })
    public String newAllowanceIp(ModelMap model) {
        String title = "Добавление надбавки ИП";
        AllowanceIp allowanceIp = new AllowanceIp();
        model.addAttribute("allowanceIp", allowanceIp);
        model.addAttribute("edit", false);
        model.addAttribute("title", title);
        return "addallowanceip";
    }

    @PostMapping(value = { "/newallowanceip" })
    public String saveAllowanceIp(@ModelAttribute("allowanceIp") AllowanceIp allowanceIp,
                                         BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "addallowanceip";
        }
        String ret = "списку надбавок ИП";
        String redirectUrl = "/allowanceip";

        allowanceIpService.create(allowanceIp);

        model.addAttribute("success", "Надбавка ИП по объекту " + allowanceIp.getFacility().getFacility() +
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

    @ModelAttribute("investors")
    public List<Users> initializeInvestors() {
        return userService.findRentors(stuffService.findByStuff("Инвестор").getId());
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

}
