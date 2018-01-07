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
@ToString(exclude = {"facility", "underFacility"})
@EqualsAndHashCode(exclude = {"facility", "underFacility"})
@Entity
@Table(name = "FacilitiesReserves")
public class FacilitiesReserves implements Serializable{

    private BigInteger id;
    private float summReserv;
    private Date dateCreateReserv;
    private float balanceReserv;
    private Date dateEndReserv;
    private UnderFacilities underFacility;
    private Facilities facility;


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
    public void setFacility(UnderFacilities underFacility){
        this.underFacility = underFacility;
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
    public String getDateCreateReservToLocalDate(){
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try{
            localDate = format.format(dateCreateReserv);
        }catch(Exception ignored){}

        return localDate;
    }

    @Transient
    public String getDateEndReservToLocalDate(){
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try{
            localDate = format.format(dateEndReserv);
        }catch(Exception ignored){}

        return localDate;
    }

}
