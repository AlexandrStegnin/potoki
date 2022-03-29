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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MoneyController {

  final StatusService statusService;
  final MoneyService moneyService;
  final FacilityService facilityService;
  final UserService userService;
  final CashSourceService cashSourceService;
  final NewCashDetailService newCashDetailService;
  final UnderFacilityService underFacilityService;
  final TypeClosingService typeClosingService;
  final AppFilterService appFilterService;
  CashFilter cashFilters = new CashFilter();
  final SearchSummary filters = new SearchSummary();

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
    Pageable pageable = prepareFilter(cashFilters);
    appFilterService.updateFilter(cashFilters, AppPage.MONEY);
    return prepareModel(pageable, cashFilters);
  }

  private Pageable prepareFilter(CashFilter cashFilters) {
    if (cashFilters.getFiltered() == 0) {
      cashFilters.setFiltered(1);
    }
    if (cashFilters.isAllRows()) {
      return new PageRequest(0, Integer.MAX_VALUE);
    } else {
      return new PageRequest(cashFilters.getPageNumber(), cashFilters.getPageSize());
    }
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
    model.addAttribute("money", new Money());
    model.addAttribute("newCash", true);
    model.addAttribute("edit", false);
    model.addAttribute("doubleCash", false);
    model.addAttribute("closeCash", false);
    model.addAttribute("operation", MoneyOperation.CREATE.getTitle());
    model.addAttribute("title", "Добавление денег инвестора");
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
   * Закрытие суммы вложения (перепродажа доли) одиночное
   *
   * @param moneyDTO DTO для закрытия
   * @return ответ
   */
  @PostMapping(path = Location.MONEY_CLOSE_RESALE)
  @ResponseBody
  public ApiResponse resaleCash(@RequestBody ResaleMoneyDTO moneyDTO) {
    return moneyService.resaleSimple(moneyDTO);
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
    ApiResponse response = new ApiResponse();

    // Получаем подобъекты, куда надо выделить долю
    List<Long> reUnderFacilityList = dividedCashDTO.getReUnderFacilitiesIdList();

    // Получаем подобъекты, которые будут использоваться для расчёта процентного соотношения разделения
    List<Long> underFacilityToCalculateShare = dividedCashDTO.getExcludedUnderFacilitiesIdList();
    int counter = 0;
    int ufCount = underFacilityToCalculateShare.size();

    for (Long reUnderFacility : reUnderFacilityList) {
      counter++;
      sendStatus(String.format("Разделяем %d из %d подобъектов", counter, ufCount));
      dividedCashDTO.setReUnderFacilityId(reUnderFacility);
      response = moneyService.divideCash(dividedCashDTO);
      underFacilityToCalculateShare.remove(reUnderFacility);
      dividedCashDTO.setReUnderFacilitiesIdList(underFacilityToCalculateShare);
    }
    sendStatus("OK");
    return response;
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
