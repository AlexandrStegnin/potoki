package com.art.model.supporting.filters;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Alexandr Stegnin
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AccountTransactionFilter extends AbstractFilter {

    private Long accountId;

    private String owner;

    private String recipient;

    private Long salePaymentId;

    private String cashType;

    private Long moneyId;

    private String payer;

    private String parentPayer;

    private FilterType filterType;

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

    public boolean isFilterByParentPayer() {
        return (parentPayer != null && !parentPayer.startsWith("Выберите"))
                && (owner == null || owner.startsWith("Выберите"))
                && (payer == null || payer.startsWith("Выберите"));
    }

    public boolean isFilterByOwnerAndPayer() {
        return isFilterByOwner() && isFilterByPayer();
    }

    public boolean isFilterByOwnerAndParentPayer() {
        return isFilterByOwner() && isFilterByParentPayer();
    }

    public boolean isFilterByOwnerAndPayerAndParentPayer() {
        return isFilterByOwner() && isFilterByPayer() && isFilterByParentPayer();
    }

    public boolean isFilterByPayerAndParentPayer() {
        return isFilterByPayer() && isFilterByParentPayer();
    }

    public FilterType getFilterType() {
        if (isClear()) {
            return FilterType.IS_CLEAR;
        }
        if (isFilterByOwner()) {
            return FilterType.BY_OWNER;
        }
        if (isFilterByPayer()) {
            return FilterType.BY_PAYER;
        }
        if (isFilterByParentPayer()) {
            return FilterType.BY_PARENT_PAYER;
        }
        if (isFilterByOwnerAndPayer()) {
            return FilterType.BY_OWNER_AND_PAYER;
        }
        if (isFilterByOwnerAndParentPayer()) {
            return FilterType.BY_OWNER_AND_PARENT_PAYER;
        }
        if (isFilterByOwnerAndPayerAndParentPayer()) {
            return FilterType.BY_OWNER_AND_PAYER_AND_PARENT_PAYER;
        }
        return FilterType.IS_CLEAR;
    }

    public enum FilterType {
        IS_CLEAR,
        BY_OWNER,
        BY_PAYER,
        BY_PARENT_PAYER,
        BY_OWNER_AND_PAYER,
        BY_OWNER_AND_PARENT_PAYER,
        BY_OWNER_AND_PAYER_AND_PARENT_PAYER,
        BY_PAYER_AND_PARENT_PAYER
    }

}
