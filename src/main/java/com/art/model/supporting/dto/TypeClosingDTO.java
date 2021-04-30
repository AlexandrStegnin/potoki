package com.art.model.supporting.dto;

import com.art.model.TypeClosing;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Alexandr Stegnin
 */

@Data
@NoArgsConstructor
public class TypeClosingDTO {

    private Long id;

    private String name;

    public TypeClosingDTO(TypeClosing entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }

}
