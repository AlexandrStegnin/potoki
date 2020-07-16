package com.art.service;

import com.art.config.application.Constant;
import com.art.model.Account;
import com.art.model.AppUser;
import com.art.model.Facility;
import com.art.model.supporting.enums.OwnerType;
import com.art.model.supporting.enums.Region;
import com.art.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public Account findByOwnerId(Long ownerId, OwnerType ownerType) {
        return accountRepository.findByOwnerIdAndOwnerType(ownerId, ownerType);
    }

    public void createAccount(AppUser user) {
        Account account = new Account();
        account.setAccountNumber(generateAccountNumber(user));
        account.setOwnerId(user.getId());
        account.setOwnerType(OwnerType.INVESTOR);
        accountRepository.save(account);
    }

    public void createAccount(Facility facility) {
        if (facility.getFullName() != null && !facility.getFullName().isEmpty()) {
            String accountNumber = generateAccountNumber(facility);
            if (accountRepository.existsByAccountNumber(accountNumber)) {
                throw new RuntimeException(
                        String.format("Номер счёта [%s] уже используется. Проверьте правильность введённых данных", accountNumber));
            }
            if (!accountNumber.isEmpty()) {
                Account account = new Account();
                account.setAccountNumber(accountNumber);
                account.setOwnerId(facility.getId());
                account.setOwnerType(OwnerType.FACILITY);
                accountRepository.save(account);
            }
        }
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

    /**
     * Сгенерировать номер счёта для объекта
     *
     * @param facility объект
     * @return сгенерированный номер счёта
     */
    private String generateAccountNumber(Facility facility) {
        /*
            5 или более цифр до пробела (если есть)
        */
        Pattern pattern = Pattern.compile("\\d{5,}\\s*");
        Matcher matcher = pattern.matcher(facility.getFullName());
        String partNumber = "";
        if (matcher.matches()) {
            partNumber = matcher.group(0);
            String regionNumber = getRegionNumber();
            return partNumber.concat(regionNumber);
        }
        return partNumber;
    }

    private String getRegionNumber() {
        return Region.TMN.getNumber();
    }

}
