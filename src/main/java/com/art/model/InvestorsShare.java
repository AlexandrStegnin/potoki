package com.art.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Date;
import java.text.SimpleDateFormat;

@Getter
@Setter
@ToString(exclude = {"investor", "facility"})
@EqualsAndHashCode(exclude = {"investor", "facility"})
@Entity
@Table(name = "INVESTORS_SHARE")
public class InvestorsShare implements Serializable {
    private BigInteger id;
    private BigInteger investorId;
    private BigInteger facilityId;
    private float share;
    private float managerExpenses;
    private float taxationExpenses;
    private float cashingExpenses;
    private float ipManagerExpenses;
    private float tempExpenses;
    private Date dateStTempExp;
    private Date dateEndTempExp;
    private Date dateStManExp;
    private Date dateEndManExp;
    private Users investor;
    private Facilities facility;
    //private List<HistoryRelationships> historyRelationships;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    @Column(name = "INVESTOR_ID", insertable = false, updatable = false)
    public BigInteger getInvestorId() {
        return investorId;
    }

    public void setInvestorId(BigInteger investorId) {
        this.investorId = investorId;
    }

    @Column(name = "FACILITY_ID", insertable = false, updatable = false)
    public BigInteger getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(BigInteger facilityId) {
        this.facilityId = facilityId;
    }

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "INVESTOR_ID", referencedColumnName = "id")
    public Users getInvestor() {
        return investor;
    }

    public void setInvestor(Users investor) {
        this.investor = investor;
    }

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "FACILITY_ID", referencedColumnName = "id")
    public Facilities getFacility() {
        return facility;
    }

    public void setFacility(Facilities facility) {
        this.facility = facility;
    }

    /*
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "FACILITY_ID", referencedColumnName = "FACILITY_ID")
    public List<HistoryRelationships> getHistoryRelationships(){
        return historyRelationships;
    }
    public void setFacility(List<HistoryRelationships> historyRelationships){
        this.historyRelationships = historyRelationships;
    }
    */

    @Transient
    public String getDateStTempExpToLocalDate() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            localDate = format.format(dateStTempExp);
        } catch (Exception ignored) {
        }

        return localDate;
    }

    @Transient
    public String getDateEndTempExpToLocalDate() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            localDate = format.format(dateEndTempExp);
        } catch (Exception ignored) {
        }

        return localDate;
    }

    @Transient
    public String getDateStManExpToLocalDate() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            localDate = format.format(dateStManExp);
        } catch (Exception ignored) {
        }

        return localDate;
    }

    @Transient
    public String getDateEndManExpToLocalDate() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            localDate = format.format(dateEndManExp);
        } catch (Exception ignored) {
        }

        return localDate;
    }
}
