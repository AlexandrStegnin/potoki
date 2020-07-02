package com.art.model;

import com.art.model.supporting.UserFacilities;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashSet;
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
@EqualsAndHashCode(exclude = {"city", "address", "manager", "investors", "underFacilities"})
@ToString(exclude = {"manager", "investors", "underFacilities"})
@Entity
@Table(name = "FACILITYES", schema = "pss_projects")
public class Facilities implements Serializable {
    private BigInteger id;
    private String facility;
    private String city;
    private String address;
    private Users manager;
    private Set<Users> investors;
    private Set<UnderFacilities> underFacilities;

    public Facilities() {

    }

    public Facilities(String id, String facility) {
        this.id = new BigInteger(id);
        this.facility = facility;
    }

    @ManyToOne(cascade =
            {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            },
            fetch = FetchType.LAZY)
    @JoinColumn(name = "MANAGER_ID", referencedColumnName = "id")
    public Users getManager() {
        return manager;
    }

    public void setManager(Users manager) {
        this.manager = manager;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USERS_FACILITYES",
            joinColumns = {@JoinColumn(name = "FACILITY_ID", referencedColumnName = "id")},
            inverseJoinColumns = @JoinColumn(name = "RENTOR_INVESTORS_ID", referencedColumnName = "id"))
    public Set<Users> getInvestors() {
        return investors;
    }

    public void setInvestors(Set<Users> investors) {
        this.investors = investors;
    }

    @OneToMany(cascade =
            {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            },
            fetch = FetchType.LAZY)
    @JoinColumn(name = "FacilityId", referencedColumnName = "Id")
    public Set<UnderFacilities> getUnderFacilities() {
        return underFacilities;
    }

    public void setUnderFacilities(Set<UnderFacilities> underFacilities) {
        this.underFacilities = underFacilities;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    @Column(name = "FACILITY")
    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    @Column(name = "CITY")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "ADDRESS")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String fullName;

    @Column(name = "full_name")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void addInvestor(Users investor) {
        if (null == this.investors) {
            this.investors = new HashSet<>();
        }
        this.investors.add(investor);
    }
}
