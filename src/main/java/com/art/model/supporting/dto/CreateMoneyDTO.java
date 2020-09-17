package com.art.model.supporting.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO для создания новой суммы инвестора
 *
 * @author Alexandr Stegnin
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateMoneyDTO extends MoneyDTO {

    private boolean createAccepted = false;

}
