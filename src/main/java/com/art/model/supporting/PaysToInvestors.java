package com.art.model.supporting;

import com.art.model.Users;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "InvestorsSummaryForIDEA")
public class PaysToInvestors implements Serializable {
    private BigInteger id;
    private BigInteger investorId;
    private String facility;
    private Date endDate;
    private String aCorTag;
    private float factPay;
    private float ostatokPosleRashodov;
    private float ostatokPosleNalogov;
    private float ostatokPosleVivoda;
    private float ostatokPoDole;
    private float summ;
    private float pribilSprodazhi;
    private Users investor;


    public  PaysToInvestors(){

    }

    public PaysToInvestors(String facility, BigInteger investorId){
        this.facility = facility;
        this.investorId = investorId;
    }

    public PaysToInvestors(String facility, Users investor){
        this.facility = facility;
        this.investor = investor;
    }

    public PaysToInvestors(String facility, BigInteger investorId, float ostatokPoDole){
        this.facility = facility;
        this.investorId = investorId;
        this.ostatokPoDole = ostatokPoDole;
    }

    public PaysToInvestors(String facility, Users investor, float ostatokPoDole){
        this.facility = facility;
        this.investor = investor;
        this.ostatokPoDole = ostatokPoDole;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    public BigInteger getId(){
        return id;
    }
    public void setId(BigInteger id){
        this.id = id;
    }

    @Column(name = "InvestorId")
    public BigInteger getInvestorId(){
        return investorId;
    }
    public void setInvestorId(BigInteger investorId){
        this.investorId = investorId;
    }

    @Column(name = "Facility")
    public String getFacility(){
        return facility;
    }
    public void setFacility(String facility){
        this.facility = facility;
    }

    @Column(name = "END_DATE")
    public Date getEndDate(){
        return endDate;
    }
    public void setEndDate(Date endDate){
        this.endDate = endDate;
    }

    @Column(name = "aCorTag")
    public String getaCorTag(){
        return aCorTag;
    }
    public void setaCorTag(String aCorTag){
        this.aCorTag = aCorTag;
    }

    @Column(name = "fact_pay")
    public float getFactPay(){
        return factPay;
    }
    public void setFactPay(float factPay){
        this.factPay = factPay;
    }

    @Column(name = "ostatok_posle_rashodov")
    public float getOstatokPosleRashodov(){
        return ostatokPosleRashodov;
    }
    public void setOstatokPosleRashodov(float ostatokPosleRashodov){
        this.ostatokPosleRashodov = ostatokPosleRashodov;
    }

    @Column(name = "ostatok_posle_nalogov")
    public float getOstatokPosleNalogov(){
        return ostatokPosleNalogov;
    }
    public void setOstatokPosleNalogov(float ostatokPosleNalogov){
        this.ostatokPosleNalogov = ostatokPosleNalogov;
    }

    @Column(name = "ostatok_posle_vivoda")
    public float getOstatokPosleVivoda(){
        return ostatokPosleVivoda;
    }
    public void setOstatokPosleVivoda(float ostatokPosleVivoda){
        this.ostatokPosleVivoda = ostatokPosleVivoda;
    }

    @Column(name = "ostatok_po_dole")
    public float getOstatokPoDole(){
        return ostatokPoDole;
    }
    public void setOstatokPoDole(float ostatokPoDole){
        this.ostatokPoDole = ostatokPoDole;
    }

    @Column(name = "summ")
    public float getSumm(){
        return summ;
    }
    public void setSumm(float summ){
        this.summ = summ;
    }

    @Column(name = "pribil_s_prodazhi")
    public float getPribilSprodazhi(){
        return pribilSprodazhi;
    }
    public void setPribilSprodazhi(float pribilSprodazhi){
        this.pribilSprodazhi = pribilSprodazhi;
    }

    @OneToOne
    @JoinColumn(name = "InvestorId", referencedColumnName = "ID", insertable = false, updatable = false)
    public Users getInvestor(){
        return investor;
    }
    public void setInvestor(Users investor){
        this.investor = investor;
    }


    @Transient
    public String getEndDateToLocalDate(){
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try{
            localDate = format.format(endDate);
        }catch(Exception ignored){}

        return localDate;
    }
}
