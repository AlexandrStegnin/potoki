package com.art.controllers;

import com.art.config.application.Location;
import com.art.model.*;
import com.art.model.supporting.AfterCashing;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.SearchSummary;
import com.art.model.supporting.dto.CloseCashDTO;
import com.art.model.supporting.dto.CreateMoneyDTO;
import com.art.model.supporting.dto.DividedCashDTO;
import com.art.model.supporting.dto.ReinvestCashDTO;
import com.art.model.supporting.enums.MoneyOperation;
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
public class MoneyController {

    private final StatusService statusService;

    private final TransactionLogService transactionLogService;

    private final AfterCashingService afterCashingService;

    private final MoneyService moneyService;

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

    public MoneyController(MoneyService moneyService,
                           StatusService statusService, TransactionLogService transactionLogService,
                           AfterCashingService afterCashingService, FacilityService facilityService,
                           InvestorsFlowsSaleService investorsFlowsSaleService, UserService userService,
                           CashSourceService cashSourceService, NewCashDetailService newCashDetailService,
                           UnderFacilityService underFacilityService,
                           InvestorsFlowsService investorsFlowsService,
                           TypeClosingService typeClosingService) {
        this.moneyService = moneyService;
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
    @GetMapping(path = Location.MONEY_LIST)
    public ModelAndView moneyByPageNumber(@PageableDefault(size = 100) @SortDefault Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("money-list");
        Page<Money> page = moneyService.findAll(cashFilters, pageable);
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
    @PostMapping(path = Location.MONEY_LIST)
    public ModelAndView moneyPageable(@ModelAttribute(value = "cashFilters") CashFilter cashFilters) {
        Pageable pageable;
        if (cashFilters.getFiltered() == 0) {
            cashFilters.setFiltered(1);
        }
        if (cashFilters.isAllRows()) {
            pageable = new PageRequest(0, Integer.MAX_VALUE);
        } else {
            pageable = new PageRequest(cashFilters.getPageNumber(), cashFilters.getPageSize());
        }
        ModelAndView modelAndView = new ModelAndView("money-list");
        Page<Money> page = moneyService.findAll(cashFilters, pageable);
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
    @GetMapping(path = Location.MONEY_CREATE)
    public String newMoney(ModelMap model) {
        String title = "Добавление денег инвестора";
        model.addAttribute("money", new Money());
        model.addAttribute("newCash", true);
        model.addAttribute("edit", false);
        model.addAttribute("doubleCash", false);
        model.addAttribute("closeCash", false);
        model.addAttribute("operation", MoneyOperation.CREATE.getTitle());
        model.addAttribute("title", title);
        return "money-add";
    }

    /**
     * Создать сумму инвестора
     *
     * @param moneyDTO сумма инвестора
     * @return ответ
     */
    @PostMapping(path = Location.MONEY_CREATE)
    @ResponseBody
    public ApiResponse createCash(@RequestBody CreateMoneyDTO moneyDTO) {
        Money money = moneyService.create(moneyDTO);
        transactionLogService.create(money, TransactionType.CREATE);
        return new ApiResponse(String.format("Деньги инвестора [%s] успешно добавлены", money.getInvestor().getLogin()));
    }

    /**
     * Обновление суммы инвестора
     *
     * @param id id суммы
     * @param model модель со страницы
     * @return страница для редактирования суммы
     */
    @GetMapping(path = Location.MONEY_EDIT_ID)
    public String editCash(@PathVariable Long id, ModelMap model) {
        String title = "Обновление данных по деньгам инвесторов";
        Money money = moneyService.findById(id);

        model.addAttribute("money", money);
        model.addAttribute("newCash", false);
        model.addAttribute("edit", true);
        model.addAttribute("closeCash", false);
        model.addAttribute("doubleCash", false);
        model.addAttribute("title", title);
        return "money-add";
    }

    /**
     * Обновление суммы инвестора
     *
     * @param money сумма для обновления
     * @param id id суммы
     * @return переадресация на страницу отображения денег инвесторов
     */
    @PostMapping(path = Location.MONEY_EDIT_ID)
    public String editCash(@ModelAttribute("investorsCash") Money money, @PathVariable("id") Long id) {
        Money dbCash = moneyService.findById(id);
        transactionLogService.update(dbCash);
        moneyService.update(money);
        return "redirect:" + Location.MONEY_LIST;
    }

    /**
     * Закрытие суммы вложения
     *
     * @param id id суммы
     * @param model модель со страницы
     * @return страница для закрытия суммы вложения
     */
    @GetMapping(path = Location.MONEY_CLOSE_ID)
    public String closeCash(@PathVariable Long id, ModelMap model) {
        String title = "Закрытие вложения";
        Money money = moneyService.findById(id);
        money.setGivenCash(money.getGivenCash().setScale(2, RoundingMode.DOWN));
        model.addAttribute("money", money);

        model.addAttribute("newCash", false);
        model.addAttribute("edit", false);
        model.addAttribute("closeCash", true);
        model.addAttribute("doubleCash", false);
        model.addAttribute("title", title);
        return "money-add";
    }

    /**
     * Закрытие суммы вложения
     *
     * @param money сумма для закрытия
     * @param id id суммы
     * @param dateClosingInvest дата закрытия
     * @param realDateGiven реальная дата передачи денег
     * @return переадресация на страницу денег инвесторов
     */
    @PostMapping(path = Location.MONEY_CLOSE_ID)
    public String closeCash(@ModelAttribute("money") Money money,
                            @PathVariable("id") Long id, @RequestParam("dateClosingInvest") Date dateClosingInvest,
                            @RequestParam("realDateGiven") Date realDateGiven) {
        ModelAndView modelAndView = new ModelAndView("money-list");

        AppUser invBuyer;
        TypeClosing closingInvest = typeClosingService.findByName("Перепродажа доли");
        NewCashDetail newCashDetail = newCashDetailService.findByName("Перепокупка доли");

        // Перепродажа доли
        if (null != money.getInvestorBuyer()) {
            invBuyer = userService.findById(money.getInvestorBuyer().getId());

            Money cash = new Money(moneyService.findById(money.getId()));
            Money newMoney = new Money(moneyService.findById(money.getId()));
            Money oldCash = moneyService.findById(money.getId());
            oldCash.setDateClosing(dateClosingInvest);
            oldCash.setTypeClosing(closingInvest);
            oldCash.setRealDateGiven(realDateGiven);

            cash.setId(null);
            cash.setInvestor(invBuyer);
            cash.setDateGiven(dateClosingInvest);
            cash.setSourceId(money.getId());
            cash.setCashSource(null);
            cash.setSource(null);
            cash.setNewCashDetail(newCashDetail);

            newMoney.setId(null);
            newMoney.setCashSource(null);
            newMoney.setSource(null);
            newMoney.setGivenCash(newMoney.getGivenCash().negate());
            newMoney.setSourceId(money.getId());
            newMoney.setDateGiven(dateClosingInvest);
            newMoney.setDateClosing(dateClosingInvest);
            newMoney.setTypeClosing(closingInvest);

            moneyService.createNew(cash);
            moneyService.createNew(newMoney);
            moneyService.update(oldCash);
            Money transactionOldCash = new Money(oldCash);
            transactionOldCash.setId(oldCash.getId());
            Set<Money> cashSet = new HashSet<>();
            cashSet.add(cash);
            cashSet.add(newMoney);
            cashSet.add(transactionOldCash);
            transactionLogService.resale(Collections.singleton(transactionOldCash), cashSet);
        } else {
            Money updatedCash = moneyService.findById(money.getId());
            transactionLogService.close(Collections.singleton(updatedCash));
            updatedCash.setDateClosing(dateClosingInvest);
            updatedCash.setRealDateGiven(realDateGiven);

            updatedCash.setTypeClosing(typeClosingService.findByName("Вывод"));
            moneyService.update(updatedCash);
        }
        modelAndView.addObject("money", moneyService.findAll());
        return "redirect:" + Location.MONEY_LIST;
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
        Money money = searchSummary.getMoney();

        Facility reFacility = searchSummary.getReFacility();
        UnderFacility reUnderFacility = searchSummary.getReUnderFacility();
        Date reinvestDate = searchSummary.getDateReinvest();

        String whatWeDoWithCash = "обновлены.";

        String invLogin = money.getInvestor().getLogin();

        if (null != reFacility && null != money.getId() &&
                null != money.getNewCashDetail() &&
                (!money.getNewCashDetail().getName().equalsIgnoreCase("Реинвестирование с продажи") &&
                        !money.getNewCashDetail().getName().equalsIgnoreCase("Реинвестирование с аренды")) &&
                (null != searchSummary.getWhat() && !searchSummary.getWhat().equalsIgnoreCase("edit"))) {
            Money newMoney = new Money();
            newMoney.setFacility(reFacility);
            newMoney.setSourceFacility(money.getFacility());
            newMoney.setUnderFacility(reUnderFacility);
            newMoney.setInvestor(money.getInvestor());
            newMoney.setGivenCash(money.getGivenCash());
            newMoney.setDateGiven(reinvestDate);
            newMoney.setCashSource(null);
            newMoney.setNewCashDetail(newCashDetailService.findByName("Реинвестирование с продажи"));
            newMoney.setShareType(money.getShareType());
            moneyService.create(newMoney);
            whatWeDoWithCash = "добавлены.";
        }
        moneyService.update(money);

        response.setMessage("Деньги инвестора " + invLogin + " успешно " + whatWeDoWithCash);
        return response;
    }

    @GetMapping(value = {"/double-cash-{id}"})
    public String doubleInvCash(@PathVariable Long id, ModelMap model) {
        String title = "Разделение строк по деньгам инвесторов";
        Money money = moneyService.findById(id);

        List<UnderFacility> underFacilityList = new ArrayList<>(0);
        UnderFacility underFacility = new UnderFacility();
        underFacility.setId(0L);
        underFacility.setName("Выберите подобъект");
        underFacilityList.add(underFacility);
        underFacilityList.addAll(underFacilityService.findAll()
                .stream()
                .filter(uf -> uf.getFacility().equals(money.getFacility()))
                .collect(Collectors.toList()));

        model.addAttribute("underFacilitiesList", underFacilityList);
        model.addAttribute("money", money);
        model.addAttribute("newCash", false);
        model.addAttribute("edit", false);
        model.addAttribute("closeCash", false);
        model.addAttribute("doubleCash", true);
        model.addAttribute("title", title);
        return "money-add";
    }

    @PostMapping(value = "/double-cash-{id}")
    public String doubleCash(@ModelAttribute("investorsCash") Money money, @PathVariable("id") int id) {
        Money newMoney = moneyService.findById(money.getId());
        Money inMemoryCash = moneyService.findById(money.getId());
        ModelAndView model = new ModelAndView("money-add");
        money.setFacility(newMoney.getFacility());
        money.setInvestor(newMoney.getInvestor());
        money.setDateGiven(newMoney.getDateGiven());
        money.setCashSource(newMoney.getCashSource());
        money.setNewCashDetail(newMoney.getNewCashDetail());
        money.setShareType(inMemoryCash.getShareType());

        BigDecimal newSum = newMoney.getGivenCash().subtract(money.getGivenCash());
        newMoney.setGivenCash(money.getGivenCash());
        money.setGivenCash(newSum);
        newMoney.setCashSource(money.getCashSource());
        newMoney.setShareType(money.getShareType());
        newMoney.setId(null);
        newMoney.setSource(money.getId().toString());
        newMoney.setUnderFacility(money.getUnderFacility());
        newMoney.setSourceFacility(money.getSourceFacility());
        newMoney.setDateReport(null);
        money.setUnderFacility(inMemoryCash.getUnderFacility());
        money.setIsDivide(1);

        if (money.getGivenCash().compareTo(BigDecimal.ZERO) == 0) {
            money.setIsReinvest(1);
            money.setIsDivide(1);
        }
        moneyService.update(newMoney);
        moneyService.update(money);

        if (money.getGivenCash().compareTo(BigDecimal.ZERO) == 0) {
            return "redirect:" + Location.MONEY_LIST;
        } else {
            model.addObject("money", money);
            return model.getViewName();
        }
    }

    @PostMapping(value = {"/deleteCashList"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse deleteCashList(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        List<Money> listToDelete = moneyService.findByIdIn(searchSummary.getCashIdList());
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
                    Money parentCash = moneyService.findById(parentCashId);
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
                                List<Money> childCash = moneyService.findBySource(deleting.getSource());
                                AfterCashing cashToDel = afterCashing.stream()
                                        .filter(ac -> ac.getOldId().equals(parentCashId))
                                        .findFirst().orElse(afterCashing.get(0));
                                parentCash.setGivenCash(cashToDel.getOldValue());
                                childCash.forEach(cbs -> moneyService.deleteById(cbs.getId()));
                                afterCashingService.deleteById(cashToDel.getId());
                            }
                        }
                        Money makeDelete =
                                moneyService.findBySource(parentCash.getId().toString())
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

                        List<Money> oldInvCash = moneyService.findBySourceId(parentCashId);
                        oldInvCash = oldInvCash.stream().filter(oc -> !deleting.getId().equals(oc.getId())).collect(Collectors.toList());
                        if (oldInvCash.size() > 0) {
                            oldInvCash.forEach(oCash -> {
                                parentCash.setGivenCash(parentCash.getGivenCash().add(oCash.getGivenCash()));
                                moneyService.deleteById(oCash.getId());
                            });
                        }
                        moneyService.update(parentCash);
                    }

                });
            }

            List<Money> cash = moneyService.findBySourceId(deleting.getId());
            if (cash.size() > 0) {
                cash.forEach(ca -> {
                    if (ca.getGivenCash().signum() == -1) {
                        moneyService.deleteById(ca.getId());
                    } else {
                        ca.setSourceId(null);
                        moneyService.update(ca);
                    }
                });
            }

            if (!Objects.equals(null, deleting.getSourceId())) {
                List<Money> monies = moneyService.findBySourceId(deleting.getSourceId())
                        .stream()
                        .filter(ic -> !Objects.equals(deleting, ic))
                        .collect(Collectors.toList());
                if (!Objects.equals(0, monies.size())) {
                    monies.forEach(investorsCash -> moneyService.deleteById(investorsCash.getId()));
                }

                Money parentCash = moneyService.findById(deleting.getSourceId());
                if (!Objects.equals(null, parentCash)) {
                    parentCash.setIsReinvest(0);
                    parentCash.setIsDivide(0);
                    parentCash.setTypeClosing(null);
                    parentCash.setDateClosing(null);
                    moneyService.update(parentCash);
                }
            }

            moneyService.deleteById(deleting.getId());
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
        if (!summary.getMoney().getUnderFacility().getName().equals("Выберите подобъект")) {
            underFacility = underFacilityService.findByName(summary.getMoney().getUnderFacility().getName());
        }

        summary.getMoney().setUnderFacility(underFacility);
        String out = moneyService.cashingAllMoney(summary);

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
        return moneyService.cashingMoney(summary);
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
        Money cash = summary.getMoney();
        if (cash != null) {
            AppUser inv = summary.getMoney().getInvestor();
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
        String out = moneyService.cashingMoney(summary);

        if (StringUtils.isEmpty(out)) {
            Page<Money> page = moneyService.findAll(cashFilters, new PageRequest(0, Integer.MAX_VALUE));
            model.addAttribute("page", page);
            model.addAttribute("cashFilters", cashFilters);
            model.addAttribute("searchSummary", filters);
            return "redirect:" + Location.MONEY_LIST;
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
        List<Money> monies = searchSummary.getMoneyList();
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

        Map<String, Money> map = moneyService.groupInvestorsCash(monies, searchSummary.getWhat());
        try {
            Set<Money> cashList = new HashSet<>();
            map.forEach((key, value) -> {
                value.setNewCashDetail(finalNewCashDetail);
                value.setGivenCash(value.getGivenCash().setScale(2, RoundingMode.CEILING));
                moneyService.createNew(value);
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
     * @param reinvestCashDTO DTO для реинвестирования
     * @return сообщение об успешном/не успешном реинвестировании
     */
    @PostMapping(path = Location.MONEY_REINVEST_SAVE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    ApiResponse saveReInvCash(@RequestBody ReinvestCashDTO reinvestCashDTO) {
        return moneyService.reinvestCash(reinvestCashDTO);
    }

    /**
     * Разделение денег инвесторов
     *
     * @param dividedCashDTO DTO для разделения
     * @return ответ об окончании операции
     */
    @PostMapping(value = Location.MONEY_DIVIDE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    ApiResponse saveDivideCash(@RequestBody DividedCashDTO dividedCashDTO) {
        ApiResponse response = moneyService.divideCash(dividedCashDTO);
        sendStatus("OK");
        return response;
    }

    /**
     * Разделение денег инвесторов (массовое)
     *
     * @param dividedCashDTO DTO для разделения
     * @return ответ об окончании операции
     */
    @PostMapping(value = Location.MONEY_DIVIDE_MULTIPLE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
            response[0] = moneyService.divideCash(dividedCashDTO);
            underFacilityToCalculateShare.remove(reUnderFacility);
            dividedCashDTO.setReUnderFacilitiesIdList(underFacilityToCalculateShare);
        });
        sendStatus("OK");
        return response[0];
    }

    /**
     * Массовое закрытие сумм
     *
     * @param closeCashDTO DTO для закрытия сумм
     * @return сообщение об успешном/не успешном массовом закрытии
     */
    @PostMapping(path = Location.MONEY_CLOSE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    ApiResponse closeCash(@RequestBody CloseCashDTO closeCashDTO) {
        return moneyService.close(closeCashDTO);
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

    private void sendStatus(String message) {
        statusService.sendStatus(message);
    }
}
