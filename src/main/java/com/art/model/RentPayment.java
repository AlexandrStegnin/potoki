package com.art.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@ToString
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Entity
@Table(name = "InvestorsFlows")
public class RentPayment implements Serializable {
    private Long id;
    private Date reportDate;
    private Facility facility;
    private UnderFacility underFacility;
    private AppUser investor;
    private String shareKind;
    private float givedCash;
    private float share;
    private float taxation;
    private float cashing;
    private float summa;
    private float sumInUnderFacility;
    private float shareForSvod;
    private float onInvestors;
    private float afterTax;
    private float afterDeductionEmptyFacility;
    private float afterCashing;
    private String reInvest;
    private Facility reFacility;
    private int isReinvest;
    private Room room;

    public RentPayment() {
    }

    public RentPayment(Facility facility) {
        this.facility = facility;
    }

    public RentPayment(Facility facility, float afterCashing) {
        this.facility = facility;
        this.afterCashing = afterCashing;
    }

    public RentPayment(Facility facility, UnderFacility underFacility, AppUser investor) {
        this.facility = facility;
        this.underFacility = underFacility;
        this.investor = investor;
    }

    public RentPayment(Facility facility, UnderFacility underFacility, AppUser investor, float afterCashing) {
        this.facility = facility;
        this.underFacility = underFacility;
        this.investor = investor;
        this.afterCashing = afterCashing;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "reFacilityId", referencedColumnName = "id")
    public Facility getReFacility() {
        return reFacility;
    }

    public void setReFacility(Facility reFacility) {
        this.reFacility = reFacility;
    }

    @Column(name = "ReportDate")
    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    @Column(name = "ShareKind")
    public String getShareKind() {
        return shareKind;
    }

    public void setShareKind(String shareKind) {
        this.shareKind = shareKind;
    }

    @Column(name = "GivedCash")
    public float getGivedCash() {
        return givedCash;
    }

    public void setGivedCash(float givedCash) {
        this.givedCash = givedCash;
    }

    @Column(name = "Share")
    public float getShare() {
        return share;
    }

    public void setShare(float share) {
        this.share = share;
    }

    @Column(name = "Taxation")
    public float getTaxation() {
        return taxation;
    }

    public void setTaxation(float taxation) {
        this.taxation = taxation;
    }

    @Column(name = "Cashing")
    public float getCashing() {
        return cashing;
    }

    public void setCashing(float cashing) {
        this.cashing = cashing;
    }

    @Column(name = "Summa")
    public float getSumma() {
        return summa;
    }

    public void setSumma(float summa) {
        this.summa = summa;
    }

    @Column(name = "OnInvestors")
    public float getOnInvestors() {
        return onInvestors;
    }

    public void setOnInvestors(float onInvestors) {
        this.onInvestors = onInvestors;
    }

    @Column(name = "AfterTax")
    public float getAfterTax() {
        return afterTax;
    }

    public void setAfterTax(float afterTax) {
        this.afterTax = afterTax;
    }

    @Column(name = "AfterCashing")
    public float getAfterCashing() {
        return afterCashing;
    }

    public void setAfterCashing(float afterCashing) {
        this.afterCashing = afterCashing;
    }

    @Column(name = "ReInvest")
    public String getReInvest() {
        return reInvest;
    }

    public void setReInvest(String reInvest) {
        this.reInvest = reInvest;
    }

    @Column(name = "SumInUnderFacility")
    public float getSumInUnderFacility() {
        return sumInUnderFacility;
    }

    public void setSumInUnderFacility(float sumInUnderFacility) {
        this.sumInUnderFacility = sumInUnderFacility;
    }

    @Column(name = "ShareForSvod")
    public float getShareForSvod() {
        return shareForSvod;
    }

    public void setShareForSvod(float shareForSvod) {
        this.shareForSvod = shareForSvod;
    }

    @Transient
    public String getReportDateToLocalDate() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            localDate = format.format(reportDate);
        } catch (Exception ignored) {
        }

        return localDate;
    }

    @Transient
    public String getYearReportDate() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        try {
            localDate = format.format(reportDate);
        } catch (Exception ignored) {
        }

        return localDate;
    }

    @Transient
    public String getMonthReportDate() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("MM");
        try {
            localDate = format.format(reportDate);
        } catch (Exception ignored) {
        }

        return localDate;
    }

    @Column(name = "AfterDeductionEmptyFacility")
    public float getAfterDeductionEmptyFacility() {
        return afterDeductionEmptyFacility;
    }

    public void setAfterDeductionEmptyFacility(float afterDeductionEmptyFacility) {
        this.afterDeductionEmptyFacility = afterDeductionEmptyFacility;
    }

    @Column(name = "IsReinvest")
    public int getIsReinvest() {
        return isReinvest;
    }

    public void setIsReinvest(int isReinvest) {
        this.isReinvest = isReinvest;
    }

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "RoomId", referencedColumnName = "id")
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
