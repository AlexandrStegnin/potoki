package com.art.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "underFacilities")
@EqualsAndHashCode
@Entity
@Table(name = "MainFlows")
public class MainFlows implements Serializable {

    private BigInteger id;
    private String planFact;
    private String fileName;
    private Date settlementDate;
    private float summa;
    private String orgName;
    private String inn;
    private String account;
    private String purposePayment;
    private String payment;
    private String levelTwo;
    private String levelThree;
    private UnderFacilities underFacilities;

    public MainFlows() {

    }

    public MainFlows(Date settlementDate, float summa, String payment, UnderFacilities underFacilities){
        this.settlementDate = settlementDate;
        this.summa = summa;
        this.payment = payment;
        this.underFacilities = underFacilities;
    }

    public MainFlows(UnderFacilities underFacilities) {
        this.underFacilities = underFacilities;
    }

    public MainFlows(UnderFacilities underFacilities, Date settlementDate) {
        this.underFacilities = underFacilities;
        this.settlementDate = settlementDate;
    }

    public MainFlows(UnderFacilities underFacilities, Date settlementDate, float summa) {
        this.underFacilities = underFacilities;
        this.settlementDate = settlementDate;
        this.summa = summa;
    }

    public MainFlows(UnderFacilities underFacilities, float summa) {
        this.underFacilities = underFacilities;
        this.summa = summa;
    }

    public MainFlows(UnderFacilities underFacilities, String payment) {
        this.underFacilities = underFacilities;
        this.payment = payment;
    }

    public MainFlows(UnderFacilities underFacilities, String payment, float summa) {
        this.underFacilities = underFacilities;
        this.payment = payment;
        this.summa = summa;
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

    @Column(name = "PlanFact")
    public String getPlanFact() {
        return planFact;
    }

    public void setPlanFact(String planFact) {
        this.planFact = planFact;
    }

    @Column(name = "FileName")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column(name = "SettlementDate")
    public Date getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
    }

    @Column(name = "Summa")
    public float getSumma() {
        return summa;
    }

    public void setSumma(float summa) {
        this.summa = summa;
    }

    @Column(name = "OrgName")
    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @Column(name = "Inn")
    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    @Column(name = "Account")
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Column(name = "PurposePayment")
    public String getPurposePayment() {
        return purposePayment;
    }

    public void setPurposePayment(String purposePayment) {
        this.purposePayment = purposePayment;
    }

    @Column(name = "Payment")
    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    /*
    @Column(name = "UnderFacilityId", insertable = false, updatable = false)
    public BigInteger getUnderFacilityId(){
        return underFacilityId;
    }
    public void setUnderFacilityId(BigInteger underFacilityId){
        this.underFacilityId = underFacilityId;
    }
    */

    @Column(name = "LevelTwo")
    public String getLevelTwo() {
        return levelTwo;
    }

    public void setLevelTwo(String levelTwo) {
        this.levelTwo = levelTwo;
    }

    @Column(name = "LevelThree")
    public String getLevelThree() {
        return levelThree;
    }

    public void setLevelThree(String levelThree) {
        this.levelThree = levelThree;
    }

    @ManyToOne
    @JoinColumn(name = "UnderFacilityId")
    public UnderFacilities getUnderFacilities() {
        return underFacilities;
    }

    public void setUnderFacilities(UnderFacilities underFacilities) {
        this.underFacilities = underFacilities;
    }

    @Transient
    public String getSettlementDateToLocalDate() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            localDate = format.format(settlementDate);
        } catch (Exception ignored) {
        }

        return localDate;
    }

    @Transient
    public String getYearSettlementDate() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        try {
            localDate = format.format(settlementDate);
        } catch (Exception ignored) {
        }

        return localDate;
    }

    @Transient
    public String getSmallYearSettlementDate() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("yy");
        try {
            localDate = format.format(settlementDate);
        } catch (Exception ignored) {
        }

        return localDate;
    }

    @Transient
    public String getMonthSettlementDate() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("MM");
        try {
            localDate = format.format(settlementDate);
        } catch (Exception ignored) {
        }

        return localDate;
    }

    @Transient
    public String getMonthNameSettlementDate() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("MMM");
        try {
            localDate = format.format(settlementDate);
        } catch (Exception ignored) {
        }

        return localDate;
    }

    @Transient
    public String getMonthAndYearSettlementDate() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("MMM yyyy");
        try {
            localDate = format.format(settlementDate);
        } catch (Exception ignored) {
        }

        return localDate;
    }

    @Transient
    public String getFacilityName() {
        return getUnderFacilities().getFacility().getFacility();
    }
}
