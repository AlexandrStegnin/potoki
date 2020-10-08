package com.art.service;

import com.art.config.exception.EntityNotFoundException;
import com.art.model.AccountTransaction;
import com.art.model.supporting.filters.AccountTransactionFilter;
import com.art.repository.AccountTransactionRepository;
import com.art.specifications.AccountTransactionSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author Alexandr Stegnin
 */

@Service
public class AccountTransactionService {

    private final AccountTransactionSpecification transactionSpecification;

    private final AccountTransactionRepository accountTransactionRepository;

    public AccountTransactionService(AccountTransactionSpecification transactionSpecification,
                                     AccountTransactionRepository accountTransactionRepository) {
        this.transactionSpecification = transactionSpecification;
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

    public Page<AccountTransaction> findAll(AccountTransactionFilter filter, Pageable pageable) {
        if (filter.getPageSize() == 0) pageable = new PageRequest(filter.getPageNumber(), filter.getTotal() + 1);
        return accountTransactionRepository.findAll(
                transactionSpecification.getFilter(filter),
                pageable
        );
    }

}
