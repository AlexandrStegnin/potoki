package com.art.model.supporting.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Alexandr Stegnin
 */

@Data
@NoArgsConstructor
public class BalanceDTO {

    private Long accountId;

    private String ownerName;

    private BigDecimal summary;

    public BalanceDTO(AccountDTO accountDTO) {
        this.accountId = accountDTO.getOwner().getId();
        this.ownerName = accountDTO.getOwner().getOwnerName();
        this.summary = accountDTO.getSummary();
    }

}
