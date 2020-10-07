package com.art.model;

import com.art.model.supporting.enums.CashType;
import com.art.model.supporting.enums.OperationType;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Alexandr Stegnin
 */

@Data
@Entity
@Table(name = "account_transaction")
public class AccountTransaction {

    @Id
    @TableGenerator(name = "accountTransactionSeqStore", table = "SEQ_STORE",
            pkColumnName = "SEQ_NAME", pkColumnValue = "ACCOUNT.TX.ID.PK",
            valueColumnName = "SEQ_VALUE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "accountTransactionSeqStore")
    @Column(name = "id")
    private Long id;

    @Column(name = "tx_date")
    private Date txDate = new Date();

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "operation_type")
    private OperationType operationType;

    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private Account accountFrom;

    @OneToOne
    @JoinColumn(name = "owner_account_id")
    private Account owner;

    @OneToOne
    @JoinColumn(name = "sale_payment_id")
    private SalePayment salePayment;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "cash_type_id")
    private CashType cashType;

    public AccountTransaction(Account owner) {
        this.owner = owner;
    }
}
