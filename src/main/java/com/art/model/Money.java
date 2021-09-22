package com.art.model;

import com.art.config.SecurityUtils;
import com.art.model.supporting.dto.CashingMoneyDTO;
import com.art.model.supporting.dto.RentPaymentDTO;
import com.art.model.supporting.dto.SalePaymentDTO;
import com.art.model.supporting.enums.CashType;
import com.art.model.supporting.enums.MoneyState;
import com.art.model.supporting.enums.ShareType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@Table(name = "money")
@ToString(exclude = {"investor", "facility", "transaction"})
@EqualsAndHashCode(exclude = {"investor", "facility", "transaction"})
public class Money implements Cash {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "given_cash")
    private BigDecimal givenCash;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_given")
    private Date dateGiven;

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "facility_id", referencedColumnName = "id")
    private Facility facility;

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "investor_id", referencedColumnName = "id")
    private AppUser investor;

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "cash_source_id", referencedColumnName = "id")
    private CashSource cashSource;

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "new_cash_detail_id", referencedColumnName = "id")
    private NewCashDetail newCashDetail;

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "under_facility_id", referencedColumnName = "id")
    private UnderFacility underFacility;

    @Column(name = "date_closing")
    private Date dateClosing;

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "type_closing_id", referencedColumnName = "id")
    private TypeClosing typeClosing;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "share_type")
    private ShareType shareType;

    @Column(name = "date_report")
    private Date dateReport;

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "source_facility_id", referencedColumnName = "id")
    private Facility sourceFacility;

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "source_under_facility_id", referencedColumnName = "id")
    private UnderFacility sourceUnderFacility;

    @Column(name = "source_flow_id")
    private String sourceFlowsId;

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;

    @Column(name = "is_reinvest")
    private int isReinvest;

    @Column(name = "source_id")
    private Long sourceId;

    @Column(name = "source")
    private String source;

    @Column(name = "is_divide")
    private int isDivide;

    @Column(name = "real_date_given")
    private Date realDateGiven;

    @Column(name = "transaction_uuid")
    private String transactionUUID;

    @Transient
    private transient AppUser investorBuyer;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private MoneyState state = MoneyState.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "acc_tx_id")
    private AccountTransaction transaction;

    @Column(name = "creation_time")
    private Date creationTime;

    @Column(name = "modified_time")
    private Date modifiedTime;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @PrePersist
    public void prePersist() {
        this.creationTime = new Date();
        this.createdBy = SecurityUtils.getUsername();
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedTime = new Date();
        this.modifiedBy = SecurityUtils.getUsername();
    }

    public Money() {
    }

    public Money(Money cash) {
        this.id = null;
        this.givenCash = cash.getGivenCash();
        this.dateGiven = cash.getDateGiven();
        this.facility = cash.getFacility();
        this.investor = cash.getInvestor();
        this.cashSource = cash.getCashSource();
        this.newCashDetail = cash.getNewCashDetail();
        this.underFacility = cash.getUnderFacility();
        this.dateClosing = cash.getDateClosing();
        this.typeClosing = cash.getTypeClosing();
        this.shareType = cash.getShareType();
        this.dateReport = cash.getDateReport();
        this.sourceFacility = cash.getSourceFacility();
        this.sourceUnderFacility = cash.getSourceUnderFacility();
        this.sourceFlowsId = cash.getSourceFlowsId();
        this.room = cash.getRoom();
        this.isReinvest = cash.getIsReinvest();
        this.sourceId = cash.getSourceId();
        this.source = cash.getId().toString();
        this.isDivide = cash.getIsDivide();
        this.realDateGiven = null;
        this.transactionUUID = null;
        this.state = MoneyState.ACTIVE;
        this.transaction = cash.getTransaction();
    }

    public Money(Facility facility, UnderFacility underFacility, AppUser investor,
                 BigDecimal givenCash, Date dateGiven, CashSource cashSource,
                 NewCashDetail newCashDetail, ShareType shareType) {
        this.facility = facility;
        this.underFacility = underFacility;
        this.investor = investor;
        this.givenCash = givenCash;
        this.dateGiven = dateGiven;
        this.cashSource = cashSource;
        this.newCashDetail = newCashDetail;
        this.shareType = shareType;
    }

    public Money(RentPayment rentPayment, RentPaymentDTO dto, Facility facility) {
        this.givenCash = BigDecimal.valueOf(rentPayment.getAfterCashing());
        this.dateGiven = dto.getDateGiven();
        this.facility = facility;
        this.investor = rentPayment.getInvestor();
        this.shareType = ShareType.fromTitle(dto.getShareType());
        this.dateReport = rentPayment.getDateReport();
        this.sourceFacility = rentPayment.getFacility();
        this.sourceUnderFacility = rentPayment.getUnderFacility();
        this.sourceFlowsId = String.valueOf(rentPayment.getId());
        this.room = rentPayment.getRoom();
        this.state = MoneyState.ACTIVE;
    }

    public Money(SalePayment salePayment, SalePaymentDTO dto, Facility facility, UnderFacility underFacility, NewCashDetail newCashDetail) {
        this.givenCash = salePayment.getProfitToReInvest();
        this.dateGiven = dto.getDateGiven();
        this.facility = facility;
        this.investor = salePayment.getInvestor();
        this.shareType = ShareType.fromTitle(dto.getShareType());
        this.dateReport = salePayment.getDateSale();
        this.sourceFacility = salePayment.getFacility();
        this.sourceUnderFacility = salePayment.getUnderFacility();
        this.sourceFlowsId = String.valueOf(salePayment.getId());
        this.underFacility = underFacility;
        this.newCashDetail = newCashDetail;
        this.state = MoneyState.ACTIVE;
    }

    public Money(SalePayment salePayment, Facility facility, UnderFacility underFacility, NewCashDetail newCashDetail, Date dateReinvest, String shareType) {
        this.givenCash = salePayment.getProfitToReInvest();
        this.dateGiven = dateReinvest;
        this.facility = facility;
        this.investor = salePayment.getInvestor();
        this.shareType = ShareType.fromTitle(shareType);
        this.dateReport = salePayment.getDateSale();
        this.sourceFacility = salePayment.getFacility();
        this.sourceUnderFacility = salePayment.getUnderFacility();
        this.sourceFlowsId = String.valueOf(salePayment.getId());
        this.underFacility = underFacility;
        this.newCashDetail = newCashDetail;
        this.state = MoneyState.ACTIVE;
    }

    public Money(AppUser investor, Facility facility, UnderFacility underFacility, CashingMoneyDTO cashingMoneyDTO) {
        this.investor = investor;
        this.facility = facility;
        this.underFacility = underFacility;
        this.dateGiven = cashingMoneyDTO.getDateCashing();
        this.givenCash = cashingMoneyDTO.getCash();
    }

    @Transient
    public String getShareTypeTitle() {
        return shareType.getTitle();
    }

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
    public String getDateClosingToLocalDate() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            localDate = format.format(dateClosing);
        } catch (Exception ignored) {
        }

        return localDate;
    }

    @Transient
    public String getDateReportToLocalDate() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            localDate = format.format(dateReport);
        } catch (Exception ignored) {
        }

        return localDate;
    }

    @Transient
    public AppUser getInvestorBuyer() {
        return investorBuyer;
    }

    public void setInvestorBuyer(AppUser investorBuyer) {
        this.investorBuyer = investorBuyer;
    }

    @Override
    public CashType getCashType() {
        return CashType.INVESTOR_CASH;
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
