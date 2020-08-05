package com.art.controllers;

import com.art.config.application.Location;
import com.art.model.*;
import com.art.model.supporting.AfterCashing;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.SearchSummary;
import com.art.model.supporting.dto.DividedCashDTO;
import com.art.model.supporting.enums.ShareType;
import com.art.model.supporting.enums.TransactionType;
import com.art.model.supporting.filters.CashFilter;
import com.art.service.*;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
@Transactional
public class InvestorCashController {

    private final StatusService statusService;

    private final TransactionLogService transactionLogService;

    private final AfterCashingService afterCashingService;

    private final InvestorCashService investorCashService;

    private final FacilityService facilityService;

    private final UserService userService;

    private final CashSourceService cashSourceService;

    private final NewCashDetailService newCashDetailService;

    private final UnderFacilityService underFacilityService;

    private final TypeClosingService typeClosingService;

    private final InvestorsFlowsService investorsFlowsService;

    private final InvestorsFlowsSaleService investorsFlowsSaleService;

    private final SearchSummary filters = new SearchSummary();

    private final CashFilter cashFilters = new CashFilter();

    public InvestorCashController(InvestorCashService investorCashService,
                                  StatusService statusService, TransactionLogService transactionLogService,
                                  AfterCashingService afterCashingService, FacilityService facilityService,
                                  InvestorsFlowsSaleService investorsFlowsSaleService, UserService userService,
                                  CashSourceService cashSourceService, NewCashDetailService newCashDetailService,
                                  UnderFacilityService underFacilityService,
                                  InvestorsFlowsService investorsFlowsService,
                                  TypeClosingService typeClosingService) {
        this.investorCashService = investorCashService;
        this.statusService = statusService;
        this.transactionLogService = transactionLogService;
        this.afterCashingService = afterCashingService;
        this.facilityService = facilityService;
        this.investorsFlowsSaleService = investorsFlowsSaleService;
        this.userService = userService;
        this.cashSourceService = cashSourceService;
        this.newCashDetailService = newCashDetailService;
        this.underFacilityService = underFacilityService;
        this.investorsFlowsService = investorsFlowsService;
        this.typeClosingService = typeClosingService;
    }

    /**
     * Получить список денег инвесторов
     *
     * @param pageable для постраничного отображения
     * @return список денег инвесторов
     */
    @GetMapping(value = "/investorscash")
    public ModelAndView invCashByPageNumber(@PageableDefault(size = 100) @SortDefault Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("cash-list");
        Page<InvestorCash> page = investorCashService.findAll(cashFilters, pageable);
        modelAndView.addObject("page", page);
        modelAndView.addObject("cashFilters", cashFilters);
        modelAndView.addObject("searchSummary", filters);
        return modelAndView;
    }

    /**
     * Получить список денег инвесторов с фильтрацией
     *
     * @param cashFilters фильтр
     * @return список денег инвесторов
     */
    @PostMapping(value = "/investorscash")
    public ModelAndView invCashPageable(@ModelAttribute(value = "cashFilters") CashFilter cashFilters) {
        Pageable pageable;
        if (cashFilters.getFiltered() == 0) {
            cashFilters.setFiltered(1);
        }
        if (cashFilters.isAllRows()) {
            pageable = new PageRequest(0, Integer.MAX_VALUE);
        } else {
            pageable = new PageRequest(cashFilters.getPageNumber(), cashFilters.getPageSize());
        }
        ModelAndView modelAndView = new ModelAndView("cash-list");
        Page<InvestorCash> page = investorCashService.findAll(cashFilters, pageable);
        modelAndView.addObject("page", page);
        modelAndView.addObject("cashFilters", cashFilters);
        modelAndView.addObject("searchSummary", filters);
        return modelAndView;
    }

    /**
     * Создать сумму инвестора
     *
     * @param model модель со страницы
     * @return страница для создания суммы
     */
    @GetMapping(value = {"/newinvestorscash"})
    public String newCash(ModelMap model) {
        String title = "Добавление денег инвестора";
        InvestorCash investorCash = new InvestorCash();

        model.addAttribute("investorsCash", investorCash);
        model.addAttribute("newCash", true);
        model.addAttribute("edit", false);
        model.addAttribute("doubleCash", false);
        model.addAttribute("closeCash", false);
        model.addAttribute("title", title);
        return "cash-add";
    }

    /**
     * Создать сумму инвестора
     *
     * @param investorCash сумма инвестора
     * @param result для валидации ошибок привязки
     * @param model модель со страницы
     * @return страница успешной операции
     */
    @PostMapping(value = {"/newinvestorscash"})
    public String saveCash(@ModelAttribute("investorsCash") InvestorCash investorCash,
                           BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "cash-add";
        }
        String ret = "списку денег инвестора";
        String redirectUrl = "/investorscash";
        investorCash = investorCashService.create(investorCash);
        transactionLogService.create(investorCash, TransactionType.CREATE);
//        if (null != investorsCash.getCashSource() &&
//                !investorsCash.getCashSource().getName().equalsIgnoreCase("Бронь")) {
//            marketingTreeService.updateMarketingTreeFromApp();
//        }
        model.addAttribute("success", "Деньги инвестора " + investorCash.getInvestor().getLogin() +
                " успешно добавлены.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registration-success";
    }

    /**
     * Обновление суммы инвестора
     *
     * @param id id суммы
     * @param model модель со страницы
     * @return страница для редактирования суммы
     */
    @GetMapping(value = {"/edit-cash-{id}"})
    public String editCash(@PathVariable Long id, ModelMap model) {
        String title = "Обновление данных по деньгам инвесторов";
        InvestorCash investorCash = investorCashService.findById(id);

        model.addAttribute("investorsCash", investorCash);
        model.addAttribute("newCash", false);
        model.addAttribute("edit", true);
        model.addAttribute("closeCash", false);
        model.addAttribute("doubleCash", false);
        model.addAttribute("title", title);
        return "cash-add";
    }

    /**
     * Обновление суммы инвестора
     *
     * @param investorCash сумма для обновления
     * @param id id суммы
     * @return переадресация на страницу отображения денег инвесторов
     */
    @PostMapping(value = "/edit-cash-{id}")
    public String editCash(@ModelAttribute("investorsCash") InvestorCash investorCash, @PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("cash-list");

        InvestorCash dbCash = investorCashService.findById(id);

        transactionLogService.update(dbCash);

        investorCashService.update(investorCash);

        SearchSummary searchSummary = new SearchSummary();
        modelAndView.addObject("searchSummary", searchSummary);
        modelAndView.addObject("investorsCash", investorCashService.findAll());

        return "redirect: /investorscash";
    }

    /**
     * Закрытие суммы вложения
     *
     * @param id id суммы
     * @param model модель со страницы
     * @return страница для закрытия суммы вложения
     */
    @GetMapping(value = {"/close-cash-{id}"})
    public String closeCash(@PathVariable Long id, ModelMap model) {
        String title = "Закрытие вложения";
        InvestorCash investorCash = investorCashService.findById(id);
        investorCash.setGivenCash(investorCash.getGivenCash().setScale(2, RoundingMode.DOWN));
        model.addAttribute("investorsCash", investorCash);

        model.addAttribute("newCash", false);
        model.addAttribute("edit", false);
        model.addAttribute("closeCash", true);
        model.addAttribute("doubleCash", false);
        model.addAttribute("title", title);
        return "cash-add";
    }

    /**
     * Закрытие суммы вложения
     *
     * @param investorCash сумма для закрытия
     * @param id id суммы
     * @param dateClosingInvest дата закрытия
     * @param realDateGiven реальная дата передачи денег
     * @return переадресация на страницу денег инвесторов
     */
    @PostMapping(value = "/close-cash-{id}")
    public String closeCash(@ModelAttribute("investorsCash") InvestorCash investorCash,
                            @PathVariable("id") Long id, @RequestParam("dateClosingInvest") Date dateClosingInvest,
                            @RequestParam("realDateGiven") Date realDateGiven) {
        ModelAndView modelAndView = new ModelAndView("cash-list");

        AppUser invBuyer;
        TypeClosing closingInvest = typeClosingService.findByName("Перепродажа доли");
        NewCashDetail newCashDetail = newCashDetailService.findByName("Перепокупка доли");

        // Перепродажа доли
        if (null != investorCash.getInvestorBuyer()) {
            invBuyer = userService.findById(investorCash.getInvestorBuyer().getId());

            InvestorCash cash = new InvestorCash(investorCashService.findById(investorCash.getId()));
            InvestorCash newInvestorCash = new InvestorCash(investorCashService.findById(investorCash.getId()));
            InvestorCash oldCash = investorCashService.findById(investorCash.getId());
            oldCash.setDateClosing(dateClosingInvest);
            oldCash.setTypeClosing(closingInvest);
            oldCash.setRealDateGiven(realDateGiven);

            cash.setId(null);
            cash.setInvestor(invBuyer);
            cash.setDateGiven(dateClosingInvest);
            cash.setSourceId(investorCash.getId());
            cash.setCashSource(null);
            cash.setSource(null);
            cash.setNewCashDetail(newCashDetail);

            newInvestorCash.setId(null);
            newInvestorCash.setCashSource(null);
            newInvestorCash.setSource(null);
            newInvestorCash.setGivenCash(newInvestorCash.getGivenCash().negate());
            newInvestorCash.setSourceId(investorCash.getId());
            newInvestorCash.setDateGiven(dateClosingInvest);
            newInvestorCash.setDateClosing(dateClosingInvest);
            newInvestorCash.setTypeClosing(closingInvest);

            investorCashService.createNew(cash);
            investorCashService.createNew(newInvestorCash);
            investorCashService.update(oldCash);
            InvestorCash transactionOldCash = new InvestorCash(oldCash);
            transactionOldCash.setId(oldCash.getId());
            Set<InvestorCash> cashSet = new HashSet<>();
            cashSet.add(cash);
            cashSet.add(newInvestorCash);
            cashSet.add(transactionOldCash);
            transactionLogService.resale(Collections.singleton(transactionOldCash), cashSet);
        } else {
            InvestorCash updatedCash = investorCashService.findById(investorCash.getId());
            transactionLogService.close(Collections.singleton(updatedCash));
            updatedCash.setDateClosing(dateClosingInvest);
            updatedCash.setRealDateGiven(realDateGiven);

            updatedCash.setTypeClosing(typeClosingService.findByName("Вывод"));
            investorCashService.update(updatedCash);
        }
        modelAndView.addObject("investorsCash", investorCashService.findAll());
        return "redirect: /investorscash";
    }

    /**
     * Реинвестирование (?) суммы инвестора
     *
     * @param searchSummary сумма для реинвестирования
     * @return ответ об успешном/не успешном реинвестировании суммы
     */
    @PostMapping(value = {"/saveCash"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse saveCash(@RequestBody SearchSummary searchSummary) {

        GenericResponse response = new GenericResponse();
        InvestorCash investorCash = searchSummary.getInvestorCash();

        Facility reFacility = searchSummary.getReFacility();
        UnderFacility reUnderFacility = searchSummary.getReUnderFacility();
        Date reinvestDate = searchSummary.getDateReinvest();

        String whatWeDoWithCash = "обновлены.";

        String invLogin = investorCash.getInvestor().getLogin();

        if (null != reFacility && null != investorCash.getId() &&
                null != investorCash.getNewCashDetail() &&
                (!investorCash.getNewCashDetail().getName().equalsIgnoreCase("Реинвестирование с продажи") &&
                        !investorCash.getNewCashDetail().getName().equalsIgnoreCase("Реинвестирование с аренды")) &&
                (null != searchSummary.getWhat() && !searchSummary.getWhat().equalsIgnoreCase("edit"))) {
            InvestorCash newInvestorCash = new InvestorCash();
            newInvestorCash.setFacility(reFacility);
            newInvestorCash.setSourceFacility(investorCash.getFacility());
            newInvestorCash.setUnderFacility(reUnderFacility);
            newInvestorCash.setInvestor(investorCash.getInvestor());
            newInvestorCash.setGivenCash(investorCash.getGivenCash());
            newInvestorCash.setDateGiven(reinvestDate);
            newInvestorCash.setCashSource(null);
            newInvestorCash.setNewCashDetail(newCashDetailService.findByName("Реинвестирование с продажи"));
            newInvestorCash.setShareType(investorCash.getShareType());
            investorCashService.create(newInvestorCash);
            whatWeDoWithCash = "добавлены.";
        }
        investorCashService.update(investorCash);

        response.setMessage("Деньги инвестора " + invLogin + " успешно " + whatWeDoWithCash);
        return response;
    }

    @GetMapping(value = {"/double-cash-{id}"})
    public String doubleInvCash(@PathVariable Long id, ModelMap model) {
        String title = "Разделение строк по деньгам инвесторов";
        InvestorCash investorCash = investorCashService.findById(id);

        List<UnderFacility> underFacilityList = new ArrayList<>(0);
        UnderFacility underFacility = new UnderFacility();
        underFacility.setId(0L);
        underFacility.setName("Выберите подобъект");
        underFacilityList.add(underFacility);
        underFacilityList.addAll(underFacilityService.findAll()
                .stream()
                .filter(uf -> uf.getFacility().equals(investorCash.getFacility()))
                .collect(Collectors.toList()));

        model.addAttribute("underFacilitiesList", underFacilityList);
        model.addAttribute("investorsCash", investorCash);
        model.addAttribute("newCash", false);
        model.addAttribute("edit", false);
        model.addAttribute("closeCash", false);
        model.addAttribute("doubleCash", true);
        model.addAttribute("title", title);
        return "cash-add";
    }

    @PostMapping(value = "/double-cash-{id}")
    public String doubleCash(@ModelAttribute("investorsCash") InvestorCash investorCash, @PathVariable("id") int id) {
        InvestorCash newInvestorCash = investorCashService.findById(investorCash.getId());
        InvestorCash inMemoryCash = investorCashService.findById(investorCash.getId());
        ModelAndView model = new ModelAndView("cash-add");
        investorCash.setFacility(newInvestorCash.getFacility());
        investorCash.setInvestor(newInvestorCash.getInvestor());
        investorCash.setDateGiven(newInvestorCash.getDateGiven());
        investorCash.setCashSource(newInvestorCash.getCashSource());
        investorCash.setNewCashDetail(newInvestorCash.getNewCashDetail());
        investorCash.setShareType(inMemoryCash.getShareType());

        BigDecimal newSum = newInvestorCash.getGivenCash().subtract(investorCash.getGivenCash());
        newInvestorCash.setGivenCash(investorCash.getGivenCash());
        investorCash.setGivenCash(newSum);
        newInvestorCash.setCashSource(investorCash.getCashSource());
        newInvestorCash.setShareType(investorCash.getShareType());
        newInvestorCash.setId(null);
        newInvestorCash.setSource(investorCash.getId().toString());
        newInvestorCash.setUnderFacility(investorCash.getUnderFacility());
        newInvestorCash.setSourceFacility(investorCash.getSourceFacility());
        newInvestorCash.setDateReport(null);
        investorCash.setUnderFacility(inMemoryCash.getUnderFacility());
        investorCash.setIsDivide(1);

        if (investorCash.getGivenCash().compareTo(BigDecimal.ZERO) == 0) {
            investorCash.setIsReinvest(1);
            investorCash.setIsDivide(1);
        }
        investorCashService.update(newInvestorCash);
        investorCashService.update(investorCash);

        if (investorCash.getGivenCash().compareTo(BigDecimal.ZERO) == 0) {
            return "redirect: /investorscash";
        } else {
            model.addObject("investorsCash", investorCash);
            return model.getViewName();
        }
    }

    @PostMapping(value = {"/deleteCashList"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse deleteCashList(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        List<InvestorCash> listToDelete = investorCashService.findByIdIn(searchSummary.getCashIdList());
        List<AfterCashing> afterCashingList = afterCashingService.findAll();
        Comparator<AfterCashing> comparator = Comparator.comparing(AfterCashing::getId);

        afterCashingList.sort(comparator.reversed());
        final int[] counter = {0};
        int count = listToDelete.size();
        listToDelete.forEach(deleting -> {
            counter[0]++;
            sendStatus(String.format("Удаляем %d из %d сумм", counter[0], count));
            if (!Objects.equals(null, deleting.getSourceFlowsId())) {
                String[] tmp = deleting.getSourceFlowsId().split(Pattern.quote("|"));
                List<BigInteger> sourceIdList = new ArrayList<>(0);
                for (String bigInt : tmp) {
                    sourceIdList.add(new BigInteger(bigInt));
                }

                sourceIdList.forEach(id -> {
                    InvestorsFlows flows = investorsFlowsService.findById(id);
                    InvestorsFlowsSale flowsSale = investorsFlowsSaleService.findById(id);
                    if (!Objects.equals(null, flows)) {
                        flows.setIsReinvest(0);
                        investorsFlowsService.update(flows);
                    }
                    if (!Objects.equals(null, flowsSale)) {
                        flowsSale.setIsReinvest(0);
                        investorsFlowsSaleService.update(flowsSale);
                    }
                });
            }
            if (!Objects.equals(null, deleting.getSource())) {

                String[] tmp = deleting.getSource().split(Pattern.quote("|"));
                List<Long> sourceIdList = new ArrayList<>(tmp.length);
                if (tmp.length > 0 && !tmp[tmp.length - 1].equals("")) {
                    for (String bigInt : tmp) {
                        sourceIdList.add(Long.valueOf(bigInt));
                    }
                }
                sourceIdList.forEach(parentCashId -> {
                    InvestorCash parentCash = investorCashService.findById(parentCashId);
                    if (!Objects.equals(null, parentCash)) {
                        List<AfterCashing> afterCashing = afterCashingList.stream()
                                .filter(ac -> ac.getOldId().equals(parentCashId))
                                .collect(Collectors.toList());
                        if ((!Objects.equals(null, deleting.getTypeClosing()) &&
                                (
                                        deleting.getTypeClosing().getName().equalsIgnoreCase("Вывод") ||
                                                deleting.getTypeClosing().getName().equalsIgnoreCase("Вывод_комиссия")
                                )
                        )) {
                            if (afterCashing.size() > 0) {
                                List<InvestorCash> childCash = investorCashService.findBySource(deleting.getSource());
                                AfterCashing cashToDel = afterCashing.stream()
                                        .filter(ac -> ac.getOldId().equals(parentCashId))
                                        .findFirst().orElse(afterCashing.get(0));
                                parentCash.setGivenCash(cashToDel.getOldValue());
                                childCash.forEach(cbs -> investorCashService.deleteById(cbs.getId()));
                                afterCashingService.deleteById(cashToDel.getId());
                            }
                        }
                        InvestorCash makeDelete =
                                investorCashService.findBySource(parentCash.getId().toString())
                                        .stream()
                                        .filter(m -> !m.getId().equals(deleting.getId()))
                                        .findFirst().orElse(null);
                        if (Objects.equals(null, makeDelete)) {
                            parentCash.setIsReinvest(0);
                            parentCash.setIsDivide(0);
                            parentCash.setTypeClosing(null);
                            parentCash.setDateClosing(null);
                        }

                        if (deleting.getFacility().equals(parentCash.getFacility()) &&
                                deleting.getInvestor().equals(parentCash.getInvestor()) &&
                                deleting.getShareType().equals(parentCash.getShareType()) &&
                                Objects.equals(null, deleting.getTypeClosing()) &&
                                deleting.getDateGiven().compareTo(parentCash.getDateGiven()) == 0) {
                            parentCash.setGivenCash(parentCash.getGivenCash().add(deleting.getGivenCash()));
                        }

                        List<InvestorCash> oldInvCash = investorCashService.findBySourceId(parentCashId);
                        oldInvCash = oldInvCash.stream().filter(oc -> !deleting.getId().equals(oc.getId())).collect(Collectors.toList());
                        if (oldInvCash.size() > 0) {
                            oldInvCash.forEach(oCash -> {
                                parentCash.setGivenCash(parentCash.getGivenCash().add(oCash.getGivenCash()));
                                investorCashService.deleteById(oCash.getId());
                            });
                        }
                        investorCashService.update(parentCash);
                    }

                });
            }

            List<InvestorCash> cash = investorCashService.findBySourceId(deleting.getId());
            if (cash.size() > 0) {
                cash.forEach(ca -> {
                    if (ca.getGivenCash().signum() == -1) {
                        investorCashService.deleteById(ca.getId());
                    } else {
                        ca.setSourceId(null);
                        investorCashService.update(ca);
                    }
                });
            }

            if (!Objects.equals(null, deleting.getSourceId())) {
                List<InvestorCash> investorCashes = investorCashService.findBySourceId(deleting.getSourceId())
                        .stream()
                        .filter(ic -> !Objects.equals(deleting, ic))
                        .collect(Collectors.toList());
                if (!Objects.equals(0, investorCashes.size())) {
                    investorCashes.forEach(investorsCash -> investorCashService.deleteById(investorsCash.getId()));
                }

                InvestorCash parentCash = investorCashService.findById(deleting.getSourceId());
                if (!Objects.equals(null, parentCash)) {
                    parentCash.setIsReinvest(0);
                    parentCash.setIsDivide(0);
                    parentCash.setTypeClosing(null);
                    parentCash.setDateClosing(null);
                    investorCashService.update(parentCash);
                }
            }

            investorCashService.deleteById(deleting.getId());
            response.setMessage("Данные успешно удалены");

        });
        sendStatus("OK");
        return response;
    }

    /**
     * Вывести все деньги по инвесторам
     *
     * @param summary модель с деньгами для вывода
     * @return сообщение об успешном/не успешном выполнении
     */
    @PostMapping(value = "/allMoneyCashing", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse allMoneyCashing(@RequestBody SearchSummary summary) {

        GenericResponse response = new GenericResponse();

        UnderFacility underFacility = null;
        if (!summary.getInvestorCash().getUnderFacility().getName().equals("Выберите подобъект")) {
            underFacility = underFacilityService.findByName(summary.getInvestorCash().getUnderFacility().getName());
        }

        summary.getInvestorCash().setUnderFacility(underFacility);
        String out = investorCashService.cashingAllMoney(summary);

        if (StringUtils.isEmpty(out)) {
            response.setMessage("Деньги инвестора " + summary.getUser().getLogin() + " успешно выведены.");
        } else {
            response.setMessage(out);
        }
        return response;
    }

    /**
     * Страница для вывода денег инвестора
     *
     * @param model модель для страницы
     * @return страница
     */
    @GetMapping(value = {"/getInvestorsCash"})
    public String getCash(ModelMap model) {
        String title = "Вывод денег инвестора";
        SearchSummary searchSummary = new SearchSummary();

        model.addAttribute("searchSummary", searchSummary);
        model.addAttribute("title", title);
        return "getInvestorsCash";
    }

    /**
     * Вывести суммы по ОДНОМУ инвестору
     *
     * @param summary модель для вывода
     * @return сообщение об успешном/не успешном выводе
     */
    @PostMapping(value = "/cashing-money")
    public @ResponseBody
    String cashing(@RequestBody SearchSummary summary) {
        if (null == summary.getUser().getId()) {
            throw new RuntimeException("Отсутствует id пользователя");
        }
        AppUser investor = userService.findById(summary.getUser().getId());
        if (null == investor) {
            throw new RuntimeException("Пользователь с id = [" + summary.getUser().getId() + "] не найден");
        }
        summary.setInvestorsList(Collections.singletonList(investor));
        return investorCashService.cashingMoney(summary);
    }

    /**
     * Вывести суммы по ОДНОМУ инвестору
     *
     * @param summary модель для вывода
     * @return сообщение об успешном/не успешном выводе
     */
    @PostMapping(value = {"/getInvestorsCash"})
    public String cashing(@ModelAttribute("searchSummary") SearchSummary summary,
                          BindingResult result, ModelMap model) {
        AppUser investor = null;
        if (result.hasErrors()) {
            return "getInvestorsCash";
        }
        InvestorCash cash = summary.getInvestorCash();
        if (cash != null) {
            AppUser inv = summary.getInvestorCash().getInvestor();
            if (inv != null) {
                if (inv.getId() != null) {
                    investor = userService.findById(inv.getId());
                } else if (inv.getLogin() != null) {
                    investor = userService.findByLogin(inv.getLogin());
                }
            }
            if (investor == null) {
                investor = inv;
            }
        }
        if (summary.getInvestorsList() == null) {
            summary.setInvestorsList(Collections.singletonList(investor));
        }
        String out = investorCashService.cashingMoney(summary);

        if (StringUtils.isEmpty(out)) {
            Page<InvestorCash> page = investorCashService.findAll(cashFilters, new PageRequest(0, Integer.MAX_VALUE));
            model.addAttribute("page", page);
            model.addAttribute("cashFilters", cashFilters);
            model.addAttribute("searchSummary", filters);
            return "redirect:/investorscash";
        } else {
            model.addAttribute("toBigSumForCashing", out);
            return "getInvestorsCash";
        }
    }

    /**
     * Реинвестирование с продажи/аренды
     *
     * @param searchSummary суммы для реинвестирования
     * @return сообщение об успешном/не успешном реинвестировании
     */
    @PostMapping(value = {"/saveReCash"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse saveReCash(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        List<InvestorCash> investorCashes = searchSummary.getInvestorCashList();
        NewCashDetail newCashDetail;

        // список для создания записи в логе по операции реинвестирования с продажи
        List<InvestorsFlowsSale> flowsSaleList = new ArrayList<>();

        // список для создания записи в логе по операции реинвестирования с аренды
        List<InvestorsFlows> flowsList = new ArrayList<>();


        if ("sale".equals(searchSummary.getWhat())) {
            newCashDetail = newCashDetailService.findByName("Реинвестирование с продажи (прибыль)");
            List<InvestorsFlowsSale> flowsSales = investorsFlowsSaleService.
                    findByIdInWithAllFields(searchSummary.getReinvestIdList());
            flowsSales.forEach(f -> {
                f.setIsReinvest(1);
                investorsFlowsSaleService.update(f);
            });
            flowsSaleList = flowsSales;
        } else {
            newCashDetail = newCashDetailService.findByName("Реинвестирование с аренды");
            List<InvestorsFlows> flows = investorsFlowsService.findByIdIn(searchSummary.getReinvestIdList());
            flows.forEach(f -> f.setIsReinvest(1));
            investorsFlowsService.saveList(flows);
            flowsList = flows;
        }

        NewCashDetail finalNewCashDetail = newCashDetail;

        Map<String, InvestorCash> map = groupInvestorsCash(investorCashes, searchSummary.getWhat());
        try {
            Set<InvestorCash> cashList = new HashSet<>();
            map.forEach((key, value) -> {
                value.setNewCashDetail(finalNewCashDetail);
                value.setGivenCash(value.getGivenCash().setScale(2, RoundingMode.CEILING));
                investorCashService.createNew(value);
                cashList.add(value);
            });

            if (flowsSaleList.size() > 0) {
                transactionLogService.reinvestmentSale(flowsSaleList, cashList);
            } else if (flowsList.size() > 0) {
                transactionLogService.reinvestmentRent(flowsList, cashList);
            }
            response.setMessage("Реинвестирование прошло успешно");

        } catch (Exception ex) {
            if ("sale".equals(searchSummary.getWhat())) {
                List<InvestorsFlowsSale> flowsSales = investorsFlowsSaleService.
                        findByIdInWithAllFields(searchSummary.getReinvestIdList());
                flowsSales.forEach(f -> {
                    f.setIsReinvest(0);
                    investorsFlowsSaleService.update(f);
                });
            } else {
                List<InvestorsFlows> flows = investorsFlowsService.findByIdIn(searchSummary.getReinvestIdList());
                flows.forEach(f -> f.setIsReinvest(0));
                investorsFlowsService.saveList(flows);
            }
            response.setError(ex.getMessage());
        }

        return response;
    }

    /**
     * Реинвестирование с продажи (сохранение)
     *
     * @param searchSummary суммы для реинвестирования
     * @return сообщение об успешном/не успешном реинвестировании
     */
    @PostMapping(value = {"/saveReInvCash"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse saveReInvCash(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        List<InvestorCash> investorCashes = searchSummary.getInvestorCashList();
        final Date[] dateClose = {null};
        final NewCashDetail newCashDetail = newCashDetailService.findByName("Реинвестирование с продажи (сохранение)");
        final TypeClosing typeClosing = typeClosingService.findByName("Реинвестирование");

        final Map<String, InvestorCash> map = groupInvestorsCash(investorCashes, "");

        map.forEach((key, value) -> {
            value.setNewCashDetail(newCashDetail);
            value.setGivenCash(value.getGivenCash().setScale(2, RoundingMode.DOWN));
            dateClose[0] = value.getDateGiven();
            investorCashService.create(value);
        });

        List<InvestorCash> oldCash = investorCashService.findByIdIn(searchSummary.getReinvestIdList());
        final Date finalDateClose = dateClose[0];
        oldCash.forEach(f -> {
            f.setIsReinvest(1);
            f.setDateClosing(finalDateClose);
            f.setTypeClosing(typeClosing);
            investorCashService.create(f);
        });

        response.setMessage("Реинвестирование прошло успешно");

        return response;
    }

    @PostMapping(value = Location.INVESTOR_CASH_DIVIDE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    ApiResponse saveDivideCash(@RequestBody DividedCashDTO dividedCashDTO) {
        ApiResponse response = divideCash(dividedCashDTO);
        sendStatus("OK");
        return response;
    }

    private ApiResponse divideCash(DividedCashDTO dividedCashDTO) {
        // Получаем id сумм, которые надо разделить
        List<Long> idsList = dividedCashDTO.getInvestorCashList();

        // Получаем список денег по идентификаторам
        List<InvestorCash> investorCashes = investorCashService.findByIdIn(idsList);

        List<Long> remainingUnderFacilityList = dividedCashDTO.getExcludedUnderFacilitiesIdList();

        // Получаем подобъект, куда надо разделить сумму
        UnderFacility underFacility = underFacilityService.findById(
                dividedCashDTO.getReUnderFacilityId());

        // Получаем объект, в который надо разделить сумму
        Facility facility = facilityService.findById(underFacility.getFacility().getId());

        // Получаем список подобъектов объекта
        List<UnderFacility> underFacilityList = underFacilityService.findByFacilityId(facility.getId());

        List<Room> rooms = new ArrayList<>(0);

        // Если в списке подобъектов присутствует подобъект, из которого должен состоять остаток суммы, заносим помещения
        // этого подобъекта в список
        underFacilityList.forEach(uf -> remainingUnderFacilityList.forEach(ruf -> {
            if (uf.getId().equals(ruf)) {
                rooms.addAll(uf.getRooms());
            }
        }));

        // Вычисляем стоимость объекта, складывая стоимости помещений, из которых должен состоять остаток
        BigDecimal coastFacility = rooms
                .stream()
                .map(Room::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, BigDecimal.ROUND_CEILING);

        // Вычисляем стоимость подобъекта, куда надо разделить сумму
        BigDecimal coastUnderFacility = underFacility.getRooms().stream()
                .map(Room::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_CEILING);

        // Вычисляем % для выделения доли
        BigDecimal divided = coastUnderFacility.divide(coastFacility, 20, BigDecimal.ROUND_CEILING);
        investorCashes = investorCashes
                .stream()
                .filter(f -> null != f.getGivenCash())
                .collect(Collectors.toList());
        int sumsCnt = investorCashes.size();
        sendStatus("Начинаем разделять суммы");
        final int[] counter = {0};
        investorCashes.forEach(f -> {
            counter[0]++;
            sendStatus(String.format("Разделеляем %d из %d сумм", counter[0], sumsCnt));
            BigDecimal invCash = f.getGivenCash();
            BigDecimal sumInUnderFacility = divided.multiply(invCash);
            BigDecimal sumRemainder = invCash.subtract(sumInUnderFacility);
            f.setIsDivide(1);
            InvestorCash cash = new InvestorCash();
            cash.setSource(f.getId().toString());
            cash.setGivenCash(sumInUnderFacility);
            cash.setDateGiven(f.getDateGiven());
            cash.setFacility(f.getFacility());
            cash.setInvestor(f.getInvestor());
            cash.setCashSource(f.getCashSource());
            cash.setNewCashDetail(f.getNewCashDetail());
            cash.setUnderFacility(underFacility);
            cash.setDateClosing(null);
            cash.setTypeClosing(null);
            cash.setShareType(f.getShareType());
            cash.setDateReport(f.getDateReport());
            cash.setSourceFacility(f.getSourceFacility());
            cash.setSourceUnderFacility(f.getSourceUnderFacility());
            cash.setRoom(f.getRoom());
            f.setGivenCash(sumRemainder);
            if (f.getGivenCash().signum() == 0) {
                f.setIsDivide(1);
                f.setIsReinvest(1);
                investorCashService.update(f);
            } else {
                investorCashService.create(f);
            }

            investorCashService.create(cash);
        });
        return new ApiResponse("Разделение сумм прошло успешно");
    }

    private GenericResponse divideCash(SearchSummary summary) {
        // Получаем id сумм, которые надо разделить
        GenericResponse response = new GenericResponse();
        List<Long> idsList = summary.getInvestorCashList().stream().map(InvestorCash::getId).collect(Collectors.toList());

        // Получаем список денег по идентификаторам
        List<InvestorCash> investorCashes = investorCashService.findByIdIn(idsList);

        List<UnderFacility> remainingUnderFacilityList = summary.getUnderFacilityList();

        // Получаем подобъект, куда надо разделить сумму
        UnderFacility underFacility = underFacilityService.findById(
                summary.getReUnderFacility().getId());

        // Получаем объект, в который надо разделить сумму
        Facility facility = facilityService.findById(underFacility.getFacility().getId());

        // Получаем список подобъектов объекта
        List<UnderFacility> underFacilityList = underFacilityService.findByFacilityId(facility.getId());

        List<Room> rooms = new ArrayList<>(0);

        // Если в списке подобъектов присутствует подобъект, из которого должен состоять остаток суммы, заносим помещения
        // этого подобъекта в список
        underFacilityList.forEach(uf -> remainingUnderFacilityList.forEach(ruf -> {
            if (uf.getId().equals(ruf.getId())) {
                rooms.addAll(uf.getRooms());
            }
        }));

        // Вычисляем стоимость объекта, складывая стоимости помещений, из которых должен состоять остаток
        BigDecimal coastFacility = rooms
                .stream()
                .map(Room::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, BigDecimal.ROUND_CEILING);

        // Вычисляем стоимость подобъекта, куда надо разделить сумму
        BigDecimal coastUnderFacility = underFacility.getRooms().stream()
                .map(Room::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_CEILING);

        // Вычисляем % для выделения доли
        BigDecimal divided = coastUnderFacility.divide(coastFacility, 20, BigDecimal.ROUND_CEILING);
        investorCashes = investorCashes
                .stream()
                .filter(f -> null != f.getGivenCash())
                .collect(Collectors.toList());
        int sumsCnt = investorCashes.size();
        sendStatus("Начинаем разделять суммы");
        final int[] counter = {0};
        investorCashes.forEach(f -> {
            counter[0]++;
            sendStatus(String.format("Разделеляем %d из %d сумм", counter[0], sumsCnt));
            BigDecimal invCash = f.getGivenCash();
            BigDecimal sumInUnderFacility = divided.multiply(invCash);
            BigDecimal sumRemainder = invCash.subtract(sumInUnderFacility);
            f.setIsDivide(1);
            InvestorCash cash = new InvestorCash();
            cash.setSource(f.getId().toString());
            cash.setGivenCash(sumInUnderFacility);
            cash.setDateGiven(f.getDateGiven());
            cash.setFacility(f.getFacility());
            cash.setInvestor(f.getInvestor());
            cash.setCashSource(f.getCashSource());
            cash.setNewCashDetail(f.getNewCashDetail());
            cash.setUnderFacility(underFacility);
            cash.setDateClosing(null);
            cash.setTypeClosing(null);
            cash.setShareType(f.getShareType());
            cash.setDateReport(f.getDateReport());
            cash.setSourceFacility(f.getSourceFacility());
            cash.setSourceUnderFacility(f.getSourceUnderFacility());
            cash.setRoom(f.getRoom());
            f.setGivenCash(sumRemainder);
            if (f.getGivenCash().signum() == 0) {
                f.setIsDivide(1);
                f.setIsReinvest(1);
                investorCashService.update(f);
            } else {
                investorCashService.create(f);
            }

            investorCashService.create(cash);
        });
        response.setMessage("Разделение сумм прошло успешно");
        return response;
    }

    private void sendStatus(String message) {
        statusService.sendStatus(message);
    }

    @PostMapping(value = Location.INVESTOR_CASH_DIVIDE_MULTIPLE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    ApiResponse divideMultipleCash(@RequestBody DividedCashDTO dividedCashDTO) {
        final ApiResponse[] response = {new ApiResponse()};

        // Получаем подобъекты, куда надо выделить долю
        List<Long> reUnderFacilityList = dividedCashDTO.getReUnderFacilitiesIdList();

        // Получаем подобъекты, которые будут использоваться для расчёта процентного соотношения разделения
        List<Long> underFacilityToCalculateShare = dividedCashDTO.getExcludedUnderFacilitiesIdList();
        final int[] counter = {0};
        int ufCount = underFacilityToCalculateShare.size();
        reUnderFacilityList.forEach(reUnderFacility -> {
            counter[0]++;
            sendStatus(String.format("Разделяем %d из %d подобъектов", counter[0], ufCount));
            dividedCashDTO.setReUnderFacilityId(reUnderFacility);
            response[0] = divideCash(dividedCashDTO);
            underFacilityToCalculateShare.remove(reUnderFacility);
            dividedCashDTO.setReUnderFacilitiesIdList(underFacilityToCalculateShare);
        });
        sendStatus("OK");
        return response[0];
    }

    /**
     * Массовое закрытие сумм
     *
     * @param searchSummary список сумм для закрытия
     * @return сообщение об успешном/не успешном массовом закрытии
     */
    @PostMapping(value = {"/closeCash"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse closeCash(@RequestBody SearchSummary searchSummary) {
        AppUser invBuyer = null;
        if (null != searchSummary.getUser()) {
            invBuyer = userService.findById(searchSummary.getUser().getId());
        }

        List<InvestorCash> cashList = new ArrayList<>(0);
        searchSummary.getCashIdList().forEach(id -> cashList.add(investorCashService.findById(id)));

        GenericResponse response = new GenericResponse();

        Date dateClose = searchSummary.getDateReinvest();
        Date realDateGiven = searchSummary.getRealDateGiven();
        AppUser finalInvBuyer = invBuyer;
        // список сумм, которые закрываем для вывода
        Set<InvestorCash> closeCashes = new HashSet<>();
        // список сумм, которые закрываем для перепродажи доли
        Set<InvestorCash> oldCashes = new HashSet<>();
        // список сумм, которые получатся на выходе
        Set<InvestorCash> newCashes = new HashSet<>();
        TypeClosing closingInvest = typeClosingService.findByName("Перепродажа доли");
        NewCashDetail newCashDetail = newCashDetailService.findByName("Перепокупка доли");

        cashList.forEach(c -> {
            if (null != finalInvBuyer) { // Перепродажа доли
                InvestorCash copyCash = new InvestorCash(c);
                InvestorCash newInvestorCash = new InvestorCash(c);

                copyCash.setInvestor(finalInvBuyer);
                copyCash.setDateGiven(dateClose);
                copyCash.setSourceId(c.getId());
                copyCash.setCashSource(null);
                copyCash.setSource(null);
                copyCash.setNewCashDetail(newCashDetail);
                copyCash.setRealDateGiven(realDateGiven);

                copyCash = investorCashService.createNew(copyCash);

                newInvestorCash.setCashSource(null);
                newInvestorCash.setGivenCash(newInvestorCash.getGivenCash().negate());
                newInvestorCash.setSourceId(c.getId());
                newInvestorCash.setSource(null);
                newInvestorCash.setDateGiven(dateClose);
                newInvestorCash.setDateClosing(dateClose);
                newInvestorCash.setTypeClosing(closingInvest);

                investorCashService.createNew(newInvestorCash);

                c.setDateClosing(dateClose);
                c.setTypeClosing(closingInvest);
                investorCashService.update(c);
                oldCashes.add(c);
                newCashes.add(c);
                newCashes.add(copyCash);
                newCashes.add(newInvestorCash);
            } else {
                InvestorCash cashForTx = new InvestorCash(c);
                cashForTx.setId(c.getId());
                c.setDateClosing(dateClose);
                c.setTypeClosing(typeClosingService.findByName("Вывод"));
                c.setRealDateGiven(realDateGiven);
                investorCashService.update(c);
                closeCashes.add(cashForTx);
            }
        });
        if (closeCashes.size() > 0) {
            transactionLogService.close(closeCashes);
        } else {
            transactionLogService.resale(oldCashes, newCashes);
        }

        response.setMessage("Массовое закрытие прошло успешно.");
        return response;
    }

    private Map<String, InvestorCash> groupInvestorsCash(List<InvestorCash> cashList, String what) {
        Map<String, InvestorCash> map = new HashMap<>(0);

        cashList.forEach(ic -> {
            InvestorCash keyMap;
            if ("sale".equals(what)) {
                keyMap = map.get(ic.getInvestor().getLogin() +
                        ic.getSourceUnderFacility().getName());
            } else {
                keyMap = map.get(ic.getInvestor().getLogin() + ic.getSourceFacility().getName());
            }

            if (Objects.equals(null, keyMap)) {
                if ("sale".equals(what)) {
                    map.put(ic.getInvestor().getLogin() +
                                    ic.getSourceUnderFacility().getName(),
                            ic);
                } else {
                    map.put(ic.getInvestor().getLogin() + ic.getSourceFacility().getName(),
                            ic);
                }

            } else {
                InvestorCash cash = new InvestorCash();
                cash.setGivenCash(ic.getGivenCash().add(keyMap.getGivenCash()));
                cash.setSource(ic.getSource());
                cash.setDateGiven(ic.getDateGiven());
                cash.setFacility(ic.getFacility());
                cash.setUnderFacility(ic.getUnderFacility());
                cash.setInvestor(ic.getInvestor());
                cash.setShareType(ic.getShareType());
                cash.setDateReport(ic.getDateReport());
                cash.setSourceFacility(ic.getSourceFacility());
                cash.setSourceUnderFacility(ic.getSourceUnderFacility());
                if (!Objects.equals(null, ic.getSource()) && !Objects.equals(null, keyMap.getSource())) {
                    cash.setSource(ic.getSource() + "|" + keyMap.getSource());
                }
                if (!Objects.equals(null, ic.getSourceFlowsId()) && !Objects.equals(null, keyMap.getSourceFlowsId())) {
                    cash.setSourceFlowsId(ic.getSourceFlowsId() + "|" + keyMap.getSourceFlowsId());
                }
                if ("sale".equals(what)) {
                    map.put(ic.getInvestor().getLogin() +
                            ic.getSourceUnderFacility().getName(), cash);
                } else {
                    map.put(ic.getInvestor().getLogin() + ic.getSourceFacility().getName(), cash);
                }

            }
        });

        return map;
    }

    @ModelAttribute("facilities")
    public List<Facility> initializeFacilities() {
        return facilityService.initializeFacilities();
    }

    @ModelAttribute("facilitiesList")
    public List<Facility> initializeMultipleFacilities() {
        return facilityService.initializeFacilitiesForMultiple();
    }

    @ModelAttribute("sourceFacilities")
    public List<Facility> initializeReFacilities() {
        return facilityService.initializeFacilities();
    }

    @ModelAttribute("investors")
    public List<AppUser> initializeInvestors() {
        return userService.initializeInvestors();
    }

    @ModelAttribute("investorsMulti")
    public List<AppUser> initializeInvestorsMultiple() {
        return userService.initializeMultipleInvestors();
    }

    @ModelAttribute("cashSources")
    public List<CashSource> initializeCashSources() {
        return cashSourceService.initializeCashSources();
    }

    @ModelAttribute("newCashDetails")
    public List<NewCashDetail> initializeNewCashDetails() {
        return newCashDetailService.initializeNewCashDetails();
    }

    @ModelAttribute("underFacilities")
    public List<UnderFacility> initializeUnderFacilities() {
        return underFacilityService.initializeUnderFacilities();
    }

    @ModelAttribute("underFacilitiesList")
    public List<UnderFacility> initializeUnderFacilitiesList() {
        return underFacilityService.initializeUnderFacilitiesList();
    }

    @ModelAttribute("sourceUnderFacilities")
    public List<UnderFacility> initializeReUnderFacilities() {
        return underFacilityService.initializeUnderFacilities();
    }

    @ModelAttribute("typeClosingInvest")
    public List<TypeClosing> initializeTypeClosingInvest() {
        return typeClosingService.init();
    }

    @ModelAttribute("shareTypes")
    public List<ShareType> initializeShareTypes() {
        return Arrays.asList(ShareType.values());
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @ModelAttribute("searchSummary")
    public SearchSummary addSearchSummary() {
        return filters;
    }

    @ModelAttribute("summary")
    public SearchSummary addSummary() {
        return filters;
    }

    @ModelAttribute("search")
    public SearchSummary addSearch() {
        return filters;
    }
}
