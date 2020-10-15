package com.art.model.supporting.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Data
public class AccountTxDTO {

    private List<Long> txIds;

    private Date dateReinvest;

    private Long facilityId;

    private Long underFacilityId;

    private String shareType;

    private List<Long> accountsIds;

    private BigDecimal cash;

}
