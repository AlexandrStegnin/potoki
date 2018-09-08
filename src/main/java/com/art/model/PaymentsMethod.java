package com.art.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "PAYMENTS_METHOD")
public class PaymentsMethod implements Serializable {
    private BigInteger id;
    private String payment;
    private int manager;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    @Column(name = "PAYMENT")
    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    @Column(name = "MANAGER")
    public int getManager() {
        return manager;
    }

    public void setManager(int manager) {
        this.manager = manager;
    }
}
