package com.art.model.supporting.dto;

import com.art.model.CashSource;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Alexandr Stegnin
 */

@Data
@NoArgsConstructor
public class CashSourceDTO {

    private Long id;

    private String name;

    private String organizationId;

    public CashSourceDTO(CashSource entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.organizationId = entity.getOrganizationId();
    }

}
