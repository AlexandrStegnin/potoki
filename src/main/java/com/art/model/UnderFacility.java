package com.art.model;

import com.art.model.supporting.dto.UnderFacilityDTO;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@NoArgsConstructor
@Table(name = "under_facility")
@ToString(exclude = {"facility", "rooms"})
@EqualsAndHashCode(exclude = {"facility", "rooms"})
public class UnderFacility implements Serializable {

    private Long id;

    private String name;

    private Facility facility;

    private Set<Room> rooms;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "facility_id", referencedColumnName = "id")
    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "underFacility")
    public Set<Room> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }

    public UnderFacility(UnderFacilityDTO dto) {
        this.name = dto.getName();
        this.facility = new Facility(dto.getFacility());
    }
}
