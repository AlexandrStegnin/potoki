package com.art.model.supporting.dto;

import com.art.model.Room;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Alexandr Stegnin
 */

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomDTO {

    Long id;

    String name;

    BigDecimal cost;

    BigDecimal roomSize;

    boolean sold;

    Date dateSale;

    Long underFacilityId;

    Date dateBuy;

    BigDecimal salePrice;

    BigDecimal totalYearProfit;

    public RoomDTO(Room room) {
        this.id = room.getId();
        this.name = room.getName();
        this.cost = room.getCost();
        this.roomSize = room.getRoomSize();
        this.sold = room.isSold();
        this.dateSale = room.getDateSale();
        this.underFacilityId = room.getUnderFacility().getId();
        this.dateBuy = room.getDateBuy();
        this.salePrice = room.getSalePrice();
        this.totalYearProfit = room.getTotalYearProfit();
    }

}
