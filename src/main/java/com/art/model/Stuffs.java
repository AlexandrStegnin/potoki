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
@Table(name = "STUFFS")
public class Stuffs implements Serializable {

    private BigInteger id;
    private String stuff;

    public Stuffs(){

    }

    public Stuffs(String id, String stuff){
        this.id = new BigInteger(id);
        this.stuff = stuff;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    public BigInteger getId(){
        return id;
    }
    public void setId(BigInteger id){
        this.id = id;
    }

    @Column(name = "STUFF")
    public String getStuff(){
        return stuff;
    }
    public void setStuff(String stuff){
        this.stuff = stuff;
    }

}
