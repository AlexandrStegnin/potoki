package com.art.controllers;

import com.art.config.application.Location;
import com.art.model.AccountTransaction;
import com.art.model.supporting.filters.AccountTransactionFilter;
import com.art.service.AccountTransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Alexandr Stegnin
 */

@Controller
public class AccountTransactionController {

    private final AccountTransactionService transactionService;

    private final AccountTransactionFilter filter = new AccountTransactionFilter();

    public AccountTransactionController(AccountTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Получить страницу для отображения списка свободных денег инвесторов
     *
     * @param pageable для постраничного отображения
     * @return страница
     */
    @GetMapping(path = Location.FREE_CASH)
    public ModelAndView accountTransactions(@PageableDefault(size = 100) @SortDefault Pageable pageable) {
        return prepareModel(filter);
    }

    /**
     * Подготовить модель для страницы
     *
     * @param filter фильтры
     */
    private ModelAndView prepareModel(AccountTransactionFilter filter) {
        ModelAndView model = new ModelAndView("account-tx-list");
        Pageable pageable = new PageRequest(filter.getPageNumber(), filter.getPageSize());
        Page<AccountTransaction> page = transactionService.findAll(filter, pageable);
        model.addObject("page", page);
        model.addObject("filter", filter);
        return model;
    }

}
