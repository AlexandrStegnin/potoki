package com.art.model.supporting;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@EqualsAndHashCode
@ToString
@Entity
@Table(name = "AfterCashing")
public class AfterCashing implements Serializable {
    private Long id;
    private Long oldId;
    private BigDecimal oldValue;

    public AfterCashing() {
    }

    public AfterCashing(Long oldId, BigDecimal oldValue) {
        this.oldId = oldId;
        this.oldValue = oldValue;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_tx_generator")
  @SequenceGenerator(name = "account_tx_generator", sequenceName = "account_transaction_id_seq")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "OldId")
    public Long getOldId() {
        return oldId;
    }

    public void setOldId(Long oldId) {
        this.oldId = oldId;
    }

    @NonNull
    @Column(name = "OldValue")
    public BigDecimal getOldValue() {
        return oldValue;
    }

    public void setOldValue(BigDecimal oldValue) {
        this.oldValue = oldValue;
    }
}
