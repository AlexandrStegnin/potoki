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
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "CASH_PAYMENTS")
public class CashPayments implements Serializable {

    private BigInteger id;
    private float summTransferCash;
    private Date dateTransferCash;
    private Facilities facility;
    private Users manager;
    private PaymentsMethod paymentsMethod;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    @Column(name = "DATE_TRANSFER_CASH")
    public Date getDateTransferCash() {
        return dateTransferCash;
    }

    public void setDateTransferCash(Date dateTransferCash) {
        this.dateTransferCash = dateTransferCash;
    }

    @Column(name = "SUMM_TRANSFER_CASH")
    public float getSummTransferCash() {
        return summTransferCash;
    }

    public void setSummTransferCash(float summTransferCash) {
        this.summTransferCash = summTransferCash;
    }

    @OneToOne
    @JoinColumn(name = "FACILITY_ID", referencedColumnName = "ID")
    public Facilities getFacility() {
        return facility;
    }

    public void setFacility(Facilities facility) {
        this.facility = facility;
    }

    @OneToOne
    @JoinColumn(name = "MANAGER_ID", referencedColumnName = "id")
    public Users getManager() {
        return manager;
    }

    public void setManager(Users manager) {
        this.manager = manager;
    }

    @OneToOne
    @JoinColumn(name = "PAYMENTS_METHOD_ID", referencedColumnName = "ID")
    public PaymentsMethod getPaymentsMethod() {
        return paymentsMethod;
    }

    public void setPaymentsMethod(PaymentsMethod paymentsMethod) {
        this.paymentsMethod = paymentsMethod;
    }

    @Transient
    public String getDateTransferCashToLocalDate() {
        String localDate = null;
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        localDate = format.format(dateTransferCash);
        return localDate;
    }

}
