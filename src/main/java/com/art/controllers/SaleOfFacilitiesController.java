package com.art.controllers;

import com.art.func.UploadExcelFunc;
import com.art.model.SaleOfFacilities;
import com.art.model.supporting.FileBucket;
import com.art.model.supporting.GenericResponse;
import com.art.service.SaleOfFacilitiesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class SaleOfFacilitiesController {

    @Resource(name = "uploadExcelFunc")
    private UploadExcelFunc uploadExcelFunc;

    @Resource(name = "saleOfFacilitiesService")
    private SaleOfFacilitiesService saleOfFacilitiesService;

    @GetMapping(value = "/saleOfFacilities")
    public String saleOfFacilitiesPage(ModelMap model) {
        FileBucket fileModel = new FileBucket();
        List<SaleOfFacilities> saleOfFacilities = saleOfFacilitiesService.findAll();
        model.addAttribute("saleOfFacilities", saleOfFacilities);
        model.addAttribute("fileBucket", fileModel);

        return "viewSaleOfFacilities";
    }

    @PostMapping(value = "/loadSaleOfFacilitiesAjax", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse loadSaleOfFacilitiesPage(MultipartHttpServletRequest request, HttpServletRequest httpServletRequest) {

        GenericResponse response = new GenericResponse();

        Iterator<String> itr = request.getFileNames();
        List<MultipartFile> multipartFiles = new ArrayList<>(0);
        while (itr.hasNext()) {
            multipartFiles.add(request.getFile(itr.next()));
        }

        MultipartFile multipartFile = multipartFiles.get(0);
        String err = "";
        try {
            err = uploadExcelFunc.ExcelParser(multipartFile, "saleOfFacilities", httpServletRequest);
            response.setMessage("Файл <b>" + multipartFile.getOriginalFilename() + "</b> успешно загружен.");
        } catch (IOException | ParseException e) {
            response.setError(e.getLocalizedMessage());
        }

        return response;
    }

    @PostMapping(value = {"/deleteSaleOfFacilities"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse deleteFlowsList() {
        GenericResponse response = new GenericResponse();
        saleOfFacilitiesService.deleteAll();
        response.setMessage("Данные успешно удалены");
        return response;
    }
}
