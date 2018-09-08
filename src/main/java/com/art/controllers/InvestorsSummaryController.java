package com.art.controllers;

import com.art.func.GetPrincipalFunc;
import com.art.func.UploadExcelFunc;
import com.art.model.*;
import com.art.model.supporting.*;
import com.art.service.*;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class InvestorsSummaryController {

    @Resource(name = "historyRelationshipsService")
    private HistoryRelationshipsService historyRelationshipsService;

    @Resource(name = "getPrincipalFunc")
    private GetPrincipalFunc getPrincipalFunc;

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @Resource(name = "uploadExcelFunc")
    private UploadExcelFunc uploadExcelFunc;

    @Resource(name = "investorsCashService")
    private InvestorsCashService investorsCashService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "investorsFlowsService")
    private InvestorsFlowsService investorsFlowsService;

    @Resource(name = "underFacilitiesService")
    private UnderFacilitiesService underFacilitiesService;

    @Resource(name = "investorsTypesService")
    private InvestorsTypesService investorsTypesService;

    @Resource(name = "shareKindService")
    private ShareKindService shareKindService;

    @Resource(name = "roomsService")
    private RoomsService roomsService;

    @RequestMapping(value = "/investorssummary", method = RequestMethod.GET)
    public String investorsSummaryPage(ModelMap model) {

        List<InvestorsSummary> investorsSummaries = historyRelationshipsService.getInvestorsSummary(
                getPrincipalFunc.getPrincipalId()
        );
        model.addAttribute("searchSummary", new SearchSummary());
        model.addAttribute("investorsSummaries", investorsSummaries);
        return "investorssummary";
    }

    @RequestMapping(value = "/investorssummarywithfacility", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String investorsSummaryWithFacilityPage(@RequestBody SearchSummary searchSummary) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Locale RU = new Locale("ru", "RU");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(RU);
        List<InvestorsSummary> investorsSummaries;

        List<InvestorsSummary> tempList = historyRelationshipsService.getInvestorsSummary(
                getPrincipalFunc.getPrincipalId());

        Date dateBeg = new Date();
        Date dateEnd = new Date();
        if (searchSummary.getDateStart() == null) {
            dateBeg = tempList.get(0).getEnd_date();
        } else {
            try {
                dateBeg = formatDate.parse(formatDate.format(searchSummary.getDateStart()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (searchSummary.getDateEnd() == null) {
            dateEnd = tempList.get(tempList.size() - 1).getEnd_date();
        } else {
            try {
                dateEnd = formatDate.parse(formatDate.format(searchSummary.getDateEnd()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Date finalDateBeg = dateBeg;
        Date finalDateEnd = dateEnd;

        if (!Objects.equals(searchSummary.getFacility(), "Выберите объект")) {

            investorsSummaries = tempList.stream()
                    .filter(summaries -> summaries.getFacility().equals(searchSummary.getFacility()))
                    .filter(summaries ->
                            (summaries.getEnd_date().compareTo(finalDateBeg) == 0 ||
                                    summaries.getEnd_date().compareTo(finalDateBeg) > 0) &&
                                    (summaries.getEnd_date().compareTo(finalDateEnd) == 0 ||
                                            summaries.getEnd_date().compareTo(finalDateEnd) < 0))
                    .collect(Collectors.toList());
        } else {

            investorsSummaries = tempList.stream()
                    .filter(summaries ->
                            (summaries.getEnd_date().compareTo(finalDateBeg) == 0 ||
                                    summaries.getEnd_date().compareTo(finalDateBeg) > 0) &&
                                    (summaries.getEnd_date().compareTo(finalDateEnd) == 0 ||
                                            summaries.getEnd_date().compareTo(finalDateEnd) < 0))
                    .collect(Collectors.toList());
        }

        StringBuilder result;
        result = new StringBuilder("<thead><tr><th>Название объекта</th><th>Период</th><th>Вид оплаты (аренда, продажа)</th>" +
                "<th>Аренда</th><th>Месячная чистая прибыль</th><th>Получено всего</th>" +
                "<th>Ежемесячная прибыль с продажи*</th></tr></thead><tbody>");
        for (InvestorsSummary summary : investorsSummaries) {
            result.append("<tr><td>")
                    .append(summary.getFacility())
                    .append("</td>").append("<td>")
                    .append(summary.getEndDateToLocalDate())
                    .append("</td>").append("<td>")
                    .append(summary.getaCorTag())
                    .append("</td>").append("<td>")
                    .append(fmt.format(summary.getFact_pay()))
                    .append("</td>").append("<td>")
                    .append(fmt.format(summary.getOstatok_po_dole()))
                    .append("</td>").append("<td>")
                    .append(fmt.format(summary.getSumm()))
                    .append("</td>").append("<td>")
                    .append(fmt.format(summary.getPribil_s_prodazhi()))
                    .append("</td>").append("</tr>")
                    .append("</tbody>");
        }

        return result.toString();
    }

    @GetMapping(value = "/paysToInv")
    public ModelAndView paysToInvPage() {
        ModelAndView modelAndView = new ModelAndView("viewinvestorsflows");
        SearchSummary searchSummary = new SearchSummary();
        modelAndView.addObject("searchSummary", searchSummary);
        modelAndView.addObject("loggedinuser", getPrincipalFunc.getLogin());
        FileBucket fileModel = new FileBucket();
        modelAndView.addObject("fileBucket", fileModel);
        List<InvestorsFlows> investorsFlows = investorsFlowsService.findAll();
        modelAndView.addObject("investorsFlows", investorsFlows);
        return modelAndView;
    }

    @PostMapping(value = "/paysToInv")
    public String ptiUploadExcel(ModelMap model, @ModelAttribute("fileBucket") FileBucket fileBucket, HttpServletRequest request) throws IOException, ParseException {
        MultipartFile multipartFile = fileBucket.getFile();
        String ret = "Выплатам инвесторам.";
        String redirectUrl = "/";
        String title = "Выплаты инвесторам";
        String err = uploadExcelFunc.ExcelParser(multipartFile, "invFlows", request);

        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        model.addAttribute("title", title);
        model.addAttribute("err", err);
        return "success";
    }

    @GetMapping(value = "/investorsCash")
    public ModelAndView iCashPage() {
        ModelAndView modelAndView = new ModelAndView("investorsCashSums");
        modelAndView.addObject("loggedinuser", getPrincipalFunc.getLogin());
        List<InvestorsTotalSum> investorsCashSums = investorsCashService.getInvestorsCashSums(
                getPrincipalFunc.getPrincipalId());
        modelAndView.addObject("investorsCashSums", investorsCashSums);
        return modelAndView;
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

    @ModelAttribute("facilitiesList")
    public List<Facilities> initializeFacilitiesList() {
        return facilityService.initializeFacilities();
    }

    @ModelAttribute("underFacilities")
    public List<UnderFacilities> initializeUnderFacilities() {
        return underFacilitiesService.initializeUnderFacilities();
    }

    @ModelAttribute("investors")
    public List<Users> initializeInvestors() {
        return userService.initializeInvestors();
    }

    @ModelAttribute("investorsTypes")
    public List<InvestorsTypes> initializeInvestorsTypes() {
        return investorsTypesService.initializeInvestorsTypes();
    }

    @ModelAttribute("shareKinds")
    public List<ShareKind> initializeShareKinds() {
        return shareKindService.init();
    }

    @ModelAttribute("rooms")
    public List<Rooms> initializeRooms() {
        return roomsService.init();
    }
}
