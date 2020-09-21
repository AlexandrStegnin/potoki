package com.art.model;

import com.art.model.supporting.enums.ShareType;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

@ToString
@EqualsAndHashCode(exclude = {"facility", "investor", "underFacility"})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "InvestorsFlowsSale")
public class SalePayment {
    private BigInteger id;
    private Facility facility;
    private AppUser investor;

    @Enumerated(EnumType.STRING)
    @Column(name = "ShareType")
    private ShareType shareType;

    private BigDecimal cashInFacility;
    private Date dateGived;
    private BigDecimal investorShare;
    private BigDecimal cashInUnderFacility;
    private BigDecimal profitToCashingAuto;
    private BigDecimal profitToCashingMain;
    private BigDecimal profitToReInvest;
    private UnderFacility underFacility;
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

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "facilityId", referencedColumnName = "id")
    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "underFacilityId", referencedColumnName = "id")
    public UnderFacility getUnderFacility() {
        return underFacility;
    }

    public void setUnderFacility(UnderFacility underFacility) {
        this.underFacility = underFacility;
    }

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "investorId", referencedColumnName = "id")
    public AppUser getInvestor() {
        return investor;
    }

    public void setInvestor(AppUser investor) {
        this.investor = investor;
    }

    @Enumerated(EnumType.STRING)
    public ShareType getShareType() {
        return shareType;
    }

    public void setShareType(ShareType shareType) {
        this.shareType = shareType;
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
