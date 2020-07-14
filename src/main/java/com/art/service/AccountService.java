package com.art.service;

import com.art.config.application.Constant;
import com.art.model.Account;
import com.art.model.AppUser;
import com.art.model.supporting.enums.Region;
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

    public Account findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public Account findByOwnerId(Long ownerId) {
        return accountRepository.findByOwnerId(ownerId);
    }

    public void createAccount(AppUser user) {
        Account account = new Account();
        account.setAccountNumber(generateAccountNumber(user));
        account.setOwnerId(user.getId());
        accountRepository.save(account);
    }

    private String generateAccountNumber(AppUser user) {
        /*
        первые 5 цифр 00000 (порядковый номер клиента),
        вторые 3 цифры (номер региона),
        далее 4 цифры (порядковый номер объекта),
        далее 2 цифры (порядковый номер подобъекта) - всего 14 символов поллучается
         */
        String clientCode = user.getLogin().substring(Constant.INVESTOR_PREFIX.length());
        String regionNumber = getRegionNumber();
        return clientCode.concat(regionNumber);
    }

    private String getRegionNumber() {
        return Region.TMN.getNumber();
    }

}
