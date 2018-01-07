package com.art.controllers;

import com.art.func.GetPrincipalFunc;
import com.art.model.CashPayments;
import com.art.model.Facilities;
import com.art.model.PaymentsMethod;
import com.art.service.CashPaymentsService;
import com.art.service.FacilityService;
import com.art.service.PaymentsMethodService;
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
public class CashPaymentsController {

    @Resource(name = "cashPaymentsService")
    private CashPaymentsService cashPaymentsService;

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @Resource(name = "paymentsMethodService")
    private PaymentsMethodService paymentsMethodService;

    @Resource(name = "getPrincipalFunc")
    private GetPrincipalFunc getPrincipalFunc;

    @Resource(name = "userService")
    private UserService userService;

    @RequestMapping(value = "/viewcashpayments", method = RequestMethod.GET)
    public String cashPaymentsPage(ModelMap model) {

        BigInteger managerId;
        managerId = getPrincipalFunc.getPrincipalId();

        List<CashPayments> cashPaymentss = cashPaymentsService
                .findByManagerId(managerId);
        model.addAttribute("cashPaymentss", cashPaymentss);

        return "viewcashpayments";
    }

    @RequestMapping(value = { "/newcashpayment" }, method = RequestMethod.GET)
    public String newCashPayment(ModelMap model) {
        String title = "Добавление наличного платежа";
        CashPayments cashPayments = new CashPayments();
        model.addAttribute("cashPayments", cashPayments);
        model.addAttribute("edit", false);
        model.addAttribute("title", title);
        return "addcashpayments";
    }

    @RequestMapping(value = { "/newcashpayment" }, method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("cashPayments") CashPayments cashPayments,
                           BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "addcashpayments";
        }
        String ret = "списку наличных платежей";
        String redirectUrl = "/viewcashpayments";

        cashPayments.setManager(userService.findById(
                facilityService.findById(cashPayments.getFacility().getId()).getManager().getId()));
        cashPaymentsService.create(cashPayments);

        model.addAttribute("success", "Наличный платёж по объекту " + cashPayments.getFacility().getFacility() + " успешно добавлен.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }


    @RequestMapping(value = { "/edit-cashpayment-{id}" }, method = RequestMethod.GET)
    public String editCashPayment(@PathVariable BigInteger id, ModelMap model) {
        String title = "Обновление данных наличного платежа";
        CashPayments cashPayments = cashPaymentsService.findById(id);

        model.addAttribute("cashPayments", cashPayments);
        model.addAttribute("edit", true);
        model.addAttribute("title", title);
        return "addcashpayments";
    }

    /**
     * This method will be called on form submission, handling POST request for
     * updating user in database. It also validates the user input
     */
    @RequestMapping(value = { "/edit-cashpayment-{id}" }, method = RequestMethod.POST)
    public String updatePayment(@ModelAttribute("cashPayments") CashPayments cashPayments,
                                BindingResult result, ModelMap model) {
        String ret = "списку наличных платежей.";
        String redirectUrl = "/viewcashpayments";
        if (result.hasErrors()) {
            return "addcashpayments";
        }

        cashPayments.setManager(userService.findByLogin(getPrincipalFunc.getLogin()));
        /*//Uncomment below 'if block' if you WANT TO ALLOW UPDATING SSO_ID in UI which is a unique key to a User.
        if(!userService.isUserSSOUnique(user.getId(), user.getSsoId())){
            FieldError ssoError =new FieldError("user","ssoId",messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
            result.addError(ssoError);
            return "registration";
        }*/

        cashPaymentsService.update(cashPayments);

        model.addAttribute("success", "Данные по наличному платежу объекта " + cashPayments.getFacility().getFacility() + " успешно обновлены.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @RequestMapping(value = { "/delete-cashpayment-{id}" }, method = RequestMethod.GET)
    public String deleteCashPayment(@PathVariable BigInteger id) {
        cashPaymentsService.deleteById(id);
        return "redirect:/viewcashpayments";
    }

    @ModelAttribute("facilities")
    public List<Facilities> initializeFacilities() {
        return facilityService.findAll();
    }

    @ModelAttribute("paymentsMethod")
    public List<PaymentsMethod> initializePaymentsMethod() {
        return paymentsMethodService.findByManager(1);
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
