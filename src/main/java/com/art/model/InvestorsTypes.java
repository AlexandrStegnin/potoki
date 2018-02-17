package com.art.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "InvestorsTypes")
public class InvestorsTypes implements Serializable {
    private BigInteger id;
    private String investorsType;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    @Column(name = "InvestorsType")
    public String getInvestorsType() {
        return investorsType;
    }

    public void setInvestorsType(String investorsType) {
        this.investorsType = investorsType;
    }
}
