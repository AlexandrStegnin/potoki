package com.art.controllers;

import com.art.model.UsersAnnexToContracts;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.filters.InvestorAnnexFilter;
import com.art.model.supporting.view.InvestorAnnex;
import com.art.service.view.InvestorAnnexService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Controller
public class AnnexController {

    private final InvestorAnnexService annexService;

    private final InvestorAnnexFilter filter;

    public AnnexController(InvestorAnnexService annexService) {
        this.annexService = annexService;
        this.filter = new InvestorAnnexFilter();
    }

    @GetMapping(path = "/investor/annexes")
    public String getAnnexes(@PageableDefault(size = 100) @SortDefault Pageable pageable, ModelMap model) {
        prepareModel(model, filter, pageable);
        return "annex";
    }

    @PostMapping(path = "/investor/annexes")
    public String getAnnexesFiltered(@PageableDefault(size = 100) @SortDefault Pageable pageable, ModelMap model,
                                     @ModelAttribute("filter") InvestorAnnexFilter filter) {
        prepareModel(model, filter, pageable);
        return "annex";
    }

    @PostMapping(path = "/investor/annexes/upload")
    public @ResponseBody
    GenericResponse uploadAnnexes(MultipartHttpServletRequest request) {
        GenericResponse response = new GenericResponse();
        try {
            String message = annexService.uploadFiles(request);
            response.setMessage(message);
        } catch (RuntimeException | IOException e) {
            response.setError(e.getMessage());
        }
        return response;
    }

    @PostMapping(path = "/investor/annexes/delete")
    public @ResponseBody
    GenericResponse deleteAnnexes(@RequestBody UsersAnnexToContracts annex) {
        GenericResponse response = new GenericResponse();
        if (null == annex.getId()) {
            response.setError("ID должен быть указан");
            return response;
        }
        String message = annexService.deleteAnnex(annex.getId());
        response.setMessage(message);
        return response;
    }

    private void prepareModel(ModelMap model, InvestorAnnexFilter filter, Pageable pageable) {
        String path = System.getProperty("catalina.home") + "/pdfFiles/";
        File file = new File(path);
        List<InvestorAnnex> contracts = annexService.findAll(filter, pageable);
        List<String> logins = annexService.getInvestors();
        model.addAttribute("files", file.listFiles());
        model.addAttribute("contracts", contracts);
        model.addAttribute("investors", logins);
        model.addAttribute("filter", filter);
    }

}
