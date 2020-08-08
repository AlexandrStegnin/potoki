package com.art.model.supporting.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Data
public class ReinvestCashDTO {

    private Long facilityToReinvestId;

    private Long underFacilityToReinvestId;

    private List<Long> investorCashIdList = new ArrayList<>();

    private Date dateClose;

    private int shareTypeId;

}
