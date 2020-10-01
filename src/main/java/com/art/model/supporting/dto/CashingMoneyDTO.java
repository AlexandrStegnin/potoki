package com.art.model.supporting.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * DTO для вывода денег
 *
 * @author Alexandr Stegnin
 */

@Data
public class CashingMoneyDTO {

    private Long id;

    private Long facilityId;

    private Long underFacilityId;

    private List<Long> investorsIds;

    private BigDecimal cash;

    private Date dateCashing;

    private BigDecimal commission;

    private BigDecimal commissionNoMore;

    private Date dateClose;

    private String operation;

    private boolean all = false;

}
