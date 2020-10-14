package com.art.model.supporting.dto;

import com.art.model.Account;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Alexandr Stegnin
 */

@Data
@AllArgsConstructor
public class AccountDTO {

    private Account owner;

    private BigDecimal summary;

}