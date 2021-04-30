package com.art.model.supporting.dto;

import com.art.model.NewCashDetail;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Alexandr Stegnin
 */

@Data
@NoArgsConstructor
public class NewCashDetailDTO {

    private Long id;

    private String name;

    public NewCashDetailDTO(NewCashDetail entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }

}
