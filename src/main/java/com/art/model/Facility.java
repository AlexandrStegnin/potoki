package com.art.model;

import com.art.model.supporting.UserFacilities;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Set;

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
@EqualsAndHashCode(exclude = {"city", "underFacilities"})
@ToString(exclude = {"underFacilities"})
@Entity
@Table(name = "facility", schema = "pss_projects")
public class Facility implements Serializable {

    private Long id;

    private String name;

    private String city;

    private Set<UnderFacility> underFacilities;

    public Facility() {
    }

    public Facility(String id, String name) {
        this.id = Long.valueOf(id);
        this.name = name;
    }

    @OneToMany(cascade =
            {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            },
            fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", referencedColumnName = "id")
    public Set<UnderFacility> getUnderFacilities() {
        return underFacilities;
    }

    public void setUnderFacilities(Set<UnderFacility> underFacilities) {
        this.underFacilities = underFacilities;
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

}
