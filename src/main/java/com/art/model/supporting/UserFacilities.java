package com.art.model.supporting;

import lombok.*;

import java.io.Serializable;
import java.math.BigInteger;

@Getter
@Setter
@ToString
@EqualsAndHashCode

public class UserFacilities implements Serializable {

    private BigInteger id;
    private String facility;

    public UserFacilities() {

    }

    public UserFacilities(BigInteger id, String facility) {
        this.id = id;
        this.facility = facility;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }
}
