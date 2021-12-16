package com.art.service.view;

import com.art.config.SecurityUtils;
import com.art.config.exception.FileUploadException;
import com.art.config.exception.UsernameNotFoundException;
import com.art.config.exception.UsernameParseException;
import com.art.model.AnnexToContracts;
import com.art.model.AppUser;
import com.art.model.UsersAnnexToContracts;
import com.art.model.supporting.filters.InvestorAnnexFilter;
import com.art.model.supporting.view.InvestorAnnex;
import com.art.repository.view.InvestorAnnexRepository;
import com.art.service.UserService;
import com.art.service.UsersAnnexToContractsService;
import com.art.specifications.InvestorAnnexSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.aarboard.nextcloud.api.NextcloudConnector;
import org.aarboard.nextcloud.api.filesharing.SharePermissions;
import org.aarboard.nextcloud.api.filesharing.ShareType;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Alexandr Stegnin
 */

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InvestorAnnexService {

  UserService userService;
  InvestorAnnexRepository annexRepository;
  InvestorAnnexSpecification specification;
  UsersAnnexToContractsService usersAnnexToContractsService;
  NextcloudConnector connector;
  Environment env;

  public List<InvestorAnnex> findAll() {
    return annexRepository.findAll();
  }

  public List<InvestorAnnex> findAll(InvestorAnnexFilter filters, Pageable pageable) {
    return annexRepository.findAll(specification.getFilter(filters), pageable).getContent();
  }

  public List<String> getInvestors() {
    List<InvestorAnnex> investorAnnexes = annexRepository.findAll();
    List<String> investors = new ArrayList<>();
    investors.add("Выберите инвестора");
    investors.addAll(investorAnnexes
        .stream()
        .map(InvestorAnnex::getInvestor)
        .distinct()
        .sorted()
        .collect(Collectors.toList()));
    return investors;
  }

  public String uploadFiles(MultipartHttpServletRequest request) throws IOException {
    Iterator<String> itr = request.getFileNames();
    List<MultipartFile> multipartFiles = new ArrayList<>(0);
    while (itr.hasNext()) {
      multipartFiles.add(request.getFile(itr.next()));
    }
    return uploadFiles(multipartFiles);
  }

  public String uploadFiles(List<MultipartFile> files) throws IOException {
    AppUser currentUser = userService.findByLogin(SecurityUtils.getUsername());
    for (MultipartFile uploadedFile : files) {
      checkFile(uploadedFile);
      Path path = Files.createTempFile("temp-", ".pdf");
      File file = path.toFile();
      uploadedFile.transferTo(file);
      AppUser investor = getInvestor(uploadedFile.getOriginalFilename());

      AnnexToContracts annex = new AnnexToContracts();
      annex.setAnnexName(uploadedFile.getOriginalFilename());
      annex.setDateLoad(new Date());
      annex.setLoadedBy(currentUser.getId());

      UsersAnnexToContracts usersAnnexToContracts = new UsersAnnexToContracts();
      usersAnnexToContracts.setAnnex(annex);
      usersAnnexToContracts.setUserId(investor.getId());
      usersAnnexToContracts.setAnnexRead(0);
      usersAnnexToContracts.setDateRead(null);
      usersAnnexToContractsService.create(usersAnnexToContracts);

      uploadFileToNextcloud(file, uploadedFile);
      Files.delete(path);
    }
    return "Файлы успешно загружены";
  }

  private void checkFile(MultipartFile uploadedFile) {
    if (uploadedFile.isEmpty()) {
      String message = String.format("Файл %s пустой", uploadedFile.getOriginalFilename());
      log.error(message);
      throw FileUploadException.build400Exception(message);
    }
    if (!uploadedFile.getOriginalFilename().endsWith(".pdf")) {
      String message = String.format("Файл %s должен быть в формате .PDF", uploadedFile.getOriginalFilename());
      log.error(message);
      throw FileUploadException.build400Exception(message);
    }
  }

  private void uploadFileToNextcloud(File file, MultipartFile uploadedFile) {
    try {
      String remoteFolder = File.separator + env.getProperty("nextcloud.folder") + File.separator;
      connector.uploadFile(file, remoteFolder + uploadedFile.getOriginalFilename());
      SharePermissions permissions = new SharePermissions(SharePermissions.SingleRight.READ);
      connector.doShare(remoteFolder + uploadedFile.getOriginalFilename(),
          ShareType.PUBLIC_LINK, "", false, null, permissions);
    } catch (Exception e) {
      throw FileUploadException.build400Exception(e.getMessage());
    }
  }

  private AppUser getInvestor(String fileName) {
    String lastName = "";
    Pattern pattern = Pattern.compile("Инвестор\\s\\d{3,}", Pattern.UNICODE_CASE);
    Matcher matcher = pattern.matcher(fileName);
    if (matcher.find()) {
      lastName = matcher.group(0);
    }
    String login;
    String[] parts = lastName.split("\\s");
    if (parts.length == 2) {
      login = "investor" + parts[1];
    } else {
      throw UsernameParseException.build400Exception("Ошибка получения логина инвестора из имени файла [" + fileName + "]");
    }
    AppUser investor = userService.findByLogin(login);
    if (null == investor) {
      throw UsernameNotFoundException.build404Exception("Пользователь с логином [" + lastName + "] не найден");
    }
    return investor;
  }

}
