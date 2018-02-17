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
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "ToshlExtract")
public class ToshlExtract implements Serializable {
    private BigInteger id;
    private Date date;
    private String account;
    private String category;
    private String tags;
    private float amount;
    private String currency;
    private float inMainCurrency;
    private String mainCurrency;
    private String description;
    private ToshlCorrectTags correctTag;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    @Column(name = "Date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "Account")
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Column(name = "Category")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Column(name = "Tags")
    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Column(name = "Amount")
    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Column(name = "Currency")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Column(name = "InMainCurrency")
    public float getInMainCurrency() {
        return inMainCurrency;
    }

    public void setInMainCurrency(float inMainCurrency) {
        this.inMainCurrency = inMainCurrency;
    }

    @Column(name = "MainCurrency")
    public String getMainCurrency() {
        return mainCurrency;
    }

    public void setMainCurrency(String mainCurrency) {
        this.mainCurrency = mainCurrency;
    }

    @Column(name = "Description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToOne
    @JoinColumn(name = "CorrectTagId", referencedColumnName = "ID")
    public ToshlCorrectTags getCorrectTag() {
        return correctTag;
    }

    public void setCorrectTag(ToshlCorrectTags correctTag) {
        this.correctTag = correctTag;
    }

    @Transient
    public String getDateToLocalDate() {
        String localDate = null;
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        localDate = format.format(date);
        return localDate;
    }
}
