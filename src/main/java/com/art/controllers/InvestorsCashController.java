package com.art.controllers;

import com.art.func.GetPrincipalFunc;
import com.art.model.*;
import com.art.model.supporting.*;
import com.art.repository.MarketingTreeRepository;
import com.art.service.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
    private MarketingTreeRepository marketingTreeRepository;

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

    @Resource(name = "getPrincipalFunc")
    private GetPrincipalFunc getPrincipalFunc;

    @GetMapping(value = "/investorscash")
    public String investorsCashPage(ModelMap model) {

        List<InvestorsCash> investorsCashes = investorsCashService.findAll()
                .stream().filter(cash -> !Objects.equals(null, cash.getFacility())).collect(Collectors.toList());
        model.addAttribute("searchSummary", new SearchSummary());
        model.addAttribute("investorsCashes", investorsCashes);

        return "viewinvestorscash";
    }

    @GetMapping(value = "/investorscash/{pageNumber}")
    public String investorsCashPagePagination(ModelMap model, @PathVariable("pageNumber") int pageNumber) {
        PaginationResult result = investorsCashService.getAllWithPageable(pageNumber, new HashMap<>(0));
        List<InvestorsCash> investorsCashes = result.getList();
        int totalPages = result.getTotalPages();
//        int totalRecords = result.getTotalRecords();
        // 1 2 3 4 5 ... 11 12 13
        List<Integer> navPages = result.getNavigationPages();

        model.addAttribute("searchSummary", new SearchSummary());
        model.addAttribute("investorsCashes", investorsCashes);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("navPages", navPages);

        return "viewinvestorscash";
    }

    @PostMapping(value = "/investorscash/{pageNumber}")
    public ModelAndView investorsCashPagePagination(ModelMap model, @ModelAttribute("searchSummary") SearchSummary searchSummary) {

        String facility = searchSummary.getFacility();
        String underFacility = searchSummary.getUnderFacility();
        String investor = searchSummary.getInvestor();
        Date dateBegin = searchSummary.getDateStart();
        Date dateEnd = searchSummary.getDateEnd();
//        int pageNumber = searchSummary.getPageNumber();

        Map<String, String> search = new HashMap<>(0);
        search.put("facility", !Objects.equals("0", facility) ? facility : "");
        search.put("underFacility", !Objects.equals("Выберите подобъект", underFacility) ? underFacility : "");
        search.put("investor", !Objects.equals("0", investor) ? investor : "");
        search.put("dateBegin", !Objects.equals(null, dateBegin) ? String.valueOf(dateBegin.getTime()) : "");
        search.put("dateEnd", !Objects.equals(null, dateEnd) ? String.valueOf(dateEnd.getTime()) : "");

        PaginationResult result = investorsCashService.getAllWithPageable(searchSummary.getPageNumber(), search);
        List<InvestorsCash> investorsCashes = result.getList();
        int totalPages = result.getTotalPages();
//        int totalRecords = result.getTotalRecords();

        // 1 2 3 4 5 ... 11 12 13
        List<Integer> navPages = result.getNavigationPages();

        model.addAttribute("searchSummary", new SearchSummary());
        model.addAttribute("investorsCashes", investorsCashes);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("navPages", navPages);

        return new ModelAndView("viewinvestorscash", model);
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
        List<InvestorsCash> cashList = investorsCashService.findByIdIn(searchSummary.getCashIdList());
        List<AfterCashing> afterCashingList = afterCashingService.findAll();
        Comparator<AfterCashing> comparator = Comparator.comparing(AfterCashing::getId);

        afterCashingList.sort(comparator.reversed());

        cashList.forEach(deleting -> {
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
                        if (afterCashing.size() > 0) {
                            List<InvestorsCash> childCash = investorsCashService.findBySource(deleting.getSource());
                            AfterCashing cashToDel = afterCashing.stream()
                                    .filter(ac -> ac.getOldId().equals(parentCashId))
                                    .findFirst().orElse(afterCashing.get(0));
                            parentCash.setGivedCash(cashToDel.getOldValue());
                            childCash.forEach(cbs -> investorsCashService.deleteById(cbs.getId()));
                            afterCashingService.deleteById(cashToDel.getId());
                        }

                        parentCash.setIsReinvest(0);
                        parentCash.setIsDivide(0);
                        parentCash.setTypeClosingInvest(null);
                        parentCash.setDateClosingInvest(null);

                        if (deleting.getFacility().equals(parentCash.getFacility()) &&
                                deleting.getInvestor().equals(parentCash.getInvestor()) &&
                                deleting.getCashType().equals(parentCash.getCashType()) &&
                                deleting.getInvestorsType().equals(parentCash.getInvestorsType()) &&
                                deleting.getShareKind().equals(parentCash.getShareKind()) &&
                                Objects.equals(null, deleting.getTypeClosingInvest()) &&
                                deleting.getDateGivedCash().compareTo(parentCash.getDateGivedCash()) == 0) {
                            parentCash.setGivedCash(parentCash.getGivedCash().add(deleting.getGivedCash()));
                        }

                        investorsCashService.update(parentCash);
                        List<InvestorsCash> oldInvCash = investorsCashService.findBySourceId(parentCashId);
                        oldInvCash = oldInvCash.stream().filter(oc -> !deleting.getId().equals(oc.getId())).collect(Collectors.toList());
                        if (oldInvCash.size() > 0) {
                            oldInvCash.forEach(oCash -> {
                                parentCash.setGivedCash(parentCash.getGivedCash().add(oCash.getGivedCash()));
                                investorsCashService.deleteById(oCash.getId());
                            });
                        }
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

            new Thread(() -> updateMailingGroups(deleting, "delete")).start();
            investorsCashService.deleteById(deleting.getId());
            response.setMessage("Данные успешно удалены");

        });
        return response;
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
    public @ResponseBody GenericResponse allMoneyCashing(@RequestBody SearchSummary searchSummary) {

        GenericResponse response = new GenericResponse();

        UnderFacilities underFacility = null;
        if (!searchSummary.getInvestorsCash().getUnderFacility().getUnderFacility().equals("Выберите подобъект")) {
            underFacility = underFacilitiesService.findByUnderFacility(searchSummary.getInvestorsCash().getUnderFacility().getUnderFacility());
        }

        searchSummary.getInvestorsCash().setUnderFacility(underFacility);
        if (cashingMoney(searchSummary, true)) {
            response.setMessage("Деньги инвестора " + searchSummary.getUser().getLogin() + " успешно выведены.");
        } else {
            response.setMessage("Что-то пошло не так ): ");
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
        String ret = "списку денег инвестора";
        String redirectUrl = "/investorscash/1";
        InvestorsCash cashForGetting = searchSummary.getInvestorsCash();

        if (cashingMoney(searchSummary, false)) {

            model.addAttribute("success", "Деньги инвестора " + cashForGetting.getInvestor().getLogin() +
                    " успешно выведены.");
            model.addAttribute("redirectUrl", redirectUrl);
            model.addAttribute("ret", ret);
            return "registrationsuccess";
        } else {
            return "getInvestorsCash";
        }
    }

    private boolean cashingMoney(SearchSummary searchSummary, boolean all) {

        InvestorsCash commissionCash = new InvestorsCash();
        TypeClosingInvest typeClosingInvest = typeClosingInvestService.findByTypeClosingInvest("Вывод");
        TypeClosingInvest typeClosingCommission = typeClosingInvestService.findByTypeClosingInvest("Вывод_комиссия");
        InvestorsCash cashForGetting = searchSummary.getInvestorsCash();
        BigDecimal commission = searchSummary.getCommission();
        BigDecimal commissionNoMore = searchSummary.getCommissionNoMore();

        commissionCash.setFacility(cashForGetting.getFacility());
        commissionCash.setUnderFacility(cashForGetting.getUnderFacility());
        commissionCash.setInvestor(cashForGetting.getInvestor());
        commissionCash.setTypeClosingInvest(typeClosingCommission);
        commissionCash.setDateClosingInvest(cashForGetting.getDateGivedCash());
        BigDecimal totalSum = new BigDecimal(BigInteger.ZERO);
        final BigDecimal[] remainderSum = new BigDecimal[1]; // сумма, которую надо вывести

        List<InvestorsCash> investorsCashes = investorsCashService.findByInvestorId(cashForGetting.getInvestor().getId())
                .stream()
                .filter(investorsCash ->
                        investorsCash.getFacility().getFacility().equalsIgnoreCase(cashForGetting.getFacility().getFacility()) &&
                                investorsCash.getGivedCash().compareTo(BigDecimal.ZERO) > 0 &&
                                Objects.equals(null, investorsCash.getTypeClosingInvest()) &&
                                (Objects.equals(null, cashForGetting.getUnderFacility()) || Objects.equals(cashForGetting.getUnderFacility(), investorsCash.getUnderFacility())))
                .sorted(Comparator.comparing(InvestorsCash::getDateGivedCash))
                .collect(Collectors.toList());
        BigDecimal sumCash = investorsCashes.stream().map(InvestorsCash::getGivedCash).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (all) {
            commission = (sumCash.multiply(commission)).divide(new BigDecimal(100), BigDecimal.ROUND_CEILING);
            if (commissionNoMore != null && commission.compareTo(commissionNoMore) > 0) {
                commission = commissionNoMore;
            }
            remainderSum[0] = sumCash;
            cashForGetting.setGivedCash(sumCash.subtract(commission));
        } else {
            commission = (cashForGetting.getGivedCash().multiply(commission)).divide(new BigDecimal(100), BigDecimal.ROUND_CEILING);
            if (commissionNoMore != null && commission.compareTo(commissionNoMore) > 0) {
                commission = commissionNoMore;
            }
            totalSum = cashForGetting.getGivedCash().add(commission);
            remainderSum[0] = totalSum;
        }
        commissionCash.setGivedCash(commission);

        if ((sumCash.compareTo(totalSum)) < 0) {
            return false;
        }

        List<AfterCashing> cashingList = new ArrayList<>(0);
        BigDecimal finalCommission = commission;
        remainderSum[0] = remainderSum[0].subtract(commission);
        InvestorsCash remainderCash = new InvestorsCash();
        investorsCashes.forEach(ic -> {
            if (remainderSum[0].compareTo(BigDecimal.ZERO) > 0) {
                if (ic.getGivedCash().subtract(remainderSum[0]).compareTo(BigDecimal.ZERO) < 0) {
                    remainderSum[0] = remainderSum[0].subtract(ic.getGivedCash());
                    cashingList.add(new AfterCashing(ic.getId(), ic.getGivedCash()));
                    ic.setTypeClosingInvest(typeClosingInvest);
                    ic.setDateClosingInvest(cashForGetting.getDateGivedCash());
                } else {
                    cashingList.add(new AfterCashing(ic.getId(), ic.getGivedCash()));
                    remainderCash.setGivedCash(remainderSum[0]);
                    remainderCash.setDateGivedCash(ic.getDateGivedCash());
                    remainderCash.setFacility(ic.getFacility());
                    remainderCash.setInvestor(ic.getInvestor());
                    remainderCash.setCashSource(ic.getCashSource());
                    remainderCash.setCashType(ic.getCashType());
                    remainderCash.setNewCashDetails(ic.getNewCashDetails());
                    remainderCash.setInvestorsType(ic.getInvestorsType());
                    remainderCash.setUnderFacility(ic.getUnderFacility());
                    remainderCash.setDateClosingInvest(cashForGetting.getDateGivedCash());
                    remainderCash.setTypeClosingInvest(typeClosingInvest);
                    remainderCash.setShareKind(ic.getShareKind());
                    remainderCash.setDateReport(ic.getDateReport());
                    remainderCash.setSourceFacility(ic.getSourceFacility());
                    remainderCash.setSourceUnderFacility(ic.getSourceUnderFacility());
                    remainderCash.setSourceFlowsId(ic.getSourceFlowsId());
                    remainderCash.setRoom(ic.getRoom());
                    remainderCash.setIsReinvest(ic.getIsReinvest());
                    remainderCash.setSourceId(ic.getSourceId());
                    remainderCash.setIsDivide(ic.getIsDivide());
                    ic.setGivedCash(ic.getGivedCash().subtract(remainderSum[0]).subtract(finalCommission));
                    remainderSum[0] = BigDecimal.ZERO;
                }
                investorsCashService.update(ic);
                cashForGetting.setCashSource(ic.getCashSource());
                cashForGetting.setCashType(ic.getCashType());
                cashForGetting.setNewCashDetails(ic.getNewCashDetails());
                cashForGetting.setInvestorsType(ic.getInvestorsType());
                cashForGetting.setShareKind(ic.getShareKind());
                cashForGetting.setDateReport(ic.getDateReport());
                cashForGetting.setSourceFacility(ic.getSourceFacility());
                cashForGetting.setSourceUnderFacility(ic.getSourceUnderFacility());
                cashForGetting.setRoom(ic.getRoom());
                cashForGetting.setSource(cashForGetting.getSource() == null ? "" + ic.getId().toString() : cashForGetting.getSource() + "|" + ic.getId().toString());
                cashForGetting.setDateClosingInvest(cashForGetting.getDateGivedCash());

                commissionCash.setDateGivedCash(ic.getDateGivedCash());
                commissionCash.setCashSource(ic.getCashSource());
                commissionCash.setCashType(ic.getCashType());
                commissionCash.setNewCashDetails(ic.getNewCashDetails());
                commissionCash.setInvestorsType(ic.getInvestorsType());
                commissionCash.setShareKind(ic.getShareKind());
                commissionCash.setDateReport(ic.getDateReport());
                commissionCash.setSourceFacility(ic.getSourceFacility());
                commissionCash.setSourceUnderFacility(ic.getSourceUnderFacility());
                commissionCash.setRoom(ic.getRoom());
                commissionCash.setSource(commissionCash.getSource() == null ? "" + ic.getId().toString() : commissionCash.getSource() + "|" + ic.getId().toString());

                remainderCash.setSource(cashForGetting.getSource());
            } else {
                ic.setTypeClosingInvest(typeClosingInvest);
                ic.setDateClosingInvest(cashForGetting.getDateGivedCash());
                investorsCashService.update(ic);
            }
        });

        if (cashForGetting.getSource().equalsIgnoreCase("")) cashForGetting.setSource(null);
        if (commissionCash.getSource().equalsIgnoreCase("")) cashForGetting.setSource(null);

        cashingList.forEach(cl -> afterCashingService.create(cl));
        cashForGetting.setTypeClosingInvest(typeClosingInvest);
        investorsCashService.create(remainderCash);
        investorsCashService.create(commissionCash);

        cashForGetting.setGivedCash(cashForGetting.getGivedCash().negate());
        commissionCash.setGivedCash(commissionCash.getGivedCash().negate());
        commissionCash.setDateGivedCash(cashForGetting.getDateGivedCash());
        investorsCashService.create(cashForGetting);
        investorsCashService.create(commissionCash);

        return true;
    }

    @PostMapping(value = "/getRating", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getRating() {
        String myLogin = getPrincipalFunc.getLogin();
        List<InvestorsCash> myCash = new ArrayList<>(0);
        List<InvestorsCash> investorsCashes = investorsCashService.findAll();
        myCash.addAll(investorsCashes.stream().filter(ic ->
                !Objects.equals(null, ic.getInvestor()) &&
                        ic.getInvestor().getLogin().equalsIgnoreCase(myLogin) &&
                        Objects.equals(null, ic.getTypeClosingInvest())
        ).collect(Collectors.toList()));

        investorsCashes = investorsCashes
                .stream()
                .filter(investorsCash ->
                        investorsCash.getGivedCash().compareTo(new BigDecimal(200000)) >= 0 &&
                                Objects.equals(null, investorsCash.getTypeClosingInvest()))
                .collect(Collectors.toList());

        Map<String, BigDecimal> myMap = new HashMap<>(0);
        myCash.forEach(ic -> myMap.merge(ic.getInvestor().getLogin(), ic.getGivedCash(), BigDecimal::add));

        Map<String, BigDecimal> map = new HashMap<>(0);

        Map<String, BigDecimal> finalMap = map;
        investorsCashes.forEach(ic -> finalMap.merge(ic.getInvestor().getLogin(), ic.getGivedCash(), BigDecimal::add));
        map = finalMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        JSONObject inputData = new JSONObject();
        JSONObject myData = new JSONObject();
        JSONArray otherDatas = new JSONArray();
        final JSONObject[] otherData = {new JSONObject()};
        final int[] place = {0};

        map.forEach((k, v) -> {
            place[0]++;
            if (k.equalsIgnoreCase(myLogin)) {
                myData.put("place", place[0]);
                myData.put("value", v);
            }
            otherData[0] = new JSONObject();
            otherData[0].put("name", k);
            otherData[0].put("value", v);
            otherDatas.put(otherData[0]);
        });
        if (myData.length() == 0) {
            myData.put("place", map.size() + 1);
            myData.put("value", myMap.get(myLogin));
        }
        inputData.put("userData", myData);
        inputData.put("otherDatas", otherDatas);

        return inputData.toString();
    }

    @PostMapping(value = {"/newinvestorscash"})
    public String saveCash(@ModelAttribute("investorsCash") InvestorsCash investorsCash,
                           BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "addinvestorscash";
        }
        String ret = "списку денег инвестора";
        String redirectUrl = "/investorscash/1";
        updateMailingGroups(investorsCash, "add");
        addFacility(investorsCash);
        investorsCashService.create(investorsCash);
        if (!investorsCash.getCashSource().getCashSource().equalsIgnoreCase("Бронь")) {
            marketingTreeRepository.updateMarketingTree(investorsCash.getInvestor().getId());
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
            addFacility(newInvestorsCash);
            investorsCashService.create(newInvestorsCash);
            whatWeDoWithCash = "добавлены.";
        }
        investorsCash = investorsCashService.update(investorsCash);
        if (!Objects.equals(null, searchSummary.getWhat()) &&
                searchSummary.getWhat().equalsIgnoreCase("closeCash")) {
            updateMailingGroups(investorsCash, "delete");
        } else {
            updateMailingGroups(investorsCash, "add");
            addFacility(investorsCash);
        }
        response.setMessage("Деньги инвестора " + invLogin + " успешно " + whatWeDoWithCash);
        return response;
    }

    @PostMapping(value = "/edit-cash-{id}")
    public String editCash(@ModelAttribute("investorsCash") InvestorsCash investorsCash, @PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("viewinvestorscash");

        updateMailingGroups(investorsCash, "add");
        addFacility(investorsCash);

        investorsCashService.update(investorsCash);
        SearchSummary searchSummary = new SearchSummary();
        modelAndView.addObject("searchSummary", searchSummary);
        modelAndView.addObject("investorsCash", investorsCashService.findAll());

        return "redirect: /investorscash/1";
    }

    @PostMapping(value = "/double-cash-{id}")
    public ModelAndView doubleCash(@ModelAttribute("investorsCash") InvestorsCash investorsCash, @PathVariable("id") int id) {
        ModelAndView model = new ModelAndView("addinvestorscash");
        InvestorsCash newInvestorsCash = investorsCashService.findById(investorsCash.getId());
        InvestorsCash inMemoryCash = investorsCashService.findById(investorsCash.getId());
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
        newInvestorsCash.setSourceFacility(investorsCash.getFacility());
        newInvestorsCash.setDateReport(null);
        investorsCash.setUnderFacility(inMemoryCash.getUnderFacility());
        investorsCash.setIsDivide(1);

        List<InvestorsCash> cash = new ArrayList<>(4);
        cash.add(newInvestorsCash);
        cash.add(investorsCash);

        ExecutorService service = Executors.newCachedThreadPool();

        cash.forEach(c -> service.submit(() -> {
            updateMailingGroups(c, "add");
            addFacility(c);
        }));

        investorsCashService.update(newInvestorsCash);
        investorsCashService.update(investorsCash);
        model.addObject("investorsCash", investorsCash);

        return model;
    }

    @PostMapping(value = {"/saveReCash"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse saveReCash(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        List<InvestorsCash> investorsCashes = searchSummary.getInvestorsCashList();
        InvestorsTypes investorsType = investorsTypesService.findByInvestorsTypes("Старый инвестор");
        CashTypes cashTypes = cashTypesService.findByCashType("Новые деньги");
        NewCashDetails newCashDetails;

        switch (searchSummary.getWhat()) {
            case "sale":
                newCashDetails = newCashDetailsService.findByNewCashDetail("Реинвестирование с продажи (прибыль)");
                List<InvestorsFlowsSale> flowsSales = investorsFlowsSaleService.
                        findByIdInWithAllFields(searchSummary.getReinvestIdList());
                flowsSales.forEach(f -> {
                    f.setIsReinvest(1);
                    investorsFlowsSaleService.update(f);
                });
                break;
            default:
                newCashDetails = newCashDetailsService.findByNewCashDetail("Реинвестирование с аренды");
                List<InvestorsFlows> flows = investorsFlowsService.findByIdIn(searchSummary.getReinvestIdList());
                flows.forEach(f -> f.setIsReinvest(1));
                investorsFlowsService.saveList(flows);
                break;
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
                addFacility(value);
                investorsCashService.create(value);
            });

            response.setMessage("Реинвестирование прошло успешно");

        } catch (Exception ex) {
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
        CashTypes cashTypes = cashTypesService.findByCashType("Старые деньги");
        NewCashDetails newCashDetails = newCashDetailsService.findByNewCashDetail("Реинвестирование с продажи (сохранение)");
        InvestorsTypes investorsType = investorsTypesService.findByInvestorsTypes("Старый инвестор");
        TypeClosingInvest typeClosingInvest = typeClosingInvestService.findByTypeClosingInvest("Реинвестирование");

        Map<String, InvestorsCash> map = groupInvestorsCash(investorsCashes, "");

        map.forEach((key, value) -> {
            value.setCashType(cashTypes);
            value.setNewCashDetails(newCashDetails);
            value.setInvestorsType(investorsType);
            value.setGivedCash(value.getGivedCash().setScale(2, RoundingMode.DOWN));
            dateClose[0] = value.getDateGivedCash();
            updateMailingGroups(value, "add");
            addFacility(value);
            investorsCashService.create(value);
        });

        List<InvestorsCash> oldCash = investorsCashService.findByIdIn(searchSummary.getReinvestIdList());
        Date finalDateClose = dateClose[0];
        oldCash.forEach(f -> {
            f.setIsReinvest(1);
            f.setDateClosingInvest(finalDateClose);
            f.setTypeClosingInvest(typeClosingInvest);
            updateMailingGroups(f, "add");
            addFacility(f);
            investorsCashService.create(f);
        });

        response.setMessage("Реинвестирование прошло успешно");

        return response;
    }

    @PostMapping(value = {"/saveDivideCash"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse saveDivideCash(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        List<InvestorsCash> investorsCashes = searchSummary.getInvestorsCashList();

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

        //underFacilitiesList.forEach(u -> rooms.addAll(u.getRooms()));
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
            addFacility(f);
            addFacility(cash);
            if (f.getGivedCash().signum() == 0) {
                List<InvestorsCash> cashList = investorsCashService.findBySourceId(f.getId());
                if (cashList.size() > 0) {
                    cashList.forEach(c -> {
                        c.setSourceId(null);
                        investorsCashService.update(c);
                    });
                }
                investorsCashService.deleteById(f.getId());
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
//            cash.setDateClosingInvest(dateClosingInvest);
//            cash.setTypeClosingInvest(closingInvest);

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
                addFacility(c);
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
            addFacility(updatedCash);

        }
        modelAndView.addObject("investorsCash", investorsCashService.findAll());
        return "redirect: /investorscash/1";
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
                cash.setNewCashDetails(newCashDetails);

                cash = investorsCashService.create(cash);

                newInvestorsCash.setCashSource(null);
                newInvestorsCash.setId(null);
                newInvestorsCash.setGivedCash(newInvestorsCash.getGivedCash().negate());
                newInvestorsCash.setSourceId(cash.getId());
                newInvestorsCash.setDateGivedCash(dateClose);
                newInvestorsCash.setDateClosingInvest(dateClose);
                newInvestorsCash.setTypeClosingInvest(closingInvest);

                ExecutorService service = Executors.newCachedThreadPool();
                List<InvestorsCash> cashes = new ArrayList<>(Arrays.asList(newInvestorsCash, cash));

                cashes.forEach(ca -> service.submit(() -> {
                    updateMailingGroups(ca, "add");
                    addFacility(ca);
                }));

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
            addFacility(c);
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
                            .filter(ic -> ic.getGivedCash().compareTo(BigDecimal.ZERO) > 0)
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

    private void addFacility(InvestorsCash cash) {
        Facilities facility = facilityService.findByIdWithInvestors(cash.getFacility().getId());
        Users investor = userService.findByIdWithFacilities(cash.getInvestor().getId());
        Set<Users> investors = facility.getInvestors();
        if (!investors.contains(investor)) {
            investors.add(investor);
            facility.setInvestors(investors);
            facilityService.merge(facility);
        }
    }

    private Map<String, InvestorsCash> groupInvestorsCash(List<InvestorsCash> cashList, String what) {
        Map<String, InvestorsCash> map = new HashMap<>(0);

        cashList.forEach(ic -> {
            InvestorsCash keyMap;
            switch (what) {
                case "sale":
                    keyMap = map.get(ic.getInvestor().getLogin() +
                            ic.getSourceUnderFacility().getUnderFacility());
                    break;

                default:
                    keyMap = map.get(ic.getInvestor().getLogin() + ic.getSourceFacility().getFacility());
                    break;
            }

            if (Objects.equals(null, keyMap)) {
                switch (what) {
                    case "sale":
                        map.put(ic.getInvestor().getLogin() +
                                        ic.getSourceUnderFacility().getUnderFacility(),
                                ic);
                        break;
                    default:
                        map.put(ic.getInvestor().getLogin() + ic.getSourceFacility().getFacility(),
                                ic);
                        break;
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
                switch (what) {
                    case "sale":
                        map.put(ic.getInvestor().getLogin() +
                                ic.getSourceUnderFacility().getUnderFacility(), cash);
                        break;
                    default:
                        map.put(ic.getInvestor().getLogin() + ic.getSourceFacility().getFacility(), cash);
                        break;
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
    public List<ShareKind> initializehareKinds() {
        return shareKindService.init();
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
