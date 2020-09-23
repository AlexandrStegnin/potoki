package com.art.model.supporting.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class DeleteMoneyDTO extends MoneyDTO {

    private List<Long> moneyIds;

}
