package com.art.model;

import com.art.model.supporting.enums.CashType;
import com.art.model.supporting.enums.OperationType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alexandr Stegnin
 */

@Data
@Entity
@NoArgsConstructor
@ToString(exclude = {"parent", "child"})
@EqualsAndHashCode(callSuper = true, exclude = {"parent", "child"})
@Table(name = "account_transaction")
public class AccountTransaction extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "parent_acc_tx_id")
    private AccountTransaction parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private Set<AccountTransaction> child;

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

    @OneToMany(mappedBy = "transaction")
    private Set<SalePayment> salePayments = new HashSet<>();

    @OneToMany(mappedBy = "transaction")
    private Set<RentPayment> rentPayments = new HashSet<>();

    @OneToMany(mappedBy = "transaction")
    private Set<Money> monies = new HashSet<>();

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "cash_type_id")
    private CashType cashType;

    @Column(name = "blocked")
    private boolean blocked = false;

    @Column(name = "cash")
    private BigDecimal cash;

    public AccountTransaction(Account owner) {
        this.owner = owner;
    }
}
