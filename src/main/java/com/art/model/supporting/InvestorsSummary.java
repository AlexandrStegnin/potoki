package com.art.model.supporting;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Transient;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class InvestorsSummary implements Serializable {

    private String facility;
    private Date end_date;
    private String aCorTag;
    private float fact_pay;

    private float ostatok_posle_rashodov;
    private float ostatok_posle_nalogov;
    private float ostatok_posle_vivoda;
    private float ostatok_po_dole;
    private float summ;
    private float pribil_s_prodazhi;

    public InvestorsSummary(String facility, Date end_date, String aCorTag,
                            float fact_pay, float ostatok_posle_rashodov, float ostatok_posle_nalogov,
                            float ostatok_posle_vivoda, float ostatok_po_dole, float summ,
                            float pribil_s_prodazhi){

        this.facility = facility;
        this.end_date = end_date;
        this.aCorTag = aCorTag;
        this.fact_pay = fact_pay;
        this.ostatok_posle_rashodov = ostatok_posle_rashodov;
        this.ostatok_posle_nalogov = ostatok_posle_nalogov;
        this.ostatok_posle_vivoda = ostatok_posle_vivoda;
        this.ostatok_po_dole = ostatok_po_dole;
        this.summ = summ;
        this.pribil_s_prodazhi = pribil_s_prodazhi;

    }


    public String getFacility(){
        return facility;
    }
    public void setFacility(String facility){
        this.facility = facility;
    }

    public Date getEnd_date(){
        return end_date;
    }
    public void setEnd_date(Date end_date){
        this.end_date = end_date;
    }

    public String getaCorTag(){
        return aCorTag;
    }
    public void setaCorTag(String aCorTag){
        this.aCorTag = aCorTag;
    }

    public float getFact_pay(){
        return fact_pay;
    }
    public void setFact_pay(float fact_pay){
        this.fact_pay = fact_pay;
    }

    public float getOstatok_posle_rashodov(){
        return ostatok_posle_rashodov;
    }
    public void setOstatok_posle_rashodov(float ostatok_posle_rashodov){
        this.ostatok_posle_rashodov = ostatok_posle_rashodov;
    }

    public float getOstatok_posle_nalogov(){
        return ostatok_posle_nalogov;
    }
    public void setOstatok_posle_nalogov(float ostatok_posle_nalogov){
        this.ostatok_posle_nalogov = ostatok_posle_nalogov;
    }

    public float getOstatok_posle_vivoda(){
        return ostatok_posle_vivoda;
    }
    public void setOstatok_posle_vivoda(float ostatok_posle_vivoda){
        this.ostatok_posle_vivoda = ostatok_posle_vivoda;
    }

    public float getOstatok_po_dole(){
        return ostatok_po_dole;
    }
    public void setOstatok_po_dole(float ostatok_po_dole){
        this.ostatok_po_dole = ostatok_po_dole;
    }

    public float getSumm(){
        return summ;
    }
    public void setSumm(float summ){
        this.summ = summ;
    }

    public float getPribil_s_prodazhi(){
        return pribil_s_prodazhi;
    }
    public void setPribil_s_prodazhi(float pribil_s_prodazhi){
        this.pribil_s_prodazhi = pribil_s_prodazhi;
    }

    @Transient
    public String getEndDateToLocalDate(){
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy", myDateFormatSymbols);
        try{
            localDate = format.format(end_date);
        }catch(Exception ignored){}

        return localDate;
    }

    private static DateFormatSymbols myDateFormatSymbols = new DateFormatSymbols(){

        @Override
        public String[] getMonths() {
            return new String[]{"январь", "февраль", "март", "апрель", "май", "июнь",
                    "июль", "август", "сентябрь", "октябрь", "ноябрь", "декабрь"};
        }

    };

}
