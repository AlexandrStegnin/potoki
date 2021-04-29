package com.art.model.supporting.dto;

import com.art.model.UnderFacility;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author Alexandr Stegnin
 */

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnderFacilityDTO {

    Long id;

    String name;

    FacilityDTO facility;

    public UnderFacilityDTO(UnderFacility entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.facility = new FacilityDTO(entity.getFacility());
    }

}
