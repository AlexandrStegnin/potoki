package com.art.model.supporting.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author Alexandr Stegnin
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnderFacilityDTO {

    Long id;

    String name;

    FacilityDTO facility;

}
