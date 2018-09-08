package com.art.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

@Entity
@Table(name = "INVESTORS_LOAD")
public class InvestorsLoad implements Serializable {
    private BigInteger id;
    private String load;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    @Column(name = "LOAD")
    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;
    }
}
