package com.art.converter;

import com.art.model.AppRole;
import com.art.service.RoleService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A converter class used in views toDate map id's toDate actual userProfile objects.
 */

@Component
public class RoleToUserRoleConverter implements Converter<String, AppRole> {

    private final RoleService roleService;

    public RoleToUserRoleConverter(RoleService roleService) {
        this.roleService = roleService;
    }

    public AppRole convert(String id) {
        Long IntId = Long.valueOf(id);
        return roleService.findById(IntId);
    }
}
