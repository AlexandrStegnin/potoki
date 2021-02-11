package com.art.model.supporting.filters;

import com.art.model.Account;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AccTxFilter extends AccountTransactionFilter {

    private List<Account> owners = new ArrayList<>();

    private List<Account> payers = new ArrayList<>();

    private List<Account> parentPayers = new ArrayList<>();

    private List<String> cashTypes = new ArrayList<>();

    public boolean isClear() {
        return (owners == null || owners.isEmpty()) &&
                (payers == null || payers.isEmpty()) &&
                (parentPayers == null || parentPayers.isEmpty()) &&
                (cashTypes == null || cashTypes.isEmpty());
    }

    public boolean isFilterByOwners() {
        return (owners != null && owners.size() > 0);
    }

    public boolean isFilterByPayers() {
        return (payers != null && payers.size() > 0);
    }

    public boolean isFilterByParentPayers() {
        return (parentPayers != null && parentPayers.size() > 0);
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

    public boolean isFilterByPayersAndParentPayers() {
        return isFilterByPayers() && isFilterByParentPayers();
    }

    public Type getType() {
        if (isFilterByOwnersAndPayersAndParentPayers()) {
            return Type.BY_OWNERS_AND_PAYERS_AND_PARENT_PAYERS;
        }
        if (isFilterByPayersAndParentPayers()) {
            return Type.BY_PAYERS_AND_PARENT_PAYERS;
        }
        if (isFilterByOwnersAndParentPayers()) {
            return Type.BY_OWNERS_AND_PARENT_PAYERS;
        }
        if (isFilterByOwnersAndPayers()) {
            return Type.BY_OWNERS_AND_PAYERS;
        }
        if (isFilterByParentPayers()) {
            return Type.BY_PARENT_PAYERS;
        }
        if (isFilterByPayers()) {
            return Type.BY_PAYERS;
        }
        if (isFilterByOwners()) {
            return Type.BY_OWNERS;
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
