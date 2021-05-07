package com.art.controllers;

import com.art.config.application.Location;
import com.art.model.AppToken;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.dto.TokenDTO;
import com.art.service.AppTokenService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @GetMapping(path = Location.TOKENS)
    public String tokensPage(ModelMap model) {
        List<AppToken> tokens = appTokenService.findAll();
        model.addAttribute("tokens", tokens);
        model.addAttribute("tokenDTO", new TokenDTO());
        return "token-list";
    }

    @ResponseBody
    @PostMapping(path = Location.TOKENS_CREATE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse createToken(@RequestBody TokenDTO dto) {
        return appTokenService.create(dto);
    }

    @ResponseBody
    @PostMapping(path = Location.TOKENS_UPDATE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse updateToken(@RequestBody TokenDTO dto) {
        return appTokenService.update(dto);
    }

    @ResponseBody
    @PostMapping(path = Location.TOKENS_DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse deleteToken(@RequestBody TokenDTO dto) {
        return appTokenService.delete(dto.getId());
    }

    @ResponseBody
    @PostMapping(path = Location.TOKENS_FIND)
    public TokenDTO findToken(@RequestBody TokenDTO dto) {
        return new TokenDTO(appTokenService.findById(dto.getId()));
    }

}
