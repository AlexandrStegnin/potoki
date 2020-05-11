package com.art.controllers;

import com.art.model.*;
import com.art.model.supporting.AfterCashing;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.SearchSummary;
import com.art.model.supporting.filters.CashFilter;
import com.art.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
public class InvestorsCashController {

    @Autowired
    private MarketingTreeService marketingTreeService;

    @Resource(name = "afterCashingService")
    private AfterCashingService afterCashingService;

    @Resource(name = "investorsCashService")
    private InvestorsCashService investorsCashService;

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "cashSourcesService")
    private CashSourcesService cashSourcesService;

    @Resource(name = "cashTypesService")
    private CashTypesService cashTypesService;

    @Resource(name = "newCashDetailsService")
    private NewCashDetailsService newCashDetailsService;

    @Resource(name = "investorsTypesService")
    private InvestorsTypesService investorsTypesService;

    @Resource(name = "underFacilitiesService")
    private UnderFacilitiesService underFacilitiesService;

    @Resource(name = "typeClosingInvestService")
    private TypeClosingInvestService typeClosingInvestService;

    @Resource(name = "shareKindService")
    private ShareKindService shareKindService;

    @Resource(name = "investorsFlowsService")
    private InvestorsFlowsService investorsFlowsService;

    @Resource(name = "mailingGroupsService")
    private MailingGroupsService mailingGroupsService;

    @Resource(name = "investorsFlowsSaleService")
    private InvestorsFlowsSaleService investorsFlowsSaleService;

    private SearchSummary filters = new SearchSummary();

    private CashFilter cashFilters = new CashFilter();

    @GetMapping(value = "/investorscash")
    public ModelAndView invCashByPageNumber(@PageableDefault(size = 100) @SortDefault Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("viewinvestorscash");
        Page<InvestorsCash> page = investorsCashService.findAll(cashFilters, pageable);
        modelAndView.addObject("page", page);
        modelAndView.addObject("cashFilters", cashFilters);
        modelAndView.addObject("searchSummary", filters);
        return modelAndView;
    }

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

    @GetMapping(value = {"/edit-cash-{id}"})
    public String editCash(@PathVariable BigInteger id, ModelMap model) {
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

    @GetMapping(value = {"/close-cash-{id}"})
    public String closeCash(@PathVariable BigInteger id, ModelMap model) {
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

    @GetMapping(value = {"/double-cash-{id}"})
    public String doubleInvCash(@PathVariable BigInteger id, ModelMap model) {
        String title = "Разделение строк по деньгам инвесторов";
        InvestorsCash investorsCash = investorsCashService.findById(id);

        List<UnderFacilities> underFacilitiesList = new ArrayList<>(0);
        UnderFacilities underFacilities = new UnderFacilities();
        underFacilities.setId(new BigInteger("0"));
        underFacilities.setUnderFacility("Выберите подобъект");
        underFacilitiesList.add(underFacilities);
        underFacilitiesList.addAll(underFacilitiesService.findAllWithCriteriaApi()
                .stream()
                .filter(cash -> cash.getFacility().equals(investorsCash.getFacility()))
                .collect(Collectors.toList()));

        model.addAttribute("underFacilitiesList", underFacilitiesList);
        model.addAttribute("investorsCash", investorsCash);
        model.addAttribute("newCash", false);
        model.addAttribute("edit", false);
        model.addAttribute("closeCash", false);
        model.addAttribute("doubleCash", true);
        model.addAttribute("title", title);
        return "addinvestorscash";
    }

    @PostMapping(value = {"/deleteCashList"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse deleteCashList(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        List<InvestorsCash> listToDelete = investorsCashService.findByIdIn(searchSummary.getCashIdList());
        List<AfterCashing> afterCashingList = afterCashingService.findAll();
        Comparator<AfterCashing> comparator = Comparator.comparing(AfterCashing::getId);

        afterCashingList.sort(comparator.reversed());

        listToDelete.forEach(deleting -> {
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
                List<BigInteger> sourceIdList = new ArrayList<>(tmp.length);
                if (tmp.length > 0 && !tmp[tmp.length - 1].equals("")) {
                    for (String bigInt : tmp) {
                        sourceIdList.add(new BigInteger(bigInt));
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
                                deleting.getCashType().equals(parentCash.getCashType()) &&
                                deleting.getInvestorsType().equals(parentCash.getInvestorsType()) &&
                                deleting.getShareKind().equals(parentCash.getShareKind()) &&
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

            updateMailingGroups(deleting, "delete");
            investorsCashService.deleteById(deleting.getId());
            response.setMessage("Данные успешно удалены");

        });
        return response;
    }

    private void deleteAllCashList(SearchSummary searchSummary) {

    }

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

    @PostMapping(value = "/allMoneyCashing", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse allMoneyCashing(@RequestBody SearchSummary searchSummary) {

        GenericResponse response = new GenericResponse();

        UnderFacilities underFacility = null;
        if (!searchSummary.getInvestorsCash().getUnderFacility().getUnderFacility().equals("Выберите подобъект")) {
            underFacility = underFacilitiesService.findByUnderFacility(searchSummary.getInvestorsCash().getUnderFacility().getUnderFacility());
        }

        searchSummary.getInvestorsCash().setUnderFacility(underFacility);
        String out = investorsCashService.cashingAllMoney(searchSummary);

        if (StringUtils.isEmpty(out)) {
            response.setMessage("Деньги инвестора " + searchSummary.getUser().getLogin() + " успешно выведены.");
        } else {
            response.setMessage(out);
        }
        return response;
    }

    @GetMapping(value = {"/getInvestorsCash"})
    public String getCash(ModelMap model) {
        String title = "Вывод денег инвестора";
        SearchSummary searchSummary = new SearchSummary();

        model.addAttribute("searchSummary", searchSummary);
        model.addAttribute("title", title);
        return "getInvestorsCash";
    }

    @PostMapping(value = {"/getInvestorsCash"})
    public String cashing(@ModelAttribute("searchSummary") SearchSummary searchSummary,
                          BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "getInvestorsCash";
        }

        String out = investorsCashService.cashingMoney(searchSummary);

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

    @PostMapping(value = {"/newinvestorscash"})
    public String saveCash(@ModelAttribute("investorsCash") InvestorsCash investorsCash,
                           BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "addinvestorscash";
        }
        String ret = "списку денег инвестора";
        String redirectUrl = "/investorscash";
        updateMailingGroups(investorsCash, "add");
        investorsCashService.create(investorsCash);
        if (!Objects.equals(null, investorsCash.getCashSource()) &&
                !investorsCash.getCashSource().getCashSource().equalsIgnoreCase("Бронь")) {
            marketingTreeService.updateMarketingTreeFromApp();
        }
        model.addAttribute("success", "Деньги инвестора " + investorsCash.getInvestor().getLogin() +
                " успешно добавлены.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @PostMapping(value = {"/saveCash"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse saveCash(@RequestBody SearchSummary searchSummary) {

        GenericResponse response = new GenericResponse();
        InvestorsCash investorsCash = searchSummary.getInvestorsCash();

        Facilities reFacility = searchSummary.getReFacility();
        UnderFacilities reUnderFacility = searchSummary.getReUnderFacility();
        Date reinvestDate = searchSummary.getDateReinvest();

        String whatWeDoWithCash = "обновлены.";

        String invLogin = investorsCash.getInvestor().getLogin();

        if (!Objects.equals(reFacility, null) && !Objects.equals(investorsCash.getId(), null) &&
                !Objects.equals(investorsCash.getNewCashDetails(), null) &&
                (!investorsCash.getNewCashDetails().getNewCashDetail().equalsIgnoreCase("Реинвестирование с продажи") &&
                        !investorsCash.getNewCashDetails().getNewCashDetail().equalsIgnoreCase("Реинвестирование с аренды")) &&
                (!Objects.equals(null, searchSummary.getWhat()) && !searchSummary.getWhat().equalsIgnoreCase("edit"))) {
            InvestorsCash newInvestorsCash = new InvestorsCash();
            newInvestorsCash.setFacility(reFacility);
            newInvestorsCash.setSourceFacility(investorsCash.getFacility());
            newInvestorsCash.setUnderFacility(reUnderFacility);
            newInvestorsCash.setInvestor(investorsCash.getInvestor());
            newInvestorsCash.setGivedCash(investorsCash.getGivedCash());
            newInvestorsCash.setDateGivedCash(reinvestDate);
            newInvestorsCash.setCashSource(null);
            newInvestorsCash.setCashType(cashTypesService.findByCashType("Старые деньги"));
            newInvestorsCash.setNewCashDetails(newCashDetailsService.findByNewCashDetail("Реинвестирование с продажи"));
            newInvestorsCash.setInvestorsType(investorsTypesService.findByInvestorsTypes("Старый инвестор"));
            newInvestorsCash.setShareKind(investorsCash.getShareKind());
            updateMailingGroups(newInvestorsCash, "add");
            investorsCashService.create(newInvestorsCash);
            whatWeDoWithCash = "добавлены.";
        }
        investorsCash = investorsCashService.update(investorsCash);
        if (!Objects.equals(null, searchSummary.getWhat()) &&
                searchSummary.getWhat().equalsIgnoreCase("closeCash")) {
            updateMailingGroups(investorsCash, "delete");
        } else {
            updateMailingGroups(investorsCash, "add");
        }
        response.setMessage("Деньги инвестора " + invLogin + " успешно " + whatWeDoWithCash);
        return response;
    }

    @PostMapping(value = "/edit-cash-{id}")
    public String editCash(@ModelAttribute("investorsCash") InvestorsCash investorsCash, @PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("viewinvestorscash");

        updateMailingGroups(investorsCash, "add");

        investorsCashService.update(investorsCash);
        SearchSummary searchSummary = new SearchSummary();
        modelAndView.addObject("searchSummary", searchSummary);
        modelAndView.addObject("investorsCash", investorsCashService.findAll());

        return "redirect: /investorscash";
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
        investorsCash.setCashType(newInvestorsCash.getCashType());
        investorsCash.setNewCashDetails(newInvestorsCash.getNewCashDetails());
        investorsCash.setInvestorsType(newInvestorsCash.getInvestorsType());
        investorsCash.setShareKind(inMemoryCash.getShareKind());

        BigDecimal newSum = newInvestorsCash.getGivedCash().subtract(investorsCash.getGivedCash());
        newInvestorsCash.setGivedCash(investorsCash.getGivedCash());
        investorsCash.setGivedCash(newSum);
        newInvestorsCash.setCashSource(investorsCash.getCashSource());
        newInvestorsCash.setShareKind(investorsCash.getShareKind());
        newInvestorsCash.setId(null);
        newInvestorsCash.setSource(investorsCash.getId().toString());
        newInvestorsCash.setUnderFacility(investorsCash.getUnderFacility());
        newInvestorsCash.setSourceFacility(investorsCash.getSourceFacility());
        newInvestorsCash.setDateReport(null);
        investorsCash.setUnderFacility(inMemoryCash.getUnderFacility());
        investorsCash.setIsDivide(1);

        List<InvestorsCash> cash = new ArrayList<>(4);
        cash.add(newInvestorsCash);
        cash.add(investorsCash);

        ExecutorService service = Executors.newCachedThreadPool();

        cash.forEach(c -> service.submit(() -> updateMailingGroups(c, "add")));
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

    @PostMapping(value = {"/saveReCash"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse saveReCash(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        List<InvestorsCash> investorsCashes = searchSummary.getInvestorsCashList();
        InvestorsTypes investorsType = investorsTypesService.findByInvestorsTypes("Старый инвестор");
        CashTypes cashTypes = cashTypesService.findByCashType("Новые деньги");
        NewCashDetails newCashDetails;

        if ("sale".equals(searchSummary.getWhat())) {
            newCashDetails = newCashDetailsService.findByNewCashDetail("Реинвестирование с продажи (прибыль)");
            List<InvestorsFlowsSale> flowsSales = investorsFlowsSaleService.
                    findByIdInWithAllFields(searchSummary.getReinvestIdList());
            flowsSales.forEach(f -> {
                f.setIsReinvest(1);
                investorsFlowsSaleService.update(f);
            });
        } else {
            newCashDetails = newCashDetailsService.findByNewCashDetail("Реинвестирование с аренды");
            List<InvestorsFlows> flows = investorsFlowsService.findByIdIn(searchSummary.getReinvestIdList());
            flows.forEach(f -> f.setIsReinvest(1));
            investorsFlowsService.saveList(flows);
        }

        NewCashDetails finalNewCashDetails = newCashDetails;

        Map<String, InvestorsCash> map = groupInvestorsCash(investorsCashes, searchSummary.getWhat());
        try {
            map.forEach((key, value) -> {
                value.setCashType(cashTypes);
                value.setNewCashDetails(finalNewCashDetails);
                value.setInvestorsType(investorsType);
                value.setGivedCash(value.getGivedCash().setScale(2, RoundingMode.CEILING));
                updateMailingGroups(value, "add");
                investorsCashService.create(value);
            });

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

    @PostMapping(value = {"/saveReInvCash"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse saveReInvCash(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        List<InvestorsCash> investorsCashes = searchSummary.getInvestorsCashList();
        final Date[] dateClose = {null};
        final CashTypes cashTypes = cashTypesService.findByCashType("Старые деньги");
        final NewCashDetails newCashDetails = newCashDetailsService.findByNewCashDetail("Реинвестирование с продажи (сохранение)");
        final InvestorsTypes investorsType = investorsTypesService.findByInvestorsTypes("Старый инвестор");
        final TypeClosingInvest typeClosingInvest = typeClosingInvestService.findByTypeClosingInvest("Реинвестирование");

        final Map<String, InvestorsCash> map = groupInvestorsCash(investorsCashes, "");

        map.forEach((key, value) -> {
            value.setCashType(cashTypes);
            value.setNewCashDetails(newCashDetails);
            value.setInvestorsType(investorsType);
            value.setGivedCash(value.getGivedCash().setScale(2, RoundingMode.DOWN));
            dateClose[0] = value.getDateGivedCash();
            updateMailingGroups(value, "add");
            investorsCashService.create(value);
        });

        List<InvestorsCash> oldCash = investorsCashService.findByIdIn(searchSummary.getReinvestIdList());
        final Date finalDateClose = dateClose[0];
        oldCash.forEach(f -> {
            f.setIsReinvest(1);
            f.setDateClosingInvest(finalDateClose);
            f.setTypeClosingInvest(typeClosingInvest);
            updateMailingGroups(f, "add");
            investorsCashService.create(f);
        });

        response.setMessage("Реинвестирование прошло успешно");

        return response;
    }

    @PostMapping(value = {"/saveDivideCash"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse saveDivideCash(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        List<BigInteger> idsList = searchSummary.getInvestorsCashList().stream().map(InvestorsCash::getId).collect(Collectors.toList());
        List<InvestorsCash> investorsCashes = investorsCashService.findByIdIn(idsList);

        List<UnderFacilities> remainingUnderFacilitiesList = searchSummary.getUnderFacilitiesList();

        UnderFacilities underFacility = underFacilitiesService.findByIdWithCriteriaApi(
                searchSummary.getReUnderFacility().getId());
        Facilities facility = facilityService.findByIdWithUnderFacilitiesAndRooms(underFacility.getFacility().getId());
        Set<UnderFacilities> underFacilitiesList = facility.getUnderFacilities();

        List<Rooms> rooms = new ArrayList<>(0);

        underFacilitiesList.forEach(uf -> remainingUnderFacilitiesList.forEach(ruf -> {
            if (uf.getId().equals(ruf.getId())) {
                rooms.addAll(uf.getRooms());
            }
        }));

        BigDecimal coastFacility = rooms
                .stream()
                .map(Rooms::getCoast)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, BigDecimal.ROUND_CEILING);

        BigDecimal coastUnderFacility = underFacility.getRooms().stream()
                .map(Rooms::getCoast)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_CEILING);

        BigDecimal divided = coastUnderFacility.divide(coastFacility, 20, BigDecimal.ROUND_CEILING);

        investorsCashes.forEach(f -> {
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
            cash.setCashType(f.getCashType());
            cash.setNewCashDetails(f.getNewCashDetails());
            cash.setInvestorsType(f.getInvestorsType());
            cash.setUnderFacility(underFacility);
            cash.setDateClosingInvest(null);
            cash.setTypeClosingInvest(null);
            cash.setShareKind(f.getShareKind());
            cash.setDateReport(f.getDateReport());
            cash.setSourceFacility(f.getSourceFacility());
            cash.setSourceUnderFacility(f.getSourceUnderFacility());
            cash.setRoom(f.getRoom());
            f.setGivedCash(sumRemainder);
            updateMailingGroups(f, "add");
            updateMailingGroups(cash, "add");
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

    @PostMapping(value = "/close-cash-{id}")
    public String closeCash(@ModelAttribute("investorsCash") InvestorsCash investorsCash,
                            @PathVariable("id") int id, @RequestParam("dateClosingInvest") Date dateClosingInvest) {
        ModelAndView modelAndView = new ModelAndView("viewinvestorscash");

        Users invBuyer;
        TypeClosingInvest closingInvest = typeClosingInvestService.findByTypeClosingInvest("Перепродажа доли");
        NewCashDetails newCashDetails = newCashDetailsService.findByNewCashDetail("Перепокупка доли");

        if (!Objects.equals(investorsCash.getInvestorBuyer(), null)) {
            invBuyer = userService.findById(investorsCash.getInvestorBuyer().getId());

            InvestorsCash cash = investorsCashService.findById(investorsCash.getId());
            InvestorsCash newInvestorsCash = investorsCashService.findById(investorsCash.getId());
            InvestorsCash oldCash = investorsCashService.findById(investorsCash.getId());
            oldCash.setDateClosingInvest(dateClosingInvest);
            oldCash.setTypeClosingInvest(closingInvest);

            ExecutorService service = Executors.newCachedThreadPool();
            List<InvestorsCash> cashes = new ArrayList<>(Arrays.asList(newInvestorsCash, cash));

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

            cashes.forEach(c -> service.submit(() -> {
                updateMailingGroups(c, "add");
            }));

            investorsCashService.create(cash);
            investorsCashService.create(newInvestorsCash);
            investorsCashService.update(oldCash);
            service.shutdown();
        } else {
            InvestorsCash updatedCash = investorsCashService.findById(investorsCash.getId());
            updatedCash.setDateClosingInvest(dateClosingInvest);

            updatedCash.setTypeClosingInvest(typeClosingInvestService.findByTypeClosingInvest("Вывод"));
            investorsCashService.update(updatedCash);

            updateMailingGroups(updatedCash, "delete");

        }
        modelAndView.addObject("investorsCash", investorsCashService.findAll());
        return "redirect: /investorscash";
    }

    @PostMapping(value = {"/closeCash"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse closeCash(@RequestBody SearchSummary searchSummary) {
        Users invBuyer = null;
        if (!Objects.equals(searchSummary.getUser(), null)) {
            invBuyer = userService.findById(searchSummary.getUser().getId());
        }

        List<InvestorsCash> cashList = new ArrayList<>(0);
        searchSummary.getCashIdList().forEach(id -> cashList.add(investorsCashService.findById(id)));

        GenericResponse response = new GenericResponse();

        Date dateClose = searchSummary.getDateReinvest();

        Users finalInvBuyer = invBuyer;
        cashList.forEach(c -> {
            if (!Objects.equals(finalInvBuyer, null)) {
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

                cash = investorsCashService.create(cash);

                newInvestorsCash.setCashSource(null);
                newInvestorsCash.setId(null);
                newInvestorsCash.setGivedCash(newInvestorsCash.getGivedCash().negate());
                newInvestorsCash.setSourceId(cash.getId());
                newInvestorsCash.setSource(null);
                newInvestorsCash.setDateGivedCash(dateClose);
                newInvestorsCash.setDateClosingInvest(dateClose);
                newInvestorsCash.setTypeClosingInvest(closingInvest);

                ExecutorService service = Executors.newCachedThreadPool();
                List<InvestorsCash> cashes = new ArrayList<>(Arrays.asList(newInvestorsCash, cash));

                cashes.forEach(ca -> service.submit(() -> updateMailingGroups(ca, "add")));

                service.shutdown();
                investorsCashService.create(newInvestorsCash);

                c.setDateClosingInvest(dateClose);
                c.setTypeClosingInvest(closingInvest);
                investorsCashService.update(c);
            } else {
                c.setDateClosingInvest(dateClose);
                c.setTypeClosingInvest(typeClosingInvestService.findByTypeClosingInvest("Вывод"));
                investorsCashService.update(c);
            }

            updateMailingGroups(c, "delete");
        });

        response.setMessage("Массовое закрытие прошло успешно.");
        return response;
    }

    private void updateMailingGroups(InvestorsCash cash, String what) {
        Facilities facility = cash.getFacility();
        MailingGroups mailingGroup = mailingGroupsService.findByGroupWithUsers(facility.getFacility());
        MailingGroups currentInvestors = mailingGroupsService.findByGroupWithUsers("Текущие инвесторы");
        if (!Objects.equals(null, mailingGroup.getId())) {
            List<Users> users = mailingGroup.getUsers();
            List<Users> currInvestors = currentInvestors.getUsers();

            Users investor = userService.findById(cash.getInvestor().getId());
            switch (what) {
                case "add":
                    if (!mailingGroup.getUsers().contains(investor)) {
                        users.add(investor);
                        mailingGroup.setUsers(users);
                        mailingGroupsService.update(mailingGroup);
                    }
                    if (!currentInvestors.getUsers().contains(investor)) {
                        currInvestors.add(investor);
                        mailingGroupsService.update(currentInvestors);
                    }
                    break;
                case "delete":
                    List<InvestorsCash> investorsCashes = investorsCashService.findByInvestorAndFacility(
                            investor.getId(), facility.getId()
                    ).stream()
                            .filter(ic -> !ic.getId().equals(cash.getId()))
                            .collect(Collectors.toList());

                    if (investorsCashes.size() == 0) {
                        users.remove(investor);
                        mailingGroup.setUsers(users);
                        mailingGroupsService.update(mailingGroup);
                    }

                    List<InvestorsCash> currentCash = investorsCashService.findByInvestorId(investor.getId())
                            .stream()
                            .filter(ic -> !ic.getId().equals(cash.getId()))
                            .filter(ic -> Objects.equals(null, ic.getTypeClosingInvest()))
                            .filter(ic -> ic.getGivedCash() != null && ic.getGivedCash().compareTo(BigDecimal.ZERO) > 0)
                            .collect(Collectors.toList());
                    if (currentCash.size() == 0) {
                        if (currentInvestors.getUsers().contains(investor)) {
                            currInvestors.remove(investor);
                            currentInvestors.setUsers(currInvestors);
                            mailingGroupsService.update(currentInvestors);
                        }
                    }
                    break;
            }

        }
    }

    private Map<String, InvestorsCash> groupInvestorsCash(List<InvestorsCash> cashList, String what) {
        Map<String, InvestorsCash> map = new HashMap<>(0);

        cashList.forEach(ic -> {
            InvestorsCash keyMap;
            if ("sale".equals(what)) {
                keyMap = map.get(ic.getInvestor().getLogin() +
                        ic.getSourceUnderFacility().getUnderFacility());
            } else {
                keyMap = map.get(ic.getInvestor().getLogin() + ic.getSourceFacility().getFacility());
            }

            if (Objects.equals(null, keyMap)) {
                if ("sale".equals(what)) {
                    map.put(ic.getInvestor().getLogin() +
                                    ic.getSourceUnderFacility().getUnderFacility(),
                            ic);
                } else {
                    map.put(ic.getInvestor().getLogin() + ic.getSourceFacility().getFacility(),
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
                cash.setInvestorsType(ic.getInvestorsType());
                cash.setShareKind(ic.getShareKind());
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
                            ic.getSourceUnderFacility().getUnderFacility(), cash);
                } else {
                    map.put(ic.getInvestor().getLogin() + ic.getSourceFacility().getFacility(), cash);
                }

            }
        });

        return map;
    }

    @ModelAttribute("facilities")
    public List<Facilities> initializeFacilities() {
        return facilityService.initializeFacilities();
    }

    @ModelAttribute("sourceFacilities")
    public List<Facilities> initializeReFacilities() {
        return facilityService.initializeFacilities();
    }

    @ModelAttribute("investors")
    public List<Users> initializeInvestors() {
        return userService.initializeInvestors();
    }

    @ModelAttribute("investorsMulti")
    public List<Users> initializeInvestorsMultiple() {
        return userService.initializeMultipleInvestors();
    }

    @ModelAttribute("cashSources")
    public List<CashSources> initializeCashSources() {
        return cashSourcesService.initializeCashSources();
    }

    @ModelAttribute("cashTypes")
    public List<CashTypes> initializeCashTypes() {
        return cashTypesService.initializeCashTypes();
    }

    @ModelAttribute("newCashDetails")
    public List<NewCashDetails> initializeNewCashDetails() {
        return newCashDetailsService.initializeNewCashDetails();
    }

    @ModelAttribute("investorsTypes")
    public List<InvestorsTypes> initializeInvestorsTypes() {
        return investorsTypesService.initializeInvestorsTypes();
    }

    @ModelAttribute("underFacilities")
    public List<UnderFacilities> initializeUnderFacilities() {
        return underFacilitiesService.initializeUnderFacilities();
    }

    @ModelAttribute("sourceUnderFacilities")
    public List<UnderFacilities> initializeReUnderFacilities() {
        return underFacilitiesService.initializeUnderFacilities();
    }

    @ModelAttribute("typeClosingInvest")
    public List<TypeClosingInvest> initializeTypeClosingInvest() {
        return typeClosingInvestService.init();
    }

    @ModelAttribute("shareKinds")
    public List<ShareKind> initializeShareKinds() {
        return shareKindService.init();
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
