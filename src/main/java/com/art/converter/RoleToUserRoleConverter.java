package com.art.converter;

import com.art.model.Roles;
import com.art.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A converter class used in views to map id's to actual userProfile objects.
 */

@Component
public class RoleToUserRoleConverter implements Converter<String, Roles>{
    @Autowired
    private RoleService roleService;

    public RoleToUserRoleConverter(RoleService roleService){
        this.roleService = roleService;
    }
    /**
     * Gets UserProfile by Id
     * @see Converter#convert(Object)
     */
    public Roles convert(String id) {
        Integer IntId = Integer.parseInt(id);
        return roleService.findById(IntId);
    }
}
