package com.art.model.supporting.filters;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Alexandr Stegnin
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AccountTransactionFilter extends AbstractFilter {

    private String owner;

    private String recipient;

    private Long salePaymentId;

    private String cashType;

    private Long moneyId;

    private String payer;

    private String parentPayer;

    public boolean isClear() {
        return (owner == null || owner.startsWith("Выберите"))
                && (payer == null || payer.startsWith("Выберите"))
                && (parentPayer == null || parentPayer.startsWith("Выберите"));
    }

    public boolean isFilterByOwner() {
        return (owner != null && !owner.startsWith("Выберите"))
                && (payer == null || payer.startsWith("Выберите"))
                && (parentPayer == null || parentPayer.startsWith("Выберите"));
    }

    public boolean isFilterByPayer() {
        return (payer != null && !payer.startsWith("Выберите"))
                && (owner == null || owner.startsWith("Выберите"))
                && (parentPayer == null || parentPayer.startsWith("Выберите"));
    }

    public boolean isFilterByOwnerAndPayer() {
        return isFilterByOwner() && isFilterByPayer();
    }

    public boolean isFilterByParentPayer() {
        return (parentPayer != null && !parentPayer.startsWith("Выберите"))
                && (owner == null || owner.startsWith("Выберите"))
                && (payer == null || payer.startsWith("Выберите"));
    }

}
