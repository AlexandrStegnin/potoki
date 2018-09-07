package com.art.func;

import com.art.model.*;
import com.art.model.supporting.PaysToInvestors;
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
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UploadExcelFunc {

    private final Pattern pattern = Pattern.compile("\\s*([;+])\\s*");
    private final Locale RU = new Locale("ru", "RU");

    @Resource(name = "saleOfFacilitiesService")
    private SaleOfFacilitiesService saleOfFacilitiesService;

    @Resource(name = "alphaExtractService")
    private AlphaExtractService alphaExtractService;

    @Resource(name = "toshlExtractService")
    private ToshlExtractService toshlExtractService;

    @Resource(name = "alphaCorrectTagsService")
    private AlphaCorrectTagsService alphaCorrectTagsService;

    @Resource(name = "toshlCorrectTagsService")
    private ToshlCorrectTagsService toshlCorrectTagsService;

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
        int rowBegin = 0;
        boolean begin = false;
        int rowNumber;

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
            case "alpha":
                List<AlphaCorrectTags> alphaCorrectTagsList = new ArrayList<>(0);
                alphaCorrectTagsList.addAll(
                        alphaCorrectTagsService.findTags());
                List<RentorsDetails> rentorsDetails = rentorsDetailsService.findAll();

                Set<String> newTagsList = new HashSet<>(0);
                List<AlphaExtract> alphaExtractList = new ArrayList<>(0);
                float debet = 0;
                float credit = 0;
                int cntCol = 0;
                String inn = "";
                String kpp = "";
                String account = "";
                String bik = "";
                String err = "";
                String orgName = "";
                String bankName = "";
                String purposePayment = "";
                String codeDebet = "";
                String docType = "";

                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                Pattern pattern = Pattern.compile(".*?аренд.*?",
                        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
                Pattern monthPtrn = Pattern.compile(
                        "(за)\\s+(январ[ь|я]|феврал[ь|я]|март|" +
                                "апрел[ь|я]|ма[й|я]|июн[ь|я]|" +
                                "июл[ь|я]|август|сентябр[ь|я]|" +
                                "октябр[ь|я]|ноябр[ь|я]|декабр[ь|я])\\s*(\\d{4})?.*?");
                Pattern monthDigPtrn = Pattern.compile("(за)\\s*([0-9]{1,2})?[./,]?([0-9]{1,2})[./,]\\d{4}.*?",
                        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
                SimpleDateFormat monthFormat = new SimpleDateFormat("dd MMMM yyyy", RU);
                SimpleDateFormat monthDigFormat = new SimpleDateFormat("dd.MM.yyyy", RU);
                SimpleDateFormat monthFormatTrim = new SimpleDateFormat("dd MMMMyyyy", RU);
                SimpleDateFormat monthFormatSlash = new SimpleDateFormat("dd/MM/yyyy", RU);
                Calendar calendar = new GregorianCalendar();

                //Iterate through each rows from first sheet
                /*
                Iterator<Row> rowIterator = sheet.iterator();

                while(rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                */

                assert sheet != null;
                while (!begin) {
                    for (Row row : sheet)
                        try {
                            format.parse(row.getCell(0).toString());
                            begin = true;
                            rowBegin = row.getRowNum();
                            break;
                        } catch (Exception ignored) {
                        }
                }
                for (Row row : sheet) {

                    rowNumber = row.getRowNum();
                    if (rowNumber >= rowBegin) {
                        if (row.getLastCellNum() > 8) {
                            try {
                                format.parse(row.getCell(0).toString()).getTime();
                            } catch (Exception ex) {
                                break;
                            }
                            cntCol = row.getLastCellNum();
                            for (int i = 0; i < row.getLastCellNum(); i++) {
                                row.getCell(i).setCellType(CellType.STRING);
                            }
                            debet = 0;
                            credit = 0;
                            Date dateOper = format.parse(row.getCell(0).toString());

                            java.sql.Date date = new java.sql.Date(dateOper.getTime());

                            AlphaExtract alphaExtract = new AlphaExtract();
                            alphaExtract.setDateOper(date);
                            alphaExtract.setDocNumber(row.getCell(1).toString());
                            try {
                                debet = Float.parseFloat(row.getCell(2).toString().replaceAll("[^0-9,.]", "")
                                        .replaceAll(",", "\\."));
                            } catch (Exception ignored) {
                            }

                            try {
                                credit = Float.parseFloat(row.getCell(3).toString().replaceAll("[^0-9,.]", "")
                                        .replaceAll(",", "\\."));
                            } catch (Exception ignored) {
                            }

                            if (cntCol == 11) {
                                try {
                                    inn = row.getCell(4).toString().split("\\n")[2].replaceAll("[^0-9]", "");
                                } catch (Exception ignored) {
                                }
                                try {
                                    kpp = row.getCell(4).toString().split("\\n")[3].replaceAll("[^0-9]", "");
                                } catch (Exception ignored) {
                                }
                                try {
                                    account = row.getCell(4).toString().split("\\n")[0].replaceAll("[\\s]+", "");
                                } catch (Exception ignored) {
                                }
                                try {
                                    bik = row.getCell(6).toString().split("\\n")[0].replaceAll("\\w", "");
                                } catch (Exception ignored) {
                                }
                                try {
                                    orgName = row.getCell(4).toString().split("\\n")[1];
                                } catch (Exception ignored) {
                                }
                                try {
                                    bankName = row.getCell(6).toString().split("\\n")[1];
                                } catch (Exception ignored) {
                                }
                                try {
                                    purposePayment = row.getCell(8).toString();
                                } catch (Exception ignored) {
                                }
                                try {
                                    codeDebet = row.getCell(9).toString();
                                } catch (Exception ignored) {
                                }
                                try {
                                    docType = row.getCell(10).toString();
                                } catch (Exception ignored) {
                                }
                            } else if (cntCol == 13) {
                                try {
                                    inn = row.getCell(5).toString();
                                } catch (Exception ignored) {
                                }
                                try {
                                    kpp = row.getCell(6).toString();
                                } catch (Exception ignored) {
                                }
                                try {
                                    account = row.getCell(7).toString().replaceAll("[\\s]+", "");
                                } catch (Exception ignored) {
                                }

                                try {
                                    bik = row.getCell(8).toString();
                                } catch (Exception ignored) {
                                }
                                try {
                                    orgName = row.getCell(4).toString();
                                } catch (Exception ignored) {
                                }
                                try {
                                    bankName = row.getCell(9).toString();
                                } catch (Exception ignored) {
                                }
                                try {
                                    purposePayment = row.getCell(10).toString();
                                } catch (Exception ignored) {
                                }
                                try {
                                    codeDebet = row.getCell(11).toString();
                                } catch (Exception ignored) {
                                }
                                try {
                                    docType = row.getCell(12).toString();
                                } catch (Exception ignored) {
                                }

                            }

                            java.sql.Date formatterDate = null;
                            StringBuilder dateStr = new StringBuilder();
                            calendar.setTime(dateOper);
                            Matcher matcher = pattern.matcher(purposePayment.toLowerCase());
                            if (matcher.find()) {
                                Matcher monthMatcher = monthPtrn.matcher(purposePayment
                                        .toLowerCase());
                                Matcher monthDigMatcher = monthDigPtrn.matcher(purposePayment
                                        .toLowerCase());
                                if (monthMatcher.find()) {
                                    if (monthMatcher.group(0).toLowerCase().matches(".*?[А-Яа-я]\\d{4}")) {
                                        dateStr.append("01 ").append(monthMatcher.group(0).toLowerCase()
                                                .replaceAll("за", "")
                                                .replaceAll("\\s+", " ").trim());
                                    } else {
                                        dateStr.append("01 ").append(monthMatcher.group(0).toLowerCase()
                                                .replaceAll("за", "")
                                                .replaceAll("\\s+", " ").trim()).append(" ")
                                                .append(calendar.get(Calendar.YEAR));
                                    }

                                    if (dateStr.toString().split("\\s").length > 2) {
                                        try {
                                            formatterDate = new java.sql.Date(monthFormat.parse(dateStr.toString())
                                                    .getTime());
                                        } catch (Exception ignored) {

                                        }
                                    } else {
                                        try {
                                            formatterDate = new java.sql.Date(monthFormatTrim.parse(dateStr.toString())
                                                    .getTime());
                                        } catch (Exception ignored) {

                                        }
                                    }
                                } else if (monthDigMatcher.find()) {
                                    if (matcher.group(0).split("/").length >= 2) {
                                        dateStr.append(matcher.group(0).toLowerCase()
                                                .replaceAll("за", "")
                                                .replaceAll("\\s+", " ").trim());
                                        try {
                                            formatterDate = new java.sql.Date(monthFormatSlash.parse(dateStr.toString())
                                                    .getTime());
                                        } catch (Exception ignored) {
                                        }
                                    } else if (matcher.group(0).split("/").length < 2) {
                                        dateStr.append("01.")
                                                .append(matcher.group(0).toLowerCase()
                                                        .replaceAll("за", "")
                                                        .replaceAll("\\s+", " ").trim());
                                        try {
                                            formatterDate = new java.sql.Date(monthFormatSlash.parse(dateStr.toString())
                                                    .getTime());
                                        } catch (Exception ignored) {
                                        }
                                    } else if (matcher.group(0).split("\\.").length == 3) {
                                        dateStr.append(matcher.group(0).toLowerCase()
                                                .replaceAll("за", "")
                                                .replaceAll("\\s+", " ").trim());
                                        try {
                                            formatterDate = new java.sql.Date(monthDigFormat.parse(dateStr.toString())
                                                    .getTime());
                                        } catch (Exception ignored) {
                                        }
                                    } else if (matcher.group(0).split("\\.").length == 2) {
                                        dateStr.append("01.")
                                                .append(matcher.group(0).toLowerCase()
                                                        .replaceAll("за", "")
                                                        .replaceAll("\\s+", " ").trim());
                                        try {
                                            formatterDate = new java.sql.Date(monthDigFormat.parse(dateStr.toString())
                                                    .getTime());
                                        } catch (Exception ignored) {
                                        }
                                    } else if (matcher.group(0).split("\\.").length == 1) {
                                        dateStr.append("01.")
                                                .append(matcher.group(0).toLowerCase()
                                                        .replaceAll("за", "")
                                                        .replaceAll("\\s+", " ").trim());
                                        try {
                                            formatterDate = new java.sql.Date(monthDigFormat.parse(dateStr.toString())
                                                    .getTime());
                                        } catch (Exception ignored) {
                                        }
                                    } else if (matcher.group(0).split(",").length == 3) {
                                        dateStr.append(matcher.group(0).toLowerCase()
                                                .replaceAll("за", "")
                                                .replaceAll("\\s+", " ")
                                                .replaceAll(",", ".")
                                                .trim());
                                        try {
                                            formatterDate = new java.sql.Date(monthDigFormat.parse(dateStr.toString())
                                                    .getTime());
                                        } catch (Exception ignored) {
                                        }
                                    } else if (matcher.group(0).split(",").length == 2) {
                                        dateStr.append("01.")
                                                .append(matcher.group(0).toLowerCase()
                                                        .replaceAll("за", "")
                                                        .replaceAll("\\s+", " ")
                                                        .replaceAll(",", ".")
                                                        .trim());
                                        try {
                                            formatterDate = new java.sql.Date(monthDigFormat.parse(dateStr.toString())
                                                    .getTime());
                                        } catch (Exception ignored) {
                                        }
                                    } else if (matcher.group(0).split(",").length == 1) {
                                        dateStr.append("01.")
                                                .append(matcher.group(0).toLowerCase()
                                                        .replaceAll("за", "")
                                                        .replaceAll("\\s+", " ")
                                                        .replaceAll(",", ".")
                                                        .trim());
                                        try {
                                            formatterDate = new java.sql.Date(monthDigFormat.parse(dateStr.toString())
                                                    .getTime());
                                        } catch (Exception ignored) {
                                        }
                                    }
                                }
                            }

                            alphaExtract.setPeriod(formatterDate);
                            alphaExtract.setDebet(debet);
                            alphaExtract.setCredit(credit);
                            alphaExtract.setOrgName(orgName);
                            alphaExtract.setInn(inn);
                            alphaExtract.setKpp(kpp);
                            alphaExtract.setAccount(account);
                            alphaExtract.setBik(bik);
                            alphaExtract.setBankName(bankName);
                            alphaExtract.setPurposePayment(purposePayment);
                            alphaExtract.setCodeDebet(codeDebet);
                            alphaExtract.setDocType(docType);

                            String[] tags = {"Аренда;56000", "Свет и ЖКХ;16000", "Вода;500"};

                            if (alphaExtract.getInn().equals("6623091110") &&
                                    alphaExtract.getAccount().equals("40702810216540000801") &&
                                    alphaExtract.getCredit() == Float.parseFloat("72500")) {

                                for (String tag : tags) {
                                    AlphaExtract alphaVainera = new AlphaExtract();
                                    alphaVainera.setDateOper(alphaExtract.getDateOper());
                                    alphaVainera.setDocNumber(alphaExtract.getDocNumber());
                                    alphaVainera.setDebet(alphaExtract.getDebet());
                                    alphaVainera.setOrgName(alphaExtract.getOrgName());
                                    alphaVainera.setInn(alphaExtract.getInn());
                                    alphaVainera.setKpp(alphaExtract.getKpp());
                                    alphaVainera.setAccount(alphaExtract.getAccount());
                                    alphaVainera.setBik(alphaExtract.getBik());
                                    alphaVainera.setBankName(alphaExtract.getBankName());
                                    alphaVainera.setPurposePayment(alphaExtract.getPurposePayment());
                                    alphaVainera.setCodeDebet(alphaExtract.getCodeDebet());
                                    alphaVainera.setDocType(alphaExtract.getDocType());
                                    alphaVainera.setPeriod(alphaExtract.getPeriod());

                                    alphaVainera.setCredit(Float.parseFloat(tag.split(";")[1]));
                                    alphaVainera.setTags(alphaCorrectTagsService.
                                            findByInnAndAccountAndCorrectTag(alphaExtract.getInn(),
                                                    alphaExtract.getAccount(), tag.split(";")[0]));
                                    err = addAlphaOrgName(alphaVainera, rentorsDetails);
                                    alphaExtractList.add(alphaVainera);

                                }

                            } else {
                                err = addAlphaTagsV2(alphaExtract, alphaCorrectTagsList, newTagsList);
                                err = addAlphaOrgName(alphaExtract, rentorsDetails);
                                alphaExtractList.add(alphaExtract);
                            }
                        }
                    }
                }
                fileInputStream.close();

                List<AlphaExtract> oldAlphaExtractList = alphaExtractService.findAll();
                List<AlphaExtract> newAlphaExtractList = alphaExtractList.stream()
                        .filter(ae -> (oldAlphaExtractList.stream()
                                .filter(old -> old.equals(ae)).count() < 1))
                        .collect(Collectors.toList());

                alphaExtractService.createList(newAlphaExtractList);

                List<AlphaCorrectTags> setTags = new ArrayList<>(0);
                List<String> tagList = newTagsList.stream().distinct().
                        collect(Collectors.toList());

                alphaCorrectTagsList = new ArrayList<>(0);
                alphaCorrectTagsList.addAll(alphaCorrectTagsService.findAll());

                for (String tags : newTagsList) {
                    for (AlphaCorrectTags aTag : alphaCorrectTagsList) {
                        if (tags.equals(aTag.getDescription())) {
                            tagList.remove(tags);
                        }
                    }
                }

                for (String tags : tagList) {
                    AlphaCorrectTags tag = new AlphaCorrectTags();
                    tag.setDescription(tags);
                    setTags.add(tag);
                }

                alphaCorrectTagsService.createList(setTags);
                break;
            case "toshl":
                List<ToshlCorrectTags> toshlCorrectTagsList = toshlCorrectTagsService.findOrderBy();
                List<ToshlExtract> toshlExtractList = new ArrayList<>(0);
                Set<String> newToshlTagsList = new HashSet<>(0);


                float amount = 0;
                float inMainCurrency = 0;

                //Iterate through each rows from first sheet
                SimpleDateFormat formatToshl = new SimpleDateFormat("MM/dd/yy");
                assert sheet != null;

                while (!begin) {
                    for (Row row : sheet) {
                        try {
                            formatToshl.parse(row.getCell(0).toString());
                            begin = true;
                            rowBegin = row.getRowNum();
                            break;
                        } catch (Exception ignored) {
                        }

                    }
                }

                for (Row row : sheet) {
                    rowNumber = row.getRowNum();
                    if (rowNumber >= rowBegin) {
                        if (row.getLastCellNum() > 8) {
                            for (int i = 0; i < row.getLastCellNum(); i++) {
                                row.getCell(i).setCellType(CellType.STRING);
                            }

                            ToshlExtract toshlExtract = new ToshlExtract();
                            toshlExtract.setDate(formatToshl.parse(row.getCell(0).toString()));
                            toshlExtract.setAccount(row.getCell(1).toString());
                            toshlExtract.setCategory(row.getCell(2).toString());
                            toshlExtract.setTags(row.getCell(3).toString());

                            try {
                                amount = Float.parseFloat(row.getCell(4).toString());
                            } catch (Exception ignored) {
                            }

                            toshlExtract.setCurrency(row.getCell(5).toString());

                            try {
                                inMainCurrency = Float.parseFloat(row.getCell(6).toString());
                            } catch (Exception ignored) {
                            }
                            toshlExtract.setMainCurrency(row.getCell(7).toString());
                            toshlExtract.setDescription(row.getCell(8).toString());

                            toshlExtract.setAmount(amount);
                            toshlExtract.setInMainCurrency(inMainCurrency);

                            err = addToshlTags(toshlExtract, toshlCorrectTagsList, newToshlTagsList);
                            toshlExtractList.add(toshlExtract);
                        }
                    }
                }
                fileInputStream.close();
                toshlExtractService.createList(toshlExtractList);

                List<ToshlCorrectTags> setToshlTags = new ArrayList<>(0);

                List<String> toshlTagList = newToshlTagsList.stream().distinct().
                        collect(Collectors.toList());

                toshlCorrectTagsList = toshlCorrectTagsService.findAll();

                for (String tags : newToshlTagsList) {
                    for (ToshlCorrectTags tTag : toshlCorrectTagsList) {
                        if (tags.equals(String.join(tTag.getTags(), tTag.getCategory()))) {
                            toshlTagList.remove(tags);
                        }
                    }
                }
                for (String tags : toshlTagList) {
                    ToshlCorrectTags tag = new ToshlCorrectTags();
                    String[] tagStr = tags.split("[|]");
                    tag.setTags(tagStr[0]);
                    tag.setCategory(tagStr[1]);
                    setToshlTags.add(tag);
                }

                toshlCorrectTagsService.createList(setToshlTags);
                break;
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
                rewriteInvestorsFlowsSale(Objects.requireNonNull(sheet));
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

    private String addAlphaTagsV2(AlphaExtract alphaExtract,
                                  List<AlphaCorrectTags> alphaCorrectTagsList,
                                  Set<String> newTagsList) {
        String ok = "";

        /* Проходим по всем тэгам из справочника */
        for (AlphaCorrectTags correctTags : alphaCorrectTagsList) {

            Pattern pInn;
            Pattern pAccount;
            Matcher mInn;
            Matcher mAccount;

            int priority = checkPriority(alphaExtract.getPurposePayment().toLowerCase());

            if (correctTags.getDocNumber() != null && correctTags.getDateOper() != null) {
                if (correctTags.getDocNumber().equals(alphaExtract.getDocNumber()) &&
                        correctTags.getDateOper().getTime() == alphaExtract.getDateOper().getTime() &&
                        ((correctTags.getDebetOrCredit().getVal().equals("Дебет") &&
                                alphaExtract.getDebet() > 0) ||
                                (correctTags.getDebetOrCredit().getVal().equals("Кредит") &&
                                        alphaExtract.getCredit() > 0))) {
                    if (supportingAlphaTags(alphaExtract.getPurposePayment().toLowerCase(),
                            correctTags.getDescription().toLowerCase(), "no")) {
                        alphaExtract.setTags(correctTags);
                    }
                }
            } else

                /* Если корректный тэг содержит ИНН или счёт, то обязательно должно быть совпадение */
                if ((correctTags.getInn() != null && correctTags.getInn().length() > 0) ||
                        (correctTags.getAccount() != null) && correctTags.getAccount().length() > 0) {

                    pInn = Pattern.compile(correctTags.getInn());
                    mInn = pInn.matcher(alphaExtract.getInn());
                    pAccount = Pattern.compile(correctTags.getAccount());
                    mAccount = pAccount.matcher(alphaExtract.getAccount());

                    if (mInn.find() && mAccount.find()) {
                        if (supportingAlphaTags(alphaExtract.getPurposePayment().toLowerCase(),
                                correctTags.getDescription().toLowerCase(),
                                "yes")) {
                            alphaExtract.setTags(correctTags);
                        }
                    }
                } else {
                    if (supportingAlphaTags(alphaExtract.getPurposePayment().toLowerCase(),
                            correctTags.getDescription().toLowerCase(), "no")) {
                        alphaExtract.setTags(correctTags);
                    }
                }

            if ((priority == 3) && alphaExtract.getTags() != null) {
                return ok;
            }

        }

        if (alphaExtract.getTags() == null) {
            newTagsList.add(alphaExtract.getPurposePayment());
        }
        return ok;
    }

    private int checkPriority(String description) {

        Pattern kom = Pattern.compile(".*?(жкх.*?|свет.*?|электроэнерг.*?).*?");
        Matcher komMatcher = kom.matcher(description);

        Pattern obesp = Pattern.compile(".*?обеспеч.*?");
        Matcher obespMatcher = obesp.matcher(description);

        Pattern arend = Pattern.compile(".*?аренд.*?");
        Matcher arendMatcher = arend.matcher(description);

        if (komMatcher.find()) {
            return 1;
        } else if (obespMatcher.find()) {
            return 2;
        } else if (arendMatcher.find()) {
            return 3;
        }
        return 0;
    }

    private String addAlphaOrgName(AlphaExtract alphaExtract,
                                   List<RentorsDetails> rentorsDetails) {
        String pattern = ".*?";
        Pattern orgPattern;
        List<RentorsDetails> filterList = rentorsDetails.stream()
                .filter(rd -> rd.getAccount().equals(alphaExtract.getAccount()) &&
                        rd.getInn().equals(alphaExtract.getInn()))
                .collect(Collectors.toList());
        if (filterList.size() == 1) {
            alphaExtract.setCorrectOrgName(filterList.get(0).getOrganization());
        } else {
            for (RentorsDetails details : filterList) {
                orgPattern = Pattern.compile(pattern + details.getOrganization()
                        .replaceAll("\"", " ")
                        .replaceAll("\\s+", " ")
                        .toLowerCase()
                        + pattern);
                Matcher orgMatcher = orgPattern.matcher(alphaExtract.getOrgName()
                        .replaceAll("/", " ")
                        .replaceAll("\"", " ")
                        .replaceAll("\\s+", " ")
                        .toLowerCase());

                if (orgMatcher.find()) {
                    alphaExtract.setCorrectOrgName(details.getOrganization());
                    break;
                }

            }
        }

        return "";
    }

    private boolean supportingAlphaTags(String description, String corrDescr, String have) {
        String ptrn = ".*?";
        String descToPtrn;
        Pattern pDescription;
        Matcher mDescription;

        /*
            Если в описании есть знак "+", тогда разбиваем на массив,
            каждый элемент которого должен быть найден.
        */

        if (corrDescr.contains("+")) {
            boolean finds = false;
            boolean totalFinds = false;
            int cntCircle = 0;

            String[] descrSplit = corrDescr.split("\\+");
            for (String descr : descrSplit) {
                cntCircle++;
                String[] firstPatterns = descr.split(";");
                if (firstPatterns.length > 1) {
                    for (String fPattr : firstPatterns) {
                        Pattern fPtrns = Pattern.compile(ptrn + fPattr.trim().toLowerCase()
                                .replaceAll("[()&$]", "")
                                + ptrn);
                        Matcher fMatchPtrns = fPtrns.matcher(description.trim().toLowerCase());
                        if (cntCircle == 1) {
                            if (fMatchPtrns.find()) {
                                finds = true;
                            }
                        } else {
                            if (fMatchPtrns.find() && finds && cntCircle > 1) {
                                totalFinds = true;
                            }
                        }
                    }
                } else {
                    Pattern fPtrn = Pattern.compile(ptrn + descr.trim().toLowerCase()
                            .replaceAll("[()&$]", "") + ptrn);
                    Matcher fMatcher = fPtrn.matcher(description.trim().toLowerCase());
                    if (cntCircle == 1) {
                        if (fMatcher.find()) {
                            finds = true;
                        }
                    } else {
                        if (fMatcher.find() && finds && cntCircle > 1) {
                            totalFinds = true;
                        }
                    }
                }
            }
            return totalFinds;
            /*
                Если в описании НЕТ знака "+" и описание пустое
            */

        } else if (corrDescr.trim().length() == 0) {
            return have.contains("no");

        /*
            Если в описании НЕТ знака "+", тогда ищем любое совпадение
        */
        } else {
            for (String fStr : pattern.split(
                    corrDescr)) {
                descToPtrn = ptrn + fStr.trim().toLowerCase().replaceAll("[()&$]", "")
                        + ptrn;
                pDescription = Pattern.compile(descToPtrn);
                mDescription = pDescription.matcher(description.
                        trim().toLowerCase());

                if (mDescription.find()) {
                    return true;
                }
            }
        }
        return false;
    }

    private String addToshlTags(ToshlExtract toshlExtract,
                                List<ToshlCorrectTags> toshlCorrectTagsList,
                                Set<String> newTagsList) {
        String ptrn = ".*?";
        String ok = "";

        /* Проходим по всем тэгам из справочника */
        for (ToshlCorrectTags correctTags : toshlCorrectTagsList) {

            Pattern pTags;
            Pattern pCategory;

            Matcher mTags = null;
            Matcher mCategory = null;

            if (correctTags.getTags() != null) {
                pTags = Pattern.compile(ptrn + correctTags.getTags().trim().toLowerCase()
                        .replaceAll("[()&$]", "") + ptrn);
                mTags = pTags.matcher(toshlExtract.getTags().trim().toLowerCase()
                        .replaceAll("[()&$]", ""));
            }
            if (correctTags.getCategory() != null) {
                pCategory = Pattern.compile(ptrn + correctTags.getCategory().trim().toLowerCase()
                        .replaceAll("[()&$]", "") + ptrn);
                mCategory = pCategory.matcher(toshlExtract.getCategory().trim().toLowerCase()
                        .replaceAll("[()&$]", ""));
            }

            if (mTags != null && mCategory != null) {
                if (mTags.find() && mCategory.find()) {
                    if (correctTags.getDateStTag() != null) {
                        if ((toshlExtract.getDate().compareTo(correctTags.getDateStTag()) == 0 ||
                                toshlExtract.getDate().compareTo(correctTags.getDateStTag()) > 0) &&
                                (toshlExtract.getDate().compareTo(correctTags.getDateEndTag()) == 0 ||
                                        toshlExtract.getDate().compareTo(correctTags.getDateEndTag()) < 0)) {
                            toshlExtract.setCorrectTag(correctTags);
                            return ok;
                        }
                    } else {
                        toshlExtract.setCorrectTag(correctTags);
                    }

                }
            }
        }

        if (toshlExtract.getCorrectTag() == null) {
            newTagsList.add(String.join("|", toshlExtract.getTags(), toshlExtract.getCategory()));
        }

        return ok;
    }

    private void rewriteSummaryData(Sheet sheet) {
        List<PaysToInvestors> paysToInvestors =
                paysToInvestorsService.findAll();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        for (Row row : sheet) {

            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(sdf.parse(row.getCell(1).toString()));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

            for (int i = 0; i < row.getLastCellNum(); i++) {
                row.getCell(i).setCellType(CellType.STRING);
            }
            BigInteger userId = new BigInteger("0");
            String lastName;
            lastName = row.getCell(2).toString();
            Users user = userService
                    .findByLastName(lastName);
            if (!Objects.equals(user, null)) {
                userId = user.getId();
            }

            BigInteger finalUserId = userId;
            paysToInvestors.forEach(pti -> {
                Calendar summaryCal = Calendar.getInstance();
                summaryCal.setTime(pti.getEndDate());

                if (pti.getFacility().equals(row.getCell(0).toString()) &&
                        summaryCal.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                        pti.getInvestorId().equals(finalUserId)) {
                    pti.setOstatokPoDole(Float.parseFloat(row.getCell(3).toString()));
                }
            });

        }

        paysToInvestorsService.saveList(paysToInvestors);

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
                    Users user = users.stream().filter(u -> u.getLastName().equalsIgnoreCase(lastName))
                            .findFirst()
                            .orElse(null);

                    List<UnderFacilities> underFacilitiesList = new ArrayList<>(0);

                    assert user != null;
                    try {
                        user.getFacilities().forEach(f -> underFacilitiesList.addAll(f.getUnderFacilities()));
                    }catch (Exception ex) {
                        System.out.println("ЯЧЕЙКА С ОШИБКОЙ =====>>> " + cel);
                    }


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

    private void rewriteInvestorsFlowsSale(Sheet sheet) {

        List<InvestorsFlowsSale> investorsFlowsSales = investorsFlowsSaleService.findAll();
        int cel = 0;
        List<InvestorsFlowsSale> investorsFlowsSaleList = new ArrayList<>(0);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
        List<Users> users = userService.findAllWithFacilitiesAndUnderFacilities();
        List<ShareKind> shareKinds = shareKindService.findAll();

        for (Row row : sheet) {
            cel++;
            if (cel > 1) {
                if (row.getCell(0) != null && row.getCell(0).getCellTypeEnum() != CellType.BLANK) {
                    Calendar calendar = Calendar.getInstance();
                    try {
                        calendar.setTime(sdf.parse(row.getCell(4).getDateCellValue().toString()));
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }

                    java.time.LocalDate cal = calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    Calendar dateSale = Calendar.getInstance();
                    try {
                        dateSale.setTime(sdf.parse(row.getCell(35).getDateCellValue().toString()));
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }

                    java.time.LocalDate calSale = dateSale.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    try {
                        for (int i = 0; i < row.getLastCellNum(); i++) {
                            row.getCell(i).setCellType(CellType.STRING);
                        }
                    } catch (Exception ignored) {
                    }

                    String lastName;
                    lastName = row.getCell(1).getStringCellValue();
                    Users user = users.stream().filter(u -> u.getLastName().equalsIgnoreCase(lastName))
                            .findFirst()
                            .orElse(null);

                    List<UnderFacilities> underFacilitiesList = new ArrayList<>(0);

                    assert user != null;
                    user.getFacilities().forEach(f -> underFacilitiesList.addAll(f.getUnderFacilities()));

                    InvestorsFlowsSale investorsFlowsSale = new InvestorsFlowsSale();
                    investorsFlowsSale.setFacility(user.getFacilities().stream()
                            .filter(f -> f.getFacility().equalsIgnoreCase(row.getCell(0).getStringCellValue()))
                            .findFirst().orElse(null));
                    investorsFlowsSale.setInvestor(user);
                    investorsFlowsSale.setShareKind(shareKinds.stream().filter(
                            shareKind -> shareKind.getShareKind()
                                    .equalsIgnoreCase(row.getCell(2).getStringCellValue())
                    ).findFirst().orElse(null));
                    investorsFlowsSale.setCashInFacility(new BigDecimal(row.getCell(3).getStringCellValue()));
                    investorsFlowsSale.setDateGived(Date.from(cal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    investorsFlowsSale.setInvestorShare(new BigDecimal(row.getCell(5).getStringCellValue()));
                    investorsFlowsSale.setCashInUnderFacility(new BigDecimal(row.getCell(6).getStringCellValue()));
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
                    investorsFlowsSale.setProfitToReInvest(new BigDecimal(row.getCell(33).getStringCellValue()));

                    investorsFlowsSale.setUnderFacility(underFacilitiesList
                            .stream()
                            .filter(uf -> uf.getUnderFacility().equalsIgnoreCase(row.getCell(34).getStringCellValue()))
                            .findFirst().orElse(null));

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
                    row.getCell(i).setCellType(CellType.STRING);
                }

                UnderFacilities underFacilities = underFacilitiesList
                        .stream()
                        .filter(uf -> uf.getUnderFacility().equalsIgnoreCase(row.getCell(13).toString()))
                        .findFirst()
                        .orElse(null);

                flows.setPlanFact(row.getCell(0).toString());
                flows.setFileName(row.getCell(1).toString());
                flows.setSettlementDate(calendar.getTime());
                flows.setSumma(Float.parseFloat(row.getCell(7).toString()));
                flows.setOrgName(row.getCell(8).toString());
                flows.setInn(row.getCell(9).toString());
                flows.setAccount(row.getCell(10).toString());
                flows.setPurposePayment(row.getCell(11).toString());
                flows.setPayment(row.getCell(12).toString());
                flows.setUnderFacilities(underFacilities);
                flows.setLevelTwo(row.getCell(14).toString());
                flows.setLevelThree(row.getCell(15).toString());

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
