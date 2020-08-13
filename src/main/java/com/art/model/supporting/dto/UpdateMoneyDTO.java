package com.art.model.supporting.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Alexandr Stegnin
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateMoneyDTO extends MoneyDTO {

    private Long id;

}
