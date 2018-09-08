package com.art.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "SaleOfFacilities")
public class SaleOfFacilities implements Serializable {
    private BigInteger id;
    private Facilities facility;
    private String investor;
    private String investorEng;
    private BigDecimal cashInFacility;
    private BigDecimal shareInvestor;
    private BigDecimal leaseInYear;
    private BigDecimal profitFromLease;
    private BigDecimal profitFromSale;
    private BigDecimal profitFromSaleInYear;
    private BigDecimal netProfitFromSale;
    private BigDecimal netProfitFromSalePlusLease;
    private BigDecimal totalYield;
    private BigDecimal capitalGains;
    private BigDecimal capital;
    private BigDecimal balanceOfCapital;

    public SaleOfFacilities(Facilities facility, String investor, BigDecimal cashInFacility, BigDecimal shareInvestor,
                            BigDecimal leaseInYear, BigDecimal profitFromLease, BigDecimal profitFromSale,
                            BigDecimal profitFromSaleInYear, BigDecimal netProfitFromSale, BigDecimal netProfitFromSalePlusLease,
                            BigDecimal totalYield, BigDecimal capitalGains, BigDecimal capital, BigDecimal balanceOfCapital,
                            String investorEng) {
        this.facility = facility;
        this.investor = investor;
        this.cashInFacility = cashInFacility;
        this.shareInvestor = shareInvestor;
        this.leaseInYear = leaseInYear;
        this.profitFromLease = profitFromLease;
        this.profitFromSale = profitFromSale;
        this.profitFromSaleInYear = profitFromSaleInYear;
        this.netProfitFromSale = netProfitFromSale;
        this.netProfitFromSalePlusLease = netProfitFromSalePlusLease;
        this.totalYield = totalYield;
        this.capitalGains = capitalGains;
        this.capital = capital;
        this.balanceOfCapital = balanceOfCapital;
        this.investorEng = investorEng;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    public BigInteger getId() {
        return id;
    }
    public void setId(BigInteger id) {
        this.id = id;
    }

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "facilityId", referencedColumnName = "id")
    public Facilities getFacility() {
        return facility;
    }
    public void setFacility(Facilities facility) {
        this.facility = facility;
    }

    @Column(name = "Investor")
    public String getInvestor() {
        return investor;
    }
    public void setInvestor(String investor) {
        this.investor = investor;
    }

    @Column(name = "CashInFacility")
    public BigDecimal getCashInFacility() {
        return cashInFacility;
    }
    public void setCashInFacility(BigDecimal cashInFacility) {
        this.cashInFacility = cashInFacility;
    }

    @Column(name = "ShareInvestor")
    public BigDecimal getShareInvestor() {
        return shareInvestor;
    }
    public void setShareInvestor(BigDecimal shareInvestor) {
        this.shareInvestor = shareInvestor;
    }

    @Column(name = "LeaseInYear")
    public BigDecimal getLeaseInYear() {
        return leaseInYear;
    }
    public void setLeaseInYear(BigDecimal leaseInYear) {
        this.leaseInYear = leaseInYear;
    }

    @Column(name = "ProfitFromLease")
    public BigDecimal getProfitFromLease() {
        return profitFromLease;
    }
    public void setProfitFromLease(BigDecimal profitFromLease) {
        this.profitFromLease = profitFromLease;
    }

    @Column(name = "ProfitFromSale")
    public BigDecimal getProfitFromSale() {
        return profitFromSale;
    }
    public void setProfitFromSale(BigDecimal profitFromSale) {
        this.profitFromSale = profitFromSale;
    }

    @Column(name = "ProfitFromSaleInYear")
    public BigDecimal getProfitFromSaleInYear() {
        return profitFromSaleInYear;
    }
    public void setProfitFromSaleInYear(BigDecimal profitFromSaleInYear) {
        this.profitFromSaleInYear = profitFromSaleInYear;
    }

    @Column(name = "NetProfitFromSale")
    public BigDecimal getNetProfitFromSale() {
        return netProfitFromSale;
    }
    public void setNetProfitFromSale(BigDecimal netProfitFromSale) {
        this.netProfitFromSale = netProfitFromSale;
    }

    @Column(name = "NetProfitFromSalePlusLease")
    public BigDecimal getNetProfitFromSalePlusLease() {
        return netProfitFromSalePlusLease;
    }
    public void setNetProfitFromSalePlusLease(BigDecimal netProfitFromSalePlusLease) {
        this.netProfitFromSalePlusLease = netProfitFromSalePlusLease;
    }

    @Column(name = "TotalYield")
    public BigDecimal getTotalYield() {
        return totalYield;
    }
    public void setTotalYield(BigDecimal totalYield) {
        this.totalYield = totalYield;
    }

    @Column(name = "CapitalGains")
    public BigDecimal getCapitalGains() {
        return capitalGains;
    }
    public void setCapitalGains(BigDecimal capitalGains) {
        this.capitalGains = capitalGains;
    }

    @Column(name = "Capital")
    public BigDecimal getCapital() {
        return capital;
    }
    public void setCapital(BigDecimal capital) {
        this.capital = capital;
    }

    @Column(name = "BalanceOfCapital")
    public BigDecimal getBalanceOfCapital() {
        return balanceOfCapital;
    }
    public void setBalanceOfCapital(BigDecimal balanceOfCapital) {
        this.balanceOfCapital = balanceOfCapital;
    }

    @Column(name = "InvestorEng")
    public String getInvestorEng() {
        return investorEng;
    }

    public void setInvestorEng(String investorEng) {
        this.investorEng = investorEng;
    }
}
