package com.art.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigInteger;

@ToString
@EqualsAndHashCode
@Entity
@Table(name = "TypeClosingInvest")
public class TypeClosingInvest {
    private BigInteger id;
    private String typeClosingInvest;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    public BigInteger getId() {
        return id;
    }
    public void setId(BigInteger id) {
        this.id = id;
    }

    @Column(name = "TypeClosingInvest")
    public String getTypeClosingInvest() {
        return typeClosingInvest;
    }
    public void setTypeClosingInvest(String typeClosingInvest) {
        this.typeClosingInvest = typeClosingInvest;
    }

}
