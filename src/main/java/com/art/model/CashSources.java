package com.art.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "CashSources")
public class CashSources {
    private BigInteger id;
    private String cashSource;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    public BigInteger getId(){
        return id;
    }
    public void setId(BigInteger id){
        this.id = id;
    }

    @Column(name = "CashSource")
    public String getCashSource(){
        return cashSource;
    }
    public void setCashSource(String cashSource){
        this.cashSource = cashSource;
    }
}
