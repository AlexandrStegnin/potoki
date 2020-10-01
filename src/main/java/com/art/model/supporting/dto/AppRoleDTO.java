package com.art.model.supporting.dto;

import com.art.model.AppRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Alexandr Stegnin
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppRoleDTO {

    private Long id;

    private String name;

    private String humanized;

    public AppRoleDTO(AppRole role) {
        this.id = role.getId();
        this.name = role.getName();
        this.humanized = role.getHumanized();
    }

}
