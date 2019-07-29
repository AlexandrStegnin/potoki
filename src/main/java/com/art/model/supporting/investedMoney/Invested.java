package com.art.model.supporting.investedMoney;

import com.art.model.InvestorsCash;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"facility"})
public class Invested {

    private String facility;

    private BigDecimal givenCash = BigDecimal.ZERO;

    private BigDecimal myCash = BigDecimal.ZERO;

    private BigDecimal openCash = BigDecimal.ZERO;

    private BigDecimal closedCash = BigDecimal.ZERO;

    private BigDecimal coast = BigDecimal.ZERO;

    private String investorLogin;

    private String typeClosingInvest;

    private List<InvestorsCash> investorsCashList;

    private BigDecimal incomeCash = BigDecimal.ZERO;

    private BigDecimal cashing = BigDecimal.ZERO;

    public Invested(InvestorsCash investorsCash) {
        this.facility = investorsCash.getFacility().getFacility();
        this.givenCash = investorsCash.getGivedCash();
        this.investorLogin = investorsCash.getInvestor() != null ? investorsCash.getInvestor().getLogin() : null;
        this.typeClosingInvest = investorsCash.getTypeClosingInvest() != null ? investorsCash.getTypeClosingInvest().getTypeClosingInvest() : null;
    }

    public Invested(String facility, BigDecimal givenCash, BigDecimal myCash, BigDecimal openCash, BigDecimal closedCash, BigDecimal coast) {
        this.facility = facility;
        this.givenCash = givenCash;
        this.myCash = myCash;
        this.openCash = openCash;
        this.closedCash = closedCash;
        this.coast = coast;
    }

}
