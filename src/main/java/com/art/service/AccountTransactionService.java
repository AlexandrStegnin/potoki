package com.art.service;

import com.art.config.exception.EntityNotFoundException;
import com.art.model.AccountTransaction;
import com.art.repository.AccountTransactionRepository;
import org.springframework.stereotype.Service;

/**
 * @author Alexandr Stegnin
 */

@Service
public class AccountTransactionService {

    private final AccountTransactionRepository accountTransactionRepository;

    public AccountTransactionService(AccountTransactionRepository accountTransactionRepository) {
        this.accountTransactionRepository = accountTransactionRepository;
    }

    public AccountTransaction create(AccountTransaction transaction) {
        return accountTransactionRepository.save(transaction);
    }

    public AccountTransaction update(AccountTransaction transaction) {
        if (transaction.getId() == null) {
            throw new RuntimeException("Не задан id транзакции");
        }
        AccountTransaction tx = accountTransactionRepository.findOne(transaction.getId());
        if (tx == null) {
            throw new EntityNotFoundException("Не найдена транзакция");
        }
        return accountTransactionRepository.save(transaction);
    }

    public void delete(AccountTransaction transaction) {
        accountTransactionRepository.delete(transaction);
    }

    public AccountTransaction findById(Long id) {
        AccountTransaction tx = accountTransactionRepository.findOne(id);
        if (tx == null) {
            throw new EntityNotFoundException("Не найдена транзакция");
        }
        return tx;
    }

}
