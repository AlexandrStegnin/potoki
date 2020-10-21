package com.art.controllers;

import com.art.config.application.Location;
import com.art.model.Facility;
import com.art.model.UnderFacility;
import com.art.model.supporting.dto.AccountSummaryDTO;
import com.art.model.supporting.dto.AccountTransactionDTO;
import com.art.model.supporting.dto.AccountTxDTO;
import com.art.model.supporting.enums.ShareType;
import com.art.model.supporting.filters.AccountTransactionFilter;
import com.art.service.AccountTransactionService;
import com.art.service.FacilityService;
import com.art.service.UnderFacilityService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

/**
 * Контроллер для работы со свободными средствами клиентов
 *
 * @author Alexandr Stegnin
 */

@Controller
public class AccountTxSummaryController {

    private final AccountTransactionService accountTransactionService;

    private final AccountTransactionFilter filter = new AccountTransactionFilter();

    private final FacilityService facilityService;

    private final UnderFacilityService underFacilityService;

    public AccountTxSummaryController(AccountTransactionService accountTransactionService, FacilityService facilityService,
                                      UnderFacilityService underFacilityService) {
        this.accountTransactionService = accountTransactionService;
        this.facilityService = facilityService;
        this.underFacilityService = underFacilityService;
    }

    @GetMapping(path = Location.TRANSACTIONS_SUMMARY)
    public ModelAndView accountsTxSummaryPage(@PageableDefault(size = 100) @SortDefault Pageable pageable) {
        return prepareModel(filter);
    }

    @PostMapping(path = Location.TRANSACTIONS_SUMMARY)
    public ModelAndView accountsTxSummaryPageFiltered(@ModelAttribute("filter") AccountTransactionFilter filter) {
        return prepareModel(filter);
    }

    @ResponseBody
    @PostMapping(path = Location.TRANSACTIONS_DETAILS)
    public List<AccountTransactionDTO> getTransactionsByAccountId(@RequestBody AccountSummaryDTO dto) {
        return accountTransactionService.getDetails(dto);
    }

    private ModelAndView prepareModel(AccountTransactionFilter filter) {
        ModelAndView model = new ModelAndView("free-cash");
        Pageable pageable = new PageRequest(filter.getPageNumber(), filter.getPageSize());
        model.addObject("page", accountTransactionService.getSummary(filter, pageable));
        model.addObject("filter", filter);
        model.addObject("accountTxDTO", new AccountTxDTO());
        return model;
    }

    @ModelAttribute("owners")
    public List<String> initOwners() {
        return accountTransactionService.initOwners();
    }

    @ModelAttribute("facilities")
    public List<Facility> initializeFacilities() {
        return facilityService.initializeFacilities();
    }

    @ModelAttribute("underFacilities")
    public List<UnderFacility> initializeUnderFacilities() {
        return underFacilityService.initializeUnderFacilities();
    }

    @ModelAttribute("shareTypes")
    public List<ShareType> initializeShareTypes() {
        return Arrays.asList(ShareType.values());
    }

    @ModelAttribute("payers")
    public List<String> initPayers() {
        return accountTransactionService.initPayers();
    }
}
