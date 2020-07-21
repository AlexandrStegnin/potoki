package com.art.controllers;

import com.art.func.UploadExcelFunc;
import com.art.model.*;
import com.art.model.supporting.FileBucket;
import com.art.model.supporting.SearchSummary;
import com.art.model.supporting.enums.ShareType;
import com.art.service.*;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class InvestorsSummaryController {

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @Resource(name = "uploadExcelFunc")
    private UploadExcelFunc uploadExcelFunc;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "investorsFlowsService")
    private InvestorsFlowsService investorsFlowsService;

    @Resource(name = "underFacilityService")
    private UnderFacilityService underFacilityService;

    @Resource(name = "roomService")
    private RoomService roomService;

    private final SearchSummary filters = new SearchSummary();

    @GetMapping(value = "/paysToInv")
    public ModelAndView paysToInvByPageNumber(@PageableDefault(size = 100, sort = "id") Pageable pageable,
                                              @RequestParam(name = "facility", required = false) String facility,
                                              @RequestParam(name = "underFacility", required = false) String underFacility,
                                              @RequestParam(name = "investor", required = false) String investor,
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                              @RequestParam(value = "startDate", required = false) LocalDate startDate,
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                              @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                              Model model, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("viewinvestorsflows");
        filters.setStartDate(startDate);
        filters.setEndDate(endDate);
        filters.setFacilityStr(facility);
        filters.setUnderFacilityStr(underFacility);
        filters.setInvestor(investor);
        FileBucket fileModel = new FileBucket();
        Page<InvestorsFlows> flowsList = investorsFlowsService.findAllFiltering(pageable, filters);
        int pageCount = flowsList.getTotalPages();
        List<InvestorsFlows> investorsFlows = flowsList.getContent();
        String queryParams = request.getQueryString();
        if (!Objects.equals(null, queryParams)) queryParams = "&" + queryParams;
        modelAndView.addObject("searchSummary", filters);
        modelAndView.addObject("fileBucket", fileModel);
        modelAndView.addObject("pageCount", pageCount);
        modelAndView.addObject("investorsFlows", investorsFlows);
        modelAndView.addObject("queryParams", queryParams);

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

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @ModelAttribute("facilitiesList")
    public List<Facility> initializeFacilitiesList() {
        return facilityService.initializeFacilities();
    }

    @ModelAttribute("underFacilities")
    public List<UnderFacility> initializeUnderFacilities() {
        return underFacilityService.initializeUnderFacilities();
    }

    @ModelAttribute("investors")
    public List<AppUser> initializeInvestors() {
        return userService.initializeInvestors();
    }

    @ModelAttribute("shareTypes")
    public List<ShareType> initializeShareTypes() {
        return Arrays.asList(ShareType.values());
    }

    @ModelAttribute("rooms")
    public List<Room> initializeRooms() {
        return roomService.init();
    }

    @ModelAttribute("searchSummary")
    public SearchSummary addSearchSummary() {
        return filters;
    }
}
