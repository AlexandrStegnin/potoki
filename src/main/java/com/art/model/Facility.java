package com.art.model;

import com.art.model.supporting.UserFacilities;
import com.art.model.supporting.dto.FacilityDTO;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

@SqlResultSetMapping(
        name = "InvestorsFacilitiesMapping",
        classes = @ConstructorResult(
                targetClass = UserFacilities.class,
                columns = {
                        @ColumnResult(name = "id", type = BigInteger.class),
                        @ColumnResult(name = "facility", type = String.class)
                }))
@NamedNativeQuery(
        name = "InvestorsFacilities",
        query = "SELECT f.ID, f.FACILITY " +
                "FROM FACILITYES f " +
                "LEFT JOIN USERS_FACILITYES uf on f.ID = uf.FACILITY_ID " +
                "LEFT JOIN USERS u on uf.RENTOR_INVESTORS_ID = u.ID " +
                "WHERE uf.RENTOR_INVESTORS_ID = ?",
        resultClass = UserFacilities.class,
        resultSetMapping = "InvestorsFacilitiesMapping")
@Data
@Entity
@Table(name = "facility", schema = "pss_projects")
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
