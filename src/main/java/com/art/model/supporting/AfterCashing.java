package com.art.model.supporting;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

@EqualsAndHashCode
@ToString
@Entity
@Table(name = "AfterCashing")
public class AfterCashing implements Serializable {
    private BigInteger id;
    private BigInteger oldId;
    private BigDecimal oldValue;

    public AfterCashing() {
    }

    public AfterCashing(BigInteger oldId, BigDecimal oldValue) {
        this.oldId = oldId;
        this.oldValue = oldValue;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public BigInteger getId() {
        return id;
    }
    public void setId(BigInteger id) {
        this.id = id;
    }

    @Column(name = "OldId")
    public BigInteger getOldId() {
        return oldId;
    }

    public void setOldId(BigInteger oldId) {
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
