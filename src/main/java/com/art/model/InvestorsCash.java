package com.art.model;

import com.art.model.supporting.enums.ShareType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@ToString(exclude = {"investor", "facility"})
@EqualsAndHashCode
@Entity
@Table(name = "InvestorsCash")
public class InvestorsCash implements Serializable {
    private Long id;
    private Long investorId;
    private Long facilityId;
    private BigDecimal givedCash;
    private Date dateGivedCash;
    private Facility facility;
    private AppUser investor;
    private CashSource cashSource;
    private NewCashDetail newCashDetail;
    private UnderFacility underFacility;
    private Date dateClosingInvest;
    private TypeClosing typeClosing;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "ShareType")
    private ShareType shareType;

    private Date dateReport;
    private Facility sourceFacility;
    private UnderFacility sourceUnderFacility;
    private String sourceFlowsId;
    private Room room;
    private int isReinvest;
    private Long sourceId;
    private String source;
    private int isDivide;

    @Transient
    private transient AppUser investorBuyer;

    public InvestorsCash() {
    }

    public InvestorsCash(Facility facility, BigDecimal givedCash) {
        this.facility = facility;
        this.givedCash = givedCash;
    }

    public InvestorsCash(BigDecimal givedCash, Date dateGivedCash, Facility facility, AppUser investor,
                         CashSource cashSource, NewCashDetail newCashDetail,
                         UnderFacility underFacility, Date dateClosingInvest,
                         TypeClosing typeClosing, ShareType shareType, Date dateReport,
                         Facility sourceFacility, UnderFacility sourceUnderFacility, String sourceFlowsId,
                         Room room, int isReinvest, Long sourceId, String source, int isDivide) {
        this.givedCash = givedCash;
        this.dateGivedCash = dateGivedCash;
        this.facility = facility;
        this.investor = investor;
        this.cashSource = cashSource;
        this.newCashDetail = newCashDetail;
        this.underFacility = underFacility;
        this.dateClosingInvest = dateClosingInvest;
        this.typeClosing = typeClosing;
        this.shareType = shareType;
        this.dateReport = dateReport;
        this.sourceFacility = sourceFacility;
        this.sourceUnderFacility = sourceUnderFacility;
        this.sourceFlowsId = sourceFlowsId;
        this.room = room;
        this.isReinvest = isReinvest;
        this.sourceId = sourceId;
        this.source = source;
        this.isDivide = isDivide;
    }

    public InvestorsCash(InvestorsCash cash) {
        this.id = null;
        this.investorId = null;
        this.facilityId = null;
        this.givedCash = cash.getGivedCash();
        this.dateGivedCash = cash.getDateGivedCash();
        this.facility = cash.getFacility();
        this.investor = cash.getInvestor();
        this.cashSource = cash.getCashSource();
        this.newCashDetail = cash.getNewCashDetail();
        this.underFacility = cash.getUnderFacility();
        this.dateClosingInvest = cash.getDateClosingInvest();
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
    }

    public InvestorsCash(BigDecimal givedCash, AppUser investor, Facility sourceFacility) {
        this.givedCash = givedCash;
        this.investor = investor;
        this.sourceFacility = sourceFacility;
    }

    public InvestorsCash(Long facilityId, Long investorId) {
        this.facilityId = facilityId;
        this.investorId = investorId;
    }

    public InvestorsCash(Facility facility, AppUser investor) {
        this.facility = facility;
        this.investor = investor;
    }

    public InvestorsCash(BigDecimal givedCash, Long facilityId, Long investorId) {
        this.givedCash = givedCash;
        this.facilityId = facilityId;
        this.investorId = investorId;
    }

    public InvestorsCash(BigDecimal givedCash, Facility facility, AppUser investor) {
        this.givedCash = givedCash;
        this.facility = facility;
        this.investor = investor;
    }

    public InvestorsCash(BigDecimal givedCash, Date dateGivedCash, Long facilityId, Long investorId) {
        this.givedCash = givedCash;
        this.dateGivedCash = dateGivedCash;
        this.facilityId = facilityId;
        this.investorId = investorId;
    }

    public InvestorsCash(BigDecimal givedCash, Date dateGivedCash, Facility facility, AppUser investor) {
        this.givedCash = givedCash;
        this.dateGivedCash = dateGivedCash;
        this.facility = facility;
        this.investor = investor;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "InvestorId", insertable = false, updatable = false)
    public Long getInvestorId() {
        return investorId;
    }

    public void setInvestorId(Long investorId) {
        this.investorId = investorId;
    }

    @Column(name = "FacilityId", insertable = false, updatable = false)
    public Long getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Long facilityId) {
        this.facilityId = facilityId;
    }

    @Column(name = "SourceFlowsId")
    public String getSourceFlowsId() {
        return sourceFlowsId;
    }

    public void setSourceFlowsId(String sourceFlowsId) {
        this.sourceFlowsId = sourceFlowsId;
    }

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "InvestorId", referencedColumnName = "id")
    public AppUser getInvestor() {
        return investor;
    }

    public void setInvestor(AppUser investor) {
        this.investor = investor;
    }

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "RoomId", referencedColumnName = "id")
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "FacilityId", referencedColumnName = "id")
    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "SourceFacilityId", referencedColumnName = "id")
    public Facility getSourceFacility() {
        return sourceFacility;
    }

    public void setSourceFacility(Facility sourceFacility) {
        this.sourceFacility = sourceFacility;
    }

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "CashSourceId", referencedColumnName = "id")
    public CashSource getCashSource() {
        return cashSource;
    }

    public void setCashSource(CashSource cashSource) {
        this.cashSource = cashSource;
    }

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "NewCashDetailId", referencedColumnName = "id")
    public NewCashDetail getNewCashDetail() {
        return newCashDetail;
    }

    public void setNewCashDetail(NewCashDetail newCashDetail) {
        this.newCashDetail = newCashDetail;
    }

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "UnderFacilityId", referencedColumnName = "id")
    public UnderFacility getUnderFacility() {
        return underFacility;
    }

    public void setUnderFacility(UnderFacility underFacility) {
        this.underFacility = underFacility;
    }

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "SourceUnderFacilityId", referencedColumnName = "id")
    public UnderFacility getSourceUnderFacility() {
        return sourceUnderFacility;
    }

    public void setSourceUnderFacility(UnderFacility sourceUnderFacility) {
        this.sourceUnderFacility = sourceUnderFacility;
    }

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "TypeClosingInvestId", referencedColumnName = "id")
    public TypeClosing getTypeClosing() {
        return typeClosing;
    }

    public void setTypeClosing(TypeClosing typeClosing) {
        this.typeClosing = typeClosing;
    }

    @Transient
    public String getShareTypeTitle() {
        return shareType.getTitle();
    }

    @Column(name = "DateGivedCash")
    public Date getDateGivedCash() {
        return dateGivedCash;
    }

    public void setDateGivedCash(Date dateGivedCash) {
        this.dateGivedCash = dateGivedCash;
    }

    @Column(name = "GivedCash")
    public BigDecimal getGivedCash() {
        return givedCash;
    }

    public void setGivedCash(BigDecimal givedCash) {
        this.givedCash = givedCash;
    }

    @Column(name = "DateClosingInvest")
    public Date getDateClosingInvest() {
        return dateClosingInvest;
    }

    public void setDateClosingInvest(Date dateClosingInvest) {
        this.dateClosingInvest = dateClosingInvest;
    }

    @Column(name = "Source")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Column(name = "SourceId")
    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    @Transient
    public String getDateGivedCashToLocalDate() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            localDate = format.format(dateGivedCash);
        } catch (Exception ignored) {
        }

        return localDate;
    }

    @Transient
    public String getDateClosingInvestToLocalDate() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            localDate = format.format(dateClosingInvest);
        } catch (Exception ignored) {
        }

        return localDate;
    }

    @Column(name = "dateReport")
    public Date getDateReport() {
        return dateReport;
    }

    public void setDateReport(Date dateReport) {
        this.dateReport = dateReport;
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

    @Column(name = "IsReinvest")
    public int getIsReinvest() {
        return isReinvest;
    }

    public void setIsReinvest(int isReinvest) {
        this.isReinvest = isReinvest;
    }

    @Column(name = "IsDivide")
    public int getIsDivide() {
        return isDivide;
    }

    public void setIsDivide(int isDivide) {
        this.isDivide = isDivide;
    }

    @Column(name = "RealDateGiven")
    private Date realDateGiven;

    public Date getRealDateGiven() {
        return realDateGiven;
    }

    public void setRealDateGiven(Date realDateGiven) {
        this.realDateGiven = realDateGiven;
    }

    private String transactionUUID;

    @Column(name = "transaction_uuid")
    public String getTransactionUUID() {
        return transactionUUID;
    }

}
