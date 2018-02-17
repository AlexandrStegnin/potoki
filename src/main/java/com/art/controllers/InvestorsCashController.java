package com.art.controllers;

import com.art.model.*;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.SearchSummary;
import com.art.service.*;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
public class InvestorsCashController {

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

    @GetMapping(value = "/investorscash")
    public String investorsCashPage(ModelMap model) {

        List<InvestorsCash> investorsCashes = investorsCashService.findAll()
                .stream().filter(cash -> !Objects.equals(null, cash.getFacility())).collect(Collectors.toList());
        model.addAttribute("searchSummary", new SearchSummary());
        model.addAttribute("investorsCashes", investorsCashes);

        return "viewinvestorscash";
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
        cashList.forEach(c -> {
            if (!Objects.equals(null, c.getSourceFlowsId())) {
                String[] tmp = c.getSourceFlowsId().split(Pattern.quote("|"));
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
            if (!Objects.equals(null, c.getSource())) {

                String[] tmp = c.getSource().split(Pattern.quote("|"));
                List<BigInteger> sourceIdList = new ArrayList<>(0);
                for (String bigInt : tmp) {
                    sourceIdList.add(new BigInteger(bigInt));
                }

                sourceIdList.forEach(id -> {
                    InvestorsCash investorsCash = investorsCashService.findById(id);
                    if (!Objects.equals(null, investorsCash)) {
                        investorsCash.setIsReinvest(0);
                        investorsCash.setIsDivide(0);
                        investorsCash.setTypeClosingInvest(null);
                        investorsCash.setDateClosingInvest(null);

                        if (c.getFacility().equals(investorsCash.getFacility()) &&
                                c.getInvestor().equals(investorsCash.getInvestor()) &&
                                c.getCashType().equals(investorsCash.getCashType()) &&
                                c.getInvestorsType().equals(investorsCash.getInvestorsType()) &&
                                c.getShareKind().equals(investorsCash.getShareKind()) &&
                                Objects.equals(null, c.getTypeClosingInvest()) &&
                                c.getDateGivedCash().compareTo(investorsCash.getDateGivedCash()) == 0) {
                            investorsCash.setGivedCash(investorsCash.getGivedCash().add(c.getGivedCash()));
                        }

                        investorsCashService.update(investorsCash);
                        List<InvestorsCash> oldInvCash = investorsCashService.findBySourceId(id);
                        oldInvCash = oldInvCash.stream().filter(oc -> !c.getId().equals(oc.getId())).collect(Collectors.toList());
                        if (oldInvCash.size() > 0) {
                            oldInvCash.forEach(oCash -> {
                                investorsCash.setGivedCash(investorsCash.getGivedCash().add(oCash.getGivedCash()));
                                investorsCashService.deleteById(oCash.getId());
                            });
                        }
                    }

                });
            }

            List<InvestorsCash> cash = investorsCashService.findBySourceId(c.getId());
            if (cash.size() != 0) {
                cash.forEach(ca -> {
                    if (ca.getGivedCash().signum() == -1) {
                        investorsCashService.deleteById(ca.getId());
                    } else {
                        ca.setSourceId(null);
                        investorsCashService.update(ca);
                    }
                });
            }

            if (!Objects.equals(null, c.getSourceId())) {
                InvestorsCash investorsCash = investorsCashService.findById(c.getSourceId());
                if (!Objects.equals(null, investorsCash)) {
                    if (investorsCash.getGivedCash().signum() == -1) {
                        investorsCashService.deleteById(investorsCash.getId());
                    } else {
                        investorsCash.setTypeClosingInvest(null);
                        investorsCash.setDateClosingInvest(null);
                        investorsCashService.update(investorsCash);
                    }
                }
            }

            updateMailingGroups(c, "delete");
            investorsCashService.deleteById(c.getId());
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

    @PostMapping(value = {"/newinvestorscash"})
    public String saveCash(@ModelAttribute("investorsCash") InvestorsCash investorsCash,
                           BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "addinvestorscash";
        }
        String ret = "списку денег инвестора";
        String redirectUrl = "/investorscash";
        //updateMailingGroups(investorsCash.getFacility(), investorsCash.getInvestor(), "add");
        investorsCashService.create(investorsCash);

        model.addAttribute("success", "Деньги инвестора " + investorsCash.getInvestor().getLogin() +
                " успешно добавлены.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @RequestMapping(value = "/searchCash", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String searchCashPage(@RequestBody SearchSummary searchSummary,
                          SecurityContextHolderAwareRequestWrapper request) {
        boolean admin = request.isUserInRole("ROLE_ADMIN");
        boolean dba = request.isUserInRole("ROLE_DBA");
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Locale RU = new Locale("ru", "RU");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(RU);
        BigInteger facilityId = new BigInteger(searchSummary.getFacility());
        BigInteger investorId = new BigInteger(searchSummary.getInvestor());
        BigInteger underFacilityId = new BigInteger(searchSummary.getUnderFacility());
        Facilities facility = facilityService.findById(facilityId);
        Users investor = userService.findById(investorId);
        UnderFacilities underFacilities = underFacilitiesService.findById(underFacilityId);
        Date dateBeg = new Date();
        Date dateEnd = new Date();
        List<InvestorsCash> tempList = investorsCashService.findAllOrderByDateGivedCashAsc()
                .stream().filter(l -> l.getGivedCash().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
        List<InvestorsCash> investorsCashes = new ArrayList<>(0);
        if (searchSummary.getDateStart() == null) {
            dateBeg = tempList
                    .get(0).getDateGivedCash();
        } else {
            try {
                dateBeg = formatDate.parse(formatDate.format(searchSummary.getDateStart()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (searchSummary.getDateEnd() == null) {
            dateEnd = tempList.get(tempList.size() - 1).getDateGivedCash();
        } else {
            try {
                dateEnd = formatDate.parse(formatDate.format(searchSummary.getDateEnd()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Date finalDateBeg = dateBeg;
        Date finalDateEnd = dateEnd;

        tempList = tempList.stream()
                .filter(tl -> (tl.getDateGivedCash().compareTo(finalDateBeg) == 0 ||
                        tl.getDateGivedCash().compareTo(finalDateBeg) > 0) &&
                        (tl.getDateGivedCash().compareTo(finalDateEnd) == 0 ||
                                tl.getDateGivedCash().compareTo(finalDateEnd) < 0))
                .collect(Collectors.toList());

        if (!Objects.equals(facility, null) && !Objects.equals(investor, null) && !Objects.equals(underFacilities, null)) {
            investorsCashes = tempList.stream()
                    .filter(cash -> !Objects.equals(cash.getFacility(), null) && cash.getFacility().equals(facility))
                    .filter(cash -> !Objects.equals(cash.getInvestor(), null) && cash.getInvestor().equals(investor))
                    .filter(cash -> !Objects.equals(cash.getUnderFacility(), null) && cash.getUnderFacility().equals(underFacilities))
                    .collect(Collectors.toList());
        } else if (!Objects.equals(facility, null) && Objects.equals(investor, null) && Objects.equals(underFacilities, null)) {
            investorsCashes = tempList.stream()
                    .filter(cash -> !Objects.equals(cash.getFacility(), null) && cash.getFacility().getFacility().equals(facility.getFacility()))
                    .collect(Collectors.toList());
        } else if (Objects.equals(facility, null) && !Objects.equals(investor, null) && Objects.equals(underFacilities, null)) {
            investorsCashes = tempList.stream()
                    .filter(cash -> !Objects.equals(cash.getInvestor(), null) && cash.getInvestor().equals(investor))
                    .collect(Collectors.toList());
        } else if (Objects.equals(facility, null) && Objects.equals(investor, null) && Objects.equals(underFacilities, null)) {
            investorsCashes = tempList;
        } else if (!Objects.equals(facility, null) && !Objects.equals(investor, null) && Objects.equals(underFacilities, null)) {
            investorsCashes = tempList.stream()
                    .filter(cash -> !Objects.equals(cash.getFacility(), null) && cash.getFacility().equals(facility))
                    .filter(cash -> !Objects.equals(cash.getInvestor(), null) && cash.getInvestor().equals(investor))
                    .collect(Collectors.toList());
        } else if (!Objects.equals(facility, null) && Objects.equals(investor, null) && !Objects.equals(underFacilities, null)) {
            investorsCashes = tempList.stream()
                    .filter(cash -> !Objects.equals(cash.getFacility(), null) && cash.getFacility().equals(facility))
                    .filter(cash -> !Objects.equals(cash.getUnderFacility(), null) && cash.getUnderFacility().equals(underFacilities))
                    .collect(Collectors.toList());
        } else if (Objects.equals(facility, null) && !Objects.equals(investor, null) && !Objects.equals(underFacilities, null)) {
            investorsCashes = tempList.stream()
                    .filter(cash -> !Objects.equals(cash.getInvestor(), null) && cash.getInvestor().equals(investor))
                    .filter(cash -> !Objects.equals(cash.getUnderFacility(), null) && cash.getUnderFacility().equals(underFacilities))
                    .collect(Collectors.toList());
        } else if (Objects.equals(facility, null) && Objects.equals(investor, null) && !Objects.equals(underFacilities, null)) {
            investorsCashes = tempList.stream()
                    .filter(cash -> !Objects.equals(cash.getUnderFacility(), null) && cash.getUnderFacility().equals(underFacilities))
                    .collect(Collectors.toList());
        }

        StringBuilder result;
        result = new StringBuilder("<thead><tr><th>№ п/п</th><th>Объект</th><th>Подобъект</th><th>Инвестор</th>" +
                "<th>Переданная сумма</th><th>Дата передачи денег</th><th>Источник денег</th>" +
                "<th>Вид денег</th><th>Детали новых денег</th><th>Вид инвестора</th><th>Дата закрытия вложения</th>" +
                "<th>Вид закрытия вложения</th>");
        if (admin || dba) {
            result.append("<th style='text-align: center' colspan='4'>Действие</th>");
        }

        String cashSource;
        String cashType;
        String cashDetail;
        String investorType;
        String investorLogin;
        String strFacility;
        String strUnderFacility;
        String dateClosingInvest;
        String typeClosingInvest;

        result.append("</tr></thead><tbody>");
        for (InvestorsCash cash : investorsCashes) {

            cashSource = "";
            cashType = "";
            cashDetail = "";
            investorType = "";
            investorLogin = "";
            strFacility = "";
            strUnderFacility = "";
            dateClosingInvest = "";
            typeClosingInvest = "";

            if (!Objects.equals(cash.getCashSource(), null)) {
                cashSource = cash.getCashSource().getCashSource();
            }
            if (!Objects.equals(cash.getCashType(), null)) {
                cashType = cash.getCashType().getCashType();
            }
            if (!Objects.equals(cash.getNewCashDetails(), null)) {
                cashDetail = cash.getNewCashDetails().getNewCashDetail();
            }
            if (!Objects.equals(cash.getInvestorsType(), null)) {
                investorType = cash.getInvestorsType().getInvestorsType();
            }
            if (!Objects.equals(cash.getFacility(), null)) {
                strFacility = cash.getFacility().getFacility();
            }
            if (!Objects.equals(cash.getInvestor(), null)) {
                investorLogin = cash.getInvestor().getLogin();
            }
            if (!Objects.equals(cash.getUnderFacility(), null)) {
                strUnderFacility = cash.getUnderFacility().getUnderFacility();
            }
            if (!Objects.equals(cash.getDateClosingInvest(), null)) {
                dateClosingInvest = cash.getDateClosingInvestToLocalDate();
            }
            if (!Objects.equals(cash.getTypeClosingInvest(), null)) {
                typeClosingInvest = cash.getTypeClosingInvest().getTypeClosingInvest();
            }

            result.append("<tr>")
                    .append("<td>").append(cash.getId()).append("</td>")
                    .append("<td>").append(strFacility).append("</td>")
                    .append("<td>").append(strUnderFacility).append("</td>")
                    .append("<td>").append(investorLogin).append("</td>")
                    .append("<td>").append(fmt.format(cash.getGivedCash())).append("</td>")
                    .append("<td>").append(cash.getDateGivedCashToLocalDate()).append("</td>")
                    .append("<td>").append(cashSource).append("</td>")
                    .append("<td>").append(cashType).append("</td>")
                    .append("<td>").append(cashDetail).append("</td>")
                    .append("<td>").append(investorType).append("</td>")
                    .append("<td>").append(dateClosingInvest).append("</td>")
                    .append("<td>").append(typeClosingInvest).append("</td>");
            if (admin || dba) {
                result.append("<td><a href='/edit-cash-").append(cash.getId()).append("'")
                        .append(" class='btn btn-success custom-width'>Изменить</a></td>");
                result.append("<td><a href='/double-cash-").append(cash.getId()).append("'")
                        .append(" class='btn btn-primary custom-width'>Разделить</a></td>");
                result.append("<td><a href='/close-cash-").append(cash.getId()).append("'")
                        .append(" class='btn btn-primary custom-width'>Закрыть</a></td>");
            }
            if (admin) {
                result.append("<td><a href='/delete-cash-").append(cash.getId()).append("'")
                        .append(" class='btn btn-danger custom-width'>Удалить</a></td>");
            }
            result.append("</tr>")
                    .append("</tbody>");
        }
        return result.toString();
    }

    @PostMapping(value = {"/saveCash"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse saveCash(@RequestBody SearchSummary searchSummary) {
        Users invBuyer = null;
        if (!Objects.equals(searchSummary.getUser(), null)) {
            invBuyer = userService.findById(searchSummary.getUser().getId());
        }

        GenericResponse response = new GenericResponse();
        InvestorsCash investorsCash = searchSummary.getInvestorsCash();

        Facilities reFacility = searchSummary.getReFacility();
        UnderFacilities reUnderFacility = searchSummary.getReUnderFacility();
        Date reinvestDate = searchSummary.getDateReinvest();

        String whatWeDoWithCash = "обновлены.";

        String invLogin = investorsCash.getInvestor().getLogin();

        if (!Objects.equals(invBuyer, null)) {
            InvestorsCash cash = investorsCashService.findById(searchSummary.getInvestorsCash().getId());
            InvestorsCash newInvestorsCash = investorsCashService.findById(searchSummary.getInvestorsCash().getId());
            cash.setId(null);
            cash.setInvestor(invBuyer);
            cash.setDateGivedCash(searchSummary.getInvestorsCash().getDateClosingInvest());
            cash.setSourceId(searchSummary.getInvestorsCash().getId());
            cash.setCashSource(null);
            newInvestorsCash.setCashSource(null);
            NewCashDetails newCashDetails = newCashDetailsService.findByNewCashDetail("Перепокупка доли");
            cash.setNewCashDetails(newCashDetails);
            newInvestorsCash.setId(null);
            newInvestorsCash.setGivedCash(newInvestorsCash.getGivedCash().negate());
            newInvestorsCash.setSourceId(searchSummary.getInvestorsCash().getId());
            newInvestorsCash.setDateGivedCash(investorsCash.getDateClosingInvest());
            newInvestorsCash.setDateClosingInvest(investorsCash.getDateClosingInvest());
            newInvestorsCash.setTypeClosingInvest(investorsCash.getTypeClosingInvest());
            updateMailingGroups(newInvestorsCash, "add");
            updateMailingGroups(cash, "add");
            addFacility(newInvestorsCash);
            addFacility(cash);
            investorsCashService.create(cash);
            investorsCashService.create(newInvestorsCash);
            invLogin = invBuyer != null ? invBuyer.getLogin() : null;
        } else if (!Objects.equals(reFacility, null) && !Objects.equals(investorsCash.getId(), null) &&
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

        if (!Objects.equals(null, searchSummary.getWhat()) &&
                searchSummary.getWhat().equalsIgnoreCase("doubleCash")) {
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
            investorsCash.setUnderFacility(inMemoryCash.getUnderFacility());
            investorsCash.setIsDivide(1);
            updateMailingGroups(newInvestorsCash, "add");
            updateMailingGroups(investorsCash, "add");
            addFacility(newInvestorsCash);
            addFacility(investorsCash);
            investorsCashService.update(newInvestorsCash);
            investorsCashService.update(investorsCash);

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

    @PostMapping(value = {"/saveReCash"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse saveReCash(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        List<InvestorsCash> investorsCashes = searchSummary.getInvestorsCashList();

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
        InvestorsTypes investorsTypes = investorsTypesService.findByInvestorsTypes("Старый инвестор");
        TypeClosingInvest typeClosingInvest = typeClosingInvestService.findByTypeClosingInvest("Реинвестирование");


        Map<String, InvestorsCash> map = groupInvestorsCash(investorsCashes, "");

        map.forEach((key, value) -> {
            value.setCashType(cashTypes);
            value.setNewCashDetails(newCashDetails);
            value.setInvestorsType(investorsTypes);
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

                updateMailingGroups(newInvestorsCash, "add");
                updateMailingGroups(cash, "add");
                updateMailingGroups(c, "delete");
                addFacility(newInvestorsCash);
                addFacility(cash);
                addFacility(c);


                investorsCashService.create(newInvestorsCash);

                c.setDateClosingInvest(dateClose);
                c.setTypeClosingInvest(closingInvest);
                investorsCashService.update(c);
            } else {
                c.setDateClosingInvest(dateClose);
                c.setTypeClosingInvest(typeClosingInvestService.findByTypeClosingInvest("Вывод"));
                investorsCashService.update(c);
                updateMailingGroups(c, "delete");
                addFacility(c);
            }
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
