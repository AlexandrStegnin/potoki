package com.art.controllers;

import com.art.config.application.Location;
import com.art.model.Account;
import com.art.model.supporting.dto.AccountSummaryDTO;
import com.art.model.supporting.dto.AccountTransactionDTO;
import com.art.model.supporting.filters.AccTxFilter;
import com.art.service.AccountTransactionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Контроллер для работы со свободными средствами клиентов
 *
 * @author Alexandr Stegnin
 */

@Controller
public class AccountTxSummaryController {

    private final AccountTransactionService accountTransactionService;

    private final AccTxFilter filter = new AccTxFilter();

    public AccountTxSummaryController(AccountTransactionService accountTransactionService) {
        this.accountTransactionService = accountTransactionService;
    }

    @GetMapping(path = Location.TRANSACTIONS_SUMMARY)
    public ModelAndView accountsTxSummaryPage(@PageableDefault(size = 100) @SortDefault Pageable pageable) {
        return prepareModel(filter);
    }

    @PostMapping(path = Location.TRANSACTIONS_SUMMARY)
    public ModelAndView accountsTxSummaryPageFiltered(@ModelAttribute("filter") AccTxFilter filter) {
        return prepareModel(filter);
    }

    @ResponseBody
    @PostMapping(path = Location.TRANSACTIONS_DETAILS)
    public List<AccountTransactionDTO> getTransactionsByAccountId(@RequestBody AccountSummaryDTO dto) {
        return accountTransactionService.getDetails(dto);
    }

    private ModelAndView prepareModel(AccTxFilter filter) {
        ModelAndView model = new ModelAndView("free-cash");
        Pageable pageable = new PageRequest(filter.getPageNumber(), filter.getPageSize());
        model.addObject("page", accountTransactionService.getSummary(filter, pageable));
        model.addObject("filter", filter);
        return model;
    }

    @ModelAttribute("owners")
    public List<Account> initOwners() {
        return accountTransactionService.initOwners();
    }

    @ModelAttribute("payers")
    public List<Account> initPayers() {
        return accountTransactionService.initPayers();
    }

}
