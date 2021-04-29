package com.art.controllers;

import com.art.config.application.Location;
import com.art.model.*;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.SearchSummary;
import com.art.model.supporting.dto.*;
import com.art.model.supporting.enums.AppPage;
import com.art.model.supporting.enums.MoneyOperation;
import com.art.model.supporting.enums.ShareType;
import com.art.model.supporting.filters.CashFilter;
import com.art.service.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
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
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@Transactional
public class MoneyController {

    private final StatusService statusService;

    private final MoneyService moneyService;

    private final FacilityService facilityService;

    private final UserService userService;

    private final CashSourceService cashSourceService;

    private final NewCashDetailService newCashDetailService;

    private final UnderFacilityService underFacilityService;

    private final TypeClosingService typeClosingService;

    private final SearchSummary filters = new SearchSummary();

    private CashFilter cashFilters = new CashFilter();

    private final AppFilterService appFilterService;

    public MoneyController(MoneyService moneyService,
                           StatusService statusService, FacilityService facilityService, UserService userService,
                           CashSourceService cashSourceService, NewCashDetailService newCashDetailService,
                           UnderFacilityService underFacilityService, TypeClosingService typeClosingService,
                           AppFilterService appFilterService) {
        this.moneyService = moneyService;
        this.statusService = statusService;
        this.facilityService = facilityService;
        this.userService = userService;
        this.cashSourceService = cashSourceService;
        this.newCashDetailService = newCashDetailService;
        this.underFacilityService = underFacilityService;
        this.typeClosingService = typeClosingService;
        this.appFilterService = appFilterService;
    }

    /**
     * Получить список денег инвесторов
     *
     * @param pageable для постраничного отображения
     * @return список денег инвесторов
     */
    @GetMapping(path = Location.MONEY_LIST)
    public ModelAndView moneyByPageNumber(@PageableDefault(size = 100) @SortDefault Pageable pageable) {
        cashFilters = (CashFilter) appFilterService.getFilter(cashFilters, CashFilter.class, AppPage.MONEY);
        return prepareModel(pageable, cashFilters);
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
        appFilterService.updateFilter(cashFilters, AppPage.MONEY);
        return prepareModel(pageable, cashFilters);
    }

    private ModelAndView prepareModel(Pageable pageable, CashFilter cashFilters) {
        ModelAndView modelAndView = new ModelAndView("money-list");
        Page<Money> page = moneyService.findAll(cashFilters, pageable);
        modelAndView.addObject("page", page);
        modelAndView.addObject("cashFilters", cashFilters);
        modelAndView.addObject("searchSummary", filters);
        modelAndView.addObject("cashingDTO", new CashingMoneyDTO());
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
        return moneyService.create(moneyDTO);
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
        String title = "Обновление суммы инвестора";
        Money money = moneyService.findById(id);
        Hibernate.initialize(money.getSourceFacility());
        Hibernate.initialize(money.getSourceUnderFacility());
        model.addAttribute("money", money);
        model.addAttribute("newCash", false);
        model.addAttribute("edit", true);
        model.addAttribute("closeCash", false);
        model.addAttribute("doubleCash", false);
        model.addAttribute("title", title);
        model.addAttribute("operation", MoneyOperation.UPDATE.getTitle());
        return "money-add";
    }

    /**
     * Обновление суммы инвестора
     *
     * @param moneyDTO DTO суммы для обновления
     * @return переадресация на страницу отображения денег инвесторов
     */
    @PostMapping(path = Location.MONEY_UPDATE)
    @ResponseBody
    public ApiResponse editCash(@RequestBody UpdateMoneyDTO moneyDTO) {
        Money money = moneyService.update(moneyDTO);
        return new ApiResponse(String.format("Деньги инвестора [%s] успешно обновлены", money.getInvestor().getLogin()));
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
        if (money.getSourceUnderFacility() != null) {
            Hibernate.initialize(money.getSourceUnderFacility().getFacility());
        }
        money.setGivenCash(money.getGivenCash().setScale(2, RoundingMode.DOWN));
        model.addAttribute("money", money);
        model.addAttribute("newCash", false);
        model.addAttribute("edit", false);
        model.addAttribute("closeCash", true);
        model.addAttribute("doubleCash", false);
        model.addAttribute("title", title);
        model.addAttribute("operation", MoneyOperation.CLOSE.getTitle());
        return "money-add";
    }

    /**
     * Закрытие суммы вложения (перепродажа доли) одиночное
     *
     * @param moneyDTO DTO для закрытия
     * @return ответ
     */
    @PostMapping(path = Location.MONEY_CLOSE_RESALE)
    @ResponseBody
    public ApiResponse resaleCash(@RequestBody ResaleMoneyDTO moneyDTO) {
        Money money = moneyService.resale(moneyDTO);
        return new ApiResponse(String.format("Перепродажа доли инвестора [%s] прошла успешно", money.getInvestor().getLogin()));
    }

    /**
     * Закрытие суммы вложения (вывод) одиночное
     *
     * @param moneyDTO DTO для закрытия
     * @return ответ
     */
    @PostMapping(path = Location.MONEY_CLOSE_CASHING_ONE)
    @ResponseBody
    public ApiResponse cashingMoney(@RequestBody CashingMoneyDTO moneyDTO) {
        Money money = moneyService.cashing(moneyDTO);
        return new ApiResponse(String.format("Вывод суммы инвестора [%s] прошёл успешно", money.getInvestor().getLogin()));
    }

    /**
     * Страница разделения денег инвесторов
     *
     * @param id id суммы для разделения
     * @param model модель
     * @return страница для разделения
     */
    @GetMapping(path = Location.MONEY_DOUBLE_ID)
    public String doubleInvCash(@PathVariable Long id, ModelMap model) {
        String title = "Разделение денег";
        Money money = moneyService.findById(id);
        money.setGivenCash(money.getGivenCash().setScale(2, RoundingMode.CEILING));
        List<UnderFacility> underFacilityList = new ArrayList<>(0);
        UnderFacility underFacility = new UnderFacility();
        underFacility.setId(0L);
        underFacility.setName("Выберите подобъект");
        underFacilityList.add(underFacility);
        underFacilityList.addAll(underFacilityService.findAll()
                .stream()
                .filter(uf -> uf.getFacility().equals(money.getFacility()))
                .collect(Collectors.toList()));
        Hibernate.initialize(money.getSourceFacility());
        Hibernate.initialize(money.getSourceUnderFacility());
        model.addAttribute("underFacilitiesList", underFacilityList);
        model.addAttribute("money", money);
        model.addAttribute("newCash", false);
        model.addAttribute("edit", false);
        model.addAttribute("closeCash", false);
        model.addAttribute("doubleCash", true);
        model.addAttribute("title", title);
        model.addAttribute("operation", MoneyOperation.DOUBLE.getTitle());
        return "money-add";
    }

    /**
     * Разделение денег инвесторов
     *
     * @param money сумма для разделениея
     * @param id id суммы
     * @return страница с деньгами или страницу для разделения остатка
     */
    @PostMapping(path = Location.MONEY_DOUBLE_ID)
    public String doubleCash(@ModelAttribute("money") Money money, @PathVariable("id") int id) {
        Money inMemoryCash = moneyService.findById(money.getId());
        Money newMoney = new Money(inMemoryCash);

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
            return "redirect:" + Location.MONEY_DOUBLE.concat("/").concat(money.getId().toString());
        }
    }

    /**
     * Удалить список денег инвесторов
     *
     * @param dto DTO для удаления
     * @return ответ
     */
    @PostMapping(path = Location.MONEY_DELETE_LIST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    ApiResponse deleteMonies(@RequestBody DeleteMoneyDTO dto) {
        return moneyService.deleteList(dto);
    }

    /**
     * Вывести суммы
     *
     * @param dto модель для вывода
     * @return сообщение об успешном/не успешном выводе
     */
    @PostMapping(path = Location.MONEY_CASHING)
    public @ResponseBody
    ApiResponse cashing(@RequestBody CashingMoneyDTO dto) {
        return moneyService.cashingMoney(dto);
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
            return "cashing";
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
            return "cashing";
        }
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

    @ResponseBody
    @PostMapping(path = Location.MONEY_ACCEPT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse acceptMonies(@RequestBody AcceptMoneyDTO dto) {
        return moneyService.accept(dto);
    }

    @ResponseBody
    @PostMapping(path = Location.MONEY_OPENED, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<InvestorCashDTO> openedMonies(@RequestBody ReBuyShareDTO dto) {
        return moneyService.getOpenedMonies(dto);
    }

    @ResponseBody
    @PostMapping(path = Location.MONEY_RE_BUY, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse reBuyShare(@RequestBody ReBuyShareDTO dto) {
        return moneyService.reBuyShare(dto);
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
