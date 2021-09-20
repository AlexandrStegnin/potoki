package com.art.model;

import com.art.model.supporting.dto.FacilityDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "facility")
@EqualsAndHashCode(of = {"id", "name", "fullName"})
public class Facility implements Serializable {

    private Long id;

    private String name;

    private String city;

    public Facility() {
    }

    public Facility(String id, String name) {
        this.id = Long.valueOf(id);
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    private String fullName;

    @Column(name = "full_name")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Facility(FacilityDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.fullName = dto.getFullName();
        this.city = dto.getCity();
    }

}
