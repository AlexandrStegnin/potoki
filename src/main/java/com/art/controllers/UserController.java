package com.art.controllers;

import com.art.config.AppSecurityConfig;
import com.art.func.GetServiceStatus;
import com.art.func.PersonalMailService;
import com.art.model.*;
import com.art.model.supporting.*;
import com.art.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "getServiceStatus")
    private GetServiceStatus getServiceStatus;

    @Resource(name = "roleService")
    private RoleService roleService;

    @Resource(name = "stuffService")
    private StuffService stuffService;

    @Resource(name = "mailingGroupsService")
    private MailingGroupsService mailingGroupsService;

    @Resource(name = "personalMailService")
    private PersonalMailService personalMailService;

    @Resource(name = "allowanceIpService")
    private AllowanceIpService allowanceIpService;

    @Resource(name = "bonusesService")
    private BonusesService bonusesService;

    @Resource(name = "cashPaymentsService")
    private CashPaymentsService cashPaymentsService;

    @Resource(name = "facilitiesServiceContractsService")
    private FacilitiesServiceContractsService facilitiesServiceContractsService;

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @Resource(name = "investorShareService")
    private InvestorShareService investorShareService;

    @Resource(name = "investorsCashService")
    private InvestorsCashService investorsCashService;

    @Resource(name = "investorsExpensesService")
    private InvestorsExpensesService investorsExpensesService;

    @Resource(name = "alphaCorrectTagsService")
    private AlphaCorrectTagsService alphaCorrectTagsService;

    @Resource(name = "rentorsDetailsService")
    private RentorsDetailsService rentorsDetailsService;

    @Resource(name = "usersAnnexToContractsService")
    private UsersAnnexToContractsService usersAnnexToContractsService;

    private Properties getProp() {
        String fileName = "mail.ru.properties";
        Properties prop = new Properties();
        InputStream input;
        try {
            input = AppSecurityConfig.class.getClassLoader().getResourceAsStream(fileName);
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop;
    }
    /*
     * В профиль
     *
     * */

    @GetMapping(value = "/profile")
    public ModelAndView toProfile(Principal principal) {

        String title = "Личный кабинет";
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("profile");
        Users user = userService.findByLoginWithAnnexes(principal.getName());

        List<UsersAnnexToContracts> usersAnnexToContracts = user.getUsersAnnexToContractsList();

        user.setPassword("");
        FileBucket fileModel = new FileBucket();
        int annexCnt = (int) user.getUsersAnnexToContractsList().stream()
                .filter(a -> a.getAnnexRead() == 0)
                .count();

        int totalAnnex = user.getUsersAnnexToContractsList().size();
        modelAndView.addObject("usersAnnexToContractsList", usersAnnexToContracts);
        modelAndView.addObject("totalAnnex", totalAnnex);
        modelAndView.addObject("annexCnt", annexCnt);
        modelAndView.addObject("fileBucket", fileModel);
        modelAndView.addObject("user", user);
        modelAndView.addObject("title", title);
        return modelAndView;
    }

    @PostMapping(value = "/profile")
    public ModelAndView profilePage(@ModelAttribute("user") Users user, Principal principal) {
        ModelAndView modelAndView = new ModelAndView("welcome");
        userService.update(user);
        return modelAndView;
    }

    /**
     * Создание пользователя
     */
    @GetMapping(value = {"/newuser"})
    public String newUser(ModelMap model) {
        String title = "Добавление пользователя";
        Users user = new Users();
        List<ActiveEnum> active = new ArrayList<>(
                Arrays.asList(ActiveEnum.values()));

        model.addAttribute("user", user);
        model.addAttribute("edit", false);
        model.addAttribute("active", active);
        model.addAttribute("title", title);
        return "registration";
    }

    @PostMapping(value = {"/newuser"})
    public String saveUser(@ModelAttribute("user") Users user, BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "registration";
        }
        String ret = "списку пользователей";
        String redirectUrl = "/admin";
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        user.setPassword(uuid);
        userService.create(user);
        if (user.getUserStuff().getStuff().equalsIgnoreCase("Инвестор")) {
            SendingMail mail = new SendingMail();
            mail.setSubject("Добро пожаловать в Доходный Дом Колесникъ!");
            mail.setBody("Здравствуйте, уважаемый " + user.getUserStuff().getStuff() + "!<br/>" +
                    "Вам предоставлен доступ в личный кабинет Доходного Дома &#171;Колесникъ&#187; (https://www.ddkolesnik.com)<br/>" +
                    "Наша видео инструкция поможет сориентироваться (https://youtu.be/nWtQdlP5GDU)<br/>" +
                    "Данные для входа:<br/>" +
                    "login: " + user.getLogin() + "<br/>" +
                    "Пароль: " + uuid + "<br/>" +
                    "С уважением,<br/>" +
                    "Сергей Колесник.");
            String who = "ДД Колесникъ";
            Properties prop = getProp();
            personalMailService.sendEmails(user, mail, prop.getProperty("mail.kolesnik"), prop.getProperty("mail.kolesnikpwd"), who,
                    null);
        }

        model.addAttribute("success", "Пользователь " + user.getLogin() + " успешно добавлен.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    @RequestMapping(value = {"/edit-user-{id}"}, method = RequestMethod.GET)
    public String editUser(@PathVariable BigInteger id, ModelMap model) {
        String title = "Обновление данных пользователя";
        Users user = userService.findByIdWithStuffsAndMailingGroupsAndFacilities(id);
        List<ActiveEnum> active = new ArrayList<>(
                Arrays.asList(ActiveEnum.values()));
        user.setPassword("");
        model.addAttribute("user", user);
        model.addAttribute("edit", true);
        model.addAttribute("active", active);
        model.addAttribute("title", title);
        return "registration";
    }

    @RequestMapping(value = {"/edit-user-{id}"}, method = RequestMethod.POST)
    public String updateUser(@ModelAttribute("user") Users user, BindingResult result, ModelMap model) {
        String ret = "списку пользователей.";
        String redirectUrl = "/admin";
        if (result.hasErrors()) {
            return "registration";
        }

        userService.update(user);

        model.addAttribute("success", "Данные пользователя " + user.getLogin() + " успешно обновлены.");
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);
        return "registrationsuccess";
    }

    /**
     * Удаление пользователя по ID.
     */
    @RequestMapping(value = {"/delete-user-{id}"}, method = RequestMethod.GET)
    public String deleteUser(@PathVariable BigInteger id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @PostMapping(value = "deleteuser", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse deleteUser(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        Users user = userService.findByIdWithStuffs(new BigInteger(searchSummary.getRentor()));
        List<AllowanceIp> allowanceIpList;
        List<Bonuses> bonusesList;
        List<CashPayments> cashPaymentsList;
        List<FacilitiesServiceContracts> facilitiesServiceContracts;
        List<InvestorsShare> investorsShareList;
        List<InvestorsCash> investorsCashes;
        List<InvestorsExpenses> investorsExpenses;
        List<RentorsDetails> rentorsDetailsList;
        Facilities facilities;
        switch (user.getUserStuff().getStuff()) {
            case "Арендатор":
                bonusesList = bonusesService.findByRentor(user);
                bonusesList.forEach(b -> {
                    b.setRentor(null);
                    bonusesService.update(b);
                });
                facilitiesServiceContracts = facilitiesServiceContractsService.findByRentor(user);
                facilitiesServiceContracts.forEach(fsc -> facilitiesServiceContractsService.deleteById(fsc.getId()));
                rentorsDetailsList = rentorsDetailsService.findByRentorId(user.getId());
                rentorsDetailsList.forEach(rd -> rentorsDetailsService.deleteById(rd.getId()));
                break;
            case "Управляющий":
                bonusesList = bonusesService.findByManager(user);
                bonusesList.forEach(b -> {
                    b.setManager(null);
                    bonusesService.update(b);
                });
                cashPaymentsList = cashPaymentsService.findByManagerId(user.getId());
                cashPaymentsList.forEach(cp -> cashPaymentsService.deleteById(cp.getId()));
                facilities = facilityService.findByManager(user);
                if (facilities != null) {
                    facilities.setManager(null);
                    facilityService.update(facilities);
                }

                break;
            case "Инвестор":
                allowanceIpList = allowanceIpService.findByInvestor(user);
                allowanceIpList.forEach(ap -> allowanceIpService.deleteById(ap.getId()));
                investorsShareList = investorShareService.findByInvestor(user);
                investorsShareList.forEach(ish -> investorShareService.deleteById(ish.getId()));
                investorsCashes = investorsCashService.findByInvestorId(user.getId());
                investorsCashes.forEach(ic -> investorsCashService.deleteById(ic.getId()));
                investorsExpenses = investorsExpensesService.findByInvestorId(user.getId());
                investorsExpenses.forEach(ie -> investorsExpensesService.deleteById(ie.getId()));
                break;
        }

        try {
            userService.deleteUser(user.getId());
            response.setMessage("Пользователь <b>" + user.getLogin() + "</b> успешно удалён.");
        } catch (Exception e) {
            response.setError("При удалении пользователя <b>" + user.getLogin() + "</b> произошла ошибка.");
        }

        return response;
    }

    @PostMapping(value = "/checkservice", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse checkService() {
        GenericResponse response = new GenericResponse();
        if (getServiceStatus.getStatus() == 1) {
            response.setMessage("1");
        } else {
            response.setMessage("0");
        }
        return response;
    }

    @GetMapping(value = "/createmail")
    public ModelAndView createMailPage(ModelMap model) {
        ModelAndView modelAndView = new ModelAndView("createmail");
        FileBucket fileModel = new FileBucket();
        FileBucket file = new FileBucket();
        model.addAttribute("fileBucket", fileModel);
        model.addAttribute("file", file);
        String ret = "профилю.";
        String redirectUrl = "/profile";
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("ret", ret);

        SendingMail sendingMail = new SendingMail();
        model.addAttribute("sendingMail", sendingMail);

        return modelAndView;
    }

    @PostMapping(value = "/createmail", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse sendMail(MultipartHttpServletRequest request) {
        String who = "ДД Колесникъ";
        Properties prop = getProp();
        GenericResponse response = new GenericResponse();

        Iterator<String> itr = request.getFileNames();
        List<MultipartFile> multipartFiles = new ArrayList<>(0);
        while (itr.hasNext()) {
            multipartFiles.add(request.getFile(itr.next()));
        }
        List<BigInteger> uIdList = new ArrayList<>(0);
        List<BigInteger> mIdList = new ArrayList<>(0);

        String sendingMail = request.getParameter("sendingMail");
        ObjectMapper mapper = new ObjectMapper();
        SendingMail mail = new SendingMail();
        try {
            mail = mapper.readValue(sendingMail, SendingMail.class);
        } catch (IOException ex) {
            response.setError(ex.getMessage());
        }
        mail.getMailingGroups().forEach(i -> mIdList.add(i.getId()));
        List<MailingGroups> mailingGroupsList = new ArrayList<>(0);
        if(mIdList.size() > 0){
            mailingGroupsList = mailingGroupsService.findByIdIn(mIdList);
        }
        mail.getUsers().forEach(l -> uIdList.add(l.getId()));
        List<Users> usersList = new ArrayList<>(0);
        if(uIdList.size() > 0){
            usersList = userService.findByIdIn(uIdList);
        }

        mail.setMailingGroups(mailingGroupsList);
        mail.setUsers(usersList);

        for (MailingGroups mailingGroups : mailingGroupsList) {
            usersList.addAll(mailingGroups.getUsers());
        }

        usersList = usersList.stream().distinct().collect(Collectors.toList());

        for (Users user : usersList) {

            response = personalMailService.sendEmails(user, mail,
                    prop.getProperty("mail.kolesnik"), prop.getProperty("mail.kolesnikpwd"), who,
                    multipartFiles);
        }

        return response;
    }

    @PostMapping(value = {"/saveuser"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse saveUser(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        List<BigInteger> facilitiesIdList = new ArrayList<>(0);
        searchSummary.getFacilityList().forEach(f -> facilitiesIdList.add(f.getId()));
        List<Facilities> facilitiesList = facilityService.findByIdIn(facilitiesIdList);
        String organization = "";
        String inn = "";
        String account = "";
        Users user = searchSummary.getUser();
        if (Objects.equals(user.getMailingGroups(), null)) {
            user.setMailingGroups(null);
        }
        if (searchSummary.getInn() != null) {
            inn = searchSummary.getInn();
        }
        if (searchSummary.getAccount() != null) {
            account = searchSummary.getAccount();
        }
        if (searchSummary.getOrganization() != null) {
            organization = searchSummary.getOrganization();
        }

        String[] tags = {"Свет:свет", "ЖКХ:жкх", "Аренда:аренд", "Обеспечительный:обеспеч"};

        List<AlphaCorrectTags> newACorTagsList = new ArrayList<>(0);
        List<RentorsDetails> newRdList = new ArrayList<>(0);
        DebetCreditEnum credit = DebetCreditEnum.CREDIT;
        String finalInn = inn;
        String finalAccount = account;
        String finalOrganization = organization;
        facilitiesList.forEach(f -> {
            Set<Users> usersList = f.getInvestors();
            usersList.add(user);
            switch (user.getUserStuff().getStuff()) {
                case "Арендатор":
                    for (String tag : tags) {
                        AlphaCorrectTags correctTags = new AlphaCorrectTags();
                        correctTags.setId(null);
                        correctTags.setFacility(f);
                        correctTags.setDebetOrCredit(credit);
                        correctTags.setCorrectTag(tag.split(":")[0]);
                        correctTags.setDescription(tag.split(":")[1]);
                        correctTags.setInn(finalInn);
                        correctTags.setAccount(finalAccount);
                        newACorTagsList.add(correctTags);
                    }
                    RentorsDetails rentorsDetails = new RentorsDetails();
                    rentorsDetails.setRentor(user);
                    rentorsDetails.setFacility(f);
                    rentorsDetails.setInn(finalInn);
                    rentorsDetails.setAccount(finalAccount);
                    rentorsDetails.setOrganization(finalOrganization);
                    f.setInvestors(usersList);
                    newRdList.add(rentorsDetails);
                    break;
                case "Инвестор":
                    f.setInvestors(usersList);
                    break;
            }
        });
        userService.create(user);
        facilityService.updateList(facilitiesList);
        alphaCorrectTagsService.createList(newACorTagsList);
        rentorsDetailsService.createList(newRdList);
        response.setMessage("Пользователь <b>" + user.getLogin() + "</b> успешно добавлен.");
        return response;
    }

    @PostMapping(value = {"/setReadToAnnex"}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse saveAnnexRead(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        UsersAnnexToContracts usersAnnexToContracts = usersAnnexToContractsService.findById(searchSummary.getUsersAnnexToContracts().getId());
        usersAnnexToContracts.setAnnexRead(1);
        usersAnnexToContracts.setDateRead(new Date());
        usersAnnexToContractsService.update(usersAnnexToContracts);
        response.setMessage("Запись успешно изменена");
        return response;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * Конвертация ролей/должностей в представление
     */

    @ModelAttribute("roles")
    public List<Roles> initializeRoles() {
        return roleService.findAll();
    }

    @ModelAttribute("userStuff")
    public List<Stuffs> initializeStuffs() {
        return stuffService.findAll();
    }

    @ModelAttribute("mailingGroups")
    public List<MailingGroups> initializeGroups() {
        return mailingGroupsService.findAll();
    }

    @ModelAttribute("investors")
    public List<Users> initializeInvestors() {
        return userService.findRentors(stuffService.findByStuff("Инвестор").getId());
    }

    @ModelAttribute("facilities")
    public List<Facilities> initializeFacilities() {
        return facilityService.findAll();
    }

}
