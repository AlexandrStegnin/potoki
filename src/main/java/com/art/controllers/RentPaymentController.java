package com.art.controllers;

import com.art.config.application.Location;
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

@Controller
public class RentPaymentController {

    private final FacilityService facilityService;

    @Resource(name = "uploadExcelFunc")
    private UploadExcelFunc uploadExcelFunc;

    private final UserService userService;

    private final RentPaymentService rentPaymentService;

    private final UnderFacilityService underFacilityService;

    private final RoomService roomService;

    private final SearchSummary filters = new SearchSummary();

    public RentPaymentController(FacilityService facilityService, UserService userService, RentPaymentService rentPaymentService, UnderFacilityService underFacilityService, RoomService roomService) {
        this.facilityService = facilityService;
        this.userService = userService;
        this.rentPaymentService = rentPaymentService;
        this.underFacilityService = underFacilityService;
        this.roomService = roomService;
    }

    @GetMapping(path = Location.RENT_PAYMENTS)
    public ModelAndView rentPayments(@PageableDefault(size = 100, sort = "id") Pageable pageable,
                                              @RequestParam(name = "facility", required = false) String facility,
                                              @RequestParam(name = "underFacility", required = false) String underFacility,
                                              @RequestParam(name = "investor", required = false) String investor,
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                              @RequestParam(value = "startDate", required = false) LocalDate startDate,
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                              @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                              Model model, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("rent-payment-list");
        filters.setStartDate(startDate);
        filters.setEndDate(endDate);
        filters.setFacilityStr(facility);
        filters.setUnderFacilityStr(underFacility);
        filters.setInvestor(investor);
        FileBucket fileModel = new FileBucket();
        Page<RentPayment> rentPaymentsList = rentPaymentService.findAllFiltering(pageable, filters);
        int pageCount = rentPaymentsList.getTotalPages();
        List<RentPayment> rentPayments = rentPaymentsList.getContent();
        String queryParams = request.getQueryString();
        if (queryParams != null) queryParams = "&" + queryParams;
        modelAndView.addObject("searchSummary", filters);
        modelAndView.addObject("fileBucket", fileModel);
        modelAndView.addObject("pageCount", pageCount);
        modelAndView.addObject("rentPayments", rentPayments);
        modelAndView.addObject("queryParams", queryParams);

        return modelAndView;
    }

    @PostMapping(path = Location.RENT_PAYMENTS)
    public String ptiUploadExcel(ModelMap model, @ModelAttribute("fileBucket") FileBucket fileBucket, HttpServletRequest request) throws IOException, ParseException {
        MultipartFile multipartFile = fileBucket.getFile();
        String ret = "Выплатам инвесторам.";
        String title = "Выплаты инвесторам";
        String err = uploadExcelFunc.ExcelParser(multipartFile, "invFlows", request);
        model.addAttribute("redirectUrl", Location.HOME);
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
