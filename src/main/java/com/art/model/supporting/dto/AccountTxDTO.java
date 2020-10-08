package com.art.model.supporting.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author Alexandr Stegnin
 */

@Data
public class AccountTxDTO {

    private Long id;

    private Long facilityId;

    private Long underFacilityId;

    private Date dateReinvest;

    private String shareType;

}
