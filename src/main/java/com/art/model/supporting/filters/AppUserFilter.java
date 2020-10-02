package com.art.model.supporting.filters;

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

    private String role;

}
