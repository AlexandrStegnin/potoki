package com.art.model.supporting;

import com.art.model.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CashFlows implements Serializable {

    private List<InvestorsFlows> investorsFlowsList;
    private List<InvestorsCash> investorsCashList;
    private List<UsersAnnexToContracts> annexes;
    private List<Facility> facilities;
    private Facility facility;
    private List<Rooms> rooms;
    private String login;

}
