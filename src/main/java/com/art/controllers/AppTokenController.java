package com.art.controllers;

import com.art.model.AppToken;
import com.art.service.AppTokenService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Controller
public class AppTokenController {

    private final AppTokenService appTokenService;

    public AppTokenController(AppTokenService appTokenService) {
        this.appTokenService = appTokenService;
    }

    @GetMapping(path = "/tokens")
    public String tokensPage(ModelMap model) {
        List<AppToken> tokens = appTokenService.findAll();
        model.addAttribute("tokens", tokens);
        return "tokens";
    }

    @GetMapping(value = {"/edit-token-{id}"})
    public String editToken(@PathVariable Long id, ModelMap model) {
        String title = "Обновление данных по токенам";
        AppToken token = appTokenService.findById(id);
        model.addAttribute("token", token);
        model.addAttribute("edit", true);
        model.addAttribute("title", title);
        return "addToken";
    }

    @PostMapping(value = "/edit-token-{id}")
    public String updateToken(@ModelAttribute("token") AppToken token, ModelMap model) {
        String ret = "списку токенов.";
        String redirectUrl = "/tokens";

        appTokenService.update(token);

        model.addAttribute("success", "Данные по токену приложения " +
                token.getAppName() + " успешно обновлены.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @GetMapping(value = {"/delete-token-{id}"})
    public String deleteToken(@PathVariable Long id) {
        appTokenService.delete(id);
        return "redirect:/tokens";
    }

    @GetMapping(value = {"/generate"})
    public String newToken(ModelMap model) {
        String title = "Добавление токена";
        AppToken token = new AppToken();
        model.addAttribute("token", token);
        model.addAttribute("edit", false);
        model.addAttribute("title", title);
        return "addToken";
    }

    @PostMapping(value = "/generate")
    public String saveToken(@ModelAttribute("token") AppToken token,
                              BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "addToken";
        }
        String ret = "списку токенов";
        String redirectUrl = "/tokens";

        appTokenService.create(token);

        model.addAttribute("success", "Токен для приложения " + token.getAppName() +
                " успешно добавлен.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

}
