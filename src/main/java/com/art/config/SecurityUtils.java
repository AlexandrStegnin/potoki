package com.art.config;

import com.art.model.AppRole;
import com.art.model.SecurityUser;
import com.art.model.AppUser;
import com.art.model.supporting.enums.UserRole;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

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
        for (AppRole role : user.getRoles()) {
            if (role.getName().equalsIgnoreCase(userRole.name())) {
                return true;
            }
        }
        return false;
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
            List<AppRole> roles = ((SecurityUser) principal).getRoles();
            AppRole admin = roles.stream()
                    .filter(role -> role.getName().equalsIgnoreCase(UserRole.ROLE_ADMIN.getSystemName()))
                    .findFirst()
                    .orElse(null);
            return admin != null;
        }
        return false;
    }

}
