package com.art.config;

import com.art.model.SecurityUser;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Alexandr Stegnin
 */

public final class SecurityUtils {

    private SecurityUtils() {

    }

    public static String getUsername() {
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof SecurityUser) {
            userName = ((SecurityUser) principal).getLogin();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

}
