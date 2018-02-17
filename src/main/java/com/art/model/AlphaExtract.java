package com.art.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.sql.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode(exclude = {"id", "pId", "orgName", "kpp", "bik",
        "bankName", "purposePayment", "codeDebet", "docType", "period", "tags"})
@Entity
@Table(name = "ALPHA_EXTRACT")
public class AlphaExtract implements Serializable {

    private BigInteger id;
    private String pId;
    private Date dateOper;
    private String docNumber;
    private float debet;
    private float credit;
    private String orgName;
    private String inn;
    private String kpp;
    private String account;
    private String bik;
    private String bankName;
    private String purposePayment;
    private String codeDebet;
    private String docType;
    private Date period;
    private String correctOrgName;
    private AlphaCorrectTags tags;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    @Column(name = "pId")
    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    @Column(name = "DATE_OPER")
    public Date getDateOper() {
        return dateOper;
    }

    public void setDateOper(Date dateOper) {
        this.dateOper = dateOper;
    }

    @Column(name = "DOC_NUMBER")
    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    @Column(name = "DEBET")
    public float getDebet() {
        return debet;
    }

    public void setDebet(float debet) {
        this.debet = debet;
    }

    @Column(name = "CREDIT")
    public float getCredit() {
        return credit;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }

    @Column(name = "ORG_NAME")
    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @Column(name = "INN")
    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    @Column(name = "KPP")
    public String getKpp() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    @Column(name = "ACCOUNT")
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Column(name = "BIK")
    public String getBik() {
        return bik;
    }

    public void setBik(String bik) {
        this.bik = bik;
    }

    @Column(name = "BANK_NAME")
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Column(name = "PURPOSE_PAYMENT")
    public String getPurposePayment() {
        return purposePayment;
    }

    public void setPurposePayment(String purposePayment) {
        this.purposePayment = purposePayment;
    }

    @Column(name = "CODE_DEBET")
    public String getCodeDebet() {
        return codeDebet;
    }

    public void setCodeDebet(String codeDebet) {
        this.codeDebet = codeDebet;
    }

    @Column(name = "DOC_TYPE")
    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    @Column(name = "Period")
    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }

    @Column(name = "CorrectOrgName")
    public String getCorrectOrgName() {
        return correctOrgName;
    }

    public void setCorrectOrgName(String correctOrgName) {
        this.correctOrgName = correctOrgName;
    }

    @OneToOne
    @JoinColumn(name = "CorrectTagId", referencedColumnName = "ID")
    public AlphaCorrectTags getTags() {
        return tags;
    }

    public void setTags(AlphaCorrectTags tags) {
        this.tags = tags;
    }

    @Transient
    public String getDateOperToLocalDate() {
        String localDate = null;
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        localDate = format.format(dateOper);
        return localDate;
    }

    @Transient
    public String getPeriodToLocalDate() {
        String localDate = null;
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            localDate = format.format(period);
        } catch (Exception ignored) {
        }
        return localDate;
    }
}
