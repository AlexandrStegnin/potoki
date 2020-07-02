package com.art.func;

import com.art.model.*;
import com.art.service.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

    @Resource(name = "saleOfFacilitiesService")
    private SaleOfFacilitiesService saleOfFacilitiesService;

    @Resource(name = "rentorsDetailsService")
    private RentorsDetailsService rentorsDetailsService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "paysToInvestorsService")
    private PaysToInvestorsService paysToInvestorsService;

    @Resource(name = "mainFlowsService")
    private MainFlowsService mainFlowsService;

    @Resource(name = "underFacilitiesService")
    private UnderFacilitiesService underFacilitiesService;

    @Resource(name = "investorsFlowsService")
    private InvestorsFlowsService investorsFlowsService;

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @Resource(name = "globalFunctions")
    private GlobalFunctions globalFunctions;

    @Resource(name = "roomsService")
    private RoomsService roomsService;

    @Resource(name = "investorsFlowsSaleService")
    private InvestorsFlowsSaleService investorsFlowsSaleService;

    @Resource(name = "shareKindService")
    private ShareKindService shareKindService;

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
            case "manual":
                rewriteSummaryData(Objects.requireNonNull(sheet));
                break;
            case "flows":
                writeMainFlows(Objects.requireNonNull(sheet));
                break;
            case "invFlows":
                HttpSession session = request.getSession(true);
                session.setMaxInactiveInterval(30 * 60);
                rewriteInvestorsFlows(Objects.requireNonNull(sheet));
                break;
            case "invFlowsSale":
                errString = rewriteInvestorsFlowsSale(Objects.requireNonNull(sheet));
                break;
            case "saleOfFacilities":
                writeSaleOfFacilities(Objects.requireNonNull(sheet));
                break;
        }
        return errString;
    }

    private void writeSaleOfFacilities(Sheet sheet) {
        int numRow = 0;

        Facilities facility;
        String strFacility;
        String investor, investorEng;
        BigDecimal cashInFacility, shareInvestor, leaseInYear, profitFromLease, profitFromSale, profitFromSaleInYear,
                netProfitFromSale, netProfitFromSalePlusLease, totalYield, capitalGains, capital, balanceOfCapital;

        saleOfFacilitiesService.deleteAll();
        List<Facilities> facilities = facilityService.findAll();
        List<SaleOfFacilities> saleOfFacilities = new ArrayList<>(0);
        for (Row row : sheet) {
            numRow++;
            if (numRow > 1 && !Objects.equals(row.getCell(0), null) &&
                    !Objects.equals(row.getCell(0).toString(), "")) {

                for (int i = 0; i < row.getLastCellNum(); i++) {
                    row.getCell(i).setCellType(CellType.STRING);
                }

                strFacility = row.getCell(0).toString();
                String finalStrFacility = strFacility;
                facility = facilities
                        .stream()
                        .filter(f -> f.getFacility().equalsIgnoreCase(finalStrFacility)).findFirst().orElse(null);

                investor = row.getCell(1).toString();
                cashInFacility = new BigDecimal(row.getCell(2).toString());
                shareInvestor = new BigDecimal(row.getCell(3).toString());
                leaseInYear = new BigDecimal(row.getCell(4).toString());
                profitFromLease = new BigDecimal(row.getCell(5).toString());
                profitFromSale = new BigDecimal(row.getCell(6).toString());
                profitFromSaleInYear = new BigDecimal(row.getCell(7).toString());
                netProfitFromSale = new BigDecimal(row.getCell(8).toString());
                netProfitFromSalePlusLease = new BigDecimal(row.getCell(9).toString());
                totalYield = new BigDecimal(row.getCell(10).toString());
                capitalGains = new BigDecimal(row.getCell(11).toString());
                capital = new BigDecimal(row.getCell(12).toString());
                balanceOfCapital = new BigDecimal(row.getCell(13).toString());
                investorEng = row.getCell(14).toString();
                SaleOfFacilities sale = new SaleOfFacilities(facility, investor, cashInFacility, shareInvestor, leaseInYear,
                        profitFromLease, profitFromSale, profitFromSaleInYear, netProfitFromSale, netProfitFromSalePlusLease,
                        totalYield, capitalGains, capital, balanceOfCapital, investorEng);

                saleOfFacilities.add(sale);

            }
        }
        saleOfFacilitiesService.createList(saleOfFacilities);
    }

    private void rewriteSummaryData(Sheet sheet) {
//        List<PaysToInvestors> paysToInvestors =
//                paysToInvestorsService.findAll();
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
//        for (Row row : sheet) {
//
//            Calendar calendar = Calendar.getInstance();
//            try {
//                calendar.setTime(sdf.parse(row.getCell(1).toString()));
//            } catch (Exception ex) {
//                System.out.println(ex.getMessage());
//            }
//
//            for (int i = 0; i < row.getLastCellNum(); i++) {
//                row.getCell(i).setCellType(CellType.STRING);
//            }
//            BigInteger userId = new BigInteger("0");
//            String lastName;
//            lastName = row.getCell(2).toString();
//            Users user = userService
//                    .findByLastName(lastName);
//            if (!Objects.equals(user, null)) {
//                userId = user.getId();
//            }
//
//            BigInteger finalUserId = userId;
//            paysToInvestors.forEach(pti -> {
//                Calendar summaryCal = Calendar.getInstance();
//                summaryCal.setTime(pti.getEndDate());
//
//                if (pti.getFacility().equals(row.getCell(0).toString()) &&
//                        summaryCal.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
//                        pti.getInvestorId().equals(finalUserId)) {
//                    pti.setOstatokPoDole(Float.parseFloat(row.getCell(3).toString()));
//                }
//            });
//
//        }
//
//        paysToInvestorsService.saveList(paysToInvestors);

    }

    private void rewriteInvestorsFlows(Sheet sheet) {

        List<InvestorsFlows> investorsFlowsTmp = investorsFlowsService.findAll();
        List<Rooms> rooms = roomsService.findAll();
        int cel = 0;
        List<InvestorsFlows> investorsFlowsList = new ArrayList<>(0);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
        List<Users> users = userService.findAllWithFacilitiesAndUnderFacilities();

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
                    Users user = users.stream().filter(u -> u.getProfile().getLastName().equalsIgnoreCase(lastName))
                            .findFirst()
                            .orElse(null);

                    List<UnderFacilities> underFacilitiesList = new ArrayList<>(0);

                    assert user != null;
                    user.getFacilities().forEach(f -> underFacilitiesList.addAll(f.getUnderFacilities()));


                    InvestorsFlows investorsFlows = new InvestorsFlows();
                    investorsFlows.setReportDate(Date.from(cal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    investorsFlows.setFacility(user.getFacilities().stream()
                            .filter(f -> f.getFacility().equalsIgnoreCase(row.getCell(1).getStringCellValue()))
                            .findFirst().orElse(null));

                    investorsFlows.setUnderFacilities(underFacilitiesList
                            .stream()
                            .filter(uf -> uf.getUnderFacility().equalsIgnoreCase(row.getCell(2).getStringCellValue()))
                            .findFirst().orElse(null));

                    investorsFlows.setRoom(rooms.stream()
                            .filter(r -> r.getRoom().equalsIgnoreCase(row.getCell(3).getStringCellValue()))
                            .findFirst().orElse(null));

                    investorsFlows.setInvestor(user);
                    investorsFlows.setShareKind(row.getCell(5).getStringCellValue());
                    investorsFlows.setGivedCash(Float.parseFloat(row.getCell(6).getStringCellValue()));
                    investorsFlows.setSumInUnderFacility(Float.parseFloat(row.getCell(7).getStringCellValue()));
                    investorsFlows.setShareForSvod(Float.parseFloat(row.getCell(8).getStringCellValue()));

                    investorsFlows.setShare(Float.parseFloat(row.getCell(9).getStringCellValue()));
                    investorsFlows.setTaxation(Float.parseFloat(row.getCell(10).getStringCellValue()));
                    investorsFlows.setCashing(Float.parseFloat(row.getCell(11).getStringCellValue()));
                    investorsFlows.setSumma(Float.parseFloat(row.getCell(13).getStringCellValue()));
                    investorsFlows.setOnInvestors(Float.parseFloat(row.getCell(14).getStringCellValue()));
                    investorsFlows.setAfterTax(Float.parseFloat(row.getCell(15).getStringCellValue()));
                    investorsFlows.setAfterDeductionEmptyFacility(Float.parseFloat(row.getCell(16).getStringCellValue()));
                    investorsFlows.setAfterCashing(Float.parseFloat(row.getCell(17).getStringCellValue()));

                    investorsFlows.setReInvest(row.getCell(18).getStringCellValue());

                    investorsFlows.setReFacility(user.getFacilities().stream()
                            .filter(f -> f.getFacility().equals(row.getCell(19).getStringCellValue()))
                            .findFirst().orElse(null));

                    List<InvestorsFlows> flowsList = investorsFlowsTmp.stream()
                            .filter(flows -> globalFunctions.getMonthInt(flows.getReportDate()) ==
                                    globalFunctions.getMonthInt(investorsFlows.getReportDate()) &&
                                    globalFunctions.getYearInt(flows.getReportDate()) ==
                                            globalFunctions.getYearInt(investorsFlows.getReportDate()) &&
                                    globalFunctions.getDayInt(flows.getReportDate()) ==
                                            globalFunctions.getDayInt(investorsFlows.getReportDate()))

                            .filter(flows -> !Objects.equals(flows.getFacility(), null) &&
                                    flows.getFacility().getId().equals(investorsFlows.getFacility().getId()))

                            .filter(flows -> !Objects.equals(investorsFlows.getUnderFacilities(), null) &&
                                    flows.getUnderFacilities().getId().equals(investorsFlows.getUnderFacilities().getId()))

                            .filter(flows -> flows.getInvestor().getId().equals(investorsFlows.getInvestor().getId()))

                            .collect(Collectors.toList());

                    if (flowsList.size() <= 0) {
                        investorsFlowsList.add(investorsFlows);
                    }
                }

            }
        }

        investorsFlowsService.saveList(investorsFlowsList);

    }

    private String rewriteInvestorsFlowsSale(Sheet sheet) {
        StringBuilder errors = new StringBuilder();
        List<InvestorsFlowsSale> investorsFlowsSales = investorsFlowsSaleService.findAll();
        int cel = 0;
        List<InvestorsFlowsSale> investorsFlowsSaleList = new ArrayList<>(0);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
        List<Users> users = userService.findAllWithFacilitiesAndUnderFacilities();
        List<ShareKind> shareKinds = shareKindService.findAll();
        checkSheet(sheet);
        for (Row row : sheet) {
            cel++;
            if (cel > 1) {
                if (row.getCell(0) != null && row.getCell(0).getCellTypeEnum() != CellType.BLANK) {
                    Calendar calendar = Calendar.getInstance();
                    try {
                        calendar.setTime(sdf.parse(row.getCell(4).getDateCellValue().toString()));
                    } catch (Exception ex) {
//                        errors.append(ex.getMessage());
                        errors.append(String.format("Не удачная попытка конвертировать строку в дату. Строка %d, столбец 5",
                                cel));
                        return errors.toString();
//                        throw new RuntimeException(String.format(
//                                "Не удачная попытка конвертировать строку в дату. Строка %d, столбец 5", cel));
                    }

                    java.time.LocalDate cal = calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    Calendar dateSale = Calendar.getInstance();
                    try {
                        dateSale.setTime(sdf.parse(row.getCell(35).getDateCellValue().toString()));
                    } catch (Exception ex) {
                        errors.append(String.format("Неудачная попытка конвертировать строку в дату. Строка %d, столбец 36",
                                cel));
                        return errors.toString();
//                        errors.append(ex.getMessage());
//                        throw new RuntimeException(String.format(
//                                "Неудачная попытка конвертировать строку в дату. Строка %d, столбец 36", cel));
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
                    if (lastName == null) {
                        errors.append(String.format("Не указан инвестор! Строка %d, столбец 2", cel));
                        return errors.toString();
//                        throw new RuntimeException(String.format(
//                                "Не указан инвестор! Строка %d, столбец 2", cel));
                    }
                    Users user = users.stream().filter(u -> u.getProfile().getLastName().equalsIgnoreCase(lastName))
                            .findFirst()
                            .orElse(null);
                    if (user == null) {
                        errors.append(String.format("Неудачная попытка найти пользователя \"%s\". Строка %d, столбец 2", lastName, cel));
                        return errors.toString();
//                        throw new RuntimeException(String.format(
//                                "Неудачная попытка найти пользователя \"%s\". Строка %d, столбец 2", lastName, cel
//                        ));
                    }
                    List<UnderFacilities> underFacilitiesList = new ArrayList<>(0);

                    user.getFacilities().forEach(f -> underFacilitiesList.addAll(f.getUnderFacilities()));

                    InvestorsFlowsSale investorsFlowsSale = new InvestorsFlowsSale();
                    Facilities facility = user.getFacilities()
                            .stream()
                            .filter(f -> f.getFacility().equalsIgnoreCase(row.getCell(0).getStringCellValue()))
                            .findFirst()
                            .orElse(null);
                    if (facility == null) {
                        errors.append(String.format("Не указан или не верно указан объект \"%s\". Строка %d, столбец 1", row.getCell(0).getStringCellValue(), cel));
                        return errors.toString();
//                        throw new RuntimeException(String.format(
//                                "Не указан или не верно указан объект \"%s\". Строка %d, столбец 1", row.getCell(0).getStringCellValue(), cel
//                        ));
                    }
                    investorsFlowsSale.setFacility(facility);
                    investorsFlowsSale.setInvestor(user);
                    ShareKind shareKind = shareKinds
                            .stream()
                            .filter(sk -> sk.getShareKind().equalsIgnoreCase(row.getCell(2).getStringCellValue()))
                            .findFirst()
                            .orElse(null);
                    if (shareKind == null) {
                        errors.append(String.format("Не указана или не верно указана доля \"%s\". Строка %d, столбец 3", row.getCell(0).getStringCellValue(), cel));
                        return errors.toString();
//                        throw new RuntimeException(String.format(
//                                "Не указана или не верно указана доля \"%s\". Строка %d, столбец 3",
//                                row.getCell(2).getStringCellValue(), cel
//                        ));
                    }
                    investorsFlowsSale.setShareKind(shareKind);
                    String strCashInFacility = row.getCell(3).getStringCellValue();
                    BigDecimal cashInFacility;
                    try {
                        cashInFacility = new BigDecimal(strCashInFacility);
                    } catch (NumberFormatException ex) {
                        errors.append(String.format("Ошибка преобразования суммы \"Вложено в объект\". Строка %d, столбец 4", cel));
                        return errors.toString();
//                        throw new NumberFormatException(
//                                String.format("Ошибка преобразования суммы \"Вложено в объект\". " +
//                                        "Строка %d, столбец 4", cel));
                    }

                    investorsFlowsSale.setCashInFacility(cashInFacility);
                    investorsFlowsSale.setDateGived(Date.from(cal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    String strInvShare = row.getCell(5).getStringCellValue();
                    BigDecimal invShare;
                    try {
                        invShare = new BigDecimal(strInvShare);
                    } catch (NumberFormatException ex) {
                        errors.append(String.format("Ошибка преобразования суммы \"Доля инвестора\". Строка %d, столбец 6", cel));
                        return errors.toString();
//                        throw new NumberFormatException(
//                                String.format("Ошибка преобразования суммы \"Доля инвестора\". " +
//                                        "Строка %d, столбец 6", cel));
                    }

                    investorsFlowsSale.setInvestorShare(invShare);

                    String strCashInUnderFacility = row.getCell(6).getStringCellValue();
                    BigDecimal cashInUnderFacility;
                    try {
                        cashInUnderFacility = new BigDecimal(strCashInUnderFacility);
                    } catch (NumberFormatException ex) {
                        errors.append(String.format("Ошибка преобразования суммы \"Вложено в подобъект\". Строка %d, столбец 6", cel));
                        return errors.toString();
//                        throw new NumberFormatException(
//                                String.format("Ошибка преобразования суммы \"Вложено в подобъект\". " +
//                                        "Строка %d, столбец 6", cel));
                    }

                    investorsFlowsSale.setCashInUnderFacility(cashInUnderFacility);

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
                    investorsFlowsSale.setProfitToCashingAuto(profitToCashingAuto);
                    investorsFlowsSale.setProfitToCashingMain(profitToCashingMain);
                    String strProfitToReinvest = row.getCell(33).getStringCellValue();
                    BigDecimal profitToReinvest;
                    try {
                        profitToReinvest = new BigDecimal(strProfitToReinvest);
                    } catch (NumberFormatException ex) {
                        errors.append(String.format("Ошибка преобразования суммы \"Сколько прибыли реинвест\". Строка %d, столбец 34", cel));
                        return errors.toString();
//                        throw new NumberFormatException(
//                                String.format("Ошибка преобразования суммы \"Сколько прибыли реинвест\". " +
//                                        "Строка %d, столбец 34", cel));
                    }

                    investorsFlowsSale.setProfitToReInvest(profitToReinvest);

                    UnderFacilities underFacility = underFacilitiesList
                            .stream()
                            .filter(uf -> uf.getUnderFacility().equalsIgnoreCase(row.getCell(34).getStringCellValue()))
                            .findFirst().orElse(null);
                    if (underFacility == null) {
                        errors.append(String.format("Не указан или не верно указан подобъект \"%s\". Строка %d, столбец 35", row.getCell(34).getStringCellValue(), cel));
                        return errors.toString();
//                        throw new RuntimeException(String.format(
//                                "Не указан или не верно указан подобъект \"%s\". Строка %d, столбец 35",
//                                row.getCell(34).getStringCellValue(), cel
//                        ));
                    }
                    investorsFlowsSale.setUnderFacility(underFacility);

                    investorsFlowsSale.setDateSale(Date.from(calSale.atStartOfDay(ZoneId.systemDefault()).toInstant()));

                    List<InvestorsFlowsSale> flowsSaleList = investorsFlowsSales.stream()
                            .filter(flows -> globalFunctions.getMonthInt(flows.getDateSale()) ==
                                    globalFunctions.getMonthInt(investorsFlowsSale.getDateSale()) &&
                                    globalFunctions.getYearInt(flows.getDateSale()) ==
                                            globalFunctions.getYearInt(investorsFlowsSale.getDateSale()) &&
                                    globalFunctions.getDayInt(flows.getDateSale()) ==
                                            globalFunctions.getDayInt(investorsFlowsSale.getDateSale()))

                            .filter(flows -> !Objects.equals(flows.getFacility(), null) &&
                                    flows.getFacility().getId().equals(investorsFlowsSale.getFacility().getId()))

                            .filter(flows -> !Objects.equals(investorsFlowsSale.getUnderFacility(), null) &&
                                    flows.getUnderFacility().getId().equals(investorsFlowsSale.getUnderFacility().getId()))

                            .filter(flows -> flows.getInvestor().getId().equals(investorsFlowsSale.getInvestor().getId()))

                            .collect(Collectors.toList());

                    if (flowsSaleList.size() <= 0) {
                        investorsFlowsSaleList.add(investorsFlowsSale);
                    }
                }

            }
        }

        investorsFlowsSaleList.forEach(flows -> investorsFlowsSaleService.create(flows));
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

    private void writeMainFlows(Sheet sheet) {
        int numRow = 0;
        mainFlowsService.deleteAllFlows();
        List<UnderFacilities> underFacilitiesList = underFacilitiesService.findAll();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        List<MainFlows> mainFlowsList = new ArrayList<>(0);
        for (Row row : sheet) {
            numRow++;
            if (numRow > 1 && !Objects.equals(row.getCell(0), null) &&
                    !Objects.equals(row.getCell(0).toString(), "")) {
                MainFlows flows = new MainFlows();
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(sdf.parse(row.getCell(2).toString()));
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    if (!Objects.equals(null, row.getCell(i))) {
                        row.getCell(i).setCellType(CellType.STRING);
                    }
                }

                UnderFacilities underFacilities = underFacilitiesList
                        .stream()
                        .filter(uf -> uf.getUnderFacility().equalsIgnoreCase(row.getCell(13).toString()))
                        .findFirst()
                        .orElse(null);

                flows.setPlanFact(row.getCell(0) == null ? "" : row.getCell(0).toString());
                flows.setFileName(row.getCell(1) == null ? "" : row.getCell(1).toString());
                flows.setSettlementDate(calendar.getTime());
                flows.setSumma(Float.parseFloat(row.getCell(7) == null ? "0" : row.getCell(7).toString()));
                flows.setOrgName(row.getCell(8) == null ? "" : row.getCell(8).toString());
                flows.setInn(row.getCell(9) == null ? "" : row.getCell(9).toString());
                flows.setAccount(row.getCell(10) == null ? "" : row.getCell(10).toString());
                flows.setPurposePayment(row.getCell(11) == null ? "" : row.getCell(11).toString());
                flows.setPayment(row.getCell(12) == null ? "" : row.getCell(12).toString());
                flows.setUnderFacilities(underFacilities);
                flows.setLevelTwo(row.getCell(14) == null ? "" : row.getCell(14).toString());
                flows.setLevelThree(row.getCell(15) == null ? "" : row.getCell(15).toString());

                mainFlowsList.add(flows);

            }
        }
        mainFlowsService.createList(mainFlowsList);
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
