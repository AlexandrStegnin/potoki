package com.art.model.supporting.dto;

import com.art.model.Facility;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Alexandr Stegnin
 */

@Data
@NoArgsConstructor
public class FacilityDTO {

    private Long id;

    private String name;

    private String fullName;

    private String city;

    public FacilityDTO(Facility facility) {
        this.id = facility.getId();
        this.name = facility.getName();
        this.fullName = facility.getFullName();
        this.city = facility.getCity();
    }

}
