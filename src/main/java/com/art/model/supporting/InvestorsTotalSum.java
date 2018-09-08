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
public class InvestorsTotalSum implements Serializable {
    private Date dateGivedCash;
    private String facility;
    private float givedCash;

    private float dolya;
    private float cleanDohod;
    private float fullDohod;

    public InvestorsTotalSum(String facility, float givedCash, float dolya,
                             float cleanDohod, float fullDohod) {
        this.facility = facility;
        this.givedCash = givedCash;
        this.dolya = dolya;
        this.cleanDohod = cleanDohod;
        this.fullDohod = fullDohod;

    }

    public InvestorsTotalSum(Date dateGivedCash, String facility, float givedCash, float dolya,
                             float cleanDohod, float fullDohod) {
        this.dateGivedCash = dateGivedCash;
        this.facility = facility;
        this.givedCash = givedCash;
        this.dolya = dolya;
        this.cleanDohod = cleanDohod;
        this.fullDohod = fullDohod;

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

    public float getCleanDohod() {
        return cleanDohod;
    }

    public void setCleanDohod(float cleanDohod) {
        this.cleanDohod = cleanDohod;
    }

    public float getFullDohod() {
        return fullDohod;
    }

    public void setFullDohod(float fullDohod) {
        this.fullDohod = fullDohod;
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
