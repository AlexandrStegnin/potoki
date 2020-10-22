package com.art.converter;

import com.art.model.Account;
import com.art.service.AccountService;
import org.springframework.core.convert.converter.Converter;

/**
 * @author Alexandr Stegnin
 */

public class AccountConverter implements Converter<Long, Account> {

    private final AccountService accountService;

    public AccountConverter(AccountService accountService) {
        this.accountService = accountService;
    }

    public Account convert(Long id) {
        return accountService.findById(id);
    }
}
