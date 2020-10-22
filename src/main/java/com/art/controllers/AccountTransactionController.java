package com.art.controllers;

import com.art.config.application.Location;
import com.art.model.AccountTransaction;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.dto.AccountTxDTO;
import com.art.model.supporting.dto.BalanceDTO;
import com.art.model.supporting.enums.CashType;
import com.art.model.supporting.filters.AccountTransactionFilter;
import com.art.service.AccountTransactionService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
     * Получить страницу для отображения списка транзакций по счетам клиентов
     *
     * @param pageable для постраничного отображения
     * @return страница
     */
    @GetMapping(path = Location.ACC_TRANSACTIONS)
    public ModelAndView accountTransactions(@PageableDefault(size = 100) @SortDefault Pageable pageable) {
        return prepareModel(filter);
    }

    /**
     * Получить отфильтрованную страницу для отображения списка транзакций по счетам клиентов
     *
     * @param filter для фильтрации результата
     * @return страница
     */
    @PostMapping(path = Location.ACC_TRANSACTIONS)
    public ModelAndView accountTransactionsFiltered(@ModelAttribute(value = "filter") AccountTransactionFilter filter) {
        return prepareModel(filter);
    }

    /**
     * Получить отфильтрованную страницу для отображения списка транзакций по счетам клиентов
     *
     * @param dto для фильтрации результата
     * @return страница
     */
//    @ResponseBody
//    @PostMapping(path = Location.ACC_TRANSACTIONS_POPUP)
//    public List<AccountTransactionDTO> accountTransactionsPopup(@RequestBody AccountSummaryDTO dto) {
//        return transactionService.fetch(dto);
//    }

    /**
     * Удалить суммы транзакций
     *
     * @param dto для удаления
     * @return ответ
     */
    @ResponseBody
    @PostMapping(path = Location.ACC_TRANSACTIONS_DELETE)
    public ApiResponse deleteTransactions(@RequestBody AccountTxDTO dto) {
        return transactionService.delete(dto);
    }

    @ResponseBody
    @PostMapping(path = Location.TRANSACTIONS_REINVEST)
    public ApiResponse reinvest(@RequestBody AccountTxDTO dto) {
        return transactionService.reinvest(dto);
    }

    @ResponseBody
    @PostMapping(path = Location.TRANSACTIONS_BALANCE)
    public BalanceDTO getBalance(@RequestBody BalanceDTO dto) {
        return transactionService.getBalance(dto.getAccountId());
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
        model.addObject("accountTxDTO", new AccountTxDTO());
        return model;
    }

    @ModelAttribute("owners")
    public List<String> initOwners() {
        return transactionService.initOwners();
    }

    @ModelAttribute("recipients")
    public List<String> initRecipients() {
        return transactionService.initRecipients();
    }

    @ModelAttribute("cashTypes")
    public List<CashType> initCashTypes() {
        return transactionService.initCashTypes();
    }

    @ModelAttribute("payers")
    public List<String> initPayers() {
        return transactionService.initPayers();
    }

    @ModelAttribute("parentPayers")
    public List<String> initParentPayers() {
        return transactionService.initParentPayers();
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

}
