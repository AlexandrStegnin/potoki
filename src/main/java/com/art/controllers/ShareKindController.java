package com.art.controllers;

import com.art.func.GetPrincipalFunc;
import com.art.model.ShareKind;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.SearchSummary;
import com.art.service.ShareKindService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Controller
public class ShareKindController {

    @Resource(name = "getPrincipalFunc")
    private GetPrincipalFunc getPrincipalFunc;

    @Resource(name = "shareKindService")
    private ShareKindService shareKindService;

    @GetMapping(value = "/viewShareKind")
    public ModelAndView typeClosingInvestPage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("viewShareKinds");
        List<ShareKind> shareKindList = shareKindService.findAll();
        ShareKind shareKind = new ShareKind();
        String title = "Добавление вида доли";
        modelAndView.addObject("shareKind", shareKind);
        modelAndView.addObject("shareKindList", shareKindList);
        modelAndView.addObject("loggedinuser", getPrincipalFunc.getLogin());
        modelAndView.addObject("title", title);
        return modelAndView;
    }

    @PostMapping(value = { "/saveShareKind" }, produces="application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse saveShareKind(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        ShareKind shareKind = new ShareKind();
        shareKind.setShareKind(searchSummary.getShareKind());
        shareKindService.create(shareKind);
        response.setMessage("Вид доли <b>" + shareKind.getShareKind() + "</b> успешно добавлен.");
        return response;
    }


    /**
     * This method will be called on form submission, handling POST request for
     * updating user in database. It also validates the user input
     */
    @PostMapping(value = { "/editShareKind" }, produces="application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse updateShareKind(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        ShareKind shareKind = shareKindService.findById(new BigInteger(searchSummary.getShareKindId()));
        shareKind.setShareKind(searchSummary.getShareKind());
        shareKindService.update(shareKind);
        response.setMessage("Вид доли <b>" + shareKind.getShareKind() + "</b> успешно изменён.");
        return response;
    }

    @PostMapping(value = { "/deleteShareKind" }, produces="application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse deleteShareKind(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        shareKindService.deleteById(new BigInteger(searchSummary.getShareKindId()));
        response.setMessage("Вид доли успешно удалён.");
        return response;
    }
}
