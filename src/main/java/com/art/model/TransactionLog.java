package com.art.model;

import com.art.config.SecurityUtils;
import com.art.model.supporting.TransactionType;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Класс для хранения информации и работы с транзакциями по операциям
 *
 * @author Alexandr Stegnin
 */

@Data
@Entity
@Table(name = "transaction_log")
public class TransactionLog {

    @Id
    @TableGenerator(name = "txLogSeqStore", table = "SEQ_STORE",
            pkColumnName = "SEQ_NAME", pkColumnValue = "TX.LOG.ID.PK",
            valueColumnName = "SEQ_VALUE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "txLogSeqStore")
    @Column(name = "id")
    private Long id;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "tx_date")
    private Date txDate;

    @ManyToMany
    @JoinTable(name = "tx_log_inv_cash",
            joinColumns = {@JoinColumn(name = "tx_id", referencedColumnName = "id")},
            inverseJoinColumns = @JoinColumn(name = "cash_id", referencedColumnName = "id"))
    private Set<InvestorsCash> investorsCashes;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType type;

    @Column(name = "rollback_enabled")
    private boolean rollbackEnabled;

    @PrePersist
    public void prePersist() {
        this.txDate = new Date();
        this.createdBy = SecurityUtils.getUsername();
    }

}
