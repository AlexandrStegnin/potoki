package com.art.model.supporting.filters;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAgreementFilter {

    List<Long> investorsId;

    List<Long> facilitiesId;

    boolean allRows = false;

    int pageNumber = 0;

    int pageSize = 100;

    int total;

}
