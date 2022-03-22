package com.art.model.supporting.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * DTO для вывода денег
 *
 * @author Alexandr Stegnin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CashingMoneyDTO {

  Long id;
  Long facilityId;
  Long underFacilityId;
  List<Long> investorsIds;
  BigDecimal cash;
  Date dateCashing;
  BigDecimal commission;
  BigDecimal commissionNoMore;
  Date dateClose;
  String operation;
  boolean all = false;

}
