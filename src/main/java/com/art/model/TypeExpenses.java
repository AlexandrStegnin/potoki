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
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "TypeExpenses")
public class TypeExpenses implements Serializable{

    private BigInteger id;
    private String typeExp;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    public BigInteger getId(){
        return id;
    }
    public void setId(BigInteger id){
        this.id = id;
    }

    @Column(name = "TypeExp")
    public String getTypeExp(){
        return typeExp;
    }
    public void setTypeExp(String typeExp){
        this.typeExp = typeExp;
    }

}
