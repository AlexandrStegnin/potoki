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

    private String ownerName;

    public AccountDTO(Account owner, Double summary, String ownerName) {
        this(owner, new BigDecimal(String.valueOf(summary)), ownerName);
    }

    public AccountDTO(Account owner, Integer summary, String ownerName) {
        this(owner, new BigDecimal(String.valueOf(summary)), ownerName);
    }

}
