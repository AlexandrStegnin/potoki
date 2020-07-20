package com.art.controllers;

import com.art.model.*;
import com.art.model.supporting.AfterCashing;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.SearchSummary;
import com.art.model.supporting.enums.ShareType;
import com.art.model.supporting.enums.TransactionType;
import com.art.model.supporting.filters.CashFilter;
import com.art.service.*;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
@Transactional
public class InvestorsCashController {

    @Autowired
    private MarketingTreeService marketingTreeService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private TransactionLogService transactionLogService;

    @Resource(name = "afterCashingService")
    private AfterCashingService afterCashingService;

    @Resource(name = "investorsCashService")
    private InvestorsCashService investorsCashService;

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "cashSourceService")
    private CashSourceService cashSourceService;

    @Resource(name = "newCashDetailsService")
    private NewCashDetailsService newCashDetailsService;

    @Resource(name = "underFacilityService")
    private UnderFacilityService underFacilityService;

    @Resource(name = "typeClosingInvestService")
    private TypeClosingInvestService typeClosingInvestService;

    @Resource(name = "investorsFlowsService")
    private InvestorsFlowsService investorsFlowsService;

    @Resource(name = "investorsFlowsSaleService")
    private InvestorsFlowsSaleService investorsFlowsSaleService;

    private final SearchSummary filters = new SearchSummary();

    private final CashFilter cashFilters = new CashFilter();

    /**
     * Получить список денег инвесторов
     *
     * @param pageable для постраничного отображения
     * @return список денег инвесторов
     */
    @GetMapping(value = "/investorscash")
    public ModelAndView invCashByPageNumber(@PageableDefault(size = 100) @SortDefault Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("viewinvestorscash");
        Page<InvestorsCash> page = investorsCashService.findAll(cashFilters, pageable);
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
        ModelAndView modelAndView = new ModelAndView("viewinvestorscash");
        Page<InvestorsCash> page = investorsCashService.findAll(cashFilters, pageable);
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
        InvestorsCash investorsCash = new InvestorsCash();

        model.addAttribute("investorsCash", investorsCash);
        model.addAttribute("newCash", true);
        model.addAttribute("edit", false);
        model.addAttribute("doubleCash", false);
        model.addAttribute("closeCash", false);
        model.addAttribute("title", title);
        return "addinvestorscash";
    }

    /**
     * Создать сумму инвестора
     *
     * @param investorsCash сумма инвестора
     * @param result для валидации ошибок привязки
     * @param model модель со страницы
     * @return страница успешной операции
     */
    @PostMapping(value = {"/newinvestorscash"})
    public String saveCash(@ModelAttribute("investorsCash") InvestorsCash investorsCash,
                           BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "addinvestorscash";
        }
        String ret = "списку денег инвестора";
        String redirectUrl = "/investorscash";
        investorsCash = investorsCashService.create(investorsCash);
        transactionLogService.create(investorsCash, TransactionType.CREATE);
        if (null != investorsCash.getCashSource() &&
                !investorsCash.getCashSource().getName().equalsIgnoreCase("Бронь")) {
            marketingTreeService.updateMarketingTreeFromApp();
        }
        model.addAttribute("success", "Деньги инвестора " + investorsCash.getInvestor().getLogin() +
                " успешно добавлены.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
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
        InvestorsCash investorsCash = investorsCashService.findById(id);

        model.addAttribute("investorsCash", investorsCash);
        model.addAttribute("newCash", false);
        model.addAttribute("edit", true);
        model.addAttribute("closeCash", false);
        model.addAttribute("doubleCash", false);
        model.addAttribute("title", title);
        return "addinvestorscash";
    }

    /**
     * Обновление суммы инвестора
     *
     * @param investorsCash сумма для обновления
     * @param id id суммы
     * @return переадресация на страницу отображения денег инвесторов
     */
    @PostMapping(value = "/edit-cash-{id}")
    public String editCash(@ModelAttribute("investorsCash") InvestorsCash investorsCash, @PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("viewinvestorscash");

        InvestorsCash dbCash = investorsCashService.findById(id);

        transactionLogService.update(dbCash);

        investorsCashService.update(investorsCash);

        SearchSummary searchSummary = new SearchSummary();
        modelAndView.addObject("searchSummary", searchSummary);
        modelAndView.addObject("investorsCash", investorsCashService.findAll());

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
        InvestorsCash investorsCash = investorsCashService.findById(id);
        investorsCash.setGivedCash(investorsCash.getGivedCash().setScale(2, RoundingMode.DOWN));
        model.addAttribute("investorsCash", investorsCash);

        model.addAttribute("newCash", false);
        model.addAttribute("edit", false);
        model.addAttribute("closeCash", true);
        model.addAttribute("doubleCash", false);
        model.addAttribute("title", title);
        return "addinvestorscash";
    }

    /**
     * Закрытие суммы вложения
     *
     * @param investorsCash сумма для закрытия
     * @param id id суммы
     * @param dateClosingInvest дата закрытия
     * @param realDateGiven реальная дата передачи денег
     * @return переадресация на страницу денег инвесторов
     */
    @PostMapping(value = "/close-cash-{id}")
    public String closeCash(@ModelAttribute("investorsCash") InvestorsCash investorsCash,
                            @PathVariable("id") int id, @RequestParam("dateClosingInvest") Date dateClosingInvest,
                            @RequestParam("realDateGiven") Date realDateGiven) {
        ModelAndView modelAndView = new ModelAndView("viewinvestorscash");

        AppUser invBuyer;
        TypeClosingInvest closingInvest = typeClosingInvestService.findByTypeClosingInvest("Перепродажа доли");
        NewCashDetails newCashDetails = newCashDetailsService.findByNewCashDetail("Перепокупка доли");

        // Перепродажа доли
        if (null != investorsCash.getInvestorBuyer()) {
            invBuyer = userService.findById(investorsCash.getInvestorBuyer().getId());

            InvestorsCash cash = investorsCashService.findById(investorsCash.getId());
            InvestorsCash newInvestorsCash = investorsCashService.findById(investorsCash.getId());
            InvestorsCash oldCash = investorsCashService.findById(investorsCash.getId());
            oldCash.setDateClosingInvest(dateClosingInvest);
            oldCash.setTypeClosingInvest(closingInvest);
            oldCash.setRealDateGiven(realDateGiven);

            cash.setId(null);
            cash.setInvestor(invBuyer);
            cash.setDateGivedCash(dateClosingInvest);
            cash.setSourceId(investorsCash.getId());
            cash.setCashSource(null);
            cash.setSource(null);
            cash.setNewCashDetails(newCashDetails);

            newInvestorsCash.setId(null);
            newInvestorsCash.setCashSource(null);
            newInvestorsCash.setSource(null);
            newInvestorsCash.setGivedCash(newInvestorsCash.getGivedCash().negate());
            newInvestorsCash.setSourceId(investorsCash.getId());
            newInvestorsCash.setDateGivedCash(dateClosingInvest);
            newInvestorsCash.setDateClosingInvest(dateClosingInvest);
            newInvestorsCash.setTypeClosingInvest(closingInvest);

            investorsCashService.createNew(cash);
            investorsCashService.createNew(newInvestorsCash);
            investorsCashService.update(oldCash);
            Set<InvestorsCash> cashSet = new HashSet<>();
            cashSet.add(cash);
            cashSet.add(newInvestorsCash);
            cashSet.add(oldCash);
            transactionLogService.resale(Collections.singleton(oldCash), cashSet);
        } else {
            InvestorsCash updatedCash = investorsCashService.findById(investorsCash.getId());
            transactionLogService.close(Collections.singleton(updatedCash));
            updatedCash.setDateClosingInvest(dateClosingInvest);
            updatedCash.setRealDateGiven(realDateGiven);

            updatedCash.setTypeClosingInvest(typeClosingInvestService.findByTypeClosingInvest("Вывод"));
            investorsCashService.update(updatedCash);
        }
        modelAndView.addObject("investorsCash", investorsCashService.findAll());
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
        InvestorsCash investorsCash = searchSummary.getInvestorsCash();

        Facility reFacility = searchSummary.getReFacility();
        UnderFacility reUnderFacility = searchSummary.getReUnderFacility();
        Date reinvestDate = searchSummary.getDateReinvest();

        String whatWeDoWithCash = "обновлены.";

        String invLogin = investorsCash.getInvestor().getLogin();

        if (null != reFacility && null != investorsCash.getId() &&
                null != investorsCash.getNewCashDetails() &&
                (!investorsCash.getNewCashDetails().getNewCashDetail().equalsIgnoreCase("Реинвестирование с продажи") &&
                        !investorsCash.getNewCashDetails().getNewCashDetail().equalsIgnoreCase("Реинвестирование с аренды")) &&
                (null != searchSummary.getWhat() && !searchSummary.getWhat().equalsIgnoreCase("edit"))) {
            InvestorsCash newInvestorsCash = new InvestorsCash();
            newInvestorsCash.setFacility(reFacility);
            newInvestorsCash.setSourceFacility(investorsCash.getFacility());
            newInvestorsCash.setUnderFacility(reUnderFacility);
            newInvestorsCash.setInvestor(investorsCash.getInvestor());
            newInvestorsCash.setGivedCash(investorsCash.getGivedCash());
            newInvestorsCash.setDateGivedCash(reinvestDate);
            newInvestorsCash.setCashSource(null);
            newInvestorsCash.setNewCashDetails(newCashDetailsService.findByNewCashDetail("Реинвестирование с продажи"));
            newInvestorsCash.setShareType(investorsCash.getShareType());
            investorsCashService.create(newInvestorsCash);
            whatWeDoWithCash = "добавлены.";
        }
        investorsCashService.update(investorsCash);

        response.setMessage("Деньги инвестора " + invLogin + " успешно " + whatWeDoWithCash);
        return response;
    }

    @GetMapping(value = {"/double-cash-{id}"})
    public String doubleInvCash(@PathVariable Long id, ModelMap model) {
        String title = "Разделение строк по деньгам инвесторов";
        InvestorsCash investorsCash = investorsCashService.findById(id);

        List<UnderFacility> underFacilityList = new ArrayList<>(0);
        UnderFacility underFacility = new UnderFacility();
        underFacility.setId(0L);
        underFacility.setName("Выберите подобъект");
        underFacilityList.add(underFacility);
        underFacilityList.addAll(underFacilityService.findAll()
                .stream()
                .filter(uf -> uf.getFacility().equals(investorsCash.getFacility()))
                .collect(Collectors.toList()));

        model.addAttribute("underFacilitiesList", underFacilityList);
        model.addAttribute("investorsCash", investorsCash);
        model.addAttribute("newCash", false);
        model.addAttribute("edit", false);
        model.addAttribute("closeCash", false);
        model.addAttribute("doubleCash", true);
        model.addAttribute("title", title);
        return "addinvestorscash";
    }

    @PostMapping(value = "/double-cash-{id}")
    public String doubleCash(@ModelAttribute("investorsCash") InvestorsCash investorsCash, @PathVariable("id") int id) {
        InvestorsCash newInvestorsCash = investorsCashService.findById(investorsCash.getId());
        InvestorsCash inMemoryCash = investorsCashService.findById(investorsCash.getId());
        ModelAndView model = new ModelAndView("addinvestorscash");
        investorsCash.setFacility(newInvestorsCash.getFacility());
        investorsCash.setInvestor(newInvestorsCash.getInvestor());
        investorsCash.setDateGivedCash(newInvestorsCash.getDateGivedCash());
        investorsCash.setCashSource(newInvestorsCash.getCashSource());
        investorsCash.setNewCashDetails(newInvestorsCash.getNewCashDetails());
        investorsCash.setShareType(inMemoryCash.getShareType());

        BigDecimal newSum = newInvestorsCash.getGivedCash().subtract(investorsCash.getGivedCash());
        newInvestorsCash.setGivedCash(investorsCash.getGivedCash());
        investorsCash.setGivedCash(newSum);
        newInvestorsCash.setCashSource(investorsCash.getCashSource());
        newInvestorsCash.setShareType(investorsCash.getShareType());
        newInvestorsCash.setId(null);
        newInvestorsCash.setSource(investorsCash.getId().toString());
        newInvestorsCash.setUnderFacility(investorsCash.getUnderFacility());
        newInvestorsCash.setSourceFacility(investorsCash.getSourceFacility());
        newInvestorsCash.setDateReport(null);
        investorsCash.setUnderFacility(inMemoryCash.getUnderFacility());
        investorsCash.setIsDivide(1);

        if (investorsCash.getGivedCash().compareTo(BigDecimal.ZERO) == 0) {
            investorsCash.setIsReinvest(1);
            investorsCash.setIsDivide(1);
        }
        investorsCashService.update(newInvestorsCash);
        investorsCashService.update(investorsCash);

        if (investorsCash.getGivedCash().compareTo(BigDecimal.ZERO) == 0) {
            return "redirect: /investorscash";
        } else {
            model.addObject("investorsCash", investorsCash);
            return model.getViewName();
        }
    }

    @PostMapping(value = {"/deleteCashList"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse deleteCashList(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        List<InvestorsCash> listToDelete = investorsCashService.findByIdIn(searchSummary.getCashIdList());
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
                    InvestorsCash parentCash = investorsCashService.findById(parentCashId);
                    if (!Objects.equals(null, parentCash)) {
                        List<AfterCashing> afterCashing = afterCashingList.stream()
                                .filter(ac -> ac.getOldId().equals(parentCashId))
                                .collect(Collectors.toList());
                        if ((!Objects.equals(null, deleting.getTypeClosingInvest()) &&
                                (
                                        deleting.getTypeClosingInvest().getTypeClosingInvest().equalsIgnoreCase("Вывод") ||
                                                deleting.getTypeClosingInvest().getTypeClosingInvest().equalsIgnoreCase("Вывод_комиссия")
                                )
                        )) {
                            if (afterCashing.size() > 0) {
                                List<InvestorsCash> childCash = investorsCashService.findBySource(deleting.getSource());
                                AfterCashing cashToDel = afterCashing.stream()
                                        .filter(ac -> ac.getOldId().equals(parentCashId))
                                        .findFirst().orElse(afterCashing.get(0));
                                parentCash.setGivedCash(cashToDel.getOldValue());
                                childCash.forEach(cbs -> investorsCashService.deleteById(cbs.getId()));
                                afterCashingService.deleteById(cashToDel.getId());
                            }
                        }
                        InvestorsCash makeDelete =
                                investorsCashService.findBySource(parentCash.getId().toString())
                                        .stream()
                                        .filter(m -> !m.getId().equals(deleting.getId()))
                                        .findFirst().orElse(null);
                        if (Objects.equals(null, makeDelete)) {
                            parentCash.setIsReinvest(0);
                            parentCash.setIsDivide(0);
                            parentCash.setTypeClosingInvest(null);
                            parentCash.setDateClosingInvest(null);
                        }

                        if (deleting.getFacility().equals(parentCash.getFacility()) &&
                                deleting.getInvestor().equals(parentCash.getInvestor()) &&
                                deleting.getShareType().equals(parentCash.getShareType()) &&
                                Objects.equals(null, deleting.getTypeClosingInvest()) &&
                                deleting.getDateGivedCash().compareTo(parentCash.getDateGivedCash()) == 0) {
                            parentCash.setGivedCash(parentCash.getGivedCash().add(deleting.getGivedCash()));
                        }

                        List<InvestorsCash> oldInvCash = investorsCashService.findBySourceId(parentCashId);
                        oldInvCash = oldInvCash.stream().filter(oc -> !deleting.getId().equals(oc.getId())).collect(Collectors.toList());
                        if (oldInvCash.size() > 0) {
                            oldInvCash.forEach(oCash -> {
                                parentCash.setGivedCash(parentCash.getGivedCash().add(oCash.getGivedCash()));
                                investorsCashService.deleteById(oCash.getId());
                            });
                        }
                        investorsCashService.update(parentCash);
                    }

                });
            }

            List<InvestorsCash> cash = investorsCashService.findBySourceId(deleting.getId());
            if (cash.size() > 0) {
                cash.forEach(ca -> {
                    if (ca.getGivedCash().signum() == -1) {
                        investorsCashService.deleteById(ca.getId());
                    } else {
                        ca.setSourceId(null);
                        investorsCashService.update(ca);
                    }
                });
            }

            if (!Objects.equals(null, deleting.getSourceId())) {
                List<InvestorsCash> investorsCashes = investorsCashService.findBySourceId(deleting.getSourceId())
                        .stream()
                        .filter(ic -> !Objects.equals(deleting, ic))
                        .collect(Collectors.toList());
                if (!Objects.equals(0, investorsCashes.size())) {
                    investorsCashes.forEach(investorsCash -> investorsCashService.deleteById(investorsCash.getId()));
                }

                InvestorsCash parentCash = investorsCashService.findById(deleting.getSourceId());
                if (!Objects.equals(null, parentCash)) {
                    parentCash.setIsReinvest(0);
                    parentCash.setIsDivide(0);
                    parentCash.setTypeClosingInvest(null);
                    parentCash.setDateClosingInvest(null);
                    investorsCashService.update(parentCash);
                }
            }

            investorsCashService.deleteById(deleting.getId());
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
        if (!summary.getInvestorsCash().getUnderFacility().getName().equals("Выберите подобъект")) {
            underFacility = underFacilityService.findByName(summary.getInvestorsCash().getUnderFacility().getName());
        }

        summary.getInvestorsCash().setUnderFacility(underFacility);
        String out = investorsCashService.cashingAllMoney(summary);

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
        return investorsCashService.cashingMoney(summary);
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
        InvestorsCash cash = summary.getInvestorsCash();
        if (cash != null) {
            AppUser inv = summary.getInvestorsCash().getInvestor();
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
        String out = investorsCashService.cashingMoney(summary);

        if (StringUtils.isEmpty(out)) {
            Page<InvestorsCash> page = investorsCashService.findAll(cashFilters, new PageRequest(0, Integer.MAX_VALUE));
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
        List<InvestorsCash> investorsCashes = searchSummary.getInvestorsCashList();
        NewCashDetails newCashDetails;

        // список для создания записи в логе по операции реинвестирования с продажи
        List<InvestorsFlowsSale> flowsSaleList = new ArrayList<>();

        // список для создания записи в логе по операции реинвестирования с аренды
        List<InvestorsFlows> flowsList = new ArrayList<>();


        if ("sale".equals(searchSummary.getWhat())) {
            newCashDetails = newCashDetailsService.findByNewCashDetail("Реинвестирование с продажи (прибыль)");
            List<InvestorsFlowsSale> flowsSales = investorsFlowsSaleService.
                    findByIdInWithAllFields(searchSummary.getReinvestIdList());
            flowsSales.forEach(f -> {
                f.setIsReinvest(1);
                investorsFlowsSaleService.update(f);
            });
            flowsSaleList = flowsSales;
        } else {
            newCashDetails = newCashDetailsService.findByNewCashDetail("Реинвестирование с аренды");
            List<InvestorsFlows> flows = investorsFlowsService.findByIdIn(searchSummary.getReinvestIdList());
            flows.forEach(f -> f.setIsReinvest(1));
            investorsFlowsService.saveList(flows);
            flowsList = flows;
        }

        NewCashDetails finalNewCashDetails = newCashDetails;

        Map<String, InvestorsCash> map = groupInvestorsCash(investorsCashes, searchSummary.getWhat());
        try {
            Set<InvestorsCash> cashList = new HashSet<>();
            map.forEach((key, value) -> {
                value.setNewCashDetails(finalNewCashDetails);
                value.setGivedCash(value.getGivedCash().setScale(2, RoundingMode.CEILING));
                investorsCashService.createNew(value);
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
        List<InvestorsCash> investorsCashes = searchSummary.getInvestorsCashList();
        final Date[] dateClose = {null};
        final NewCashDetails newCashDetails = newCashDetailsService.findByNewCashDetail("Реинвестирование с продажи (сохранение)");
        final TypeClosingInvest typeClosingInvest = typeClosingInvestService.findByTypeClosingInvest("Реинвестирование");

        final Map<String, InvestorsCash> map = groupInvestorsCash(investorsCashes, "");

        map.forEach((key, value) -> {
            value.setNewCashDetails(newCashDetails);
            value.setGivedCash(value.getGivedCash().setScale(2, RoundingMode.DOWN));
            dateClose[0] = value.getDateGivedCash();
            investorsCashService.create(value);
        });

        List<InvestorsCash> oldCash = investorsCashService.findByIdIn(searchSummary.getReinvestIdList());
        final Date finalDateClose = dateClose[0];
        oldCash.forEach(f -> {
            f.setIsReinvest(1);
            f.setDateClosingInvest(finalDateClose);
            f.setTypeClosingInvest(typeClosingInvest);
            investorsCashService.create(f);
        });

        response.setMessage("Реинвестирование прошло успешно");

        return response;
    }

    @PostMapping(value = "/saveDivideCash", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    GenericResponse saveDivideCash(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = divideCash(searchSummary);
        sendStatus("OK");
        return response;
    }

    private GenericResponse divideCash(SearchSummary summary) {
        // Получаем id сумм, которые надо разделить
        GenericResponse response = new GenericResponse();
        List<Long> idsList = summary.getInvestorsCashList().stream().map(InvestorsCash::getId).collect(Collectors.toList());

        // Получаем список денег по идентификаторам
        List<InvestorsCash> investorsCashes = investorsCashService.findByIdIn(idsList);

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
        investorsCashes = investorsCashes
                .stream()
                .filter(f -> null != f.getGivedCash())
                .collect(Collectors.toList());
        int sumsCnt = investorsCashes.size();
        sendStatus("Начинаем разделять суммы");
        final int[] counter = {0};
        investorsCashes.forEach(f -> {
            counter[0]++;
            sendStatus(String.format("Разделеляем %d из %d сумм", counter[0], sumsCnt));
            BigDecimal invCash = f.getGivedCash();
            BigDecimal sumInUnderFacility = divided.multiply(invCash);
            BigDecimal sumRemainder = invCash.subtract(sumInUnderFacility);
            f.setIsDivide(1);
            InvestorsCash cash = new InvestorsCash();
            cash.setSource(f.getId().toString());
            cash.setGivedCash(sumInUnderFacility);
            cash.setDateGivedCash(f.getDateGivedCash());
            cash.setFacility(f.getFacility());
            cash.setInvestor(f.getInvestor());
            cash.setCashSource(f.getCashSource());
            cash.setNewCashDetails(f.getNewCashDetails());
            cash.setUnderFacility(underFacility);
            cash.setDateClosingInvest(null);
            cash.setTypeClosingInvest(null);
            cash.setShareType(f.getShareType());
            cash.setDateReport(f.getDateReport());
            cash.setSourceFacility(f.getSourceFacility());
            cash.setSourceUnderFacility(f.getSourceUnderFacility());
            cash.setRoom(f.getRoom());
            f.setGivedCash(sumRemainder);
            if (f.getGivedCash().signum() == 0) {
                f.setIsDivide(1);
                f.setIsReinvest(1);
                investorsCashService.update(f);
            } else {
                investorsCashService.create(f);
            }

            investorsCashService.create(cash);
        });
        response.setMessage("Разделение сумм прошло успешно");
        return response;
    }

    private void sendStatus(String message) {
        statusService.sendStatus(message);
    }

    @PostMapping(value = "/divide-multiple", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    GenericResponse divideMultipleCash(@RequestBody SearchSummary summary) {
        final GenericResponse[] response = {new GenericResponse()};

        // Получаем подобъекты, куда надо выделить долю
        List<UnderFacility> reUnderFacilityList = summary.getReUnderFacilityList();

        // Получаем подобъекты, которые будут использоваться для расчёта процентного соотношения разделения
        List<UnderFacility> underFacilityToCalculateShare = summary.getUnderFacilityList();
        final int[] counter = {0};
        int ufCount = underFacilityToCalculateShare.size();
        reUnderFacilityList.forEach(reUnderFacility -> {
            counter[0]++;
            sendStatus(String.format("Разделяем %d из %d подобъектов", counter[0], ufCount));
            summary.setReUnderFacility(reUnderFacility);
            response[0] = divideCash(summary);
            underFacilityToCalculateShare.remove(reUnderFacility);
            summary.setReUnderFacilityList(underFacilityToCalculateShare);
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

        List<InvestorsCash> cashList = new ArrayList<>(0);
        searchSummary.getCashIdList().forEach(id -> cashList.add(investorsCashService.findById(id)));

        GenericResponse response = new GenericResponse();

        Date dateClose = searchSummary.getDateReinvest();
        Date realDateGiven = searchSummary.getRealDateGiven();
        AppUser finalInvBuyer = invBuyer;
        // список сумм, которые закрываем для вывода
        Set<InvestorsCash> closeCashes = new HashSet<>();
        // список сумм, которые закрываем для перепродажи доли
        Set<InvestorsCash> oldCashes = new HashSet<>();
        // список сумм, которые получатся на выходе
        Set<InvestorsCash> newCashes = new HashSet<>();
        cashList.forEach(c -> {
            if (null != finalInvBuyer) { // Перепродажа доли
                TypeClosingInvest closingInvest = typeClosingInvestService.findByTypeClosingInvest("Перепродажа доли");
                NewCashDetails newCashDetails = newCashDetailsService.findByNewCashDetail("Перепокупка доли");

                InvestorsCash cash = investorsCashService.findById(c.getId());
                InvestorsCash newInvestorsCash = investorsCashService.findById(c.getId());

                cash.setId(null);
                cash.setInvestor(finalInvBuyer);
                cash.setDateGivedCash(dateClose);
                cash.setSourceId(c.getId());
                cash.setCashSource(null);
                cash.setSource(null);
                cash.setNewCashDetails(newCashDetails);
                cash.setRealDateGiven(realDateGiven);

                cash = investorsCashService.createNew(cash);

                newInvestorsCash.setCashSource(null);
                newInvestorsCash.setId(null);
                newInvestorsCash.setGivedCash(newInvestorsCash.getGivedCash().negate());
                newInvestorsCash.setSourceId(cash.getId());
                newInvestorsCash.setSource(null);
                newInvestorsCash.setDateGivedCash(dateClose);
                newInvestorsCash.setDateClosingInvest(dateClose);
                newInvestorsCash.setTypeClosingInvest(closingInvest);

                investorsCashService.createNew(newInvestorsCash);

                c.setDateClosingInvest(dateClose);
                c.setTypeClosingInvest(closingInvest);
                investorsCashService.update(c);
                oldCashes.add(c);
                newCashes.add(c);
                newCashes.add(cash);
                newCashes.add(newInvestorsCash);
            } else {
                InvestorsCash cashForTx = new InvestorsCash(c);
                cashForTx.setId(c.getId());
                c.setDateClosingInvest(dateClose);
                c.setTypeClosingInvest(typeClosingInvestService.findByTypeClosingInvest("Вывод"));
                c.setRealDateGiven(realDateGiven);
                investorsCashService.update(c);
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

    private Map<String, InvestorsCash> groupInvestorsCash(List<InvestorsCash> cashList, String what) {
        Map<String, InvestorsCash> map = new HashMap<>(0);

        cashList.forEach(ic -> {
            InvestorsCash keyMap;
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
                InvestorsCash cash = new InvestorsCash();
                cash.setGivedCash(ic.getGivedCash().add(keyMap.getGivedCash()));
                cash.setSource(ic.getSource());
                cash.setDateGivedCash(ic.getDateGivedCash());
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
    public List<NewCashDetails> initializeNewCashDetails() {
        return newCashDetailsService.initializeNewCashDetails();
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
    public List<TypeClosingInvest> initializeTypeClosingInvest() {
        return typeClosingInvestService.init();
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
}
