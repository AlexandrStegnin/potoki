package com.art.controllers;

import com.art.model.TransactionLog;
import com.art.model.supporting.dto.TransactionLogDTO;
import com.art.model.supporting.filters.TxLogFilter;
import com.art.service.TransactionLogService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.art.config.application.Location.*;

/**
 * @author Alexandr Stegnin
 */

@Controller
public class TransactionLogController {

    private final TransactionLogService transactionLogService;

    private final TxLogFilter filter;

    public TransactionLogController(TransactionLogService transactionLogService) {
        this.transactionLogService = transactionLogService;
        this.filter = new TxLogFilter();
    }

    /**
     * Получить список всех транзакций
     *
     * @return список транзакций
     */
    @GetMapping(path = URL_TRANSACTIONS)
    public String findAll(@PageableDefault(size = 100) @SortDefault Pageable pageable, ModelMap model) {
        prepareModel(model, filter, pageable);
        return "transactions";
    }

    @PostMapping(path = URL_TRANSACTIONS)
    public String getTxFiltered(@PageableDefault(size = 100) @SortDefault Pageable pageable, ModelMap model,
                                @ModelAttribute("filter") TxLogFilter filter) {
        prepareModel(model, filter, pageable);
        return "transactions";
    }

    /**
     * Получить транзакцию по id
     *
     * @param txId id транзакции
     * @return найденная транзакция
     */
    @GetMapping(path = URL_TRANSACTIONS_TX_ID)
    @ResponseBody
    public TransactionLogDTO findById(@PathVariable Long txId) {
        return new TransactionLogDTO(transactionLogService.findById(txId));
    }

    /**
     * Откатить операцию
     *
     * @param txId id транзакции
     * @return сообщение об успешном/не успешном откате операции
     */
    @PostMapping(path = URL_TRANSACTIONS_TX_ID_ROLLBACK)
    public String rollback(@PathVariable Long txId) {
        return transactionLogService.rollbackTransaction(txId);
    }

    private void prepareModel(ModelMap model, TxLogFilter filter, Pageable pageable) {
        List<TransactionLog> transactions = transactionLogService.findAll(filter, pageable);
        List<TransactionLogDTO> dtoList = transactions
                .stream()
                .map(TransactionLogDTO::new)
                .collect(Collectors.toList());
        List<String> logins = transactionLogService.getInvestors();
        Map<Integer, String> types = transactionLogService.getTypes();
        List<String> creators = transactionLogService.getCreators();
        model.addAttribute("transactions", dtoList);
        model.addAttribute("investors", logins);
        model.addAttribute("creators", creators);
        model.addAttribute("filter", filter);
        model.addAttribute("types", types);
    }

}