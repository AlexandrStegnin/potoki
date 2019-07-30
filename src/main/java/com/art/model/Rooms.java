package com.art.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "Rooms")
@ToString(exclude = "underFacility")
@EqualsAndHashCode(exclude = "underFacility")
public class Rooms implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;

    @Column(name = "Room")
    private String room;

    @Column(name = "Coast")
    private BigDecimal coast;

    @Column(name = "RoomSize")
    private BigDecimal roomSize;

    @Column(name = "Sold")
    private boolean sold;

    @Column(name = "DateOfSale")
    private Date dateOfSale;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "UnderFacilityId", referencedColumnName = "Id")
    private UnderFacilities underFacility;

    @Column(name = "BuyDate")
    private Date buyDate;

    @Column(name = "SalePrice")
    private BigDecimal salePrice;

}
