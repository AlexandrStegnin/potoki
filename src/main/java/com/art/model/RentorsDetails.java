package com.art.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"moreInfo", "inn", "organization", "account", "rentor", "facility"})
@ToString(exclude = {"rentor", "facility"})
@Entity
@Table(name = "RentorsDetails")
public class RentorsDetails implements Serializable {

    private BigInteger id;
    private String moreInfo;
    private String inn;
    private String organization;
    private String account;

    private Users rentor;
    private Facilities facility;

    @Id
    @GeneratedValue
    @Column(name = "Id")
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
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

    @Column(name = "Organization")
    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    @OneToOne
    @JoinColumn(name = "RentorId", referencedColumnName = "id")
    public Users getRentor() {
        return rentor;
    }

    public void setRentor(Users rentor) {
        this.rentor = rentor;
    }

    @OneToOne
    @JoinColumn(name = "FacilityId", referencedColumnName = "ID")
    public Facilities getFacility() {
        return facility;
    }

    public void setFacility(Facilities facility) {
        this.facility = facility;
    }
}
