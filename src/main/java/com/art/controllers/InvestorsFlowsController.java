package com.art.controllers;

import com.art.func.GetPrincipalFunc;
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

    @Resource(name = "saleOfFacilitiesService")
    private SaleOfFacilitiesService saleOfFacilitiesService;

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

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "roomsService")
    private RoomsService roomsService;

    @Resource(name = "investorsFlowsSaleService")
    private InvestorsFlowsSaleService investorsFlowsSaleService;

    @Resource(name = "usersAnnexToContractsService")
    private UsersAnnexToContractsService usersAnnexToContractsService;

    private final String SHARE_KIND = "Основная доля";

    @PostMapping(value = "/loadFlowsAjax", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse loadFlows(MultipartHttpServletRequest request, HttpServletRequest httpServletRequest) {

        GenericResponse response = new GenericResponse();

        Iterator<String> itr = request.getFileNames();
        List<MultipartFile> multipartFiles = new ArrayList<>(0);
        while (itr.hasNext()) {
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

    @PostMapping(value = "/showCalculating", produces = "application/json;charset=UTF-8")
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
        user.getFacilities().stream().forEach(System.out::println);
        */
        return createDataForCalculation(searchSummary.getFacility(), period, getPrincipalFunc.getPrincipalId(), searchSummary.getTableForSearch());
    }

    private GenericResponse createDataForCalculation(String facilityName, Date period, BigInteger investorId, String table) {
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
                    if (table.equalsIgnoreCase("monthPays")) {
                        return i.getReportDate().compareTo(period) == 0;
                    } else {
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


        List<InvestorsFlows> paysToInvestors = investorsFlowsList
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

            if (!Objects.equals(fPays.getUnderFacilities(), null)) {
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
            produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse investorsSumDetailsPage(@RequestBody SearchSummary searchSummary) {

        Locale RU = new Locale("ru", "RU");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(RU);
        NumberFormat fmtPerc = NumberFormat.getPercentInstance(RU);

        List<InvestorsTotalSum> tempList = investorsCashService.getInvestorsCashSumsDetails(
                getPrincipalFunc.getPrincipalId()
        );

        List<InvestorsTotalSum> investorsTotalSums = tempList.stream()
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
    public String cashFlows(ModelMap model) {
        return "viewFlows";
    }

    @PostMapping(value = "/getIncomes")
    public @ResponseBody
    CashFlows getIncomes(ModelMap model) {
        Users investor = userService.findByIdWithAnnexesAndFacilities(getPrincipalFunc.getPrincipalId());
        List<InvestorsFlows> investorsFlowsList = investorsFlowsService.findByInvestorId(investor.getId());

        List<BigInteger> facilitiesIdList = new ArrayList<>(0);
        investor.getFacilities().forEach(f -> facilitiesIdList.add(f.getId()));

        List<MainFlows> mainFlows = mainFlowsService.findByFacilityIdIn(facilitiesIdList);

        List<InvestorsCash> investorsCashList = investorsCashService.findByInvestorId(investor.getId());

        List<UsersAnnexToContracts> annexes = investor.getUsersAnnexToContractsList().stream().distinct().collect(Collectors.toList());

        List<Rooms> rooms = roomsService.findByFacilitiesId(facilitiesIdList);

        CashFlows cashFlows = new CashFlows();
        cashFlows.setMainFlowsList(mainFlows);
        cashFlows.setInvestorsFlowsList(investorsFlowsList);
        cashFlows.setInvestorsCashList(investorsCashList);
        cashFlows.setAnnexes(annexes);
        cashFlows.setRooms(rooms);

        return cashFlows;
    }

    @PostMapping(value = "/getInvestorsFlows")
    public @ResponseBody
    CashFlows getInvestorsFlows(ModelMap model) {
        List<InvestorsFlows> investorsFlowsList = investorsFlowsService.findByInvestorIdWithFacilitiesUnderFacilities(getPrincipalFunc.getPrincipalId());

        CashFlows cashFlows = new CashFlows();
        cashFlows.setInvestorsFlowsList(investorsFlowsList);

        return cashFlows;
    }

    @PostMapping(value = "/getAnnexesList")
    public @ResponseBody
    CashFlows getAnnexes(ModelMap model) {
        Users investor = userService.findByIdWithAnnexesAndFacilities(getPrincipalFunc.getPrincipalId());
        List<UsersAnnexToContracts> usersAnnexToContracts = usersAnnexToContractsService.findByUserId(investor.getId());

        CashFlows cashFlows = new CashFlows();
        cashFlows.setAnnexes(usersAnnexToContracts);

        return cashFlows;
    }

    @PostMapping(value = "/getInvestorsCashList")
    public @ResponseBody
    CashFlows getInvestorsCash(ModelMap model) {
        return getCashFlows();
    }

    private CashFlows getCashFlows() {
        Users investor = userService.findByIdWithFacilities(getPrincipalFunc.getPrincipalId());
        List<InvestorsCash> investorsCashList = investorsCashService.findAllWithAllFields();
        List<BigInteger> idList = new ArrayList<>(0);
        investor.getFacilities().forEach(f -> idList.add(f.getId()));

        List<Rooms> rooms = roomsService.findAll();

        List<SaleOfFacilities> saleOfFacilities = saleOfFacilitiesService.findAll();

        CashFlows cashFlows = new CashFlows();
        cashFlows.setInvestorsCashList(investorsCashList);
        cashFlows.setRooms(rooms);
        cashFlows.setSaleOfFacilities(saleOfFacilities);
        return cashFlows;
    }

    @PostMapping(value = "/getMainFlows")
    public @ResponseBody
    CashFlows getMainFlows(ModelMap model) {
        Users investor = userService.findByIdWithFacilities(getPrincipalFunc.getPrincipalId());

        List<BigInteger> facilitiesIdList = new ArrayList<>(0);
        investor.getFacilities().forEach(f -> facilitiesIdList.add(f.getId()));

        List<MainFlows> mainFlows = mainFlowsService.findByFacilityIdIn(facilitiesIdList);

        Facilities facility = investorsCashService.getSumCash(getPrincipalFunc.getPrincipalId());

        CashFlows cashFlows = new CashFlows();
        cashFlows.setMainFlowsList(mainFlows);
        cashFlows.setFacility(facility);

        return cashFlows;
    }

    @PostMapping(value = "/getInvestorCash")
    public @ResponseBody
    CashFlows getCash(ModelMap model) {
        return getCashFlows();
    }

    @PostMapping(value = {"/deleteFlowsList"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse deleteFlowsList(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        try {
            if ("sale".equals(searchSummary.getWhat())) {
                List<BigInteger> deletedChildesIds = new ArrayList<>();
                List<InvestorsFlowsSale> listToDelete = investorsFlowsSaleService.findByIdIn(searchSummary.getCashIdList());
                listToDelete.forEach(ltd -> {
                    if (!deletedChildesIds.contains(ltd.getId())) {
                        List<InvestorsFlowsSale> childFlows = new ArrayList<>();
                        InvestorsFlowsSale parentFlow = investorsFlowsSaleService.findParentFlow(ltd, childFlows);
                        if (parentFlow.getIsReinvest() == 1) parentFlow.setIsReinvest(0);
                        childFlows = investorsFlowsSaleService.findAllChildes(parentFlow, childFlows, 0);
                        childFlows.sort(Comparator.comparing(InvestorsFlowsSale::getId).reversed());
                        childFlows.forEach(cf -> {
                            deletedChildesIds.add(cf.getId());
                            parentFlow.setProfitToReInvest(parentFlow.getProfitToReInvest().add(cf.getProfitToReInvest()));
                            investorsFlowsSaleService.deleteById(cf.getId());
                            investorsFlowsSaleService.update(parentFlow);
                        });
                        if (parentFlow.getId().equals(ltd.getId())) {
                            investorsFlowsSaleService.deleteById(parentFlow.getId());
                        }
                    }
                });
            } else {
                investorsFlowsService.deleteByIdIn(searchSummary.getCashIdList());
            }
            response.setMessage("Данные успешно удалены");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return response;
    }

    @GetMapping(value = "/deleteFlows")
    public String deleteFlows() {
        investorsFlowsService.delete();
        return "redirect:/paysToInv";
    }
}
