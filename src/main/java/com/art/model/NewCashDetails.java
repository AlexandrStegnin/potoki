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
@Table(name = "NewCashDetails")
public class NewCashDetails {
    private BigInteger id;
    private String newCashDetail;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    public BigInteger getId(){
        return id;
    }
    public void setId(BigInteger id){
        this.id = id;
    }

    @Column(name = "newCashDetail")
    public String getNewCashDetail(){
        return newCashDetail;
    }
    public void setNewCashDetail(String newCashDetail){
        this.newCashDetail = newCashDetail;
    }
}
