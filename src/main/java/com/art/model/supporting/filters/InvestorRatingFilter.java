package com.art.model.supporting.filters;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

/**
 * @author Alexandr Stegnin
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvestorRatingFilter {

  Date firstRatingDate;
  Date secondRatingDate;

}
