package com.art.controllers;

import com.art.func.UploadExcelFunc;
import com.art.model.*;
import com.art.model.supporting.FileBucket;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.SearchSummary;
import com.art.service.*;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Controller
public class InvestorsFlowsSaleController {
    @Resource(name = "uploadExcelFunc")
    private UploadExcelFunc uploadExcelFunc;

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @Resource(name = "investorsFlowsSaleService")
    private InvestorsFlowsSaleService investorsFlowsSaleService;

    @Resource(name = "underFacilitiesService")
    private UnderFacilitiesService underFacilitiesService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "investorsTypesService")
    private InvestorsTypesService investorsTypesService;

    @Resource(name = "shareKindService")
    private ShareKindService shareKindService;

    @GetMapping(value = "/flowsSale")
    public ModelAndView flowsSale() {
        ModelAndView modelAndView = new ModelAndView("viewInvestorsFlowsSale");
        SearchSummary searchSummary = new SearchSummary();
        modelAndView.addObject("searchSummary", searchSummary);
        FileBucket fileModel = new FileBucket();
        modelAndView.addObject("fileBucket", fileModel);
        List<InvestorsFlowsSale> investorsFlowsSale = investorsFlowsSaleService.findAll();
        modelAndView.addObject("investorsFlowsSale", investorsFlowsSale);
        return modelAndView;
    }

    @PostMapping(value = "/loadFlowsSaleAjax", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse loadFlowsSale(MultipartHttpServletRequest request, HttpServletRequest httpServletRequest) {

        GenericResponse response = new GenericResponse();

        Iterator<String> itr = request.getFileNames();
        List<MultipartFile> multipartFiles = new ArrayList<>(0);
        while (itr.hasNext()) {
            multipartFiles.add(request.getFile(itr.next()));
        }

        MultipartFile multipartFile = multipartFiles.get(0);
        String err = "";
        try {
            err = uploadExcelFunc.ExcelParser(multipartFile, "invFlowsSale", httpServletRequest);
            response.setMessage("Файл <b>" + multipartFile.getOriginalFilename() + "</b> успешно загружен.");
        } catch (IOException | ParseException e) {
            System.out.println(err);
            e.printStackTrace();
            response.setError(e.getLocalizedMessage());
        }

        return response;
    }

    @PostMapping(value = "/divideFlows", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse divideFlowsSale(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        BigInteger flowId = searchSummary.getDivideSumId();
        BigDecimal divideSum = searchSummary.getDivideSum();
        InvestorsFlowsSale oldFlows = investorsFlowsSaleService.findById(flowId);
        InvestorsFlowsSale newFlows = investorsFlowsSaleService.findById(flowId);
        oldFlows.setProfitToReInvest(oldFlows.getProfitToReInvest().subtract(divideSum));
        newFlows.setId(null);
        newFlows.setProfitToReInvest(divideSum);
        newFlows.setSourceId(oldFlows.getId());
        investorsFlowsSaleService.update(oldFlows);
        investorsFlowsSaleService.create(newFlows);
        response.setMessage(oldFlows.getProfitToReInvest().toPlainString());
        return response;
    }

    @GetMapping(value = "/deleteFlowsSale")
    public String deleteFlows() {
        investorsFlowsSaleService.delete();
        return "redirect:/flowsSale";
    }

    @ModelAttribute("facilities")
    public List<Facilities> initializeFacilities() {
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

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

}
