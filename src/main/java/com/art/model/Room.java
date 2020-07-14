package com.art.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "room")
@ToString(exclude = "underFacility")
@EqualsAndHashCode(exclude = "underFacility")
public class Room implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "coast")
    private BigDecimal coast;

    @Column(name = "room_size")
    private BigDecimal roomSize;

    @Column(name = "sold")
    private boolean sold;

    @Column(name = "date_sale")
    private Date dateSale;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "under_facility_id", referencedColumnName = "id")
    private UnderFacilities underFacility;

    @Column(name = "date_buy")
    private Date dateBuy;

    @Column(name = "sale_price")
    private BigDecimal salePrice;

    @Column(name = "total_year_profit")
    private BigDecimal totalYearProfit;

}
