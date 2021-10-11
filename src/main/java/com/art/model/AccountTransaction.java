package com.art.model;

import com.art.model.supporting.enums.CashType;
import com.art.model.supporting.enums.OperationType;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(exclude = {"parent", "child"})
@Table(name = "account_transaction")
public class AccountTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_tx_generator")
  @SequenceGenerator(name = "account_tx_generator", sequenceName = "account_transaction_id_seq")
  @Column(name = "id")
  Long id;

  @ManyToOne
  @JoinColumn(name = "parent_acc_tx_id")
  AccountTransaction parent;

  @OneToMany(mappedBy = "parent", orphanRemoval = true)
  Set<AccountTransaction> child;

  @Column(name = "tx_date")
  Date txDate = new Date();

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "operation_type")
  OperationType operationType;

  @ManyToOne
  @JoinColumn(name = "payer_account_id")
  Account payer;

  @OneToOne
  @JoinColumn(name = "owner_account_id")
  Account owner;

  @ManyToOne
  @JoinColumn(name = "recipient_account_id")
  Account recipient;

  @OneToMany
  @JoinColumn(name = "acc_tx_id", referencedColumnName = "id")
  Set<SalePayment> salePayments = new HashSet<>();

  @OneToMany
  @JoinColumn(name = "acc_tx_id", referencedColumnName = "id")
  Set<RentPayment> rentPayments = new HashSet<>();

  @OneToMany(mappedBy = "transaction")
  Set<Money> monies = new HashSet<>();

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "cash_type_id")
  CashType cashType;

  @Column(name = "blocked")
  boolean blocked = false;

  @Column(name = "cash")
  BigDecimal cash;


  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "creation_time")
  Date creationTime;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "modified_time")
  Date modifiedTime;

  @PrePersist
  public void prePersist() {
    this.creationTime = new Date();
  }

  @PreUpdate
  public void preUpdate() {
    this.modifiedTime = new Date();
  }

  public AccountTransaction(Account owner) {
    this.owner = owner;
  }

  public void removeMoney(Money money) {
    if (this.monies != null) {
      this.monies.remove(money);
      money.setTransaction(null);
    }
  }

  public void removeSalePayment(SalePayment salePayment) {
    if (this.salePayments != null) {
      this.salePayments.remove(salePayment);
      salePayment.setAccTxId(null);
    }
  }

  public void removeRentPayment(RentPayment rentPayment) {
    if (this.rentPayments != null) {
      this.rentPayments.remove(rentPayment);
      rentPayment.setAccTxId(null);
    }
  }

}
