package com.art.model.supporting.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author Alexandr Stegnin
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ResaleMoneyDTO extends MoneyDTO {

    private Long id;

    private Long buyerId;

    private Date dateClose;

    private Long typeCloseId;

    private Date realDateGiven;

}
