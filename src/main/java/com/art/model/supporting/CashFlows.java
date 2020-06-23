package com.art.model.supporting;

import com.art.model.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CashFlows implements Serializable {

    private List<MainFlows> mainFlowsList;
    private List<InvestorsFlows> investorsFlowsList;
    private List<InvestorsCash> investorsCashList;
    private List<UsersAnnexToContracts> annexes;
    private List<Facilities> facilities;
    private Facilities facility;
    private List<Rooms> rooms;
    private String login;
    private List<SaleOfFacilities> saleOfFacilities;

}
