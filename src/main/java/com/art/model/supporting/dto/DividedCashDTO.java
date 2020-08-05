package com.art.model.supporting.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Data
public class DividedCashDTO {

    private List<Long> investorCashList = new ArrayList<>();

    private Long reUnderFacilityId;

    private List<Long> excludedUnderFacilitiesIdList = new ArrayList<>();

    private List<Long> reUnderFacilitiesIdList = new ArrayList<>();

}
