package com.art.controllers;

import com.art.func.GetPrincipalFunc;
import com.art.model.Facilities;
import com.art.model.InvestorsShare;
import com.art.model.Users;
import com.art.service.FacilityService;
import com.art.service.InvestorShareService;
import com.art.service.StuffService;
import com.art.service.UserService;
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
public class InvestorsShareController {

    @Resource(name = "investorShareService")
    private InvestorShareService investorShareService;

    @Resource(name = "getPrincipalFunc")
    private GetPrincipalFunc getPrincipalFunc;

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "stuffService")
    private StuffService stuffService;

    @RequestMapping(value = "/investorsshare", method = RequestMethod.GET)
    public String investorsSharePage(ModelMap model) {

        List<InvestorsShare> investorsShares = investorShareService.findAll();
        model.addAttribute("investorsShares", investorsShares);
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());

        return "viewinvestorsshare";
    }

    @RequestMapping(value = { "/edit-share-{id}" }, method = RequestMethod.GET)
    public String editShare(@PathVariable BigInteger id, ModelMap model) {
        String title = "Обновление данных долей";
        InvestorsShare investorsShare = investorShareService.findById(id);

        model.addAttribute("investorsShare", investorsShare);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());
        model.addAttribute("title", title);
        return "addinvestorshare";
    }

    @RequestMapping(value = { "/edit-share-{id}" }, method = RequestMethod.POST)
    public String updateShare(@ModelAttribute("investorsShare") InvestorsShare investorsShare,
                                BindingResult result, ModelMap model) {
        String ret = "списку долей.";
        String redirectUrl = "/investorsshare";
        if (result.hasErrors()) {
            return "addinvestorshare";
        }

        investorShareService.update(investorsShare);

        model.addAttribute("success", "Данные по доле " + investorsShare.getInvestor().getFullName() +
                " успешно обновлены.");
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @RequestMapping(value = { "/delete-share-{id}" }, method = RequestMethod.GET)
    public String deleteShare(@PathVariable BigInteger id) {
        investorShareService.deleteById(id);
        return "redirect:/investorsshare";
    }

    @RequestMapping(value = { "/newinvestorshare" }, method = RequestMethod.GET)
    public String newShare(ModelMap model) {
        String title = "Добавление доли";
        InvestorsShare investorsShare = new InvestorsShare();
        model.addAttribute("investorsShare", investorsShare);
        model.addAttribute("edit", false);
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());
        model.addAttribute("title", title);
        return "addinvestorshare";
    }

    @RequestMapping(value = { "/newinvestorshare" }, method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("investorsShare") InvestorsShare investorsShare,
                           BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "addinvestorshare";
        }
        String ret = "списку долей";
        String redirectUrl = "/investorsshare";

        investorShareService.create(investorsShare);

        model.addAttribute("success", "Доля инвестора " + investorsShare.getInvestor().getFullName() +
                " успешно добавлена.");
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @ModelAttribute("facilities")
    public List<Facilities> initializeFacilities() {
        return facilityService.findAll();
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
