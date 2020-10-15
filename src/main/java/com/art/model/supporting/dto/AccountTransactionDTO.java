package com.art.model.supporting.dto;

import com.art.model.AccountTransaction;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * DTO для отображения данных на клиенте
 *
 * @author Alexandr Stegnin
 */

@Data
public class AccountTransactionDTO {

    private Date txDate;

    private String operationType;

    private String payer;

    private String owner;

    private String recipient;

    private BigDecimal cash;

    private String cashType;

    private boolean blocked;

    public AccountTransactionDTO(AccountTransaction transaction) {
        this.txDate = transaction.getTxDate();
        this.operationType = transaction.getOperationType().getTitle();
        this.payer = transaction.getPayer().getOwnerName();
        this.owner = transaction.getOwner().getOwnerName();
        this.recipient = transaction.getRecipient().getOwnerName();
        this.cashType = transaction.getCashType().getTitle();
        this.cash = transaction.getCash();
        this.blocked = transaction.isBlocked();
    }

}
