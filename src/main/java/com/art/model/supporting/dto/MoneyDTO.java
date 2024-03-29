package com.art.model.supporting.dto;

import com.art.model.supporting.enums.MoneyOperation;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Alexandr Stegnin
 */

@Data
public abstract class MoneyDTO {

    private Long facilityId;

    private Long underFacilityId;

    private Long investorId;

    private BigDecimal cash;

    private Date dateGiven;

    private Long cashSourceId;

    private Long newCashDetailId;

    private int shareTypeId;
    
    private MoneyOperation operation;

    private Date realDateGiven;

}
