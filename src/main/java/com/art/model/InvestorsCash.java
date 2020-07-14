package com.art.model;

import com.art.model.supporting.InvestorsTotalSum;
import com.art.model.supporting.enums.ShareType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

@SqlResultSetMappings(
        {
                @SqlResultSetMapping(
                        name = "InvestorsTotalSumDetailsMapping",
                        classes = @ConstructorResult(
                                targetClass = InvestorsTotalSum.class,
                                columns = {
                                        @ColumnResult(name = "dateGivedCash", type = Date.class),
                                        @ColumnResult(name = "facility", type = String.class),
                                        @ColumnResult(name = "givedCash", type = float.class),
                                        @ColumnResult(name = "dolya", type = float.class),
                                        @ColumnResult(name = "cleanDohod", type = float.class),
                                        @ColumnResult(name = "fullDohod", type = float.class)
                                }))
                ,
                @SqlResultSetMapping(
                        name = "InvestorsTotalSumMapping",
                        classes = @ConstructorResult(
                                targetClass = InvestorsTotalSum.class,
                                columns = {
                                        @ColumnResult(name = "facility", type = String.class),
                                        @ColumnResult(name = "givedCash", type = float.class),
                                        @ColumnResult(name = "dolya", type = float.class),
                                        @ColumnResult(name = "cleanDohod", type = float.class),
                                        @ColumnResult(name = "fullDohod", type = float.class)
                                }))
                ,
                @SqlResultSetMapping(
                        name = "InvestorsCashSumsMapping",
                        classes = @ConstructorResult(
                                targetClass = InvestorsTotalSum.class,
                                columns = {
                                        @ColumnResult(name = "dateGivedCash", type = Date.class),
                                        @ColumnResult(name = "facility", type = String.class),
                                        @ColumnResult(name = "givedCash", type = float.class),
                                        @ColumnResult(name = "dolya", type = float.class),
                                        @ColumnResult(name = "cleanDohod", type = float.class),
                                        @ColumnResult(name = "fullDohod", type = float.class)
                                }))
                ,
                @SqlResultSetMapping(
                        name = "InvestorsCashSumsDetailsMapping",
                        classes = @ConstructorResult(
                                targetClass = InvestorsTotalSum.class,
                                columns = {
                                        @ColumnResult(name = "dateGivedCash", type = Date.class),
                                        @ColumnResult(name = "facility", type = String.class),
                                        @ColumnResult(name = "givedCash", type = float.class),
                                        @ColumnResult(name = "dolya", type = float.class),
                                        @ColumnResult(name = "cleanDohod", type = float.class),
                                        @ColumnResult(name = "fullDohod", type = float.class)
                                }))
        }
)
@NamedNativeQueries(
        {
                @NamedNativeQuery(
                        name = "InvestorsTotalSumDetails",
                        query = "SELECT DateGivedCash, Facility, GivedCash, dolya, cleanDohod, fullDohod " +
                                "FROM InvestorsTotalSumForIDEADetails " +
                                "WHERE InvestorId = ? " +
                                "ORDER BY Facility, DateGivedCash",
                        resultClass = InvestorsTotalSum.class,
                        resultSetMapping = "InvestorsTotalSumDetailsMapping")
                ,
                @NamedNativeQuery(
                        name = "InvestorsTotalSum",
                        query = "SELECT Facility, GivedCash, dolya, cleanDohod, fullDohod " +
                                "FROM InvestorsTotalSumForIDEA " +
                                "WHERE InvestorId = ? " +
                                "ORDER BY Facility",
                        resultClass = InvestorsTotalSum.class,
                        resultSetMapping = "InvestorsTotalSumMapping")
                ,
                @NamedNativeQuery(
                        name = "InvestorsCashSums",
                        query = "SELECT MAX(DateGivedCash) AS DateGivedCash, Facility, SUM(GivedCash) AS GivedCash, " +
                                "0 AS dolya, 0 AS cleanDohod, 0 AS fullDohod " +
                                "FROM InvestorsCash ic " +
                                "LEFT JOIN FACILITYES f ON ic.FacilityId = f.ID " +
                                "WHERE InvestorId = ? " +
                                "GROUP BY InvestorId, FacilityId, Facility " +
                                "ORDER BY Facility",
                        resultClass = InvestorsTotalSum.class,
                        resultSetMapping = "InvestorsCashSumsMapping")
                ,
                @NamedNativeQuery(
                        name = "InvestorsCashSumsDetails",
                        query = "SELECT DateGivedCash, Facility, GivedCash, " +
                                "0 AS dolya, 0 AS cleanDohod, 0 AS fullDohod " +
                                "FROM InvestorsCash ic " +
                                "LEFT JOIN FACILITYES f ON ic.FacilityId = f.ID " +
                                "WHERE InvestorId = ? " +
                                "ORDER BY Facility",
                        resultClass = InvestorsTotalSum.class,
                        resultSetMapping = "InvestorsCashSumsDetailsMapping")
        }
)


@Getter
@Setter
@ToString(exclude = {"investor", "facility"})
@EqualsAndHashCode
@Entity
@Table(name = "InvestorsCash")
public class InvestorsCash implements Serializable {
    private BigInteger id;
    private BigInteger investorId;
    private BigInteger facilityId;
    private BigDecimal givedCash;
    private Date dateGivedCash;
    private Facility facility;
    private Users investor;
    private CashSources cashSource;
    private NewCashDetails newCashDetails;
    private UnderFacilities underFacility;
    private Date dateClosingInvest;
    private TypeClosingInvest typeClosingInvest;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "ShareType")
    private ShareType shareType;

    private Date dateReport;
    private Facility sourceFacility;
    private UnderFacilities sourceUnderFacility;
    private String sourceFlowsId;
    private Rooms room;
    private int isReinvest;
    private BigInteger sourceId;
    private String source;
    private int isDivide;

    @Transient
    private transient Users investorBuyer;

    public InvestorsCash() {

    }

    public InvestorsCash(Facility facility, BigDecimal givedCash) {
        this.facility = facility;
        this.givedCash = givedCash;
    }

    public InvestorsCash(BigDecimal givedCash, Date dateGivedCash, Facility facility, Users investor,
                         CashSources cashSource, NewCashDetails newCashDetails,
                         UnderFacilities underFacility, Date dateClosingInvest,
                         TypeClosingInvest typeClosingInvest, ShareType shareType, Date dateReport,
                         Facility sourceFacility, UnderFacilities sourceUnderFacility, String sourceFlowsId,
                         Rooms room, int isReinvest, BigInteger sourceId, String source, int isDivide) {
        this.givedCash = givedCash;
        this.dateGivedCash = dateGivedCash;
        this.facility = facility;
        this.investor = investor;
        this.cashSource = cashSource;
        this.newCashDetails = newCashDetails;
        this.underFacility = underFacility;
        this.dateClosingInvest = dateClosingInvest;
        this.typeClosingInvest = typeClosingInvest;
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
        this.newCashDetails = cash.getNewCashDetails();
        this.underFacility = cash.getUnderFacility();
        this.dateClosingInvest = cash.getDateClosingInvest();
        this.typeClosingInvest = cash.getTypeClosingInvest();
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

    public InvestorsCash(BigDecimal givedCash, Users investor, Facility sourceFacility) {
        this.givedCash = givedCash;
        this.investor = investor;
        this.sourceFacility = sourceFacility;
    }

    public InvestorsCash(BigInteger facilityId, BigInteger investorId) {
        this.facilityId = facilityId;
        this.investorId = investorId;
    }

    public InvestorsCash(Facility facility, Users investor) {
        this.facility = facility;
        this.investor = investor;
    }

    public InvestorsCash(BigDecimal givedCash, BigInteger facilityId, BigInteger investorId) {
        this.givedCash = givedCash;
        this.facilityId = facilityId;
        this.investorId = investorId;
    }

    public InvestorsCash(BigDecimal givedCash, Facility facility, Users investor) {
        this.givedCash = givedCash;
        this.facility = facility;
        this.investor = investor;
    }

    public InvestorsCash(BigDecimal givedCash, Date dateGivedCash, BigInteger facilityId, BigInteger investorId) {
        this.givedCash = givedCash;
        this.dateGivedCash = dateGivedCash;
        this.facilityId = facilityId;
        this.investorId = investorId;
    }

    public InvestorsCash(BigDecimal givedCash, Date dateGivedCash, Facility facility, Users investor) {
        this.givedCash = givedCash;
        this.dateGivedCash = dateGivedCash;
        this.facility = facility;
        this.investor = investor;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    @Column(name = "InvestorId", insertable = false, updatable = false)
    public BigInteger getInvestorId() {
        return investorId;
    }

    public void setInvestorId(BigInteger investorId) {
        this.investorId = investorId;
    }

    @Column(name = "FacilityId", insertable = false, updatable = false)
    public BigInteger getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(BigInteger facilityId) {
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
    public Users getInvestor() {
        return investor;
    }

    public void setInvestor(Users investor) {
        this.investor = investor;
    }

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "RoomId", referencedColumnName = "id")
    public Rooms getRoom() {
        return room;
    }

    public void setRoom(Rooms room) {
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
    public CashSources getCashSource() {
        return cashSource;
    }

    public void setCashSource(CashSources cashSource) {
        this.cashSource = cashSource;
    }

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "NewCashDetailId", referencedColumnName = "id")
    public NewCashDetails getNewCashDetails() {
        return newCashDetails;
    }

    public void setNewCashDetails(NewCashDetails newCashDetails) {
        this.newCashDetails = newCashDetails;
    }

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "UnderFacilityId", referencedColumnName = "id")
    public UnderFacilities getUnderFacility() {
        return underFacility;
    }

    public void setUnderFacility(UnderFacilities underFacility) {
        this.underFacility = underFacility;
    }

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "SourceUnderFacilityId", referencedColumnName = "id")
    public UnderFacilities getSourceUnderFacility() {
        return sourceUnderFacility;
    }

    public void setSourceUnderFacility(UnderFacilities sourceUnderFacility) {
        this.sourceUnderFacility = sourceUnderFacility;
    }

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "TypeClosingInvestId", referencedColumnName = "id")
    public TypeClosingInvest getTypeClosingInvest() {
        return typeClosingInvest;
    }

    public void setTypeClosingInvest(TypeClosingInvest typeClosingInvest) {
        this.typeClosingInvest = typeClosingInvest;
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
    public BigInteger getSourceId() {
        return sourceId;
    }

    public void setSourceId(BigInteger sourceId) {
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
    public Users getInvestorBuyer() {
        return investorBuyer;
    }

    public void setInvestorBuyer(Users investorBuyer) {
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

}
