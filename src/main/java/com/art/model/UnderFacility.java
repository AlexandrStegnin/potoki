package com.art.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = {"facility", "rooms"})
@EqualsAndHashCode(exclude = {"facility", "rooms"})
@Entity
@Table(name = "under_facility")
public class UnderFacility implements Serializable {

    private Long id;
    private String name;
    private Facility facility;
    private Set<Room> rooms;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "facility_id", referencedColumnName = "id")
    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "underFacility")
    public Set<Room> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }

}
