package com.art.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@ToString(exclude = "underFacility")
@EqualsAndHashCode(exclude = "underFacility")
@Entity
@Table(name = "Rooms")
public class Rooms implements Serializable {
    private BigInteger id;
    private UnderFacilities underFacility;
    private String room;
    private BigDecimal coast;
    private BigDecimal roomSize;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    @Column(name = "Room")
    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @Column(name = "Coast")
    public BigDecimal getCoast() {
        return coast;
    }

    public void setCoast(BigDecimal coast) {
        this.coast = coast;
    }

    @Column(name = "RoomSize")
    public BigDecimal getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(BigDecimal roomSize) {
        this.roomSize = roomSize;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UnderFacilityId", referencedColumnName = "Id")
    public UnderFacilities getUnderFacility() {
        return underFacility;
    }

    public void setUnderFacility(UnderFacilities underFacility) {
        this.underFacility = underFacility;
    }

}
