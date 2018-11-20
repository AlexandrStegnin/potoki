package com.art.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

@ToString
@EqualsAndHashCode(exclude = {"facility", "investor", "shareKind", "underFacility"})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "InvestorsFlowsSale")
public class InvestorsFlowsSale implements Serializable {
    private BigInteger id;
    private Facilities facility;
    private Users investor;
    private ShareKind shareKind;
    private BigDecimal cashInFacility;
    private Date dateGived;
    private BigDecimal investorShare;
    private BigDecimal cashInUnderFacility;
    private BigDecimal profitToCashingAuto;
    private BigDecimal profitToCashingMain;
    private BigDecimal profitToReInvest;
    private UnderFacilities underFacility;
    private Date dateSale;
    private int isReinvest;

    @Column
    private BigInteger sourceId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "facilityId", referencedColumnName = "id")
    public Facilities getFacility() {
        return facility;
    }

    public void setFacility(Facilities facility) {
        this.facility = facility;
    }

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "underFacilityId", referencedColumnName = "id")
    public UnderFacilities getUnderFacility() {
        return underFacility;
    }

    public void setUnderFacility(UnderFacilities underFacility) {
        this.underFacility = underFacility;
    }

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "investorId", referencedColumnName = "id")
    public Users getInvestor() {
        return investor;
    }

    public void setInvestor(Users investor) {
        this.investor = investor;
    }

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "shareKindId", referencedColumnName = "id")
    public ShareKind getShareKind() {
        return shareKind;
    }

    public void setShareKind(ShareKind shareKind) {
        this.shareKind = shareKind;
    }

    @Column(name = "CashInFacility")
    public BigDecimal getCashInFacility() {
        return cashInFacility;
    }

    public void setCashInFacility(BigDecimal cashInFacility) {
        this.cashInFacility = cashInFacility;
    }

    @Column(name = "DateGived")
    public Date getDateGived() {
        return dateGived;
    }

    public void setDateGived(Date dateGived) {
        this.dateGived = dateGived;
    }

    @Column(name = "InvestorShare")
    public BigDecimal getInvestorShare() {
        return investorShare;
    }

    public void setInvestorShare(BigDecimal investorShare) {
        this.investorShare = investorShare;
    }

    @Column(name = "CashInUnderFacility")
    public BigDecimal getCashInUnderFacility() {
        return cashInUnderFacility;
    }

    public void setCashInUnderFacility(BigDecimal cashInUnderFacility) {
        this.cashInUnderFacility = cashInUnderFacility;
    }

    @Column(name = "ProfitToCashingAuto")
    public BigDecimal getProfitToCashingAuto() {
        return profitToCashingAuto;
    }

    public void setProfitToCashingAuto(BigDecimal profitToCashingAuto) {
        this.profitToCashingAuto = profitToCashingAuto;
    }

    @Column(name = "ProfitToCashingMain")
    public BigDecimal getProfitToCashingMain() {
        return profitToCashingMain;
    }

    public void setProfitToCashingMain(BigDecimal profitToCashingMain) {
        this.profitToCashingMain = profitToCashingMain;
    }

    @Column(name = "ProfitToReInvest")
    public BigDecimal getProfitToReInvest() {
        return profitToReInvest;
    }

    public void setProfitToReInvest(BigDecimal profitToReInvest) {
        this.profitToReInvest = profitToReInvest;
    }

    @Column(name = "DateSale")
    public Date getDateSale() {
        return dateSale;
    }

    public void setDateSale(Date dateSale) {
        this.dateSale = dateSale;
    }

    @Column(name = "IsReinvest")
    public int getIsReinvest() {
        return isReinvest;
    }

    public void setIsReinvest(int isReinvest) {
        this.isReinvest = isReinvest;
    }

    @Transient
    public String getDateGivedToLocalDate() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            localDate = format.format(dateGived);
        } catch (Exception ignored) {
        }

        return localDate;
    }

    @Transient
    public String getDateSaleToLocalDate() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            localDate = format.format(dateSale);
        } catch (Exception ignored) {
        }

        return localDate;
    }

}
