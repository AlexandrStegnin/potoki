package com.art.model.supporting;

import com.art.model.*;

import java.io.Serializable;
import java.util.List;

public class CashFlows implements Serializable {

    private List<MainFlows> mainFlowsList;
    private List<InvestorsFlows> investorsFlowsList;
    private List<InvestorsCash> investorsCashList;
    private List<UsersAnnexToContracts> annexes;
    private List<Facilities> facilities;
    private Facilities facility;
    private List<Rooms> rooms;
    private String login;

    public List<MainFlows> getMainFlowsList() {
        return mainFlowsList;
    }

    public void setMainFlowsList(List<MainFlows> mainFlowsList) {
        this.mainFlowsList = mainFlowsList;
    }

    public List<InvestorsFlows> getInvestorsFlowsList() {
        return investorsFlowsList;
    }

    public void setInvestorsFlowsList(List<InvestorsFlows> investorsFlowsList) {
        this.investorsFlowsList = investorsFlowsList;
    }

    public List<InvestorsCash> getInvestorsCashList() {
        return investorsCashList;
    }

    public void setInvestorsCashList(List<InvestorsCash> investorsCashList) {
        this.investorsCashList = investorsCashList;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public List<UsersAnnexToContracts> getAnnexes() {
        return annexes;
    }

    public void setAnnexes(List<UsersAnnexToContracts> annexes) {
        this.annexes = annexes;
    }

    public List<Facilities> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<Facilities> facilities) {
        this.facilities = facilities;
    }

    public List<Rooms> getRooms() {
        return rooms;
    }

    public void setRooms(List<Rooms> rooms) {
        this.rooms = rooms;
    }

    public Facilities getFacility() {
        return facility;
    }

    public void setFacility(Facilities facility) {
        this.facility = facility;
    }
}
