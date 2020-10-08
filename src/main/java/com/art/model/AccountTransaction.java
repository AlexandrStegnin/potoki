package com.art.model;

import com.art.model.supporting.enums.CashType;
import com.art.model.supporting.enums.OperationType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Alexandr Stegnin
 */

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "account_transaction")
public class AccountTransaction extends AbstractEntity {

    @Column(name = "tx_date")
    private Date txDate = new Date();

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "operation_type")
    private OperationType operationType;

    @ManyToOne
    @JoinColumn(name = "payer_account_id")
    private Account payer;

    @OneToOne
    @JoinColumn(name = "owner_account_id")
    private Account owner;

    @ManyToOne
    @JoinColumn(name = "recipient_account_id")
    private Account recipient;

    @OneToOne
    @JoinColumn(name = "sale_payment_id")
    private SalePayment salePayment;

    @OneToOne
    @JoinColumn(name = "money_id")
    private Money money;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "cash_type_id")
    private CashType cashType;

    @Column(name = "blocked")
    private boolean blocked = false;

    public AccountTransaction(Account owner) {
        this.owner = owner;
    }
}
