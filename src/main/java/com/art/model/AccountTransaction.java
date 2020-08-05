package com.art.model;

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
    private Date txDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type")
    private OperationType operationType;

    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private Account accountFrom;

    @OneToOne
    @JoinColumn(name = "to_account_id")
    private Account accountTo;

    @OneToOne
    @JoinColumn(name = "cash_id")
    private InvestorCash cash;

}
