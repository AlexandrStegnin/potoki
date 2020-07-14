package com.art.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = {"facility", "rooms"})
@EqualsAndHashCode(exclude = {"facility", "rooms"})
@Entity
@Table(name = "UnderFacilities")
public class UnderFacilities implements Serializable {

    private BigInteger id;
    private BigInteger facilityId;
    private String underFacility;
    private Facility facility;
    private Set<Rooms> rooms;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FacilityId", referencedColumnName = "Id")
    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }


    @Column(name = "FacilityId", insertable = false, updatable = false)
    public BigInteger getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(BigInteger facilityId) {
        this.facilityId = facilityId;
    }


    @Column(name = "UnderFacility")
    public String getUnderFacility() {
        return underFacility;
    }

    public void setUnderFacility(String underFacility) {
        this.underFacility = underFacility;
    }

    @OneToMany(cascade =
            {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST,
                    CascadeType.REMOVE
            },
            fetch = FetchType.LAZY, mappedBy = "underFacility")
//    @JoinColumn(name = "UnderFacilityId", referencedColumnName = "Id")
    public Set<Rooms> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Rooms> rooms) {
        this.rooms = rooms;
    }

}
