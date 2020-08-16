package com.art.model.supporting.dto;

import lombok.Data;

import java.util.Date;

/**
 * DTO для вывода одиночной суммы
 *
 * @author Alexandr Stegnin
 */

@Data
public class CashingMoneyDTO {

    private Long id;

    private Date dateClose;

    private Date realDateGiven;

}
