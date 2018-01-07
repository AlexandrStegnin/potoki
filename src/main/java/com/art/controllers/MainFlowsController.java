package com.art.controllers;

import com.art.func.GetPrincipalFunc;
import com.art.func.GlobalFunctions;
import com.art.model.Facilities;
import com.art.model.InvestorsFlows;
import com.art.model.MainFlows;
import com.art.model.Users;
import com.art.model.supporting.FileBucket;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.SearchSummary;
import com.art.model.supporting.UserFacilities;
import com.art.service.*;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MainFlowsController {

    @Resource(name = "getPrincipalFunc")
    private GetPrincipalFunc getPrincipalFunc;

    @Resource(name = "mainFlowsService")
    private MainFlowsService mainFlowsService;

    @Resource(name = "investorsCashService")
    private InvestorsCashService investorsCashService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "globalFunctions")
    private GlobalFunctions globalFunctions;

    @Resource(name = "paysToInvestorsService")
    private PaysToInvestorsService paysToInvestorsService;

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @Resource(name = "investorsFlowsService")
    private InvestorsFlowsService investorsFlowsService;


    @GetMapping(value = "/investorsAllFlows")
    public String viewflows(ModelMap model) {
        FileBucket fileModel = new FileBucket();
        model.addAttribute("fileBucket", fileModel);
        //List<InvestorsFlows> investorsFlows = investorsFlowsService.findAll();
        List<MainFlows> mainFlows = mainFlowsService.findAll();
        model.addAttribute("mainFlows", mainFlows);
        //model.addAttribute("investorsFlows", investorsFlows);
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());
        return "viewmainflows";
    }
    /*
    @GetMapping(value = "/investorsflows")
    public String viewiFlows(ModelMap model) {
        Locale RU = new Locale("ru", "RU");
        List<MainFlows> mainFlows = mainFlowsService.findAll();

        Users investor = userService.findById(getPrincipalFunc.getPrincipalId());
        List<MainFlows> invFlows = mainFlows.stream()
                .filter(i -> i.getUnderFacilities().getFacility().getInvestors().contains(investor))
                .collect(Collectors.toList());
        List<MainFlows> finalFlows;

        final int[] id = {0};
        float summa = (float)invFlows.stream().mapToDouble(MainFlows::getSumma).sum();
        final Comparator<MainFlows> comp = (mf1, mf2) -> Float.compare(mf1.getSumma(), mf2.getSumma());

        String monthAndYear;
        Date dateMax = mainFlows.stream().map(MainFlows::getSettlementDate).max(Date::compareTo).orElse(null);
        SimpleDateFormat monthFormatter = new SimpleDateFormat("MMMM", RU);
        SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy", RU);
        monthAndYear = monthFormatter.format(dateMax) + " " + yearFormatter.format(dateMax);

        finalFlows = invFlows.stream().filter(f -> {
            assert dateMax != null;
            return globalFunctions.getMonthInt(f.getSettlementDate()) == globalFunctions.getMonthInt(dateMax) &&
                    globalFunctions.getYearInt(f.getSettlementDate()) == globalFunctions.getYearInt(dateMax);
        })
                .collect(Collectors.toList());
        float finalSumma = (float)finalFlows.stream().mapToDouble(MainFlows::getSumma).sum();
        MainFlows finalMaxSum = null;
        float fMaxSum = 0;
        try{
            finalMaxSum = finalFlows.stream()
                    .max(comp)
                    .get();
        }catch (Exception ignored){

        }
        fMaxSum = Objects.equals(finalMaxSum, null) ? 0 : finalMaxSum.getSumma();

        MainFlows finalMinSum = null;
        float fMinSum = 0;
        try{
            finalMinSum = finalFlows.stream()
                    .min(comp)
                    .get();
        }catch (Exception ignored){

        }
        fMinSum = Objects.equals(finalMinSum, null) ? 0 : finalMinSum.getSumma();


        Map<String, String> years = new HashMap<>(0);
        Map<String, String> months = new HashMap<>(0);
        Map<String, String> pays = new HashMap<>(0);
        Map<String, String> uFacilities = new HashMap<>(0);
        Map<String, String> facilities = new HashMap<>(0);

        years.put(String.valueOf(id[0]), "Выберите год");
        months.put(String.valueOf(id[0]), "Выберите месяц");
        pays.put(String.valueOf(id[0]), "Выберите платёж");
        uFacilities.put(String.valueOf(id[0]), "Выберите подобъект");
        facilities.put(String.valueOf(id[0]), "Выберите объект");

        List<InvestorsCash> invCash = investorsCashService.findByInvestorId(investor.getId());
        BigDecimal summaCash = (BigDecimal) invCash.stream().mapToDouble(InvestorsCash::getGivedCash).sum();
        List<InvestorsCash> finInvCash =
                invCash.stream().collect(
                        Collectors.groupingBy(
                                cash ->
                                        new InvestorsCash(
                                                cash.getFacility(),
                                                cash.getInvestor()),
                                Collectors.summingDouble(InvestorsCash::getGivedCash)
                        )).entrySet().stream()
                        .map(entry -> new InvestorsCash(
                                Float.parseFloat(entry.getValue().toString()),
                                entry.getKey().getFacility(),
                                entry.getKey().getInvestor()))
                        .collect(Collectors.toList());

        List<MainFlows> allMainFlows =
                invFlows.stream().collect(
                        Collectors.groupingBy(
                                flows ->
                                        new MainFlows(
                                                flows.getUnderFacilities(),
                                                flows.getPayment()),
                                Collectors.summingDouble(MainFlows::getSumma)
                        )).entrySet().stream()
                        .map(entry -> new MainFlows(
                                entry.getKey().getUnderFacilities(),
                                entry.getKey().getPayment(),
                                Float.parseFloat(entry.getValue().toString())))
                        .collect(Collectors.toList());

        float maxSum = 0;
        try{
            maxSum = allMainFlows.stream()
                    .max(comp)
                    .get()
                    .getSumma();
        }catch (Exception ignored){

        }


        float minSum = 0;
        try {
            minSum = allMainFlows.stream()
                    .min(comp)
                    .get()
                    .getSumma();
        }catch (Exception ignored){

        }


        invFlows.stream().distinct().forEach(flows -> {
            try{
                years.put(flows.getYearSettlementDate(), flows.getYearSettlementDate());
                months.put(flows.getMonthSettlementDate(), flows.getMonthSettlementDate());
                pays.put(flows.getPayment(), flows.getPayment());
                uFacilities.put(flows.getUnderFacilities().getUnderFacility(), flows.getUnderFacilities().getUnderFacility());
                facilities.put(flows.getUnderFacilities().getFacility().getFacility(),
                        flows.getUnderFacilities().getFacility().getFacility());
            }catch (Exception ignored){
            }
        });

        SearchSummary searchSummary = new SearchSummary();
        SearchSummary searchSummaryF = new SearchSummary();

        model.addAttribute("searchSummary", searchSummary);
        model.addAttribute("searchSummaryF", searchSummaryF);
        model.addAttribute("facilities", facilities);
        model.addAttribute("monthAndYear", monthAndYear);
        model.addAttribute("pays", pays);
        model.addAttribute("uFacilities", uFacilities);
        model.addAttribute("finalFlows", finalFlows);
        model.addAttribute("finalSumma", finalSumma);
        model.addAttribute("invCash", finInvCash);
        model.addAttribute("summaCash", summaCash);

        model.addAttribute("finalMaxSum", fMaxSum);
        model.addAttribute("finalMinSum", fMinSum);
        model.addAttribute("maxSum", maxSum);
        model.addAttribute("minSum", minSum);

        model.addAttribute("summa", summa);
        model.addAttribute("allMainFlows", allMainFlows);
        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());

        model.addAttribute("years", years);
        model.addAttribute("months", months);
        return "viewmainflowsinv";
    }
    */
    @RequestMapping(value = "/mainFlowsInv", method = RequestMethod.POST,
            produces="application/json;charset=UTF-8")
    public @ResponseBody
    String mainFlowsInvPage(@RequestBody SearchSummary searchSummary) {
        String tableForSearch = searchSummary.getTableForSearch();
        Users investor = userService.findById(getPrincipalFunc.getPrincipalId());
        List<MainFlows> tempFlows = mainFlowsService.findAllWithCriteriaApi()
                .stream()
                .filter(f -> f.getUnderFacilities().getFacility().getInvestors().contains(investor))
                .collect(Collectors.toList());

        Date dateMax = tempFlows.stream().map(MainFlows::getSettlementDate).max(Date::compareTo).orElse(null);
        List<MainFlows> allInvFlows;
        List<MainFlows> invFlows;

        StringBuilder result = new StringBuilder();
        int iYear = 0;
        if(searchSummary.getiYear() != null && !searchSummary.getiYear().equals("Выберите год")){
            iYear = Integer.parseInt(searchSummary.getiYear());
        }
        int iMonth = 0;
        if(searchSummary.getiMonth() != null && !searchSummary.getiMonth().equals("Выберите месяц")){
            iMonth = Integer.parseInt(searchSummary.getiMonth());
        }
        Facilities facility = facilityService.findByFacility(searchSummary.getFacility());
        if(facility != null){
            allInvFlows = tempFlows.stream()
                    .filter(i -> i.getUnderFacilities().getFacility().equals(facility))
                    .collect(Collectors.toList());

            invFlows = allInvFlows.stream()
                    .filter(i -> {
                        assert dateMax != null;
                        return getMonthInt(i.getSettlementDate()) == getMonthInt(dateMax) &&
                                getYearInt(i.getSettlementDate()) == getYearInt(dateMax);
                    })
                    .collect(Collectors.toList());
        }else {
            allInvFlows = tempFlows;

            invFlows = allInvFlows.stream()
                    .filter(i -> {
                        assert dateMax != null;
                        return getMonthInt(i.getSettlementDate()) == getMonthInt(dateMax) &&
                                getYearInt(i.getSettlementDate()) == getYearInt(dateMax);
                    })
                    .collect(Collectors.toList());
        }

        switch (tableForSearch){
            case "invPaysMonth":
                result = createTableData(searchSummary.getTableForSearch(), iYear, iMonth, "Выберите платёж"/*searchSummary.getPay()*/,
                        searchSummary.getFacility(), "Выберите подобъект"/*searchSummary.getUnderFacility()*/, invFlows,
                        investorsFlowsService, getPrincipalFunc);
                break;
            case "invPaysAll":
                result = createTableData(searchSummary.getTableForSearch(), iYear, iMonth, "Выберите платёж"/*searchSummary.getPay()*/,
                        searchSummary.getFacility(), "Выберите подобъект"/*searchSummary.getUnderFacility()*/, allInvFlows,
                        investorsFlowsService, getPrincipalFunc);
                break;
        }

        return result.toString();
    }

    @RequestMapping(value = "/goPays", method = RequestMethod.POST,
            produces="application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse invPaysMonthPage(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();

        Date dateMax = mainFlowsService.findAllWithCriteriaApi()
                .stream()
                .map(MainFlows::getSettlementDate).max(Date::compareTo).orElse(null);
        StringBuilder result = createDataForPays(facilityService, searchSummary.getFacility(), investorsFlowsService,
                getPrincipalFunc.getPrincipalId(), dateMax, "monthPays");
        response.setMessage(result.toString());
        return response;
    }

    @RequestMapping(value = "/goPaysAll", method = RequestMethod.POST,
            produces="application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse invPaysAllPage(@RequestBody SearchSummary searchSummary) {

        StringBuilder result = createDataForPays(facilityService, searchSummary.getFacility(), investorsFlowsService,
                getPrincipalFunc.getPrincipalId(), null, "allPays");

        GenericResponse response = new GenericResponse();
        response.setMessage(result.toString());
        return response;
    }

    @RequestMapping(value = "/goArendaAll", method = RequestMethod.POST,
            produces="application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse arendaAllPage(@RequestBody SearchSummary searchSummary) {
        Users investor = userService.findById(getPrincipalFunc.getPrincipalId());
        Facilities facility = facilityService.findByFacility(searchSummary.getFacility());
        List<MainFlows> mainFlows = mainFlowsService.findAllWithCriteriaApi();
        StringBuilder result = createDataFromLink(investor, facility, mainFlows, "allArenda");
        GenericResponse response = new GenericResponse();
        response.setMessage(result.toString());
        return response;
    }

    @RequestMapping(value = "/goArendaMonth", method = RequestMethod.POST,
            produces="application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse arendaMonthPage(@RequestBody SearchSummary searchSummary) {

        Users investor = userService.findById(getPrincipalFunc.getPrincipalId());
        Facilities facility = facilityService.findByFacility(searchSummary.getFacility());
        List<MainFlows> mainFlows = mainFlowsService.findAllWithCriteriaApi();
        StringBuilder result = createDataFromLink(investor, facility, mainFlows, "monthArenda");
        GenericResponse response = new GenericResponse();
        response.setMessage(result.toString());
        return response;
    }

    @RequestMapping(value = "/goRashodiAll", method = RequestMethod.POST,
            produces="application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse rashodiAllPage(@RequestBody SearchSummary searchSummary) {
        Users investor = userService.findById(getPrincipalFunc.getPrincipalId());
        Facilities facility = facilityService.findByFacility(searchSummary.getFacility());
        List<MainFlows> mainFlows = mainFlowsService.findAllWithCriteriaApi();
        StringBuilder result = createDataFromLink(investor, facility, mainFlows, "allRashodi");
        GenericResponse response = new GenericResponse();
        response.setMessage(result.toString());
        return response;
    }

    @RequestMapping(value = "/goRashodiMonth", method = RequestMethod.POST,
            produces="application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse rashodiMonthPage(@RequestBody SearchSummary searchSummary) {
        Users investor = userService.findById(getPrincipalFunc.getPrincipalId());
        Facilities facility = facilityService.findByFacility(searchSummary.getFacility());
        List<MainFlows> mainFlows = mainFlowsService.findAllWithCriteriaApi();
        StringBuilder result = createDataFromLink(investor, facility, mainFlows, "monthRashodi");
        GenericResponse response = new GenericResponse();
        response.setMessage(result.toString());
        return response;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @ModelAttribute("facilities")
    public List<UserFacilities> initializeFacilities() {
        return getPrincipalFunc.getUserFacilities(getPrincipalFunc.getPrincipalId());
    }
    private static StringBuilder createDataFromLink(Users investor, Facilities facility, List<MainFlows> mainFlows, String tbl){
        Locale RU = new Locale("ru", "RU");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(RU);
        fmt.setMinimumFractionDigits(2);
        Date dateMax = null;
        switch (tbl){
            case "monthRashodi":
                dateMax = mainFlows.stream().map(MainFlows::getSettlementDate).max(Date::compareTo).orElse(null);
            case "allRashodi":
                mainFlows = mainFlows.stream()
                        .filter(mf -> mf.getUnderFacilities().getFacility().getInvestors().contains(investor))
                        .filter(mf -> mf.getSumma() < 0)
                        .collect(Collectors.toList());
                break;
            case "monthArenda":
                dateMax = mainFlows.stream().map(MainFlows::getSettlementDate).max(Date::compareTo).orElse(null);
            case "allArenda":
                mainFlows = mainFlows.stream()
                        .filter(mf -> mf.getUnderFacilities().getFacility().getInvestors().contains(investor))
                        .filter(mf -> mf.getSumma() > 0)
                        .collect(Collectors.toList());
                break;
        }

        Date finalDateMax = dateMax;
        mainFlows = mainFlows
                .stream()
                .filter(p -> Objects.equals(facility, null) || p.getUnderFacilities().getFacility().equals(facility))
                .filter(p -> Objects.equals(finalDateMax, null) || getMonthInt(p.getSettlementDate()) == getMonthInt(finalDateMax) &&
                        getYearInt(p.getSettlementDate()) == getYearInt(finalDateMax))
                .collect(Collectors.toList())
                .stream().collect(
                        Collectors.groupingBy(
                                pays ->
                                        new MainFlows(
                                                pays.getUnderFacilities(),
                                                pays.getPayment()),
                                Collectors.summingDouble(MainFlows::getSumma)
                        )).entrySet().stream()
                .map(entry -> new MainFlows(
                        entry.getKey().getUnderFacilities(),
                        entry.getKey().getPayment(),
                        Float.parseFloat(entry.getValue().toString())))
                .collect(Collectors.toList());

        StringBuilder result = new StringBuilder();
            result.append("<thead>")
                    .append("<tr>")
                        .append("<th>Объект</th>")
                        .append("<th style='color: rgb(255, 255, 255)' id='uf'>Подобъект</th>")
                        .append("<th style='color: rgb(255, 255, 255)' id='pay'>Платёж</th>")
                        .append("<th>Сумма</th>")
                        .append("<th>Детали</th>")
                    .append("</tr>")
                    .append("</thead><tbody>");

        Map<String, Double> map = mainFlows.stream()
                .collect(Collectors.groupingBy(mf -> mf.getUnderFacilities()
                        .getFacility().getFacility(), Collectors.summingDouble(MainFlows::getSumma)));

        List<MainFlows> flowsByUnderFacilities =
                mainFlows.stream().collect(
                        Collectors.groupingBy(
                                pays ->
                                        new MainFlows(
                                                pays.getUnderFacilities()),
                                Collectors.summingDouble(MainFlows::getSumma)
                        )).entrySet().stream()
                        .map(entry -> new MainFlows(
                                entry.getKey().getUnderFacilities(),
                                Float.parseFloat(entry.getValue().toString())))
                        .collect(Collectors.toList());

        List<MainFlows> flowsByPayments =
                mainFlows.stream().collect(
                        Collectors.groupingBy(
                                pays ->
                                        new MainFlows(
                                                pays.getUnderFacilities(),
                                                pays.getPayment()),
                                Collectors.summingDouble(MainFlows::getSumma)
                        )).entrySet().stream()
                        .map(entry -> new MainFlows(
                                entry.getKey().getUnderFacilities(),
                                entry.getKey().getPayment(),
                                Float.parseFloat(entry.getValue().toString())))
                        .collect(Collectors.toList());

        map.forEach((k, v) -> {
            result
                    .append("<tbody class='labels'>")
                        .append("<tr>")
                            .append("<td>").append(k).append("</td>")
                            .append("<td></td>")
                            .append("<td></td>")
                            .append("<td class='sum'>").append(fmt.format(v)).append("</td>")
                            .append("<td class='details'>")
                            .append("<label for='").append(k).append("'>Показать</label>")
                            .append("<input type='checkbox' id='").append(k).append("' data-toggle='toggle' />")
                            .append("</td>")
                        .append("</tr>")
                    .append("</tbody>")
                    .append("<tbody class='hideShow'>");

            flowsByUnderFacilities.forEach(f -> {
                if(f.getUnderFacilities().getFacility().getFacility().equalsIgnoreCase(k)){
                    result
                            .append("<tr class='levelUnderFacilities'>")
                            .append("<td></td>")
                            .append("<td>").append(f.getUnderFacilities().getUnderFacility()).append("</td>")
                            .append("<td></td>")
                            .append("<td class='sum'>").append(fmt.format(f.getSumma())).append("</td>")
                            .append("<td style='text-align: center' class='more'>Показать</td>")
                            .append("</tr>");

                    flowsByPayments.forEach(fByPayments -> {
                        if(fByPayments.getUnderFacilities().getUnderFacility()
                                        .equalsIgnoreCase(f.getUnderFacilities().getUnderFacility())){
                            result
                                    .append("<tr class='levelPays'>")
                                    .append("<td></td>")
                                    .append("<td></td>")
                                    .append("<td>").append(fByPayments.getPayment()).append("</td>")
                                    .append("<td class='sum'>").append(fmt.format(fByPayments.getSumma())).append("</td>")
                                    .append("<td></td>")
                                    .append("</tr>");
                        }
                    });
                }

            });
            result.append("</tbody>");
        });

        return result;
    }
    private static StringBuilder createTableData(String tblName, int iYear, int iMonth, String pay, String facility,
                                                 String underFacility, List<MainFlows> tempList,
                                                 InvestorsFlowsService investorsFlowsService, GetPrincipalFunc getPrincipalFunc){

        Locale RU = new Locale("ru", "RU");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(RU);
        fmt.setMinimumFractionDigits(2);
        StringBuilder result = new StringBuilder();
        List<MainFlows> mainFlows;
        float summa;
                /* ГОД */
                if (iYear != 0 && iMonth == 0 &&
                        Objects.equals(underFacility, "Выберите подобъект") &&
                        Objects.equals(pay, "Выберите платёж")) {
                    mainFlows = tempList.stream()
                            .filter(mf -> getYearInt(mf.getSettlementDate()) == iYear)
                            .collect(Collectors.toList());
                /* ГОД И МЕСЯЦ */
                } else if (iYear != 0 && iMonth != 0 &&
                        Objects.equals(underFacility, "Выберите подобъект") &&
                        Objects.equals(pay, "Выберите платёж")) {
                    mainFlows = tempList.stream()
                            .filter(mf -> getMonthInt(mf.getSettlementDate()) == iMonth)
                            .filter(mf -> getYearInt(mf.getSettlementDate()) == iYear)
                            .collect(Collectors.toList());
                /* ГОД И ПЛАТЁЖ */
                } else if (iYear != 0 && iMonth == 0 &&
                        Objects.equals(underFacility, "Выберите подобъект") &&
                        !Objects.equals(pay, "Выберите платёж")) {
                    mainFlows = tempList.stream()
                            .filter(mf -> mf.getPayment().equals(pay))
                            .filter(mf -> getYearInt(mf.getSettlementDate()) == iYear)
                            .collect(Collectors.toList());
                /* ГОД И ПОДОБЪЕКТ */
                } else if (iYear != 0 && iMonth == 0 &&
                        !Objects.equals(underFacility, "Выберите подобъект") &&
                        Objects.equals(pay, "Выберите платёж")) {
                    mainFlows = tempList.stream()
                            .filter(mf -> mf.getUnderFacilities().getUnderFacility().equals(underFacility))
                            .filter(mf -> getYearInt(mf.getSettlementDate()) == iYear)
                            .collect(Collectors.toList());
                /* МЕСЯЦ */
                } else if (iYear == 0 && iMonth != 0 &&
                        Objects.equals(underFacility, "Выберите подобъект") &&
                        Objects.equals(pay, "Выберите платёж")) {
                    mainFlows = tempList.stream()
                            .filter(mf -> getMonthInt(mf.getSettlementDate()) == iMonth)
                            .collect(Collectors.toList());
                /* МЕСЯЦ И ПЛАТЁЖ */
                } else if (iYear == 0 && iMonth != 0 &&
                        Objects.equals(underFacility, "Выберите подобъект") &&
                        !Objects.equals(pay, "Выберите платёж")) {
                    mainFlows = tempList.stream()
                            .filter(mf -> mf.getPayment().equals(pay))
                            .filter(mf -> getMonthInt(mf.getSettlementDate()) == iMonth)
                            .collect(Collectors.toList());
                /* МЕСЯЦ И ПОДОБЪЕКТ */
                } else if (iYear == 0 && iMonth != 0 &&
                        !Objects.equals(underFacility, "Выберите подобъект") &&
                        Objects.equals(pay, "Выберите платёж")) {
                    mainFlows = tempList.stream()
                            .filter(mf -> mf.getUnderFacilities().getUnderFacility().equals(underFacility))
                            .filter(mf -> getMonthInt(mf.getSettlementDate()) == iMonth)
                            .collect(Collectors.toList());
                /* ПЛАТЁЖ */
                } else if (iYear == 0 && iMonth == 0 &&
                        Objects.equals(underFacility, "Выберите подобъект") &&
                        !Objects.equals(pay, "Выберите платёж")) {
                    mainFlows = tempList.stream()
                            .filter(mf -> mf.getPayment().equals(pay))
                            .collect(Collectors.toList());
                /* ПЛАТЁЖ И ПОДОБЪЕКТ */
                } else if (iYear == 0 && iMonth == 0 &&
                        !Objects.equals(underFacility, "Выберите подобъект") &&
                        !Objects.equals(pay, "Выберите платёж")) {
                    mainFlows = tempList.stream()
                            .filter(mf -> mf.getUnderFacilities().getUnderFacility().equals(underFacility))
                            .filter(mf -> mf.getPayment().equals(pay))
                            .collect(Collectors.toList());
                /* ПОДОБЪЕКТ */
                } else if (iYear == 0 && iMonth == 0 &&
                        !Objects.equals(underFacility, "Выберите подобъект") &&
                        Objects.equals(pay, "Выберите платёж")) {
                    mainFlows = tempList.stream()
                            .filter(mf -> mf.getUnderFacilities().getUnderFacility().equals(underFacility))
                            .collect(Collectors.toList());
                /* ГОД, МЕСЯЦ, ПЛАТЁЖ */
                } else if (iYear != 0 && iMonth != 0 &&
                        Objects.equals(underFacility, "Выберите подобъект") &&
                        !Objects.equals(pay, "Выберите платёж")) {
                    mainFlows = tempList.stream()
                            .filter(mf -> mf.getUnderFacilities().getUnderFacility().equals(underFacility))
                            .filter(mf -> getMonthInt(mf.getSettlementDate()) == iMonth)
                            .filter(mf -> getYearInt(mf.getSettlementDate()) == iYear)
                            .collect(Collectors.toList());
                /* ГОД, МЕСЯЦ, ПЛАТЁЖ, ПОДОБЪЕКТ */
                } else if (iYear != 0 && iMonth != 0 &&
                        !Objects.equals(underFacility, "Выберите подобъект") &&
                        !Objects.equals(pay, "Выберите платёж")) {
                    mainFlows = tempList.stream()
                            .filter(mf -> mf.getUnderFacilities().getUnderFacility().equals(underFacility))
                            .filter(mf -> mf.getPayment().equals(pay))
                            .filter(mf -> getMonthInt(mf.getSettlementDate()) == iMonth)
                            .filter(mf -> getYearInt(mf.getSettlementDate()) == iYear)
                            .collect(Collectors.toList());
                /* МЕСЯЦ, ПЛАТЁЖ, ПОДОБЪЕКТ */
                } else if (iYear == 0 && iMonth != 0 &&
                        !Objects.equals(underFacility, "Выберите подобъект") &&
                        !Objects.equals(pay, "Выберите платёж")) {
                    mainFlows = tempList.stream()
                            .filter(mf -> mf.getUnderFacilities().getUnderFacility().equals(underFacility))
                            .filter(mf -> mf.getPayment().equals(pay))
                            .filter(mf -> getMonthInt(mf.getSettlementDate()) == iMonth)
                            .collect(Collectors.toList());
                /* ГОД, МЕСЯЦ, ПОДОБЪЕКТ */
                } else if (iYear != 0 && iMonth != 0 &&
                        !Objects.equals(underFacility, "Выберите подобъект") &&
                        Objects.equals(pay, "Выберите платёж")) {
                    mainFlows = tempList.stream()
                            .filter(mf -> mf.getUnderFacilities().getUnderFacility().equals(underFacility))
                            .filter(mf -> getMonthInt(mf.getSettlementDate()) == iMonth)
                            .filter(mf -> getYearInt(mf.getSettlementDate()) == iYear)
                            .collect(Collectors.toList());
                } else {
                    mainFlows = tempList;
                }
                Date dateMaxFlows = mainFlows.stream().map(MainFlows::getSettlementDate).max(Date::compareTo).orElse(null);

                /* Доходы инвестора месяц начало */
                List<InvestorsFlows> investorsFlows = investorsFlowsService.findByInvestorId(getPrincipalFunc.getPrincipalId());
                Date dateMaxInvestors = investorsFlows.stream().map(InvestorsFlows::getReportDate).max(Date::compareTo).orElse(null);
                if(!Objects.equals(facility, null) && facility.length() > 0){
                    investorsFlows = investorsFlows.stream()
                            .filter(f -> f.getFacility().getFacility().equalsIgnoreCase(facility))
                            .collect(Collectors.toList());
                }
                float dohodMonth = (float) investorsFlows.stream()
                        .filter(inv -> getMonthInt(inv.getReportDate()) == getMonthInt(dateMaxInvestors) &&
                                getYearInt(inv.getReportDate()) == getYearInt(dateMaxInvestors))
                        .mapToDouble(InvestorsFlows::getAfterCashing).sum();
                /* Доходы инвестора месяц конец */

                /* Доходы инвестора все начало */
                float dohodAll = (float) investorsFlows.stream()
                        .mapToDouble(InvestorsFlows::getAfterCashing).sum();
                /* Доходы инвестора все конец */

                /* Сумма аренды за месяц начало */
                float arendaMonth = (float)mainFlows.stream()
                        .filter(mf -> mf.getSumma() > 0)
                        .filter(mf -> getMonthInt(mf.getSettlementDate()) == getMonthInt(dateMaxFlows) &&
                                getYearInt(mf.getSettlementDate()) == getYearInt(dateMaxFlows))
                        .mapToDouble(MainFlows::getSumma).sum();
                /* Сумма аренды за месяц конец */

                /* Сумма аренды вся начало */
                float arendaAll = (float) mainFlows.stream()
                        .filter(mf -> mf.getSumma() > 0)
                        .mapToDouble(MainFlows::getSumma)
                        .sum();
                /* Сумма аренды вся конец */

                /* Сумма расходов месяц начало */
                float rashodiMonth = (float) mainFlows.stream()
                        .filter(mf -> mf.getSumma() < 0)
                        .filter(mf -> getMonthInt(mf.getSettlementDate()) == getMonthInt(dateMaxFlows) &&
                                getYearInt(mf.getSettlementDate()) == getYearInt(dateMaxFlows))
                        .mapToDouble(MainFlows::getSumma)
                        .sum();
                /* Сумма расходов месяц конец */

                /* Сумма расходов вся начало */
                float rashodiAll = (float)mainFlows.stream()
                        .filter(mf -> mf.getSumma() < 0)
                        .mapToDouble(MainFlows::getSumma)
                        .sum();
                /* Сумма расходов вся конец */

                float percent;
                result = new StringBuilder("<thead><tr><th></th>" +
                        "<th style='width: 20%'>Сумма</th><th style='width: 20%'>Подробно</th>");
                result.append("</tr></thead><tbody>");
                result.append("<tr>");
                switch (tblName){
                    case "invPaysMonth":
                        result.append("<td>Вы заработали</td>");
                        percent = arendaMonth > 0 ? Math.round(dohodMonth / arendaMonth)*100 : Math.round(dohodMonth / dohodMonth)*100;
                        percent = percent == 100 ? percent : percent + 5;
                        result.append("<td style='background: linear-gradient(to right, white 50%, rgb(100, 195, 132) 50%, rgb(229, 245, 234) ")
                                .append((percent + 100) / 2).append("%, ").append("white ").append(percent)
                                .append("%);' class='sum'>")
                                .append(fmt.format(dohodMonth))
                                .append("</td>");
                        result.append("<td><a id='goPays' name='monthPays' href='/mainFlowsInv' class='btn btn-link custom-width'>Подробно</a></td></tr>");

                        result.append("<tr><td>Аренда</td>");
                        percent = Math.round(arendaMonth / arendaMonth)*100;
                        percent = percent == 100 ? percent : percent + 5;
                        result.append("<td style='background: linear-gradient(to right, white 50%, rgb(100, 195, 132) 50%, rgb(229, 245, 234) ")
                                .append((percent + 100) / 2).append("%, ").append("white ").append(percent)
                                .append("%);' class='sum'>")
                                .append(fmt.format(arendaMonth))
                                .append("</td>");
                        result.append("<td><a id='goArendaMonth' name='monthArenda' href='/mainFlowsInv' class='btn btn-link custom-width'>Подробно</a></td></tr>");

                        result.append("<tr><td>Расходы</td>");
                        percent = Math.round(rashodiMonth / rashodiMonth)*100;
                        percent = percent == 100 ? percent : percent + 5;
                        result.append("<td style='background: linear-gradient(to left, white 50%, rgb(255, 0, 0) 50%, rgb(255, 210, 210) ")
                                .append((percent + 100) / 2).append("%, ").append("white ").append(percent)
                                .append("%);' class='sum'>")
                                .append(fmt.format(rashodiMonth))
                                .append("</td>");
                        result.append("<td><a id='goRashodiMonth' name='monthRashodi' href='/mainFlowsInv' class='btn btn-link custom-width'>Подробно</a></td>");

                        result.append("</tr></tbody>");
                        break;
                    case "invPaysAll":
                        result.append("<td>Вы заработали</td>");
                        percent = arendaAll > 0 ? Math.round(dohodAll / arendaAll)*100 : Math.round(dohodAll / dohodAll)*100;
                        percent = percent == 100 ? percent : percent + 5;
                        result.append("<td style='background: linear-gradient(to right, white 50%, rgb(100, 195, 132) 50%, rgb(229, 245, 234) ")
                                .append((percent + 100) / 2).append("%, ").append("white ").append(percent)
                                .append("%);' class='sum'>")
                                .append(fmt.format(dohodAll))
                                .append("</td>");
                        result.append("<td><a id='goPaysAll' name='allPays' href='/#' class='btn btn-link custom-width'>Подробно</a></td></tr>");

                        result.append("<tr><td>Аренда</td>");
                        percent = Math.round(arendaAll / arendaAll)*100;
                        percent = percent == 100 ? percent : percent + 5;
                        result.append("<td style='background: linear-gradient(to right, white 50%, rgb(100, 195, 132) 50%, rgb(229, 245, 234) ")
                                .append((percent + 100) / 2).append("%, ").append("white ").append(percent)
                                .append("%);' class='sum'>")
                                .append(fmt.format(arendaAll))
                                .append("</td>");
                        result.append("<td><a id='goArendaAll' name='allArenda' href='/mainFlowsInv' class='btn btn-link custom-width'>Подробно</a></td></tr>");

                        result.append("<tr><td>Расходы</td>");
                        percent = Math.round(rashodiAll / rashodiAll)*100;
                        percent = percent == 100 ? percent : percent + 5;
                        result.append("<td style='background: linear-gradient(to left, white 50%, rgb(255, 0, 0) 50%, rgb(255, 210, 210) ")
                                .append((percent + 100) / 2).append("%, ").append("white ").append(percent)
                                .append("%);' class='sum'>")
                                .append(fmt.format(rashodiAll))
                                .append("</td>");
                        result.append("<td><a id='goRashodiAll' name='allRashodi' href='/mainFlowsInv' class='btn btn-link custom-width'>Подробно</a></td>");

                        result.append("</tr></tbody>");
                        break;
                }

        return result;
    }

    private static StringBuilder createDataForPays(FacilityService facilityService, String strFacility,
                                                   InvestorsFlowsService investorsFlowsService, BigInteger investorId,
                                                   Date dateMax, String table){
        GenericResponse response = new GenericResponse();
        Locale RU = new Locale("ru", "RU");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(RU);
        fmt.setMinimumFractionDigits(2);
        Facilities facility = facilityService.findByFacility(strFacility);
        List<InvestorsFlows> paysToInvestors = investorsFlowsService.findByInvestorId(investorId);

        paysToInvestors = paysToInvestors
                .stream()
                .filter(p -> Objects.equals(facility, null) || p.getFacility().equals(facility))
                .filter(p -> {
                    if(!Objects.equals(dateMax, null)){
                        return p.getReportDate().compareTo(dateMax) == 0;
                    }else {
                        return true;
                    }
                })
                .collect(Collectors.toList())
                .stream().collect(
                        Collectors.groupingBy(
                                pays ->
                                        new InvestorsFlows(
                                                pays.getFacility(),
                                                pays.getUnderFacilities(),
                                                pays.getInvestor()),
                                Collectors.summingDouble(InvestorsFlows::getAfterCashing)
                        )).entrySet().stream()
                .map(entry -> new InvestorsFlows(
                        entry.getKey().getFacility(),
                        entry.getKey().getUnderFacilities(),
                        entry.getKey().getInvestor(),
                        Float.parseFloat(entry.getValue().toString())))
                .collect(Collectors.toList());

        List<InvestorsFlows> groupFlows = paysToInvestors
                .stream().collect(
                        Collectors.groupingBy(
                                pays ->
                                        new InvestorsFlows(
                                                pays.getFacility()),
                                Collectors.summingDouble(InvestorsFlows::getAfterCashing)
                        )).entrySet().stream()
                .map(entry -> new InvestorsFlows(
                        entry.getKey().getFacility(),
                        Float.parseFloat(entry.getValue().toString())))
                .collect(Collectors.toList());

        StringBuilder result = new StringBuilder();
        result
                .append("<thead>")
                .append("<tr>")
                .append("<th style='text-align: center;'>Объект</th>")
                .append("<th style='text-align: center; color: rgb(255, 255, 255);' id='uf'>Подобъект</th>")
                .append("<th style='text-align: center;'>Сумма</th>")
                .append("<th style='text-align: center;'>Детали</th>")
                .append("<th style='text-align: center;'>Как посчитали</th>")
                .append("</tr>")
                .append("</thead><<tbody>");

        final String[] underFacility = {""};

        List<InvestorsFlows> finalPaysToInvestors = paysToInvestors;
        groupFlows.forEach(pays -> {

            result  .append("<tbody class='labels'>")
                    .append("<tr>")
                    .append("<td style='padding: 0'>").append(pays.getFacility().getFacility()).append("</td>")
                    .append("<td></td>")
                    .append("<td style='text-align: right'>").append(fmt.format(pays.getAfterCashing())).append("</td>")
                    .append("<td class='details'><label for='").append(pays.getFacility().getFacility()).append("'>Показать</label>")
                    .append("<input type='checkbox' id='").append(pays.getFacility().getFacility()).append("' data-toggle='toggle' />")
                    .append("<td style='text-align: center'")
                    .append("'><a href='howCalc' id='howCalculate' data-table='").append(table).append("' name='")
                    .append(pays.getFacility().getFacility()).append("' class='btn btn-link'>Как посчитали</a></td>")
                    .append("</td>")
                    .append("</tr>");
            result.append("</tbody>");
            result.append("<tbody class='hideShow'>");

            finalPaysToInvestors.forEach(fPays -> {

                if(!Objects.equals(fPays.getUnderFacilities(), null)){
                    underFacility[0] = fPays.getUnderFacilities().getUnderFacility();
                }

                if(Objects.equals(pays.getFacility(), fPays.getFacility())){
                    result.append("<tr>")
                            .append("<td></td>")
                            .append("<td style='padding: 0'>").append(underFacility[0]).append("</td>")
                            .append("<td style='text-align: right' class='sum'>").append(fmt.format(fPays.getAfterCashing())).append("</td>")
                            .append("<td style='padding: 0'></td>")
                            .append("<td style='padding: 0'></td>")
                            .append("</tr>");
                }

            });
            result.append("</tbody>");
        });
        result.append("</tbody>");
        response.setMessage(result.toString());

        return result;
    }

    private static int getMonthInt(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        return Integer.parseInt(dateFormat.format(date));
    }
    private static int getYearInt(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        return Integer.parseInt(dateFormat.format(date));
    }

    private static int getPercentForGradient(float maxSumm, float nextSumm){
        maxSumm = maxSumm < 0 ? Math.round(maxSumm * (-1)) : Math.round(maxSumm);
        nextSumm = nextSumm < 0 ? Math.round(nextSumm * (-1)) : Math.round(nextSumm);
        int percent = 0;
        percent = Math.round(nextSumm / maxSumm * 100);

        return percent;
    }
}
