package com.art.func;

import com.art.model.InvestorsCash;
import com.art.model.SecurityUser;
import com.art.model.supporting.UserFacilities;
import com.art.service.FacilityService;
import com.art.service.InvestorsCashService;
import com.art.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GetPrincipalFunc {

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @Resource(name = "investorsCashService")
    private InvestorsCashService investorsCashService;

    public String getLogin(){
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal instanceof SecurityUser){
            userName = ((SecurityUser) principal).getLogin();
        } else{
            userName = principal.toString();
        }
        return userName;
    }

    public String getFullName(){
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof SecurityUser) {
            userName = ((SecurityUser) principal).getFullName();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

    public BigInteger getPrincipalId(){
        BigInteger userId = new BigInteger("0");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof SecurityUser) {
            userId = ((SecurityUser) principal).getId();
        }
        return userId;
    }

    public List<UserFacilities> getUserFacilities(BigInteger userId){
        List<UserFacilities> userFacilities = new ArrayList<>(0);
        UserFacilities defaultFacility = new UserFacilities(new BigInteger("0"), "Выберите объект");
        userFacilities.add(defaultFacility);
        userFacilities.addAll(facilityService.getInvestorsFacility(userId));
        return userFacilities;
    }

    public Date getMinDateGived(BigInteger investorId){
        Date dateMin = investorsCashService.findByInvestorId(investorId)
                .stream()
                .map(InvestorsCash::getDateGivedCash)
                .min(Date::compareTo).orElse(null);
        return dateMin;
    }

}
