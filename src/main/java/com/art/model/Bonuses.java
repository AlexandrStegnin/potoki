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
@ToString(exclude = {"facility", "bonusTypes", "manager", "rentor"})
@EqualsAndHashCode(exclude = {"facility", "bonusTypes", "manager", "rentor"})
@Entity
@Table(name = "Bonuses")
public class Bonuses implements Serializable {

    private BigInteger id;
    private float summa;
    private Date dateStBonus;
    private int countMonths;

    private Facilities facility;
    private BonusTypes bonusTypes;
    private Users manager;
    private Users rentor;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    @OneToOne
    @JoinColumn(name = "BonusTypeId", referencedColumnName = "ID")
    public BonusTypes getBonusTypes() {
        return bonusTypes;
    }

    public void setBonusTypes(BonusTypes bonusTypes) {
        this.bonusTypes = bonusTypes;
    }

    @OneToOne
    @JoinColumn(name = "FacilityId", referencedColumnName = "ID")
    public Facilities getFacility() {
        return facility;
    }

    public void setFacility(Facilities facility) {
        this.facility = facility;
    }

    @OneToOne
    @JoinColumn(name = "ManagerId", referencedColumnName = "ID")
    public Users getManager() {
        return manager;
    }

    public void setManager(Users manager) {
        this.manager = manager;
    }

    @OneToOne
    @JoinColumn(name = "RentorId", referencedColumnName = "ID")
    public Users getRentor() {
        return rentor;
    }

    public void setRentor(Users rentor) {
        this.rentor = rentor;
    }

    @Transient
    public String getDateStBonusToLocalDate() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            localDate = format.format(dateStBonus);
        } catch (Exception ignored) {
        }

        return localDate;
    }
}
