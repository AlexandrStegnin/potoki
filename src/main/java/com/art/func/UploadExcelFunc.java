package com.art.func;

import com.art.model.*;
import com.art.model.supporting.enums.ShareType;
import com.art.service.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class UploadExcelFunc {

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "rentPaymentService")
    private RentPaymentService rentPaymentService;

    @Resource(name = "globalFunctions")
    private GlobalFunctions globalFunctions;

    @Resource(name = "roomService")
    private RoomService roomService;

    @Resource(name = "investorsFlowsSaleService")
    private InvestorsFlowsSaleService investorsFlowsSaleService;

    @Autowired
    private FacilityService facilityService;

    @Autowired
    private UnderFacilityService underFacilityService;

    public String ExcelParser(MultipartFile file, String what, HttpServletRequest request) throws IOException, ParseException {
        String errString = "";
        InputStream fileInputStream = null;

        Workbook workbook;
        Sheet sheet = null;

        try {
            fileInputStream = new BufferedInputStream(file.getInputStream());
            //Get the workbook instance for XLSX file
            workbook = getWorkbook(fileInputStream, file.getOriginalFilename());
            sheet = workbook.getSheetAt(0);
        } catch (IOException ex) {
            errString = ex.getMessage();
            ex.printStackTrace();
        }

        switch (what) {
            case "invFlows":
                HttpSession session = request.getSession(true);
                session.setMaxInactiveInterval(30 * 60);
                rewriteInvestorsFlows(Objects.requireNonNull(sheet));
                break;
            case "invFlowsSale":
                errString = rewriteInvestorsFlowsSale(Objects.requireNonNull(sheet));
                break;
        }
        return errString;
    }

    private void rewriteInvestorsFlows(Sheet sheet) {

        List<RentPayment> rentPaymentTmp = rentPaymentService.findAll();
        List<Room> rooms = roomService.findAll();
        int cel = 0;
        List<RentPayment> rentPaymentList = new ArrayList<>(0);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
        List<AppUser> users = userService.findAll();

        for (Row row : sheet) {
            cel++;
            if (cel > 1) {
                if (row.getCell(0) != null && row.getCell(0).getCellTypeEnum() != CellType.BLANK) {
                    Calendar calendar = Calendar.getInstance();
                    try {
                        calendar.setTime(sdf.parse(row.getCell(0).getDateCellValue().toString()));
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }

                    java.time.LocalDate cal = calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    try {
                        for (int i = 0; i < row.getLastCellNum(); i++) {
                            row.getCell(i).setCellType(CellType.STRING);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println(cel);
                    }


                    String lastName;
                    lastName = row.getCell(4).getStringCellValue();
                    AppUser user = users.stream().filter(u -> u.getProfile().getLastName().equalsIgnoreCase(lastName))
                            .findFirst()
                            .orElse(null);

                    List<UnderFacility> underFacilityList = new ArrayList<>(0);

                    String underFacilityName = row.getCell(2).getStringCellValue();
                    if (null == underFacilityName || underFacilityName.isEmpty()) {
                        return;
                    }
                    UnderFacility underFacility = underFacilityService.findByName(underFacilityName);
                    if (null == underFacility) {
                        return;
                    }
//                    assert user != null;
//                    user.getFacilities().forEach(f -> underFacilitiesList.addAll(f.getUnderFacilities()));
                    String facilityName = row.getCell(1).getStringCellValue();
                    if (null == facilityName || facilityName.isEmpty()) {
                        return;
                    }
                    Facility facility = facilityService.findByFacility(facilityName);
                    if (null == facility) {
                        return;
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

                    String reFacilityName = row.getCell(19).getStringCellValue();
                    if (null == reFacilityName || reFacilityName.isEmpty()) {
                        return;
                    }
                    Facility reFacility = facilityService.findByFacility(reFacilityName);
                    if (null == reFacility) {
                        return;
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

                    if (flowsList.size() <= 0) {
                        rentPaymentList.add(rentPayment);
                    }
                }

            }
        }

        rentPaymentService.saveList(rentPaymentList);

    }

    private String rewriteInvestorsFlowsSale(Sheet sheet) {
        checkSheet(sheet);
        StringBuilder errors = new StringBuilder();
        List<SalePayment> salePayments = investorsFlowsSaleService.findAll();
        int cel = 0;
        List<SalePayment> salePaymentList = new ArrayList<>(0);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
        List<AppUser> users = userService.findAll();
        List<ShareType> shareKinds = Arrays.asList(ShareType.values());
        Map<String, Facility> facilities = new HashMap<>();
        Map<String, UnderFacility> underFacilities = new HashMap<>();
        for (Row row : sheet) {
            cel++;
            if (cel > 1) {
                if (row.getCell(0) != null && row.getCell(0).getCellTypeEnum() != CellType.BLANK) {
                    Calendar calendar = Calendar.getInstance();
                    try {
                        calendar.setTime(sdf.parse(row.getCell(4).getDateCellValue().toString()));
                    } catch (Exception ex) {
                        errors.append(String.format("Не удачная попытка конвертировать строку в дату. Строка %d, столбец 5",
                                cel));
                        return errors.toString();
                    }

                    java.time.LocalDate cal = calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    Calendar dateSale = Calendar.getInstance();
                    try {
                        dateSale.setTime(sdf.parse(row.getCell(35).getDateCellValue().toString()));
                    } catch (Exception ex) {
                        errors.append(String.format("Неудачная попытка конвертировать строку в дату. Строка %d, столбец 36",
                                cel));
                        return errors.toString();
                    }

                    java.time.LocalDate calSale = dateSale.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    try {
                        for (int i = 0; i < row.getLastCellNum(); i++) {
                            row.getCell(i).setCellType(CellType.STRING);
                        }
                    } catch (Exception ex) {
                        errors.append(ex.getLocalizedMessage());
                    }

                    String lastName;
                    lastName = row.getCell(1).getStringCellValue();
                    if (lastName == null || lastName.isEmpty()) {
                        errors.append(String.format("Не указан инвестор! Строка %d, столбец 2", cel));
                        return errors.toString();
                    }
                    AppUser user = users.stream().filter(u -> u.getProfile().getLastName().equalsIgnoreCase(lastName))
                            .findFirst()
                            .orElse(null);
                    if (user == null) {
                        errors.append(String.format("Неудачная попытка найти пользователя \"%s\". Строка %d, столбец 2", lastName, cel));
                        return errors.toString();
                    }

                    String facilityName = row.getCell(0).getStringCellValue();
                    if (null == facilityName || facilityName.isEmpty()) {
                        errors.append(String.format("Не указан объект! Строка %d, столбец 1", cel));
                        return errors.toString();
                    }
                    SalePayment salePayment = new SalePayment();
                    Facility facility = facilities.get(facilityName);
                    if (facility == null) {
                        facility = facilityService.findByFacility(facilityName);
                        if (facility == null) {
                            errors.append(String.format("Не указан или не верно указан объект \"%s\". Строка %d, столбец 1", facilityName, cel));
                            return errors.toString();
                        }
                    }
                    facilities.putIfAbsent(facilityName, facility);

                    salePayment.setFacility(facility);
                    salePayment.setInvestor(user);
                    ShareType shareKind = shareKinds
                            .stream()
                            .filter(share -> share.getTitle().equalsIgnoreCase(row.getCell(2).getStringCellValue()))
                            .findFirst()
                            .orElse(null);
                    if (shareKind == null) {
                        errors.append(String.format("Не указана или не верно указана доля \"%s\". Строка %d, столбец 3", row.getCell(0).getStringCellValue(), cel));
                        return errors.toString();
                    }
                    salePayment.setShareType(shareKind);
                    String strCashInFacility = row.getCell(3).getStringCellValue();
                    BigDecimal cashInFacility;
                    try {
                        cashInFacility = new BigDecimal(strCashInFacility);
                    } catch (NumberFormatException ex) {
                        errors.append(String.format("Ошибка преобразования суммы \"Вложено в объект\". Строка %d, столбец 4", cel));
                        return errors.toString();
                    }

                    salePayment.setCashInFacility(cashInFacility);
                    salePayment.setDateGived(Date.from(cal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    String strInvShare = row.getCell(5).getStringCellValue();
                    BigDecimal invShare;
                    try {
                        invShare = new BigDecimal(strInvShare);
                    } catch (NumberFormatException ex) {
                        errors.append(String.format("Ошибка преобразования суммы \"Доля инвестора\". Строка %d, столбец 6", cel));
                        return errors.toString();
                    }

                    salePayment.setInvestorShare(invShare);

                    String strCashInUnderFacility = row.getCell(6).getStringCellValue();
                    BigDecimal cashInUnderFacility;
                    try {
                        cashInUnderFacility = new BigDecimal(strCashInUnderFacility);
                    } catch (NumberFormatException ex) {
                        errors.append(String.format("Ошибка преобразования суммы \"Вложено в подобъект\". Строка %d, столбец 6", cel));
                        return errors.toString();
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
                        errors.append(String.format("Ошибка преобразования суммы \"Сколько прибыли реинвест\". Строка %d, столбец 34", cel));
                        return errors.toString();
                    }

                    salePayment.setProfitToReInvest(profitToReinvest);

                    String underFacilityName = row.getCell(34).getStringCellValue();
                    if (null == underFacilityName || underFacilityName.isEmpty()) {
                        errors.append(String.format("Не указан или не верно указан подобъект \"%s\". Строка %d, столбец 35", underFacilityName, cel));
                        return errors.toString();
                    }
                    UnderFacility underFacility = underFacilities.get(underFacilityName);
                    if (underFacility == null) {
                        underFacility = underFacilityService.findByName(underFacilityName);
                        if (underFacility == null) {
                            errors.append(String.format("Не указан или не верно указан подобъект \"%s\". Строка %d, столбец 35", underFacilityName, cel));
                            return errors.toString();
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

                    if (flowsSaleList.size() <= 0) {
                        salePaymentList.add(salePayment);
                    }
                }

            }
        }

        salePaymentList.forEach(flows -> investorsFlowsSaleService.create(flows));
        return errors.toString();
    }

    private void checkSheet(Sheet sheet) {
        int colCount = 0;
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell != null && cell.getCellTypeEnum() != CellType.BLANK) {
                    colCount++;
                }
            }
            break;
        }
        if (colCount < 36) throw new RuntimeException("Проверьте количество колонок в файле!");
    }

    private static <R> Predicate<R> not(Predicate<R> predicate) {
        return predicate.negate();
    }

    private static Workbook getWorkbook(InputStream inputStream, String excelPath) throws IOException {
        Workbook workbook;
        if (excelPath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else if (excelPath.endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("Файл не является excel файлом");
        }
        return workbook;

    }
}
