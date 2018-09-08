package com.art.controllers;

import com.art.func.GetPrincipalFunc;
import com.art.model.*;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.SearchSummary;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AlphaExtractController {

    private final String title = "Разделение строк по выгрузкам Альфа банка";

    @Resource(name = "alphaExtractService")
    private AlphaExtractService alphaExtractService;

    @Resource(name = "alphaCorrectTagsService")
    private AlphaCorrectTagsService alphaCorrectTagsService;

    @Resource(name = "getPrincipalFunc")
    private GetPrincipalFunc getPrincipalFunc;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "stuffService")
    private StuffService stuffService;

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @Resource(name = "rentorsDetailsService")
    private RentorsDetailsService rentorsDetailsService;

    @GetMapping(value = {"/double-alphaextract-{id}"})
    public String doubleAlphaExtract(@PathVariable BigInteger id, ModelMap model) {
        AlphaExtract alphaExtract = alphaExtractService.findById(id);

        model.addAttribute("alphaExtract", alphaExtract);
        model.addAttribute("title", title);
        return "addalphaextract";
    }

    @GetMapping(value = {"/edit-alphaextract-{id}"})
    public String editAlphaExtract(@PathVariable BigInteger id, ModelMap model) {
        AlphaExtract alphaExtract = alphaExtractService.findById(id);

        model.addAttribute("alphaExtract", alphaExtract);
        model.addAttribute("edit", true);
        model.addAttribute("title", title);
        return "addalphaextract";
    }

    @PostMapping(value = {"/edit-alphaextract-{id}"})
    public String edAlphaExtract(@ModelAttribute("alphaExtract") AlphaExtract alphaExtract,
                                 BindingResult result, ModelMap model) {

        String ret = "списку выгрузок Альфа банка.";
        String redirectUrl = "/alphaextract";
        if (result.hasErrors()) {
            return "addalphaextract";
        }

        AlphaExtract newAlpha = alphaExtractService.findById(alphaExtract.getId());
        newAlpha.setDebet(alphaExtract.getDebet());
        newAlpha.setCredit(alphaExtract.getCredit());
        newAlpha.setPurposePayment(alphaExtract.getPurposePayment());
        newAlpha.setTags(alphaExtract.getTags());
        newAlpha.setPeriod(alphaExtract.getPeriod());

        alphaExtractService.update(newAlpha);

        model.addAttribute("success", "Строка успешно добавлена.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        model.addAttribute("title", title);
        return "redirect:/alphaextract";
    }

    @PostMapping(value = {"/double-alphaextract-{id}"})
    public String updateAlphaExtract(@ModelAttribute("alphaExtract") AlphaExtract alphaExtract,
                                     BindingResult result, ModelMap model) {

        //String title = "Разделение строк по выгрузкам Альфа банка";
        String ret = "списку выгрузок Альфа банка.";
        String redirectUrl = "/alphaextract";
        if (result.hasErrors()) {
            return "addalphaextract";
        }

        AlphaExtract newAlphaExtract = alphaExtractService.findById(alphaExtract.getId());
        newAlphaExtract.setId(null);
        newAlphaExtract.setpId(
                String.join("_", newAlphaExtract.getDateOperToLocalDate(), newAlphaExtract.getDocNumber(),
                        String.valueOf(newAlphaExtract.getDebet()), String.valueOf(newAlphaExtract.getCredit()))
                /*.replaceAll("\\.", "_")*/);
        newAlphaExtract.setDebet(alphaExtract.getDebet());
        newAlphaExtract.setCredit(alphaExtract.getCredit());
        newAlphaExtract.setPurposePayment(alphaExtract.getPurposePayment());
        newAlphaExtract.setTags(alphaExtract.getTags());
        newAlphaExtract.setPeriod(alphaExtract.getPeriod());
        alphaExtractService.update(newAlphaExtract);

        model.addAttribute("success", "Строка успешно дублирована.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        model.addAttribute("title", title);
        return "addalphaextract";
    }

    @GetMapping(value = {"/delete-alphaextract-{id}"})
    public String deleteAlphaExtract(@PathVariable BigInteger id) {
        alphaExtractService.deleteById(id);
        return "redirect:/alphaextract";
    }

    @PostMapping(value = {"/findrentors"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse findRentors(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        List<Users> rentors;
        Facilities facility = facilityService.findByIdWithRentorInvestors(new BigInteger(searchSummary.getFacility()));
        rentors = facility.getInvestors().stream().filter(inv -> inv.getUserStuff().getStuff().equals("Арендатор"))
                .collect(Collectors.toList());
        StringBuilder stringBuilder = new StringBuilder("<option value='0'>Выберите арендатора</option>");
        rentors.forEach(r ->
                stringBuilder.append("<option value=").append(r.getId()).append(">").append(r.getLogin()).append("</option>"));

        response.setMessage(stringBuilder.toString());
        return response;
    }

    @PostMapping(value = {"/findcorrecttags"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse findCorrectTags(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        BigInteger facilityId = new BigInteger(searchSummary.getFacility());
        BigInteger rentorId;
        List<RentorsDetails> rentorsDetailsList;
        if (searchSummary.getRentor() != null) {
            rentorId = new BigInteger(searchSummary.getRentor());
            rentorsDetailsList = rentorsDetailsService.findByRentorId(rentorId);
        } else {
            rentorsDetailsList = rentorsDetailsService.findByFacilityId(facilityId);
        }

        List<AlphaCorrectTags> correctTagsList = alphaCorrectTagsService.findByFacilityId(facilityId);
        List<AlphaCorrectTags> correctTagsListFinal = new ArrayList<>(0);

        if (rentorsDetailsList.size() > 0) {
            for (RentorsDetails details : rentorsDetailsList) {
                correctTagsListFinal.addAll(correctTagsList.stream()
                        .filter(corTag -> corTag.getFacility().equals(details.getFacility()) &&
                                corTag.getInn().equals(details.getInn()) &&
                                corTag.getAccount().equals(details.getAccount()))
                        .collect(Collectors.toList()));
            }
        } else {
            correctTagsListFinal = new ArrayList<>(correctTagsList);
        }
        correctTagsListFinal = correctTagsListFinal.stream().distinct().collect(Collectors.toList());
        StringBuilder stringBuilder = new StringBuilder("<option value='0'>Выберите тэг</option>");
        correctTagsListFinal.forEach(ct ->
                stringBuilder.append("<option value=").append(ct.getId()).append(">").append(ct.getCorrectTag()).append("</option>"));
        response.setMessage(stringBuilder.toString());
        return response;
    }

    @ModelAttribute("facilities")
    public List<Facilities> initializeFacilities() {
        return facilityService.initializeFacilities();
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
