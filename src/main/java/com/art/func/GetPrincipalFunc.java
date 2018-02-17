package com.art.func;

import com.art.model.*;
import com.art.model.supporting.UserFacilities;
import com.art.service.*;
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

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "investorsFlowsService")
    private InvestorsFlowsService investorsFlowsService;

    @Resource(name = "mainFlowsService")
    private MainFlowsService mainFlowsService;

    @Resource(name = "investorsCashService")
    private InvestorsCashService investorsCashService;

    @Resource(name = "usersAnnexToContractsService")
    private UsersAnnexToContractsService usersAnnexToContractsService;

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

    public BigInteger getPrincipalId() {
        BigInteger userId = new BigInteger("0");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof SecurityUser) {
            userId = ((SecurityUser) principal).getId();
        }
        return userId;
    }

    public List<UserFacilities> getUserFacilities(BigInteger userId) {
        List<UserFacilities> userFacilities = new ArrayList<>(0);
        UserFacilities defaultFacility = new UserFacilities(new BigInteger("0"), "Выберите объект");
        userFacilities.add(defaultFacility);
        userFacilities.addAll(facilityService.getInvestorsFacility(userId));
        return userFacilities;
    }

    public void updateDemoUser() {
        BigInteger copyId = new BigInteger("31");
        BigInteger demoId;
        String demoLogin = "investor-demo";

        Users copy = userService.findWithAllFields(copyId);
        Users demo = userService.findByLogin(demoLogin);
        demoId = demo.getId();

        demo = copy;
        demo.setId(demoId);
        demo.setLogin(demoLogin);
        demo.setPassword("123");
        List<BigInteger> idList = new ArrayList<>(0);
        demo.getFacilityes().forEach(f -> idList.add(f.getId()));

        List<InvestorsFlows> demoInvestorsFlows = investorsFlowsService.findByInvestorId(demoId);
        List<InvestorsCash> demoInvestorsCashes = investorsCashService.findByInvestorId(demoId);
        List<UsersAnnexToContracts> demoUsersAnnexToContracts = usersAnnexToContractsService.findByUserId(demoId);

        List<BigInteger> invFlowsId = new ArrayList<>(0);
        if (demoInvestorsFlows.size() > 0) {
            demoInvestorsFlows.forEach(dif -> invFlowsId.add(dif.getId()));
            investorsFlowsService.deleteByIdIn(invFlowsId);
        }
        if (demoInvestorsCashes.size() > 0) {
            demoInvestorsCashes.forEach(dic -> investorsCashService.deleteById(dic.getId()));
        }
        if (demoUsersAnnexToContracts.size() > 0) {
            demoUsersAnnexToContracts.forEach(annex -> usersAnnexToContractsService.deleteById(annex.getId()));
        }

        List<InvestorsFlows> copyInvestorsFlows = investorsFlowsService.findByInvestorId(copyId);
        Users finalDemo = demo;
        copyInvestorsFlows.forEach(cif -> {
            cif.setInvestor(finalDemo);
            cif.setId(null);
        });
        investorsFlowsService.saveList(copyInvestorsFlows);

        List<InvestorsCash> copyInvestorsCashes = investorsCashService.findByInvestorId(copyId);
        copyInvestorsCashes.forEach(cic -> {
            cic.setId(null);
            cic.setInvestor(finalDemo);
        });
        investorsCashService.saveAll(copyInvestorsCashes);

        List<UsersAnnexToContracts> copyUsersAnnexToContracts = usersAnnexToContractsService.findByUserId(copyId);
        copyUsersAnnexToContracts.forEach(copyAnnex -> {
            copyAnnex.setId(null);
            copyAnnex.setUserId(demoId);
            usersAnnexToContractsService.update(copyAnnex);
        });

        userService.create(demo);
    }

}
