package com.art.model;

import com.art.model.supporting.DebetCreditEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@ToString
@Entity
@Table(name = "AlphaCorrectTags")
public class AlphaCorrectTags implements Serializable{
    private BigInteger id;
    private String description;
    private String correctTag;
    private String inn;
    private String account;
    private DebetCreditEnum debetOrCredit;
    private Facilities facility;
    private String docNumber;
    private Date dateOper;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    public BigInteger getId(){
        return id;
    }
    public void setId(BigInteger id){
        this.id = id;
    }

    @Column(name = "CorrectTag")
    public String getCorrectTag(){
        return correctTag;
    }
    public void setCorrectTag(String correctTag){
        this.correctTag = correctTag;
    }

    @Column(name = "Description")
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }

    @Column(name = "Inn")
    public String getInn(){
        return inn;
    }
    public void setInn(String inn){
        this.inn = inn;
    }

    @Column(name = "DocNumber")
    public String getDocNumber(){
        return docNumber;
    }
    public void setDocNumber(String docNumber){
        this.docNumber = docNumber;
    }

    @Column(name = "Account")
    public String getAccount(){
        return account;
    }
    public void setAccount(String account){
        this.account = account;
    }


    @Column(name = "DateOper")
    public Date getDateOper(){
        return dateOper;
    }
    public void setDateOper(Date dateOper){
        this.dateOper = dateOper;
    }


    @Enumerated(EnumType.STRING)
    @Column(name = "DebetOrCredit")
    public DebetCreditEnum getDebetOrCredit(){
        return debetOrCredit;
    }
    public void setDebetOrCredit(DebetCreditEnum debetOrCredit){
        this.debetOrCredit = debetOrCredit;
    }

    @OneToOne
    @JoinColumn(name = "FacilityId", referencedColumnName = "ID")
    public Facilities getFacility(){
        return facility;
    }
    public void setFacility(Facilities facility){
        this.facility = facility;
    }

    @Transient
    public String getDateOperToLocalDate(){
        String localDate = null;
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try{
            localDate = format.format(dateOper);
        }catch(Exception ignored){}
        return localDate;
    }

}
