package com.art.controllers;

import com.art.config.application.Location;
import com.art.model.AppUser;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.enums.UserRole;
import com.art.model.supporting.enums.UserStatus;
import com.art.model.supporting.filters.Filterable;
import com.art.service.InvestorsFlowsService;
import com.art.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    private final UserService userService;

    private final InvestorsFlowsService investorsFlowsService;

    public AdminController(UserService userService, InvestorsFlowsService investorsFlowsService) {
        this.userService = userService;
        this.investorsFlowsService = investorsFlowsService;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(path = Location.ADMIN)
    public String adminPage(ModelMap model) {
        List<AppUser> users = userService.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(path = Location.CATALOGUE)
    public String cataloguePage(ModelMap model) {
        return "catalogues";
    }

    @PostMapping(path = Location.UPDATE_INV_DEMO, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    ApiResponse updateInvestorDemo() {
        investorsFlowsService.updateInvestorDemo();
        return new ApiResponse("Данные инвестора демо успешно обновлены");
    }

    @ModelAttribute("userStatuses")
    public List<Filterable> initializeStatuses() {
        List<Filterable> statusesAndRoles = new ArrayList<>();
        statusesAndRoles.add(UserStatus.ALL);
        statusesAndRoles.add(UserRole.ROLE_INVESTOR);
        statusesAndRoles.add(UserRole.ROLE_MANAGER);
        statusesAndRoles.add(UserRole.ROLE_ADMIN);
        statusesAndRoles.add(UserStatus.CONFIRMED);
        statusesAndRoles.add(UserStatus.NOT_CONFIRMED);
        return statusesAndRoles;
    }
}
