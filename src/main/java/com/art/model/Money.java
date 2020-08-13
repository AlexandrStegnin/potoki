package com.art.model;

import com.art.model.supporting.enums.ShareType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@ToString(exclude = {"investor", "facility"})
@EqualsAndHashCode
@Entity
@Table(name = "money")
public class Money implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "given_cash")
    private BigDecimal givenCash;

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
        this.sourceUnderFacility = cash.sourceUnderFacility;
        this.sourceFlowsId = cash.getSourceFlowsId();
        this.room = cash.getRoom();
        this.isReinvest = cash.getIsReinvest();
        this.sourceId = cash.getSourceId();
        this.source = cash.getId().toString();
        this.isDivide = cash.getIsDivide();
        this.realDateGiven = null;
        this.transactionUUID = null;
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

}
