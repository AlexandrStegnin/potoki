package com.art.config;

import com.art.model.Roles;
import com.art.model.SecurityUser;
import com.art.model.Users;
import com.art.model.supporting.enums.UserRole;
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

    public static String getInvestorDemoLogin() {
        return "investor-demo";
    }

    public static boolean isUserInRole(Users user, UserRole userRole) {
        for (Roles role : user.getRoles()) {
            if (role.getRole().equalsIgnoreCase(userRole.name())) {
                return true;
            }
        }
        return false;
    }

}
