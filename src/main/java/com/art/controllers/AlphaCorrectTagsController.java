package com.art.controllers;

import com.art.func.GetPrincipalFunc;
import com.art.model.AlphaCorrectTags;
import com.art.model.Facilities;
import com.art.model.supporting.DebetCreditEnum;
import com.art.service.AlphaCorrectTagsService;
import com.art.service.FacilityService;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class AlphaCorrectTagsController {

    @Resource(name = "alphaCorrectTagsService")
    private AlphaCorrectTagsService alphaCorrectTagsService;

    @Resource(name = "getPrincipalFunc")
    private GetPrincipalFunc getPrincipalFunc;

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @GetMapping(value = "/alphacorrecttags")
    public String alphaCorrectTagsPage(ModelMap model) {

        List<AlphaCorrectTags> alphaCorrectTags = alphaCorrectTagsService
                .findAll();
        model.addAttribute("alphaCorrectTags", alphaCorrectTags);
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());

        return "viewalphacorrecttags";
    }

    @GetMapping(value = { "/newalphatag" })
    public String newAlphaTag(ModelMap model) {
        String title = "Добавление тэга Альфа банка";
        AlphaCorrectTags alphaCorrectTags = new AlphaCorrectTags();
        List<DebetCreditEnum> enums = new ArrayList<DebetCreditEnum>(
                Arrays.asList(DebetCreditEnum.values()));
        model.addAttribute("alphaCorrectTags", alphaCorrectTags);
        model.addAttribute("enums", enums);
        model.addAttribute("edit", false);
        model.addAttribute("title", title);
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());
        return "addalphatags";
    }

    @PostMapping(value = { "/newalphatag" })
    public String saveAlphaTags(@ModelAttribute("alphaCorrectTags")
                                                        AlphaCorrectTags alphaCorrectTags,
                                                BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "addalphatags";
        }
        String ret = "списку тэгов Альфа банка";
        String redirectUrl = "/alphacorrecttags";

        alphaCorrectTagsService.create(alphaCorrectTags);

        model.addAttribute("success", "Тэг Альфа банка " +
                alphaCorrectTags.getCorrectTag() + " успешно добавлен.");
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @GetMapping(value = { "/edit-alphatags-{id}" })
    public String editAlphaTags(@PathVariable BigInteger id, ModelMap model) {
        String title = "Обновление данных по тэгам Альфа банка";
        AlphaCorrectTags alphaCorrectTags = alphaCorrectTagsService.findById(id);
        List<DebetCreditEnum> enums = new ArrayList<DebetCreditEnum>(
                Arrays.asList(DebetCreditEnum.values()));
        model.addAttribute("enums", enums);
        model.addAttribute("alphaCorrectTags", alphaCorrectTags);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());
        model.addAttribute("title", title);
        return "addalphatags";
    }

    /**
     * This method will be called on form submission, handling POST request for
     * updating user in database. It also validates the user input
     */
    @PostMapping(value = { "/edit-alphatags-{id}" })
    public String updateAlphaTags(
            @ModelAttribute("alphaCorrectTags") AlphaCorrectTags alphaCorrectTags,
            BindingResult result, ModelMap model) {
        String ret = "списку тэгов Альфа банка.";
        String redirectUrl = "/alphacorrecttags";
        if (result.hasErrors()) {
            return "addalphatags";
        }

        alphaCorrectTagsService.update(alphaCorrectTags);

        model.addAttribute("success", "Тэг Альфа банка " +
                alphaCorrectTags.getCorrectTag() + " успешно обновлён.");
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @GetMapping(value = { "/delete-alphatags-{id}" })
    public String deleteAlphaTags(@PathVariable BigInteger id) {
        alphaCorrectTagsService.deleteById(id);
        return "redirect:/alphacorrecttags";
    }

    @ModelAttribute("facilities")
    public List<Facilities> initializeFacilities() {
        return facilityService.findAll();
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
