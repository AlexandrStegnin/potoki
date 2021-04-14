package com.art.model.supporting.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReBuyShareDTO {

    Long buyerId;

    BigDecimal buyerCash;

    Long sellerId;

    Long facilityId;

    List<InvestorCashDTO> openedCash = new ArrayList<>();

    Date realDateGiven;

}
