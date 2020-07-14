package com.art.func;

import com.art.model.Roles;
import com.art.model.SecurityUser;
import com.art.model.supporting.UserFacilities;
import com.art.service.FacilityService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetPrincipalFunc {

    @Resource(name = "facilityService")
    private FacilityService facilityService;

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

    public Long getPrincipalId() {
        Long userId = Long.valueOf("0");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof SecurityUser) {
            userId = ((SecurityUser) principal).getId();
        }
        return userId;
    }

    @Deprecated
    public List<UserFacilities> getUserFacilities(Long userId) {
        List<UserFacilities> userFacilities = new ArrayList<>(0);
        UserFacilities defaultFacility = new UserFacilities(new BigInteger("0"), "Выберите объект");
        userFacilities.add(defaultFacility);
        userFacilities.addAll(facilityService.getInvestorsFacility(userId));
        return userFacilities;
    }

    public boolean haveAdminRole() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof SecurityUser) {
            List<Roles> roles = ((SecurityUser) principal).getRoles();
            Roles admin = roles.stream()
                    .filter(role -> role.getRole().equalsIgnoreCase("ROLE_ADMIN"))
                    .findFirst()
                    .orElse(null);
            return admin != null;
        }
        return false;
    }
}
