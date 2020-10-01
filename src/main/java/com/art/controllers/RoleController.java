package com.art.controllers;

import com.art.config.application.Location;
import com.art.model.AppRole;
import com.art.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Controller
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping(path = Location.ROLE_LIST)
    public String getRoles(ModelMap model) {
        List<AppRole> roles = roleService.findAll();
        model.addAttribute("roles", roles);
        return "role-list";
    }

}
