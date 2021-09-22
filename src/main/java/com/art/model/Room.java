package com.art.model;

import com.art.model.supporting.dto.RoomDTO;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "room")
@ToString(exclude = "underFacility")
@EqualsAndHashCode(exclude = "underFacility")
public class Room implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "room_size")
    private BigDecimal roomSize;

    @Column(name = "sold")
    private boolean sold;

    @Column(name = "date_sale")
    private Date dateSale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "under_facility_id", referencedColumnName = "id")
    private UnderFacility underFacility;

    @Column(name = "date_buy")
    private Date dateBuy;

    @Column(name = "sale_price")
    private BigDecimal salePrice;

    @Column(name = "total_year_profit")
    private BigDecimal totalYearProfit;

    public Room(RoomDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.cost = dto.getCost();
        this.roomSize = dto.getRoomSize();
        this.sold = dto.isSold();
        this.dateSale = dto.getDateSale();
        this.dateBuy = dto.getDateBuy();
        this.salePrice = dto.getSalePrice();
        this.totalYearProfit = dto.getTotalYearProfit();
        this.underFacility = new UnderFacility(dto.getUnderFacility());
    }

}
