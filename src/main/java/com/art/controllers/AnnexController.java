package com.art.controllers;

import com.art.config.SecurityUtils;
import com.art.config.application.Location;
import com.art.model.AppUser;
import com.art.model.UsersAnnexToContracts;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.filters.InvestorAnnexFilter;
import com.art.model.supporting.model.AnnexModel;
import com.art.model.supporting.view.InvestorAnnex;
import com.art.service.UserService;
import com.art.service.UsersAnnexToContractsService;
import com.art.service.view.InvestorAnnexService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Controller
public class AnnexController {

    private final InvestorAnnexService annexService;

    private final InvestorAnnexFilter filter;

    private final UserService userService;

    private final UsersAnnexToContractsService usersAnnexToContractsService;

    public AnnexController(InvestorAnnexService annexService, UserService userService,
                           UsersAnnexToContractsService usersAnnexToContractsService) {
        this.annexService = annexService;
        this.userService = userService;
        this.usersAnnexToContractsService = usersAnnexToContractsService;
        this.filter = new InvestorAnnexFilter();
    }

    @GetMapping(path = Location.INVESTOR_ANNEXES)
    public String getAnnexes(@PageableDefault(size = 100) @SortDefault Pageable pageable, ModelMap model) {
        prepareModel(model, filter, pageable);
        return "annex-list";
    }

    @PostMapping(path = Location.INVESTOR_ANNEXES)
    public String getAnnexesFiltered(@PageableDefault(size = 100) @SortDefault Pageable pageable, ModelMap model,
                                     @ModelAttribute("filter") InvestorAnnexFilter filter) {
        prepareModel(model, filter, pageable);
        return "annex-list";
    }

    @PostMapping(path = Location.INVESTOR_ANNEXES_UPLOAD)
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

    @PostMapping(path = Location.INVESTOR_ANNEXES_DELETE)
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

    @PostMapping(path = Location.INVESTOR_ANNEXES_DELETE_LIST)
    public @ResponseBody
    GenericResponse deleteAnnexesList(@RequestBody AnnexModel annex) {
        GenericResponse response = new GenericResponse();
        if (null == annex || annex.getAnnexIdList().size() == 0) {
            response.setError("Список ID должен быть указан");
            return response;
        }
        String message = annexService.deleteAnnexex(annex.getAnnexIdList());
        response.setMessage(message);
        return response;
    }

    @RequestMapping("/annexToContract/{fileName}")
    void getFile(HttpServletResponse response, @PathVariable String fileName) throws IOException {
        AppUser currentUser = userService.findByLogin(SecurityUtils.getUsername());
        if (null == currentUser) {
            throw new SecurityException("Пользователь не найден");
        }
        List<UsersAnnexToContracts> annexes = usersAnnexToContractsService.findByUserAndAnnexName(currentUser.getId(), fileName);
        if (annexes.size() == 0) {
            throw new RuntimeException("Приложение [" + fileName + "] не найдено");
        }
        String path = System.getProperty("catalina.home") + "/pdfFiles";

        if (!fileName.contains(".pdf")) {
            fileName = fileName + ".pdf";
        }
        path = path + File.separator + fileName;
        File file = new File(path);
        FileInputStream inputStream = new FileInputStream(file);

        response.setContentType("application/pdf");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "inline;filename=\"" + fileName + "\"");
        FileCopyUtils.copy(inputStream, response.getOutputStream());

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
