package com.art.controllers;

import com.art.config.application.Location;
import com.art.model.AppRole;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.dto.AppRoleDTO;
import com.art.service.RoleService;
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
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping(path = Location.ROLE_LIST)
    public String getRoles(ModelMap model) {
        List<AppRole> roles = roleService.findAll();
        model.addAttribute("roles", roles);
        model.addAttribute("roleDTO", new AppRoleDTO());
        return "role-list";
    }

    @ResponseBody
    @PostMapping(path = Location.ROLE_CREATE)
    public ApiResponse createRole(@RequestBody AppRoleDTO dto) {
        return roleService.create(dto);
    }

    @ResponseBody
    @PostMapping(path = Location.ROLE_FIND)
    public AppRoleDTO findRole(@RequestBody AppRoleDTO dto) {
        return new AppRoleDTO(roleService.findById(dto.getId()));
    }

    @ResponseBody
    @PostMapping(path = Location.ROLE_UPDATE)
    public ApiResponse updateRole(@RequestBody AppRoleDTO dto) {
        return roleService.update(dto);
    }

    @ResponseBody
    @PostMapping(path = Location.ROLE_DELETE)
    public ApiResponse deleteRole(@RequestBody AppRoleDTO dto) {
        return roleService.delete(dto);
    }

}
