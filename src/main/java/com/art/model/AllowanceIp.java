package com.art.model;

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
@ToString(exclude = {"facility", "underFacility", "investor"})
@EqualsAndHashCode(exclude = {"facility", "underFacility", "investor"})
@Entity
@Table(name = "AllowanceIp")
public class AllowanceIp implements Serializable {

    private BigInteger id;
    private Date dateStart;
    private float allowance;
    private Facilities facility;
    private UnderFacilities underFacility;
    private Users investor;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    public BigInteger getId(){
        return id;
    }
    public void setId(BigInteger id){
        this.id = id;
    }

    @OneToOne
    @JoinColumn(name = "UnderFacilityId", referencedColumnName = "ID")
    public UnderFacilities getUnderFacility(){
        return underFacility;
    }
    public void setUnderFacility(UnderFacilities underFacility){
        this.underFacility = underFacility;
    }

    @OneToOne
    @JoinColumn(name = "InvestorId", referencedColumnName = "ID")
    public Users getInvestor(){
        return investor;
    }
    public void setInvestor(Users investor){
        this.investor = investor;
    }

    @OneToOne
    @JoinColumn(name = "FacilityId", referencedColumnName = "ID")
    public Facilities getFacility(){
        return facility;
    }
    public void setFacility(Facilities facility){
        this.facility = facility;
    }

    @Transient
    public String getDateStartToLocalDate(){
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try{
            localDate = format.format(dateStart);
        }catch(Exception ignored){}

        return localDate;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public float getAllowance() {
        return allowance;
    }

    public void setAllowance(float allowance) {
        this.allowance = allowance;
    }
}
