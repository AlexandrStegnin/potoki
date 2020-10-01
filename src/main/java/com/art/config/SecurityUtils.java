package com.art.config;

import com.art.model.AppRole;
import com.art.model.AppUser;
import com.art.model.SecurityUser;
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

    public static boolean isUserInRole(AppUser user, UserRole userRole) {
        AppRole role = user.getRole();
        return role.getName().equalsIgnoreCase(userRole.name());
    }

    public static Long getUserId() {
        Long userId = 0L;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof SecurityUser) {
            userId = ((SecurityUser) principal).getId();
        }
        return userId;
    }

    public static boolean isAdmin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof SecurityUser) {
            AppRole role = ((SecurityUser) principal).getRole();
            return role.getName().equalsIgnoreCase(UserRole.ROLE_ADMIN.getSystemName());
        }
        return false;
    }

}
