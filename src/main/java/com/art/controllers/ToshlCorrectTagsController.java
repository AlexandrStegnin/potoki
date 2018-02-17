package com.art.controllers;

import com.art.model.ToshlCorrectTags;
import com.art.service.ToshlCorrectTagsService;
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
public class ToshlCorrectTagsController {

    @Resource(name = "toshlCorrectTagsService")
    private ToshlCorrectTagsService toshlCorrectTagsService;

    @GetMapping(value = "/toshlcorrecttags")
    public String toshlCorrectTagsPage(ModelMap model) {

        List<ToshlCorrectTags> toshlCorrectTagss = toshlCorrectTagsService.findAll();
        model.addAttribute("toshlCorrectTagss", toshlCorrectTagss);

        return "viewtoshlcorrecttags";
    }

    @GetMapping(value = {"/newtoshltag"})
    public String newToshlTag(ModelMap model) {
        String title = "Добавление тэга Toshl";
        ToshlCorrectTags toshlCorrectTags = new ToshlCorrectTags();
        model.addAttribute("toshlCorrectTags", toshlCorrectTags);
        model.addAttribute("edit", false);
        model.addAttribute("title", title);
        return "addtoshltags";
    }

    @PostMapping(value = {"/newtoshltag"})
    public String saveToshlTags(@ModelAttribute("toshlCorrectTagsTags")
                                        ToshlCorrectTags toshlCorrectTags,
                                BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "addtoshltags";
        }
        String ret = "списку тэгов Toshl";
        String redirectUrl = "/toshlcorrecttags";

        toshlCorrectTagsService.create(toshlCorrectTags);

        model.addAttribute("success", "Тэг Toshl " +
                toshlCorrectTags.getCorrectTag() + " успешно добавлен.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @GetMapping(value = {"/edit-toshltags-{id}"})
    public String editToshlTags(@PathVariable BigInteger id, ModelMap model) {
        String title = "Обновление данных по тэгам Toshl";
        ToshlCorrectTags toshlCorrectTags = toshlCorrectTagsService.find(id);
        model.addAttribute("toshlCorrectTags", toshlCorrectTags);
        model.addAttribute("edit", true);
        model.addAttribute("title", title);
        return "addtoshltags";
    }

    @PostMapping(value = {"/edit-toshltags-{id}"})
    public String updateToshlTags(
            @ModelAttribute("toshlCorrectTags") ToshlCorrectTags toshlCorrectTags,
            BindingResult result, ModelMap model) {
        String ret = "списку тэгов Toshl.";
        String redirectUrl = "/toshlcorrecttags";
        if (result.hasErrors()) {
            return "addtoshltags";
        }

        toshlCorrectTagsService.update(toshlCorrectTags);

        model.addAttribute("success", "Тэг Toshl " +
                toshlCorrectTags.getCorrectTag() + " успешно обновлён.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @GetMapping(value = {"/delete-toshltags-{id}"})
    public String deleteToshlTags(@PathVariable BigInteger id) {
        toshlCorrectTagsService.deleteById(id);
        return "redirect:/toshlcorrecttags";
    }

}
