package com.art.controllers;

import com.art.config.SecurityUtils;
import com.art.config.application.Location;
import com.art.config.exception.AnnexNotFoundException;
import com.art.config.exception.UsernameNotFoundException;
import com.art.model.AppUser;
import com.art.model.UsersAnnexToContracts;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.filters.InvestorAnnexFilter;
import com.art.model.supporting.model.AnnexModel;
import com.art.model.supporting.view.InvestorAnnex;
import com.art.service.UserService;
import com.art.service.UsersAnnexToContractsService;
import com.art.service.view.InvestorAnnexService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.aarboard.nextcloud.api.NextcloudConnector;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * @author Alexandr Stegnin
 */
@Controller
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AnnexController {

  InvestorAnnexService annexService;
  InvestorAnnexFilter filter = new InvestorAnnexFilter();
  UserService userService;
  UsersAnnexToContractsService usersAnnexToContractsService;
  NextcloudConnector connector;
  Environment env;

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

  @PostMapping(path = Location.INVESTOR_ANNEXES_DELETE_LIST)
  public @ResponseBody
  ApiResponse deleteAnnexesList(@RequestBody AnnexModel annex) {
    annexService.deleteList(annex.getAnnexIdList());
    return ApiResponse.builder()
        .message("Записи удалены")
        .status(HttpStatus.OK.value())
        .build();
  }

  @RequestMapping("/annexes/{annexId}")
  public void getFile(HttpServletResponse response, @PathVariable BigInteger annexId) throws IOException {
    AppUser currentUser = userService.findByLogin(SecurityUtils.getUsername());
    if (Objects.isNull(currentUser)) {
      throw UsernameNotFoundException.build404Exception("Пользователь не найден");
    }
    UsersAnnexToContracts annex = usersAnnexToContractsService.findById(annexId);
    if (Objects.isNull(annex)) {
      throw AnnexNotFoundException.build404Exception("Приложение не найдено");
    }
    if (!annex.getUserId().equals(SecurityUtils.getUserId())) {
      throw new SecurityException("Доступ к файлу запрещён");
    }

    Path path = Files.createTempFile("temp-", ".pdf");

    boolean transferred = connector.downloadFile(getPath(annex), path.getParent().toAbsolutePath() + File.separator);
    if (!transferred) {
      log.error("Error get file {}", annex.getAnnex().getAnnexName());
    }
    File file = new File(path.getParent() + File.separator + annex.getAnnex().getAnnexName());
    try (FileInputStream inputStream = new FileInputStream(file)) {
      response.setContentType(MediaType.APPLICATION_PDF_VALUE);
      response.setContentLength((int) file.length());
      response.setHeader("Content-Disposition", "inline;filename=\"" + file.getName() + "\"");
      FileCopyUtils.copy(inputStream, response.getOutputStream());
    }
    boolean deleted = Files.deleteIfExists(file.toPath());
    if (!deleted) {
      log.debug("Can't delete file {}", file.getName());
    }
    deleted = Files.deleteIfExists(path);
    if (!deleted) {
      log.debug("Can't delete file {}", path.toFile().getName());
    }
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

  private String getPath(UsersAnnexToContracts attachment) {
    return File.separator + env.getProperty("nextcloud.folder") + File.separator + attachment.getAnnex().getAnnexName();
  }

}
