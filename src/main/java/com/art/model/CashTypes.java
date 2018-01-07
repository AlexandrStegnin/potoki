package com.art.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "CashTypes")
public class CashTypes {
    private BigInteger id;
    private String cashType;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    public BigInteger getId(){
        return id;
    }
    public void setId(BigInteger id){
        this.id = id;
    }

    @Column(name = "CashType")
    public String getCashType(){
        return cashType;
    }
    public void setCashType(String cashType){
        this.cashType = cashType;
    }
}
