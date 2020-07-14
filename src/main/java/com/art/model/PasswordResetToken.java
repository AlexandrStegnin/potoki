package com.art.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

@ToString
@EqualsAndHashCode
@Entity
@Table(name = "PasswordResetToken")
public class PasswordResetToken implements Serializable {
    private static final int EXPIRATION = 24;

    public PasswordResetToken() {
    }

    public PasswordResetToken(String token, AppUser appUser) {
        this.token = token;
        this.appUser = appUser;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @OneToOne(targetEntity = AppUser.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "UserId")
    private AppUser appUser;

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    private Date expiryDate;

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {

        Calendar cal = new GregorianCalendar();
        cal.setTime(expiryDate);
        cal.add(Calendar.HOUR, EXPIRATION);
        this.expiryDate = new java.sql.Date(cal.getTime().getTime());
    }

}
