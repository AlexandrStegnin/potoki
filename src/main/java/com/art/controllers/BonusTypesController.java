package com.art.controllers;

import com.art.func.GetPrincipalFunc;
import com.art.model.BonusTypes;
import com.art.service.BonusTypesService;
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
public class BonusTypesController {

    @Resource(name = "bonusTypesService")
    private BonusTypesService bonusTypesService;

    @Resource(name = "getPrincipalFunc")
    private GetPrincipalFunc getPrincipalFunc;

    @GetMapping(value = "/bonustypes")
    public String bonusTypesPage(ModelMap model) {

        List<BonusTypes> bonusTypes = bonusTypesService.findAll();
        model.addAttribute("bonusTypes", bonusTypes);

        return "viewbonustypes";
    }

    @GetMapping(value = { "/edit-bonustypes-{id}" })
    public String editBonusTypes(@PathVariable BigInteger id, ModelMap model) {
        String title = "Обновление данных по бонусам";
        BonusTypes bonusTypes = bonusTypesService.findById(id);
        model.addAttribute("bonusTypes", bonusTypes);
        model.addAttribute("edit", true);
        model.addAttribute("title", title);
        return "addbonustypes";
    }

    @PostMapping(value = { "/edit-bonustypes-{id}" })
    public String updateBonusTypes(@ModelAttribute("bonusTypes") BonusTypes bonusTypes,
                                           BindingResult result, ModelMap model) {
        String ret = "списку бонусов.";
        String redirectUrl = "/bonustypes";
        if (result.hasErrors()) {
            return "addbonustypes";
        }

        bonusTypesService.update(bonusTypes);

        model.addAttribute("success", "Данные по бонусу " +
                bonusTypes.getBonusType() +
                " успешно обновлены.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @GetMapping(value = { "/delete-bonustypes-{id}" })
    public String deleteBoonusTypes(@PathVariable BigInteger id) {
        bonusTypesService.deleteById(id);
        return "redirect:/bonustypes";
    }

    @GetMapping(value = { "/newbonustypes" })
    public String newBonusTypes(ModelMap model) {
        String title = "Добавление бонуса";
        BonusTypes bonusTypes = new BonusTypes();
        model.addAttribute("bonusTypes", bonusTypes);
        model.addAttribute("edit", false);
        model.addAttribute("title", title);
        return "addbonustypes";
    }

    @PostMapping(value = { "/newbonustypes" })
    public String saveBonusTypes(@ModelAttribute("bonusTypes") BonusTypes bonusTypes,
                                         BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "addbonustypes";
        }
        String ret = "списку бонусов";
        String redirectUrl = "/bonustypes";

        bonusTypesService.create(bonusTypes);

        model.addAttribute("success", "Бонус " + bonusTypes.getBonusType() +
                " успешно добавлен.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

}
