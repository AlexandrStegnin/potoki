package com.art.model;

import com.art.model.supporting.enums.CashType;
import com.art.model.supporting.enums.ShareType;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;


@Data
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sale_payment")
@EqualsAndHashCode(exclude = {"facility", "investor", "underFacility"})
public class SalePayment implements Cash {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "facility_id", referencedColumnName = "id")
    private Facility facility;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "investor_id", referencedColumnName = "id")
    private AppUser investor;

    @Enumerated(EnumType.STRING)
    @Column(name = "share_type")
    private ShareType shareType;

    @Column(name = "cash_in_facility")
    private BigDecimal cashInFacility;

    @Column(name = "date_given")
    private Date dateGiven;

    @Column(name = "investor_share")
    private BigDecimal investorShare;

    @Column(name = "cash_in_under_facility")
    private BigDecimal cashInUnderFacility;

    @Column(name = "profit_to_cashing_auto")
    private BigDecimal profitToCashingAuto;

    @Column(name = "profit_to_cashing_main")
    private BigDecimal profitToCashingMain;

    @Column(name = "profit_to_reinvest")
    private BigDecimal profitToReInvest;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "under_facility_id", referencedColumnName = "id")
    private UnderFacility underFacility;

    @Column(name = "date_sale")
    private Date dateSale;

    @Column(name = "is_reinvest")
    private int isReinvest;

    @Column(name = "source_id")
    private Long sourceId;

    @Column(name = "acc_tx_id")
    private Long accTxId;

    @Transient
    public String getDateGivenToLocalDate() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            localDate = format.format(dateGiven);
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

    public SalePayment(SalePayment salePayment) {
        this.id = null;
        this.facility = salePayment.getFacility();
        this.investor = salePayment.getInvestor();
        this.shareType = salePayment.getShareType();
        this.cashInFacility = salePayment.getCashInFacility();
        this.dateGiven = salePayment.getDateGiven();
        this.investorShare = salePayment.getInvestorShare();
        this.cashInUnderFacility = salePayment.getCashInUnderFacility();
        this.profitToCashingAuto = salePayment.getProfitToCashingAuto();
        this.profitToCashingMain = salePayment.getProfitToCashingMain();
        this.underFacility = salePayment.getUnderFacility();
        this.dateSale = salePayment.getDateSale();
        this.isReinvest = 0;
        this.sourceId = null;
        this.profitToReInvest = null;
        this.accTxId = salePayment.getAccTxId();
    }

    @Override
    public BigDecimal getGivenCash() {
        return this.profitToReInvest;
    }

    @Override
    public CashType getCashType() {
        return CashType.SALE_CASH;
    }

    @Override
    public String getOwnerName() {
        return this.investor.getLogin();
    }

    @Override
    public String getFromName() {
        return this.facility.getFullName();
    }
}
