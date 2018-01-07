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
@Table(name = "AnnexToContracts")
public class AnnexToContracts implements Serializable{

    private BigInteger id;
    private String annexName;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    public BigInteger getId(){
        return id;
    }
    public void setId(BigInteger id){
        this.id = id;
    }

    @Column(name = "AnnexName")
    public String getAnnexName() {
        return annexName;
    }
    public void setAnnexName(String annexName) {
        this.annexName = annexName;
    }
}
