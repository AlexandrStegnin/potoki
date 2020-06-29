package com.art.service;

import com.art.model.Account;
import com.art.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account findById(Long id) {
        return accountRepository.findOne(id);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account create(Account account) {
        return accountRepository.saveAndFlush(account);
    }

    public Account update(Account account) {
        return accountRepository.saveAndFlush(account);
    }

    public void delete(Account account) {
        accountRepository.delete(account);
    }

    public void delete(Long id) {
        accountRepository.delete(id);
    }

    public Account findByAccountNumber(Long accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

}
