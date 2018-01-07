package com.art.model;

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
@Table(name = "ToshlCorrectTags")
public class ToshlCorrectTags implements Serializable {

    private BigInteger id;
    private String tags;
    private String correctTag;
    private String category;
    private Date dateStTag;
    private Date dateEndTag;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    public BigInteger getId(){
        return id;
    }
    public void setId(BigInteger id){
        this.id = id;
    }

    @Column(name = "Tags")
    public String getTags(){
        return tags;
    }
    public void setTags(String tags){
        this.tags = tags;
    }

    @Column(name = "CorrectTag")
    public String getCorrectTag(){
        return correctTag;
    }
    public void setCorrectTag(String correctTag){
        this.correctTag = correctTag;
    }

    @Column(name = "Category")
    public String getCategory(){
        return category;
    }
    public void setCategory(String category){
        this.category = category;
    }

    @Column(name = "DateStTag")
    public Date getDateStTag(){
        return dateStTag;
    }
    public void setDateStTag(Date dateStTag){
        this.dateStTag = dateStTag;
    }

    @Column(name = "DateEndTag")
    public Date getDateEndTag(){
        return dateEndTag;
    }
    public void setDateEndTag(Date dateEndTag){
        this.dateEndTag = dateEndTag;
    }

    @Transient
    public String getDateStTagToLocalDate(){
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try{
            localDate = format.format(dateStTag);
        }catch(Exception ignored){}

        return localDate;
    }

    @Transient
    public String getDateEndTagToLocalDate(){
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try{
            localDate = format.format(dateEndTag);
        }catch(Exception ignored){}

        return localDate;
    }
}
