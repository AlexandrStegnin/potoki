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
@ToString(exclude = {"facility", "paymentsMethod", "rentor"})
@EqualsAndHashCode
@Entity
@Table(name = "FacilitiesServiceContracts")
public class FacilitiesServiceContracts implements Serializable {

    private BigInteger id;
    private String contractNumber;
    private Date dateStartContract;
    private int dayToPay;
    private float area;
    private float summPayment;
    private float discount;
    private int timeDiscount;
    private String comments;

    private Facilities facility;
    private PaymentsMethod paymentsMethod;
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
    @JoinColumn(name = "FacilityId", referencedColumnName = "ID")
    public Facilities getFacility() {
        return facility;
    }

    public void setFacility(Facilities facility) {
        this.facility = facility;
    }

    @OneToOne
    @JoinColumn(name = "PaymentsMethodId", referencedColumnName = "ID")
    public PaymentsMethod getPaymentsMethod() {
        return paymentsMethod;
    }

    public void setPaymentsMethod(PaymentsMethod paymentsMethod) {
        this.paymentsMethod = paymentsMethod;
    }

    @OneToOne
    @JoinColumn(name = "RentorId", referencedColumnName = "id")
    public Users getRentor() {
        return rentor;
    }

    public void setRentor(Users rentor) {
        this.rentor = rentor;
    }

    @Transient
    public String getDateStartContractToLocalDate() {
        String localDate = null;
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        localDate = format.format(dateStartContract);
        return localDate;
    }

}
