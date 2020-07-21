package com.art.func;

import com.art.model.AppRole;
import com.art.model.SecurityUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetPrincipalFunc {

    public String getLogin() {
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof SecurityUser) {
            userName = ((SecurityUser) principal).getLogin();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

    public boolean haveAdminRole() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof SecurityUser) {
            List<AppRole> roles = ((SecurityUser) principal).getRoles();
            AppRole admin = roles.stream()
                    .filter(role -> role.getName().equalsIgnoreCase("ROLE_ADMIN"))
                    .findFirst()
                    .orElse(null);
            return admin != null;
        }
        return false;
    }
}
