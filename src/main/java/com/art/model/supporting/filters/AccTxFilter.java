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

}
