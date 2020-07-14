package com.art.service.view;

import com.art.config.SecurityUtils;
import com.art.model.AnnexToContracts;
import com.art.model.AppUser;
import com.art.model.UsersAnnexToContracts;
import com.art.model.supporting.filters.InvestorAnnexFilter;
import com.art.model.supporting.view.InvestorAnnex;
import com.art.repository.view.InvestorAnnexRepository;
import com.art.service.StatusService;
import com.art.service.UserService;
import com.art.service.UsersAnnexToContractsService;
import com.art.specifications.InvestorAnnexSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
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
public class InvestorAnnexService {

    private final InvestorAnnexRepository annexRepository;

    private final InvestorAnnexSpecification specification;

    private final UsersAnnexToContractsService usersAnnexToContractsService;

    private final UserService userService;

    private final StatusService statusService;

    public InvestorAnnexService(InvestorAnnexRepository annexRepository, InvestorAnnexSpecification specification,
                                UsersAnnexToContractsService usersAnnexToContractsService, UserService userService,
                                StatusService statusService) {
        this.annexRepository = annexRepository;
        this.specification = specification;
        this.usersAnnexToContractsService = usersAnnexToContractsService;
        this.userService = userService;
        this.statusService = statusService;
    }

    /**
     * Получить список всех приложений инвесторов
     *
     * @return - список приложений
     */
    public List<InvestorAnnex> findAll() {
        return annexRepository.findAll();
    }

    /**
     * Получить список приложений инвесторов с фильтрами и постраничной загрузкой
     *
     * @param filters  - фильтр
     * @param pageable - постраничная загрузка
     * @return - список приложений
     */
    public List<InvestorAnnex> findAll(InvestorAnnexFilter filters, Pageable pageable) {
        return annexRepository.findAll(specification.getFilter(filters), pageable).getContent();
    }

    /**
     * Получить список инвесторов, у которых загружены приложения
     *
     * @return - список логинов инвесторов
     */
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

    /**
     * Загрузить файлы
     *
     * @param request - запрос, содержащий список файлов
     * @return - статус загрузки
     * @throws IOException - если возникла ошибка при работе с файлами
     * @throws RuntimeException - при ошибке с файлами
     */
    public String uploadFiles(MultipartHttpServletRequest request) throws IOException, RuntimeException {
        Iterator<String> itr = request.getFileNames();
        List<MultipartFile> multipartFiles = new ArrayList<>(0);
        while (itr.hasNext()) {
            multipartFiles.add(request.getFile(itr.next()));
        }
        return uploadFiles(multipartFiles);
    }

    public String uploadFiles(List<MultipartFile> files) throws IOException, RuntimeException {
        String path = System.getProperty("catalina.home") + "/pdfFiles/";
        AppUser currentUser = userService.findByLogin(SecurityUtils.getUsername());
        int counter = 0;
        int filesCnt = files.size();
        for (MultipartFile uploadedFile : files) {
            counter++;
            statusService.sendStatus(String.format("Загружаем %d из %d файлов", counter, filesCnt));
            String fileName = uploadedFile.getOriginalFilename();
            if (uploadedFile.isEmpty()) {
                String message = "Файл [" + fileName + "] пустой";
                log.error(message);
                return message;
            }
            if (!fileName.endsWith(".pdf")) {
                String message = "Файл [" + fileName + "] должен быть в формате .PDF";
                log.error(message);
                return message;
            }
            File file = new File(path + fileName);
            if (file.exists()) {
                String message = "Файл [" + fileName + "] уже существует";
                log.error(message);
                continue;
            }
            AppUser investor;
            try {
                investor = getInvestor(fileName);
            } catch (RuntimeException e) {
                return e.getMessage();
            }
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            AnnexToContracts annex = new AnnexToContracts();
            annex.setAnnexName(fileName);
            annex.setDateLoad(new Date());
            annex.setLoadedBy(currentUser.getId());
            annex.setFilePath(path);

            UsersAnnexToContracts usersAnnexToContracts = new UsersAnnexToContracts();
            usersAnnexToContracts.setAnnex(annex);
            usersAnnexToContracts.setUserId(investor.getId());
            usersAnnexToContracts.setAnnexRead(0);
            usersAnnexToContracts.setDateRead(null);
            usersAnnexToContractsService.create(usersAnnexToContracts);
            uploadedFile.transferTo(file);
        }
        statusService.sendStatus("OK");
        return "Файлы успешно загружены";
    }

    /**
     * Получить пользователя по имени файла
     *
     * @param fileName- имя файла
     * @return - пользователь
     * @throws RuntimeException - если пользователь не найден или название файла не позволило прочитать логин
     */
    private AppUser getInvestor(String fileName) throws RuntimeException {
        String lastName = "";
        Pattern pattern = Pattern.compile("Инвестор\\s\\d{3,}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            lastName = matcher.group(0);
        }
        String login;
        String[] parts = lastName.split("\\s");
        if (parts.length == 2) {
            login = "investor" + parts[1];
        } else {
            throw new RuntimeException("Ошибка получения логина инвестора из имени файла [" + fileName + "]");
        }
        AppUser investor = userService.findByLogin(login);
        if (null == investor) {
            throw new RuntimeException("Пользователь с логином [" + lastName + "] не найден");
        }
        return investor;
    }

    /**
     * Удалить приложение по id
     *
     * @param annexId - id приложения
     * @return - статус операции
     */
    public String deleteAnnex(BigInteger annexId) {
        UsersAnnexToContracts annex = usersAnnexToContractsService.findById(annexId);
        if (null == annex) {
            return String.format("Приложение с id = [%d] не найдено", annexId);
        }
        String fileName = annex.getAnnex().getAnnexName();
        String path = annex.getAnnex().getFilePath();
        if (null == path) {
            return String.format("Файл с id = [%d] не найден на файловой системе", annexId);
        }
        File file = new File(path + fileName);
        usersAnnexToContractsService.deleteById(annexId);
        if (!file.exists()) {
            return String.format("Файл [%s] не найден на файловой системе", annex.getAnnex().getAnnexName());
        }
        boolean deleted = file.delete();
        String success = deleted ? "успешно" : "не";
        return String.format("Приложение к договору [%s] %s удалено", annex.getAnnex().getAnnexName(), success);
    }

    /**
     * Удалить приложения по списку id
     *
     * @param annexIdList - id приложения
     * @return - статус операции
     */
    public String deleteAnnexex(List<BigInteger> annexIdList) {
        final int[] deletedCount = {0};
        final int[] noDeletedCount = {0};
        StringBuilder messageBuilder = new StringBuilder();
        annexIdList.forEach(annexId -> {
            UsersAnnexToContracts annex = usersAnnexToContractsService.findById(annexId);
            if (null == annex) {
                noDeletedCount[0]++;
                return;
            }
            String fileName = annex.getAnnex().getAnnexName();
            String path = annex.getAnnex().getFilePath();
            if (null == path) {
                noDeletedCount[0]++;
                return;
            }
            File file = new File(path + fileName);
            usersAnnexToContractsService.deleteById(annexId);
            if (!file.exists()) {
                noDeletedCount[0]++;
                return;
            }
            boolean deleted = file.delete();
            if (deleted) {
                deletedCount[0]++;
            } else {
                noDeletedCount[0]++;
            }
        });
        messageBuilder.append(String.format("Успешно удалены [%d шт]", deletedCount[0]));
        if (noDeletedCount[0] > 0) {
            messageBuilder.append("\n").append(String.format("НЕ удалены [%d шт]", noDeletedCount[0]));
        }
        return messageBuilder.toString();
    }

}
