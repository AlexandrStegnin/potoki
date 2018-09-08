package com.art.model.supporting;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Transient;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class InvestorsPlanSale implements Serializable {

    private Date dateGivedCash;
    private String facility;
    private float givedCash;

    private float dolya;
    private float dohodNaRukiObshii;
    private float dohodnostGodovaya3;

    public InvestorsPlanSale(Date dateGivedCash, String facility, float givedCash, float dolya,
                             float dohodNaRukiObshii, float dohodnostGodovaya3) {
        this.dateGivedCash = dateGivedCash;
        this.facility = facility;
        this.givedCash = givedCash;
        this.dolya = dolya;
        this.dohodNaRukiObshii = dohodNaRukiObshii;
        this.dohodnostGodovaya3 = dohodnostGodovaya3;

    }

    public Date getDateGivedCash() {
        return dateGivedCash;
    }

    public void setDateGivedCash(Date dateGivedCash) {
        this.dateGivedCash = dateGivedCash;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public float getGivedCash() {
        return givedCash;
    }

    public void setGivedCash(float givedCash) {
        this.givedCash = givedCash;
    }

    public float getDolya() {
        return dolya;
    }

    public void setDolya(float dolya) {
        this.dolya = dolya;
    }

    public float getDohodNaRukiObshii() {
        return dohodNaRukiObshii;
    }

    public void setDohodNaRukiObshii(float dohodNaRukiObshii) {
        this.dohodNaRukiObshii = dohodNaRukiObshii;
    }

    public float getDohodnostGodovaya3() {
        return dohodnostGodovaya3;
    }

    public void setDohodnostGodovaya3(float dohodnostGodovaya3) {
        this.dohodnostGodovaya3 = dohodnostGodovaya3;
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

}
