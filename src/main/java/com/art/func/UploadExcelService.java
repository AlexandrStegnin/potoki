package com.art.func;

import com.art.model.*;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.enums.ShareType;
import com.art.model.supporting.enums.UploadType;
import com.art.service.*;
import com.art.util.ExcelUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервис для загрузки Excel файлов
 *
 * @author Alexandr Stegnin
 */

@Service
public class UploadExcelService {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);

    private final UserService userService;

    private final RoomService roomService;

    private final FacilityService facilityService;

    private final UnderFacilityService underFacilityService;

    private final RentPaymentService rentPaymentService;

    private final SalePaymentService salePaymentService;

    private final GlobalFunctions globalFunctions;

    public UploadExcelService(UserService userService, RoomService roomService, FacilityService facilityService,
                              UnderFacilityService underFacilityService, RentPaymentService rentPaymentService,
                              SalePaymentService salePaymentService, GlobalFunctions globalFunctions) {
        this.userService = userService;
        this.roomService = roomService;
        this.facilityService = facilityService;
        this.underFacilityService = underFacilityService;
        this.rentPaymentService = rentPaymentService;
        this.salePaymentService = salePaymentService;
        this.globalFunctions = globalFunctions;
    }

    public ApiResponse upload(MultipartHttpServletRequest request, UploadType type) {
        Iterator<String> itr = request.getFileNames();
        List<MultipartFile> multipartFiles = new ArrayList<>(0);
        while (itr.hasNext()) {
            multipartFiles.add(request.getFile(itr.next()));
        }
        MultipartFile file = multipartFiles.get(0);
        return upload(file, request, type);
    }

    /**
     * Загрузить excel файл с данными по продаже/аренде
     *
     * @param file файл с клиента
     * @param request запрос
     * @param type вид загружаемого файла
     * @return ответ об успешном/неудачном выполнении операции
     */
    public ApiResponse upload(MultipartFile file, HttpServletRequest request, UploadType type) {
        ApiResponse response = new ApiResponse();
        InputStream fileInputStream;
        Workbook workbook;
        Sheet sheet;
        try {
            fileInputStream = new BufferedInputStream(file.getInputStream());
            workbook = ExcelUtils.getWorkbook(fileInputStream, file.getOriginalFilename());
            sheet = workbook.getSheetAt(0);
        } catch (IOException ex) {
            return new ApiResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
        switch (type) {
            case RENT:
                HttpSession session = request.getSession(true);
                session.setMaxInactiveInterval(30 * 60);
                response = uploadRent(sheet);
                break;
            case SALE:
                response = uploadSale(sheet);
                break;
        }
        return response;
    }

    /**
     * Загрузить excel файл с данными о выплатах инвесторам по аренде
     *
     * @param sheet лист excel файла
     * @return ответ об успешном/неудачном выполнении
     */
    private ApiResponse uploadRent(Sheet sheet) {
        List<Room> rooms = roomService.findAll();
        List<AppUser> users = userService.findAll();
        List<RentPayment> rentPaymentTmp = rentPaymentService.findAll();
        int cel = 0;
        List<RentPayment> rentPaymentList = new ArrayList<>(0);

        for (Row row : sheet) {
            cel++;
            if (cel > 1) {
                if (row.getCell(0) != null && row.getCell(0).getCellTypeEnum() != CellType.BLANK) {
                    Calendar calendar = Calendar.getInstance();
                    try {
                        calendar.setTime(FORMAT.parse(row.getCell(0).getDateCellValue().toString()));
                    } catch (Exception ignored) {}

                    java.time.LocalDate cal = calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    try {
                        for (int i = 0; i < row.getLastCellNum(); i++) {
                            row.getCell(i).setCellType(CellType.STRING);
                        }
                    } catch (Exception ignored) {}

                    String lastName;
                    lastName = row.getCell(4).getStringCellValue();
                    AppUser user = users.stream().filter(u -> u.getProfile().getLastName().equalsIgnoreCase(lastName))
                            .findFirst()
                            .orElse(null);

                    String underFacilityName = row.getCell(2).getStringCellValue();
                    if (underFacilityName == null || underFacilityName.isEmpty()) {
                        break;
                    }
                    UnderFacility underFacility = underFacilityService.findByName(underFacilityName);
                    if (underFacility == null) {
                        break;
                    }
                    String facilityName = row.getCell(1).getStringCellValue();
                    if (facilityName == null || facilityName.isEmpty()) {
                        break;
                    }
                    Facility facility = facilityService.findByName(facilityName);
                    if (facility == null) {
                        break;
                    }
                    RentPayment rentPayment = new RentPayment();
                    rentPayment.setDateReport(Date.from(cal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    rentPayment.setFacility(facility);

                    rentPayment.setUnderFacility(underFacility);

                    rentPayment.setRoom(rooms.stream()
                            .filter(r -> r.getName().equalsIgnoreCase(row.getCell(3).getStringCellValue()))
                            .findFirst().orElse(null));

                    rentPayment.setInvestor(user);
                    rentPayment.setShareType(row.getCell(5).getStringCellValue());
                    rentPayment.setGivenCash(Float.parseFloat(row.getCell(6).getStringCellValue()));
                    rentPayment.setSumInUnderFacility(Float.parseFloat(row.getCell(7).getStringCellValue()));
                    rentPayment.setShareForSvod(Float.parseFloat(row.getCell(8).getStringCellValue()));

                    rentPayment.setShare(Float.parseFloat(row.getCell(9).getStringCellValue()));
                    rentPayment.setTaxation(Float.parseFloat(row.getCell(10).getStringCellValue()));
                    rentPayment.setCashing(Float.parseFloat(row.getCell(11).getStringCellValue()));
                    rentPayment.setSumma(Float.parseFloat(row.getCell(13).getStringCellValue()));
                    rentPayment.setOnInvestor(Float.parseFloat(row.getCell(14).getStringCellValue()));
                    rentPayment.setAfterTax(Float.parseFloat(row.getCell(15).getStringCellValue()));
                    rentPayment.setAfterDeductionEmptyFacility(Float.parseFloat(row.getCell(16).getStringCellValue()));
                    rentPayment.setAfterCashing(Float.parseFloat(row.getCell(17).getStringCellValue()));

                    rentPayment.setReInvest(row.getCell(18).getStringCellValue());

                    Facility reFacility = null;
                    String reFacilityName = row.getCell(19).getStringCellValue();
                    if (reFacilityName != null && !reFacilityName.isEmpty()) {
                        reFacility = facilityService.findByName(reFacilityName);
                    }

                    rentPayment.setReFacility(reFacility);

                    List<RentPayment> flowsList = rentPaymentTmp.stream()
                            .filter(flows -> globalFunctions.getMonthInt(flows.getDateReport()) ==
                                    globalFunctions.getMonthInt(rentPayment.getDateReport()) &&
                                    globalFunctions.getYearInt(flows.getDateReport()) ==
                                            globalFunctions.getYearInt(rentPayment.getDateReport()) &&
                                    globalFunctions.getDayInt(flows.getDateReport()) ==
                                            globalFunctions.getDayInt(rentPayment.getDateReport()))

                            .filter(flows -> !Objects.equals(flows.getFacility(), null) &&
                                    flows.getFacility().getId().equals(rentPayment.getFacility().getId()))

                            .filter(flows -> !Objects.equals(rentPayment.getUnderFacility(), null) &&
                                    flows.getUnderFacility().getId().equals(rentPayment.getUnderFacility().getId()))

                            .filter(flows -> flows.getInvestor().getId().equals(rentPayment.getInvestor().getId()))

                            .collect(Collectors.toList());

                    if (flowsList.size() == 0) {
                        rentPaymentList.add(rentPayment);
                    }
                }

            }
        }
        rentPaymentService.saveList(rentPaymentList);
        return new ApiResponse("Загрузка файла с данными по аренде завершена");
    }

    /**
     * Загрузить excel файл с данными о выплатах инвесторам по продаже
     *
     * @param sheet лист excel файла
     * @return ответ об успешном/неудачном выполнении
     */
    private ApiResponse uploadSale(Sheet sheet) {
        if (!ExcelUtils.isCorrect(sheet)) {
            return new ApiResponse("Проверьте кол-во столбцов в файле. Должно быть 36", HttpStatus.BAD_REQUEST.value());
        }
        List<AppUser> users = userService.findAll();
        List<SalePayment> salePayments = salePaymentService.findAll();
        int cel = 0;
        List<SalePayment> salePaymentList = new ArrayList<>(0);

        List<ShareType> shareKinds = Arrays.asList(ShareType.values());
        Map<String, Facility> facilities = new HashMap<>();
        Map<String, UnderFacility> underFacilities = new HashMap<>();
        for (Row row : sheet) {
            cel++;
            if (cel > 1) {
                if (row.getCell(0) != null && row.getCell(0).getCellTypeEnum() != CellType.BLANK) {
                    Calendar calendar = Calendar.getInstance();
                    try {
                        calendar.setTime(FORMAT.parse(row.getCell(4).getDateCellValue().toString()));
                    } catch (Exception ex) {
                        return new ApiResponse(String.format("Не удачная попытка конвертировать строку в дату. Строка %d, столбец 5", cel),
                                HttpStatus.PRECONDITION_FAILED.value());
                    }

                    java.time.LocalDate cal = calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    Calendar dateSale = Calendar.getInstance();
                    try {
                        dateSale.setTime(FORMAT.parse(row.getCell(35).getDateCellValue().toString()));
                    } catch (Exception ex) {
                        return new ApiResponse(String.format("Неудачная попытка конвертировать строку в дату. Строка %d, столбец 36", cel),
                                HttpStatus.PRECONDITION_FAILED.value());
                    }

                    java.time.LocalDate calSale = dateSale.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    try {
                        for (int i = 0; i < row.getLastCellNum(); i++) {
                            row.getCell(i).setCellType(CellType.STRING);
                        }
                    } catch (Exception ignored) {}

                    String lastName;
                    lastName = row.getCell(1).getStringCellValue();
                    if (lastName == null || lastName.isEmpty()) {
                        return new ApiResponse(String.format("Не указан инвестор! Строка %d, столбец 2", cel),
                                HttpStatus.PRECONDITION_FAILED.value());
                    }
                    AppUser user = users.stream().filter(u -> u.getProfile().getLastName().equalsIgnoreCase(lastName))
                            .findFirst()
                            .orElse(null);
                    if (user == null) {
                        return new ApiResponse(String.format("Неудачная попытка найти пользователя \"%s\". Строка %d, столбец 2", lastName, cel),
                                HttpStatus.PRECONDITION_FAILED.value());
                    }

                    String facilityName = row.getCell(0).getStringCellValue();
                    if (facilityName == null || facilityName.isEmpty()) {
                        return new ApiResponse(String.format("Не указан объект! Строка %d, столбец 1", cel),
                                HttpStatus.PRECONDITION_FAILED.value());
                    }
                    SalePayment salePayment = new SalePayment();
                    Facility facility = facilities.get(facilityName);
                    if (facility == null) {
                        facility = facilityService.findByName(facilityName);
                        if (facility == null) {
                            return new ApiResponse(String.format("Не указан или не верно указан объект \"%s\". Строка %d, столбец 1", facilityName, cel),
                                    HttpStatus.PRECONDITION_FAILED.value());
                        }
                    }
                    facilities.putIfAbsent(facilityName, facility);

                    salePayment.setFacility(facility);
                    salePayment.setInvestor(user);
                    String shareType = row.getCell(2).getStringCellValue();
                    if (shareType == null) {
                        return new ApiResponse(String.format("Не указана доля. Строка %d, столбец 3", cel),
                                HttpStatus.PRECONDITION_FAILED.value());
                    }
                    ShareType shareKind = shareKinds
                            .stream()
                            .filter(share -> share.getTitle().equalsIgnoreCase(shareType))
                            .findFirst()
                            .orElse(null);
                    if (shareKind == null) {
                        return new ApiResponse(String.format("Не указана или не верно указана доля \"%s\". Строка %d, столбец 3", shareType, cel),
                                HttpStatus.PRECONDITION_FAILED.value());
                    }
                    salePayment.setShareType(shareKind);
                    String strCashInFacility = row.getCell(3).getStringCellValue();
                    BigDecimal cashInFacility;
                    try {
                        cashInFacility = new BigDecimal(strCashInFacility);
                    } catch (NumberFormatException ex) {
                        return new ApiResponse(String.format("Ошибка преобразования суммы \"Вложено в объект\". Строка %d, столбец 4", cel),
                                HttpStatus.PRECONDITION_FAILED.value());
                    }

                    salePayment.setCashInFacility(cashInFacility);
                    salePayment.setDateGiven(Date.from(cal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    String strInvShare = row.getCell(5).getStringCellValue();
                    BigDecimal invShare;
                    try {
                        invShare = new BigDecimal(strInvShare);
                    } catch (NumberFormatException ex) {
                        return new ApiResponse(String.format("Ошибка преобразования суммы \"Доля инвестора\". Строка %d, столбец 6", cel),
                                HttpStatus.PRECONDITION_FAILED.value());
                    }

                    salePayment.setInvestorShare(invShare);

                    String strCashInUnderFacility = row.getCell(6).getStringCellValue();
                    BigDecimal cashInUnderFacility;
                    try {
                        cashInUnderFacility = new BigDecimal(strCashInUnderFacility);
                    } catch (NumberFormatException ex) {
                        return new ApiResponse(String.format("Ошибка преобразования суммы \"Вложено в подобъект\". Строка %d, столбец 6", cel),
                                HttpStatus.PRECONDITION_FAILED.value());
                    }

                    salePayment.setCashInUnderFacility(cashInUnderFacility);

                    BigDecimal profitToCashingAuto;
                    BigDecimal profitToCashingMain;
                    if (row.getCell(31).getStringCellValue().length() > 0) {
                        profitToCashingAuto = new BigDecimal(row.getCell(31).getStringCellValue());
                    } else {
                        profitToCashingAuto = BigDecimal.ZERO;
                    }
                    if (row.getCell(32).getStringCellValue().length() > 0) {
                        profitToCashingMain = new BigDecimal(row.getCell(32).getStringCellValue());
                    } else {
                        profitToCashingMain = BigDecimal.ZERO;
                    }
                    salePayment.setProfitToCashingAuto(profitToCashingAuto);
                    salePayment.setProfitToCashingMain(profitToCashingMain);
                    String strProfitToReinvest = row.getCell(33).getStringCellValue();
                    BigDecimal profitToReinvest;
                    try {
                        profitToReinvest = new BigDecimal(strProfitToReinvest);
                    } catch (NumberFormatException ex) {
                        return new ApiResponse(String.format("Ошибка преобразования суммы \"Сколько прибыли реинвест\". Строка %d, столбец 34", cel),
                                HttpStatus.PRECONDITION_FAILED.value());
                    }

                    salePayment.setProfitToReInvest(profitToReinvest);

                    String underFacilityName = row.getCell(34).getStringCellValue();
                    if (underFacilityName == null || underFacilityName.isEmpty()) {
                        return new ApiResponse(String.format("Не указан или не верно указан подобъект \"%s\". Строка %d, столбец 35", underFacilityName, cel),
                                HttpStatus.PRECONDITION_FAILED.value());
                    }
                    UnderFacility underFacility = underFacilities.get(underFacilityName);
                    if (underFacility == null) {
                        underFacility = underFacilityService.findByName(underFacilityName);
                        if (underFacility == null) {
                            return new ApiResponse(String.format("Не указан или не верно указан подобъект \"%s\". Строка %d, столбец 35", underFacilityName, cel),
                                    HttpStatus.PRECONDITION_FAILED.value());
                        }
                    }
                    underFacilities.putIfAbsent(underFacilityName, underFacility);

                    salePayment.setUnderFacility(underFacility);

                    salePayment.setDateSale(Date.from(calSale.atStartOfDay(ZoneId.systemDefault()).toInstant()));

                    List<SalePayment> flowsSaleList = salePayments.stream()
                            .filter(flows -> globalFunctions.getMonthInt(flows.getDateSale()) ==
                                    globalFunctions.getMonthInt(salePayment.getDateSale()) &&
                                    globalFunctions.getYearInt(flows.getDateSale()) ==
                                            globalFunctions.getYearInt(salePayment.getDateSale()) &&
                                    globalFunctions.getDayInt(flows.getDateSale()) ==
                                            globalFunctions.getDayInt(salePayment.getDateSale()))

                            .filter(flows -> !Objects.equals(flows.getFacility(), null) &&
                                    flows.getFacility().getId().equals(salePayment.getFacility().getId()))

                            .filter(flows -> !Objects.equals(salePayment.getUnderFacility(), null) &&
                                    flows.getUnderFacility().getId().equals(salePayment.getUnderFacility().getId()))

                            .filter(flows -> flows.getInvestor().getId().equals(salePayment.getInvestor().getId()))

                            .collect(Collectors.toList());

                    if (flowsSaleList.size() == 0) {
                        salePaymentList.add(salePayment);
                    }
                }

            }
        }
        salePaymentList.forEach(salePaymentService::create);
        return new ApiResponse("Загрузка файла с данными о продаже завершена");
    }

}
