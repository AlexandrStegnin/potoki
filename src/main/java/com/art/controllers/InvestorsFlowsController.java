package com.art.controllers;

import com.art.func.UploadExcelFunc;
import com.art.model.InvestorsFlowsSale;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.SearchSummary;
import com.art.service.InvestorsFlowsSaleService;
import com.art.service.InvestorsFlowsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


@Controller
public class InvestorsFlowsController {

    @Resource(name = "uploadExcelFunc")
    private UploadExcelFunc uploadExcelFunc;

    @Resource(name = "investorsFlowsService")
    private InvestorsFlowsService investorsFlowsService;

    @Resource(name = "investorsFlowsSaleService")
    private InvestorsFlowsSaleService investorsFlowsSaleService;

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
            uploadExcelFunc.ExcelParser(multipartFile, "invFlows", httpServletRequest);
            response.setMessage("Файл <b>" + multipartFile.getOriginalFilename() + "</b> успешно загружен.");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            response.setError(e.getLocalizedMessage());
        }
        return response;
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
