package com.art.model.supporting.filters;

import com.art.model.Account;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AccTxFilter extends AccountTransactionFilter {

    private List<Account> owners;

    private List<Account> payers;

    private List<Account> parentPayers;

    private List<String> cashTypes;

    public boolean isClear() {
        return (owners == null || owners.isEmpty()) &&
                (payers == null || payers.isEmpty()) &&
                (parentPayers == null || parentPayers.isEmpty()) &&
                (cashTypes == null || cashTypes.isEmpty());
    }

    public boolean isFilterByOwners() {
        return (owners != null && owners.size() > 0)
                && (payers == null || payers.isEmpty())
                && (parentPayers == null || parentPayers.isEmpty());
    }

    public boolean isFilterByPayers() {
        return (payers != null && payers.size() > 0)
                && (owners == null || owners.isEmpty())
                && (parentPayers == null || parentPayers.isEmpty());
    }

    public boolean isFilterByParentPayers() {
        return (parentPayers != null && parentPayers.size() > 0)
                && (owners == null || owners.isEmpty())
                && (payers == null || payers.isEmpty());
    }

    public boolean isFilterByOwnersAndPayers() {
        return isFilterByOwners() && isFilterByPayers();
    }

    public boolean isFilterByOwnersAndParentPayers() {
        return isFilterByOwners() && isFilterByParentPayers();
    }

    public boolean isFilterByOwnersAndPayersAndParentPayers() {
        return isFilterByOwners() && isFilterByPayers() && isFilterByParentPayers();
    }

    public boolean isFilterByPayerAndParentPayer() {
        return isFilterByPayers() && isFilterByParentPayers();
    }

    public boolean isFilterByPayersAndParentPayers() {
        return isFilterByPayers() && isFilterByParentPayers();
    }

    public Type getType() {
        if (isClear()) {
            return Type.IS_CLEAR;
        }
        if (isFilterByOwners()) {
            return Type.BY_OWNERS;
        }
        if (isFilterByPayers()) {
            return Type.BY_PAYERS;
        }
        if (isFilterByParentPayers()) {
            return Type.BY_PARENT_PAYERS;
        }
        if (isFilterByOwnersAndPayers()) {
            return Type.BY_OWNERS_AND_PAYERS;
        }
        if (isFilterByOwnersAndParentPayers()) {
            return Type.BY_OWNERS_AND_PARENT_PAYERS;
        }
        if (isFilterByOwnersAndPayersAndParentPayers()) {
            return Type.BY_OWNERS_AND_PAYERS_AND_PARENT_PAYERS;
        }
        if (isFilterByPayersAndParentPayers()) {
            return Type.BY_PAYERS_AND_PARENT_PAYERS;
        }
        return Type.IS_CLEAR;
    }

    public enum Type {
        IS_CLEAR,
        BY_OWNERS,
        BY_PAYERS,
        BY_PARENT_PAYERS,
        BY_OWNERS_AND_PAYERS,
        BY_OWNERS_AND_PARENT_PAYERS,
        BY_OWNERS_AND_PAYERS_AND_PARENT_PAYERS,
        BY_PAYERS_AND_PARENT_PAYERS
    }

}
