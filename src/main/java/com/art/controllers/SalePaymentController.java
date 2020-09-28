package com.art.controllers;

import com.art.config.application.Location;
import com.art.func.UploadExcelService;
import com.art.model.AppUser;
import com.art.model.Facility;
import com.art.model.SalePayment;
import com.art.model.UnderFacility;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.FileBucket;
import com.art.model.supporting.SearchSummary;
import com.art.model.supporting.dto.SalePaymentDTO;
import com.art.model.supporting.dto.SalePaymentDivideDTO;
import com.art.model.supporting.enums.ShareType;
import com.art.model.supporting.enums.UploadType;
import com.art.model.supporting.filters.FlowsSaleFilter;
import com.art.service.FacilityService;
import com.art.service.SalePaymentService;
import com.art.service.UnderFacilityService;
import com.art.service.UserService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class SalePaymentController {

    private final UploadExcelService uploadExcelService;

    private final FacilityService facilityService;

    private final SalePaymentService salePaymentService;

    private final UnderFacilityService underFacilityService;

    private final UserService userService;

    private final FlowsSaleFilter filter = new FlowsSaleFilter();

    public SalePaymentController(UploadExcelService uploadExcelService, FacilityService facilityService,
                                 SalePaymentService salePaymentService, UnderFacilityService underFacilityService,
                                 UserService userService) {
        this.uploadExcelService = uploadExcelService;
        this.facilityService = facilityService;
        this.salePaymentService = salePaymentService;
        this.underFacilityService = underFacilityService;
        this.userService = userService;
    }

    /**
     * Получить страницу для отображения списка денег инвесторов с продажи
     *
     * @param pageable для постраничного отображения
     * @return страница
     */
    @GetMapping(path = Location.SALE_PAYMENTS)
    public ModelAndView paymentsSale(@PageableDefault(size = 100) @SortDefault Pageable pageable) {
        return prepareModel(filter);
    }

    /**
     * Получить страницу для отображения списка денег инвесторов с продажи с фильтрами
     *
     * @param filter фильтры
     * @return страница
     */
    @PostMapping(path = Location.SALE_PAYMENTS)
    public ModelAndView paymentsSaleFiltered(@ModelAttribute("filter") FlowsSaleFilter filter) {
        return prepareModel(filter);
    }

    /**
     * Загрузить файл выплат по продаже
     *
     * @param request запрос
     * @return сообщение об успешной/неудачной загрузке
     */
    @PostMapping(path = Location.SALE_PAYMENTS_UPLOAD)
    @ResponseBody
    public ApiResponse uploadSalePayments(MultipartHttpServletRequest request) {
        return uploadExcelService.upload(request, UploadType.SALE);
    }

    /**
     * Удалить выбранные данные о выплатах (аренда)
     *
     * @return сообщение об успешном/неудачном выполнении
     */
    @PostMapping(path = Location.SALE_PAYMENTS_DELETE_CHECKED)
    @ResponseBody
    public ApiResponse deleteSalePaymentsChecked(@RequestBody SalePaymentDTO dto) {
        return salePaymentService.deleteChecked(dto);
    }

    /**
     * Реинвестирование сумм с выплат (продажа)
     *
     * @param dto DTO для реинвестирования
     * @return ответ о выполнении
     */
    @PostMapping(path = Location.SALE_PAYMENTS_REINVEST)
    @ResponseBody
    public ApiResponse reinvestSalePayments(@RequestBody SalePaymentDTO dto) {
        return salePaymentService.reinvest(dto);
    }

    @PostMapping(path = Location.SALE_PAYMENTS_DIVIDE)
    public @ResponseBody
    ApiResponse divideSalePayments(@RequestBody SalePaymentDivideDTO divideDTO) {
        return salePaymentService.divideSalePayment(divideDTO);
    }

    /**
     * Подготовить модель для страницы
     *
     * @param filters фильтры
     */
    private ModelAndView prepareModel(FlowsSaleFilter filters) {
        ModelAndView model = new ModelAndView("sale-payment-list");
        FileBucket fileModel = new FileBucket();
        Pageable pageable = new PageRequest(filters.getPageNumber(), filters.getPageSize());
        Page<SalePayment> page = salePaymentService.findAll(filters, pageable);
        model.addObject("page", page);
        model.addObject("fileBucket", fileModel);
        model.addObject("filter", filters);
        model.addObject("searchSummary", new SearchSummary());
        model.addObject("salePaymentDTO", new SalePaymentDTO());
        return model;
    }

    @ModelAttribute("facilities")
    public List<Facility> initializeFacilities() {
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

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

}
