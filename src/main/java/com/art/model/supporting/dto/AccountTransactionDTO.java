package com.art.model.supporting.dto;

import com.art.model.Account;
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
        this.payer = getName(transaction.getPayer());
        this.owner = getName(transaction.getOwner());
        this.recipient = getName(transaction.getRecipient());
        this.cashType = transaction.getCashType().getTitle();
        this.cash = transaction.getCash();
        this.blocked = transaction.isBlocked();
    }

    private String getName(Account account) {
        String name = "";
        if (account != null) {
            name = account.getOwnerName();
        }
        return name;
    }

}
