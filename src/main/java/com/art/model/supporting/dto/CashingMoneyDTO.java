package com.art.model.supporting.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * DTO для вывода одиночной суммы
 *
 * @author Alexandr Stegnin
 */

@Data
public class CashingMoneyDTO {

    private Long id;

    private Long facilityId;

    private Long underFacilityId;

    private Long investorId;

    private BigDecimal givenCash;

    private Date dateCashing;

    private BigDecimal commission;

    private BigDecimal commissionNoMore;

    private Date dateClose;

    private String operation;

}
