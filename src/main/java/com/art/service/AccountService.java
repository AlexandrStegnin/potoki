package com.art.service;

import com.art.config.application.Constant;
import com.art.model.Account;
import com.art.model.AppUser;
import com.art.model.Facility;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.enums.OwnerType;
import com.art.model.supporting.enums.Region;
import com.art.repository.AccountRepository;
import org.springframework.http.HttpStatus;
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

    /**
     * Создать счёт для пользователя (инвестора)
     *
     * @param user пользователь
     */
    public void createAccount(AppUser user) {
        Account account = new Account();
        account.setAccountNumber(generateAccountNumber(user));
        account.setOwnerId(user.getId());
        account.setOwnerType(OwnerType.INVESTOR);
        accountRepository.save(account);
    }

    /**
     * Создать счёт для объекта
     *
     * @param facility объект
     */
    public void createAccount(Facility facility) {
        String accountNumber = generateAccountNumber(facility);
        if (accountNumber.isEmpty()) {
            return;
        }
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setOwnerId(facility.getId());
        account.setOwnerType(OwnerType.FACILITY);
        accountRepository.save(account);
    }

    /**
     * Проверить номер счёта по реквизитам
     *
     * @param facility объект
     * @return ответ
     */
    public ApiResponse checkAccountNumber(Facility facility) {
        ApiResponse apiResponse = new ApiResponse();
        if (facility.getFullName() != null && !facility.getFullName().isEmpty()) {
            String accountNumber = generateAccountNumber(facility);
            if (accountNumber.isEmpty()) {
                return null;
            }
            if (accountRepository.existsByAccountNumber(accountNumber)) {
                apiResponse.setMessage(String.format("Номер счёта [%s] уже используется. Проверьте правильность введённых данных", accountNumber));
                apiResponse.setStatus(HttpStatus.PRECONDITION_FAILED.value());
                return apiResponse;
            }
        }
        return null;
    }

    /**
     * Сгенерировать номер счёта для пользователя
     *
     * @param user пользователь
     * @return сгенерированный номер
     */
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
        String accountNumber = "";
        if (matcher.matches()) {
            return matcher.group(0);
        }
        return accountNumber;
    }

    private String getRegionNumber() {
        return Region.TMN.getNumber();
    }

    /**
     * Создать счёт для подобъекта
     *
     * @param underFacilityId id подобъекта
     * @param parentAccountNumber номер счёта объекта родителя
     * @param countUnderFacilities кол-во подобъектов объекта
     */
    public void createAccount(Long underFacilityId, String parentAccountNumber, int countUnderFacilities) {
        String accNumberSuffix = countUnderFacilities > 9 ? "" : "0";
        String underFacilityAccountNumber = parentAccountNumber.concat(accNumberSuffix).concat(String.valueOf(countUnderFacilities + 1));
        Account account = new Account();
        account.setAccountNumber(underFacilityAccountNumber);
        account.setOwnerId(underFacilityId);
        account.setOwnerType(OwnerType.UNDER_FACILITY);
        accountRepository.save(account);
    }
}
