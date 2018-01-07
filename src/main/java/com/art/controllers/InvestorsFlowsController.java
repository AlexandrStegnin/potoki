package com.art.controllers;

import com.art.func.GetPrincipalFunc;
import com.art.func.GlobalFunctions;
import com.art.func.UploadExcelFunc;
import com.art.model.*;
import com.art.model.supporting.CashFlows;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.InvestorsTotalSum;
import com.art.model.supporting.SearchSummary;
import com.art.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Controller
public class InvestorsFlowsController {

    @PersistenceContext(name = "persistanceUnit")
    protected EntityManager emf;

    @Resource(name = "uploadExcelFunc")
    private UploadExcelFunc uploadExcelFunc;

    @Resource(name = "mainFlowsService")
    private MainFlowsService mainFlowsService;

    @Resource(name = "investorsFlowsService")
    private InvestorsFlowsService investorsFlowsService;

    @Resource(name = "getPrincipalFunc")
    private GetPrincipalFunc getPrincipalFunc;

    @Resource(name = "investorsCashService")
    private InvestorsCashService investorsCashService;

    @Resource(name = "globalFunctions")
    private GlobalFunctions globalFunctions;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "roomsService")
    private RoomsService roomsService;

    @Resource(name = "investorsFlowsSaleService")
    private InvestorsFlowsSaleService investorsFlowsSaleService;

    private final String SHARE_KIND = "Основная доля";

    @PostMapping(value = "/loadFlowsAjax", produces="application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse loadFlows(MultipartHttpServletRequest request, HttpServletRequest httpServletRequest) {

        GenericResponse response = new GenericResponse();

        Iterator<String> itr = request.getFileNames();
        List<MultipartFile> multipartFiles = new ArrayList<>(0);
        while (itr.hasNext()){
            multipartFiles.add(request.getFile(itr.next()));
        }

        MultipartFile multipartFile = multipartFiles.get(0);

        try {
            String err = uploadExcelFunc.ExcelParser(multipartFile, "invFlows", httpServletRequest);
            response.setMessage("Файл <b>" + multipartFile.getOriginalFilename() + "</b> успешно загружен.");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            response.setError(e.getLocalizedMessage());
        }

        return response;
    }
    /*
    @GetMapping(value = "/investorsflows2")
    public String viewiFlows2(ModelMap model) {
        Locale RU = new Locale("ru", "RU");
        Users investor = userService.findById(getPrincipalFunc.getPrincipalId());
        List<InvestorsFlows> investorsFlowsList = investorsFlowsService.findByInvestorId(investor.getId());


        List<MainFlows> mainFlows = mainFlowsService.findAllWithCriteriaApi();

        List<MainFlows> invFlows = mainFlows.stream()
                .filter(i -> i.getUnderFacilities().getFacility().getInvestors().contains(investor))
                .collect(Collectors.toList());

        Date dateMax = invFlows.stream().map(MainFlows::getSettlementDate).max(Date::compareTo).orElse(null);

        float summaArendiMonth = (float)invFlows.stream()
                .filter(a -> a.getSumma() > 0)
                .filter(a -> globalFunctions.getMonthInt(a.getSettlementDate()) == globalFunctions.getMonthInt(dateMax) &&
                        globalFunctions.getYearInt(a.getSettlementDate()) == globalFunctions.getYearInt(dateMax))
                .collect(Collectors.toList()).stream().mapToDouble(MainFlows::getSumma).sum();

        float summaArendiAll = (float)invFlows.stream()
                .filter(a -> a.getSumma() > 0)
                .collect(Collectors.toList())
                .stream()
                .mapToDouble(MainFlows::getSumma).sum();

        float summaRashodiMonth = (float)invFlows.stream()
                .filter(r -> r.getSumma() < 0)
                .filter(r -> globalFunctions.getMonthInt(r.getSettlementDate()) == globalFunctions.getMonthInt(dateMax) &&
                        globalFunctions.getYearInt(r.getSettlementDate()) == globalFunctions.getYearInt(dateMax))
                .collect(Collectors.toList())
                .stream()
                .mapToDouble(MainFlows::getSumma).sum();

        float summaRashodiAll = (float)invFlows.stream()
                .filter(r -> r.getSumma() < 0)
                .collect(Collectors.toList())
                .stream()
                .mapToDouble(MainFlows::getSumma).sum();

        Date dateMaxPays = mainFlows.stream().map(MainFlows::getSettlementDate).max(Date::compareTo).orElse(null);
        float summaPaysMonth;
        float summaPays;
        if(!Objects.equals(dateMaxPays, null)){
            summaPaysMonth = (float) investorsFlowsList.stream()
                    .filter(p -> globalFunctions.getMonthInt(p.getReportDate()) == globalFunctions.getMonthInt(dateMaxPays) &&
                            globalFunctions.getYearInt(p.getReportDate()) == globalFunctions.getYearInt(dateMaxPays))
                    .collect(Collectors.toList())
                    .stream()
                    .mapToDouble(InvestorsFlows::getAfterCashing).sum();
        }else{
            summaPaysMonth = (float) investorsFlowsList.stream()
                    .mapToDouble(InvestorsFlows::getAfterCashing).sum();
        }


        summaPays = (float) investorsFlowsList.stream()
                .mapToDouble(InvestorsFlows::getAfterCashing).sum();

        final int[] id = {0};
        final Comparator<MainFlows> comp = (mf1, mf2) -> Float.compare(mf1.getSumma(), mf2.getSumma());

        String monthAndYear;

        SimpleDateFormat monthFormatter = new SimpleDateFormat("MMMM", RU);
        SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy", RU);
        monthAndYear = Objects.equals(dateMax, null) ?
                monthFormatter.format(dateMaxPays) + " " + yearFormatter.format(dateMaxPays) :
                monthFormatter.format(dateMax) + " " + yearFormatter.format(dateMax);

        Map<String, String> facilities = new HashMap<>(0);
        facilities.put(String.valueOf(id[0]), "Выберите объект");

        List<InvestorsCash> invCash = investorsCashService.findByInvestorId(investor.getId());
        BigDecimal summaCash = new BigDecimal(invCash.stream().mapToDouble(InvestorsCash::getGivedCash).sum(), MathContext.DECIMAL64);
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

        float finalMaxSumMonth = summaArendiMonth > summaPaysMonth ? summaArendiMonth : summaPaysMonth;
        float finalMaxSumAll = summaArendiAll > summaPays ? summaArendiAll : summaPays;
        final String[] facility = {""};
        investorsFlowsList.stream().distinct().forEach(flows -> {
            facility[0] = flows.getUnderFacilities().getFacility().getFacility();
            if(!facilities.containsKey(facility[0])){
                facilities.put(facility[0], facility[0]);
            }
        });

        SearchSummary searchSummaryF = new SearchSummary();

        model.addAttribute("facilities", facilities);
        model.addAttribute("monthAndYear", monthAndYear);
        model.addAttribute("searchSummaryF", searchSummaryF);

        model.addAttribute("summaArendiMonth", summaArendiMonth);
        model.addAttribute("summaArendiAll", summaArendiAll);

        model.addAttribute("summaPays", summaPays);
        model.addAttribute("summaPaysMonth", summaPaysMonth);

        model.addAttribute("invCash", finInvCash);
        model.addAttribute("summaCash", summaCash);

        model.addAttribute("finalMaxSumMonth", finalMaxSumMonth);
        model.addAttribute("finalMaxSumAll", finalMaxSumAll);

        model.addAttribute("summaRashodiMonth", summaRashodiMonth);
        model.addAttribute("summaRashodiAll", summaRashodiAll);

        model.addAttribute("loggedinuser", getPrincipalFunc.getLogin());

        return "viewmainflowsinv2";
    }
    */
    @PostMapping(value = "/showCalculating", produces="application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse showCalculating(@RequestBody SearchSummary searchSummary, HttpServletRequest httpServletRequest) {

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
        Date period = null;
        try {
            period = sdf.parse(searchSummary.getPeriod());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /*
        Query q = this.emf.createQuery("SELECT u FROM Users u JOIN FETCH u.facilityes i WHERE u.id = :id");
        q.setParameter("id", new BigInteger("32"));
        Users user = (Users) q.getSingleResult();
        */
        /*
        EntityGraph<Users> graph = emf.createEntityGraph(Users.class);
        graph.addAttributeNodes("facilityes");
        CriteriaBuilder builder = emf.getCriteriaBuilder();
        CriteriaQuery<Users> query = builder.createQuery(Users.class);
        Root<Users> root = query.from(Users.class);
        CriteriaQuery<Users> criteriaQuery =
                query.select(root).where(builder.and(builder.equal(root.get("id"),  new BigInteger("32"))));
        Users user = emf.createQuery(criteriaQuery)
                .setHint("javax.persistence.fetchgraph",
                        emf.getEntityGraph("users.facilityes")).getSingleResult();
        user.getFacilityes().stream().forEach(System.out::println);
        */
        return createDataForCalculation(searchSummary.getFacility(), period, getPrincipalFunc.getPrincipalId(), searchSummary.getTableForSearch());
    }

    private GenericResponse createDataForCalculation(String facilityName, Date period, BigInteger investorId, String table){
        GenericResponse response = new GenericResponse();
        List<InvestorsFlows> investorsFlowsList = investorsFlowsService.findByInvestorId(investorId);
        Locale RU = new Locale("ru", "RU");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(RU);
        fmt.setMinimumFractionDigits(2);
        NumberFormat fmtPercent = NumberFormat.getPercentInstance(RU);
        fmtPercent.setMinimumFractionDigits(2);

        Date dateMaxPays = investorsFlowsList.stream().map(InvestorsFlows::getReportDate).max(Date::compareTo).orElse(null);

        investorsFlowsList = investorsFlowsList
                .stream()
                .filter(i -> Objects.equals(facilityName, null) || i.getFacility().getFacility().equalsIgnoreCase(facilityName))
                .filter(i -> {
                    if(table.equalsIgnoreCase("monthPays")){
                        return i.getReportDate().compareTo(period) == 0;
                    }else {
                        return true;
                    }
                })
                .collect(Collectors.toList());

        float rentSum = (float) investorsFlowsList
                .stream()
                .filter(i -> i.getShareKind().equalsIgnoreCase(SHARE_KIND))
                .mapToDouble(InvestorsFlows::getSumma)
                .sum();

        float invShare = (float) investorsFlowsList
                .stream()
                .filter(share -> share.getReportDate().compareTo(dateMaxPays) == 0)
                .mapToDouble(InvestorsFlows::getShareForSvod)
                .sum();

        float rentAfterCosts = (float) investorsFlowsList
                .stream()
                .mapToDouble(InvestorsFlows::getOnInvestors)
                .sum();

        float tax = (float) investorsFlowsList
                .stream()
                .mapToDouble(InvestorsFlows::getTaxation)
                .average()
                .orElse(0);

        float sumAfterTax = (float) investorsFlowsList
                .stream()
                .mapToDouble(InvestorsFlows::getAfterTax)
                .sum();

        float cashingComission = (float) investorsFlowsList
                .stream()
                .mapToDouble(InvestorsFlows::getCashing)
                .average()
                .orElse(0);

        float sumAfterCashing = (float) investorsFlowsList
                .stream()
                .mapToDouble(InvestorsFlows::getAfterCashing)
                .sum();


        List<InvestorsFlows> paysToInvestors  = investorsFlowsList
                .stream()
                .filter(p -> Objects.equals(facilityName, null) || p.getFacility().getFacility().equalsIgnoreCase(facilityName))
                .collect(Collectors.toList())
                .stream().collect(
                        Collectors.groupingBy(
                                pays ->
                                        new InvestorsFlows(
                                                pays.getFacility(),
                                                pays.getUnderFacilities(),
                                                pays.getInvestor()),
                                Collectors.summingDouble(InvestorsFlows::getSumma)
                        )).entrySet().stream()
                .map(entry -> new InvestorsFlows(
                        entry.getKey().getFacility(),
                        entry.getKey().getUnderFacilities(),
                        entry.getKey().getInvestor(),
                        Float.parseFloat(entry.getValue().toString())))
                .collect(Collectors.toList());

        StringBuilder result = new StringBuilder();
        result.append("<thead>")
                .append("<tr>")
                .append("<th style='text-align: center;'>Объект</th>")
                .append("<th style='text-align: center; color: rgb(255, 255, 255);' id='ufUnd'>Подобъект</th>")
                .append("<th style='text-align: center;' data-toggle='tooltip' title='После брокериджа и др. расходов'>Аренда по объекту</th>")
                .append("<th style='text-align: center;'>Доля в объекте</th>")
                .append("<th style='text-align: center;' data-toggle='tooltip' title='Аренда по объекту умножается на Вашу долю в объекте'>Аренда после расходов (ваша доля)</th>")
                .append("<th style='text-align: center;'>Налог</th>")
                .append("<th style='text-align: center;' data-toggle='tooltip' title='Из аренды после расходов (ваша доля) вычитаем налог'>После налогообложения</th>")
                .append("<th style='text-align: center;'>Комиссия за вывод денег</th>")
                .append("<th style='text-align: center;' data-toggle='tooltip' title='Из дохода после налогообложения вычитаем комиссию за вывод денег'>После вывода денег</th>")
                .append("<th style='text-align: center;'>Детали</th>")
                .append("</tr>")
                .append("</thead><tbody>");

        final String[] underFacility = {""};

        result.append("<tbody class='labels'>")
            .append("<tr>")
                .append("<td style='padding: 0'>").append(facilityName).append("</td>")
                .append("<td></td>")
                .append("<td style='text-align: right'>").append(fmt.format(rentSum)).append("</td>")
                .append("<td style='text-align: right'>").append(fmtPercent.format(invShare)).append("</td>")
                .append("<td style='text-align: right'>").append(fmt.format(rentAfterCosts)).append("</td>")
                .append("<td style='text-align: right'>").append(fmtPercent.format(tax)).append("</td>")
                .append("<td style='text-align: right'>").append(fmt.format(sumAfterTax)).append("</td>")
                .append("<td style='text-align: right'>").append(fmtPercent.format(cashingComission)).append("</td>")
                .append("<td style='text-align: right'>").append(fmt.format(sumAfterCashing)).append("</td>")
                .append("<td class='details'><label for='").append(facilityName).append("Und'>Показать</label>")
                .append("<input type='checkbox' id='").append(facilityName).append("Und' data-toggle='toggleUnd' /></td>")
            .append("</tr>");
        result.append("</tbody>");
        result.append("<tbody class='hideShowUnd'>");

        final float[] rentSumUnd = new float[1];
        final float[] invShareUnd = new float[1];
        final float[] rentAfterCostsUnd = new float[1];
        final float[] taxUnd = new float[1];
        final float[] sumAfterTaxUnd = new float[1];
        final float[] cashingComissionUnd = new float[1];
        final float[] sumAfterCashingUnd = new float[1];

        List<InvestorsFlows> finalInvestorsFlowsList = investorsFlowsList;
        paysToInvestors.forEach(fPays -> {

            if(!Objects.equals(fPays.getUnderFacilities(), null)){
                underFacility[0] = fPays.getUnderFacilities().getUnderFacility();
            }

            rentSumUnd[0] = (float) finalInvestorsFlowsList
                    .stream()
                    .filter(i -> i.getShareKind().equalsIgnoreCase(SHARE_KIND))
                    .filter(i -> i.getUnderFacilities().getUnderFacility().equalsIgnoreCase(underFacility[0]))
                    .mapToDouble(InvestorsFlows::getSumma)
                    .sum();
            invShareUnd[0] = (float) finalInvestorsFlowsList
                    .stream()
                    .filter(share -> share.getReportDate().compareTo(dateMaxPays) == 0)
                    .filter(share -> share.getUnderFacilities().getUnderFacility().equalsIgnoreCase(underFacility[0]))
                    .mapToDouble(InvestorsFlows::getShareForSvod)
                    .sum();
            rentAfterCostsUnd[0] = (float) finalInvestorsFlowsList
                    .stream()
                    .filter(rent -> rent.getUnderFacilities().getUnderFacility().equalsIgnoreCase(underFacility[0]))
                    .mapToDouble(InvestorsFlows::getOnInvestors)
                    .sum();
            taxUnd[0] = (float) finalInvestorsFlowsList
                    .stream()
                    .filter(t -> t.getUnderFacilities().getUnderFacility().equalsIgnoreCase(underFacility[0]))
                    .mapToDouble(InvestorsFlows::getTaxation)
                    .average()
                    .orElse(0);
            sumAfterTaxUnd[0] = (float) finalInvestorsFlowsList
                    .stream()
                    .filter(s -> s.getUnderFacilities().getUnderFacility().equalsIgnoreCase(underFacility[0]))
                    .mapToDouble(InvestorsFlows::getAfterTax)
                    .sum();
            cashingComissionUnd[0] = (float) finalInvestorsFlowsList
                    .stream()
                    .filter(c -> c.getUnderFacilities().getUnderFacility().equalsIgnoreCase(underFacility[0]))
                    .mapToDouble(InvestorsFlows::getCashing)
                    .average()
                    .orElse(0);
            sumAfterCashingUnd[0] = (float) finalInvestorsFlowsList
                    .stream()
                    .filter(s -> s.getUnderFacilities().getUnderFacility().equalsIgnoreCase(underFacility[0]))
                    .mapToDouble(InvestorsFlows::getAfterCashing)
                    .sum();

            result.append("<tr>")
                    .append("<td></td>")
                    .append("<td style='padding: 0'>").append(underFacility[0]).append("</td>")
                    .append("<td style='text-align: right'>").append(fmt.format(rentSumUnd[0]))
                    .append("<td style='text-align: right'>").append(fmtPercent.format(invShareUnd[0]))
                    .append("<td style='text-align: right'>").append(fmt.format(rentAfterCostsUnd[0]))
                    .append("<td style='text-align: right'>").append(fmtPercent.format(taxUnd[0]))
                    .append("<td style='text-align: right'>").append(fmt.format(sumAfterTaxUnd[0]))
                    .append("<td style='text-align: right'>").append(fmtPercent.format(cashingComissionUnd[0]))
                    .append("<td style='text-align: right'>").append(fmt.format(sumAfterCashingUnd[0]))
                    .append("<td></td>")
                .append("</tr>");

        });
        result.append("</tbody></tbody>");

        /*
        result.append("<tr>")
                .append("<td>").append(facilityName).append("</td>")
                .append("<td></td>")
                .append("<td>").append(fmt.format(rentSum)).append("</td>")
                .append("<td>").append(fmtPercent.format(invShare)).append("</td>")
                .append("<td>").append(fmt.format(rentAfterCosts)).append("</td>")
                .append("<td>").append(fmtPercent.format(tax)).append("</td>")
                .append("<td>").append(fmt.format(sumAfterTax)).append("</td>")
                .append("<td>").append(fmtPercent.format(cashingComission)).append("</td>")
                .append("<td>").append(fmt.format(sumAfterCashing)).append("</td>")
                .append("</tr>");
        result.append("</tbody>");
        */
        response.setMessage(result.toString());

        return response;
    }

    @RequestMapping(value = "/sumdetails", method = RequestMethod.POST,
            produces="application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse investorsSumDetailsPage(@RequestBody SearchSummary searchSummary) {

        Locale RU = new Locale("ru", "RU");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(RU);
        NumberFormat fmtPerc = NumberFormat.getPercentInstance(RU);

        List<InvestorsTotalSum> tempList  = investorsCashService.getInvestorsCashSumsDetails(
                getPrincipalFunc.getPrincipalId()
        );

        List<InvestorsTotalSum> investorsTotalSums= tempList.stream()
                .filter(iSums -> iSums.getFacility().equals(searchSummary.getFacility()))
                .collect(Collectors.toList());

        StringBuilder result = new StringBuilder("<thead><tr><th style='text-align: center;'>Дата вложения</th>" +
                "<th style='text-align: center;'>Название объекта</th><th style='text-align: center;'>Сумма вложений, руб.</th>");
        result.append("</tr></thead><tbody>");

        for (InvestorsTotalSum summs : investorsTotalSums) {
            result
                    .append("<tr><td>")
                    .append(summs.getDateGivedCashToLocalDate())
                    .append("</td>")
                    .append("<td>")
                    .append(summs.getFacility())
                    .append("</td>")
                    .append("<td>")
                    .append(fmt.format(summs.getGivedCash()))
                    .append("</td>")
                    .append("</tr>");
        }
        result.append("</tbody>");
        GenericResponse response = new GenericResponse();
        response.setMessage(result.toString());
        return response;
    }

    @GetMapping(value = "/cashflows")
    public String cashFlows(ModelMap model){
        return "viewFlows";
    }

    @PostMapping(value = "/getIncomes")
    public @ResponseBody CashFlows getIncomes(ModelMap model){
        Users investor = userService.findByIdWithAnnexes(getPrincipalFunc.getPrincipalId());
        List<InvestorsFlows> investorsFlowsList = investorsFlowsService.findByInvestorId(investor.getId());

        List<MainFlows> mainFlows = mainFlowsService.findAllWithCriteriaApi();

        List<MainFlows> invFlows = mainFlows.stream()
                .filter(i -> !Objects.equals(null, i.getUnderFacilities()) && i.getUnderFacilities().getFacility().getInvestors().contains(investor))
                .collect(Collectors.toList());

        List<InvestorsCash> investorsCashList = investorsCashService.findByInvestorId(investor.getId());

        List<UsersAnnexToContracts> annexes = investor.getUsersAnnexToContractsList();

        CashFlows cashFlows = new CashFlows();
        cashFlows.setMainFlowsList(invFlows);
        cashFlows.setInvestorsFlowsList(investorsFlowsList);
        cashFlows.setInvestorsCashList(investorsCashList);
        cashFlows.setAnnexes(annexes);

        return cashFlows;
    }

    @PostMapping(value = "/getInvestorsFlows")
    public @ResponseBody CashFlows getInvestorsFlows(ModelMap model){
        Users investor = userService.findById(getPrincipalFunc.getPrincipalId());
        List<InvestorsFlows> investorsFlowsList = investorsFlowsService.findByInvestorId(investor.getId());

        CashFlows cashFlows = new CashFlows();
        cashFlows.setInvestorsFlowsList(investorsFlowsList);

        return cashFlows;
    }

    @PostMapping(value = "/getMainFlows")
    public @ResponseBody CashFlows getMainFlows(ModelMap model){
        Users investor = userService.findById(getPrincipalFunc.getPrincipalId());
        List<MainFlows> mainFlows = mainFlowsService.findAllWithCriteriaApi();

        List<MainFlows> invFlows = mainFlows.stream()
                .filter(i -> i.getUnderFacilities().getFacility().getInvestors().contains(investor))
                .collect(Collectors.toList());

        CashFlows cashFlows = new CashFlows();
        cashFlows.setMainFlowsList(invFlows);

        return cashFlows;
    }

    @PostMapping(value = "/getInvestorCash")
    public @ResponseBody CashFlows getCash(ModelMap model){
        Users investor = userService.findById(getPrincipalFunc.getPrincipalId());
        List<InvestorsCash> cash = investorsCashService.findByInvestorId(investor.getId());

        CashFlows cashFlows = new CashFlows();
        cashFlows.setInvestorsCashList(cash);

        return cashFlows;
    }

    @PostMapping(value = { "/deleteFlowsList" }, produces="application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse deleteFlowsList(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        try {
            switch (searchSummary.getWhat()){
                case "sale":
                    investorsFlowsSaleService.deleteByIdIn(searchSummary.getCashIdList());
                    break;

                default:
                    investorsFlowsService.deleteByIdIn(searchSummary.getCashIdList());
                    break;
            }
        response.setMessage("Данные успешно удалены");

        }catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }

        return response;
    }

    private String getMonthAndYearSettlementDate(String settlementDate){
        Locale RU = new Locale("ru", "RU");
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("MMM yyyy", RU);
        try{
            localDate = format.format(settlementDate);
        }catch(Exception ignored){}

        return localDate;
    }

    @GetMapping(value = "/deleteFlows")
    public String deleteFlows(){
        investorsFlowsService.delete();
        return "redirect:/paysToInv";
    }
}
