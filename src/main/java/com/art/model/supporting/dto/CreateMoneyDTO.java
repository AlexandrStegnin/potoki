package com.art.model.supporting.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * DTO для создания новой суммы инвестора
 *
 * @author Alexandr Stegnin
 */

@Data
public class CreateMoneyDTO {

    private Long facilityId;

    private Long underFacilityId;

    private Long investorId;

    private BigDecimal cash;

    private Date dateGiven;

    private Long cashSourceId;

    private Long newCashDetailId;

    private int shareTypeId;

}
