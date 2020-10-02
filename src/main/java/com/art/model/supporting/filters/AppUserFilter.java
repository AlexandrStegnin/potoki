package com.art.model.supporting.filters;

import com.art.model.supporting.dto.AppRoleDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Alexandr Stegnin
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AppUserFilter extends AbstractFilter {

    private String login;

    private boolean deactivated = false;

    private boolean confirmed = true;

    private AppRoleDTO role;

}
