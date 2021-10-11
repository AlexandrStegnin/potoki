package com.art.model;

import com.art.config.SecurityUtils;
import com.art.model.supporting.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_log_generator")
  @SequenceGenerator(name = "transaction_log_generator", sequenceName = "transaction_log_id_seq")
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
  private Set<Money> monies;

  @Enumerated(EnumType.STRING)
  @Column(name = "transaction_type")
  private TransactionType type;

  @Column(name = "rollback_enabled")
  private boolean rollbackEnabled;

  @OneToOne
  @JsonIgnore
  @JoinColumn(name = "blocked_from")
  private TransactionLog blockedFrom;

  @PrePersist
  public void prePersist() {
    this.txDate = new Date();
    this.createdBy = SecurityUtils.getUsername();
  }

}
