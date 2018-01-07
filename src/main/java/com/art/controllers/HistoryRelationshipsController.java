package com.art.controllers;

import com.art.func.GetPrincipalFunc;
import com.art.model.*;
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
public class HistoryRelationshipsController {

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "getPrincipalFunc")
    private GetPrincipalFunc getPrincipalFunc;

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @Resource(name = "stuffService")
    private StuffService stuffService;

    @Resource(name = "paymentsMethodService")
    private PaymentsMethodService paymentsMethodService;

    @Resource(name = "paymentsTypeService")
    private PaymentsTypeService paymentsTypeService;

    @Resource(name = "historyRelationshipsService")
    private HistoryRelationshipsService historyRelationshipsService;

    @RequestMapping(value = { "/newpayment" }, method = RequestMethod.GET)
    public String newPayment(ModelMap model) {
        String title = "Добавление платежа";
        HistoryRelationships relationships = new HistoryRelationships();
        model.addAttribute("relationships", relationships);
        model.addAttribute("edit", false);
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());
        model.addAttribute("title", title);
        return "addpayments";
    }

    @RequestMapping(value = { "/newpayment" }, method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("relationships") HistoryRelationships relationships,
                           BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "addpayments";
        }
        String ret = "списку платежей";
        String redirectUrl = "/viewpayments";

        relationships.setManager(userService.findById(
                facilityService.findById(relationships.getFacility().getId()).getManager().getId()));
        historyRelationshipsService.create(relationships);

        model.addAttribute("success", "Платёж по объекту " + relationships.getFacility().getFacility() + " успешно добавлен.");
        //GetPrincipalFunc getPrincipalFunc = new GetPrincipalFunc(userService);
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @RequestMapping(value = "/viewpayments", method = RequestMethod.GET)
    public String paymentsPage(ModelMap model) {

        BigInteger managerId, rentorId;
        managerId = getPrincipalFunc.getPrincipalId();
        rentorId = managerId;

        List<HistoryRelationships> relationships = historyRelationshipsService
                .findByManagerIdOrRentorId(managerId, rentorId);
        model.addAttribute("relationships", relationships);
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());

        /*
        if (isRememberMeAuthenticated()) {
            setRememberMeTargetUrlToSession(httpServletRequest);
            model.addAttribute("loginUpdate", true);
        }
        */
        return "viewpayments";
    }

    @RequestMapping(value = { "/edit-payment-{id}" }, method = RequestMethod.GET)
    public String editPayment(@PathVariable BigInteger id, ModelMap model) {
        String title = "Обновление данных платежа";
        HistoryRelationships relationships = historyRelationshipsService.findById(id);

        model.addAttribute("relationships", relationships);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());
        model.addAttribute("title", title);
        return "addpayments";
    }

    @RequestMapping(value = { "/edit-payment-{id}" }, method = RequestMethod.POST)
    public String updatePayment(@ModelAttribute("relationships") HistoryRelationships relationships,
                                BindingResult result, ModelMap model) {
        String ret = "списку платежей.";
        String redirectUrl = "/viewpayments";
        if (result.hasErrors()) {
            return "addpayments";
        }

        relationships.setManager(userService.findById(getPrincipalFunc.getPrincipalId()));
        /*//Uncomment below 'if block' if you WANT TO ALLOW UPDATING SSO_ID in UI which is a unique key to a User.
        if(!userService.isUserSSOUnique(user.getId(), user.getSsoId())){
            FieldError ssoError =new FieldError("user","ssoId",messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
            result.addError(ssoError);
            return "registration";
        }*/

        historyRelationshipsService.update(relationships);

        model.addAttribute("success", "Данные по платежу объекта " + relationships.getFacility().getFacility() +
                " успешно обновлены.");
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }


    @RequestMapping(value = { "/delete-payment-{id}" }, method = RequestMethod.GET)
    public String deletePayment(@PathVariable BigInteger id) {
        historyRelationshipsService.deleteById(id);
        return "redirect:/viewpayments";
    }

    @ModelAttribute("facility")
    public List<Facilities> initializeFacilityes() {
        return facilityService.findAll();
    }

    @ModelAttribute("rentor")
    public List<Users> initializeRentors() {
        //return userService.findManagers(stuffService.findByStuff("Арендатор").getId());
        return userService.findRentors(stuffService.findByStuff("Арендатор").getId());
    }

    @ModelAttribute("paymentsMethod")
    public List<PaymentsMethod> initializePaymentsMethod() {
        return paymentsMethodService.findByManager(0);
    }

    @ModelAttribute("paymentsType")
    public List<PaymentsType> initializePaymentsTypes() {
        return paymentsTypeService.findAll();
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

}
