package com.art.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
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

    public PasswordResetToken(String token, Users users) {
        this.token = token;
        this.users = users;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @OneToOne(targetEntity = Users.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "UserId")
    private Users users;

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
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
