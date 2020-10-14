package com.art.model.supporting.dto;

import com.art.model.AccountTransaction;
import com.art.model.Money;
import com.art.model.SalePayment;
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

    private BigDecimal salePayment;

    private BigDecimal money;

    private BigDecimal cash;

    private String cashType;

    private boolean blocked;

    public AccountTransactionDTO(AccountTransaction transaction) {
        this.txDate = transaction.getTxDate();
        this.operationType = transaction.getOperationType().getTitle();
        this.payer = transaction.getPayer().getOwnerName();
        this.owner = transaction.getOwner().getOwnerName();
        this.recipient = transaction.getRecipient().getOwnerName();
        this.salePayment = convert(transaction.getSalePayment());
        this.money = convert(transaction.getMoney());
        this.cashType = transaction.getCashType().getTitle();
        this.cash = convert(transaction.getSalePayment(), transaction.getMoney());
        this.blocked = transaction.isBlocked();
    }

    private BigDecimal convert(SalePayment salePayment) {
        if (salePayment == null) {
            return BigDecimal.ZERO;
        }
        return salePayment.getProfitToReInvest();
    }

    private BigDecimal convert(Money money) {
        if (money == null) {
            return BigDecimal.ZERO;
        }
        return money.getGivenCash();
    }

    private BigDecimal convert(SalePayment salePayment, Money money) {
        BigDecimal cash = BigDecimal.ZERO;
        if (salePayment != null) {
            cash = cash.add(salePayment.getProfitToReInvest());
        }
        if (money != null) {
            cash = cash.add(money.getGivenCash());
        }
        return cash;
    }

}
