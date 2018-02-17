package com.art.controllers;

import com.art.func.UploadExcelFunc;
import com.art.model.AlphaExtract;
import com.art.model.ToshlExtract;
import com.art.model.supporting.FileBucket;
import com.art.service.AlphaExtractService;
import com.art.service.ToshlExtractService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class UploadExcelController {

    @Resource(name = "uploadExcelFunc")
    private UploadExcelFunc uploadExcelFunc;

    @Resource(name = "alphaExtractService")
    private AlphaExtractService alphaExtractService;

    @Resource(name = "toshlExtractService")
    private ToshlExtractService toshlExtractService;

    @RequestMapping(value = "/alphaextract", method = RequestMethod.GET)
    public String viewextract(ModelMap model) {
        FileBucket fileModel = new FileBucket();
        model.addAttribute("fileBucket", fileModel);
        List<AlphaExtract> alphaExtracts = alphaExtractService.findAll();
        model.addAttribute("alphaExtracts", alphaExtracts);
        return "viewalphaextract";
    }

    @GetMapping(value = "/toshlextract")
    public String viewtoshlextract(ModelMap model) {
        FileBucket fileModel = new FileBucket();
        model.addAttribute("fileBucket", fileModel);
        List<ToshlExtract> toshlExtracts = toshlExtractService.findAll();
        model.addAttribute("toshlExtracts", toshlExtracts);
        return "viewtoshlextract";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String singleFileUpload(ModelMap model) {
        FileBucket fileModel = new FileBucket();
        model.addAttribute("fileBucket", fileModel);
        return "uploadexcel";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadExcel(ModelMap model, @ModelAttribute("fileBucket") FileBucket fileBucket, HttpServletRequest request) throws IOException, ParseException {
        MultipartFile multipartFile = fileBucket.getFile();
        String ret = "Альфа банка.";
        String redirectUrl = "/alphaextract";
        String title = "Выписка Альфа банка";
        String err = uploadExcelFunc.ExcelParser(multipartFile, "alpha", request);

        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        model.addAttribute("title", title);
        model.addAttribute("err", err);
        return "success";
    }

    @RequestMapping(value = "/uploadtoshl", method = RequestMethod.GET)
    public String singleToshlUpload(ModelMap model) {
        FileBucket fileModel = new FileBucket();
        model.addAttribute("fileBucket", fileModel);
        return "uploadexcel";
    }

    @RequestMapping(value = "/uploadtoshl", method = RequestMethod.POST)
    public String uploadToshl(ModelMap model, @ModelAttribute("fileBucket") FileBucket fileBucket, HttpServletRequest request)
            throws IOException, ParseException {
        String ret = "Toshl.";
        String redirectUrl = "/toshlextract";
        String title = "Выписка Toshl";
        MultipartFile multipartFile = fileBucket.getFile();
        String err = uploadExcelFunc.ExcelParser(multipartFile, "toshl", request);
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        model.addAttribute("err", err);
        model.addAttribute("title", title);
        return "success";
    }

    @RequestMapping(value = "/deletedata", method = RequestMethod.GET)
    public String deleteUpload(ModelMap model) {
        alphaExtractService.deleteAllByPIdIsNull();
        return "redirect:/alphaextract";
    }

    @RequestMapping(value = "/deletetoshldata", method = RequestMethod.GET)
    public String deleteToshlUpload(ModelMap model) {
        toshlExtractService.delete();
        return "redirect:/toshlextract";
    }

    @PostMapping(value = "/uploadflows")
    public String uploadFlows(ModelMap model, @ModelAttribute("fileBucket") FileBucket fileBucket, HttpServletRequest request)
            throws IOException, ParseException {
        String ret = "потоков инвесторов";
        String redirectUrl = "/investorsAllFlows";
        String title = "Потоки инвесторов. Ручники";
        MultipartFile multipartFile = fileBucket.getFile();
        String err = uploadExcelFunc.ExcelParser(multipartFile, "flows", request);
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        model.addAttribute("err", err);
        model.addAttribute("title", title);
        return "success";
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
