package com.art.controllers;

import com.art.config.SecurityUtils;
import com.art.config.application.Location;
import com.art.model.*;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.FileBucket;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.SearchSummary;
import com.art.model.supporting.dto.UserDTO;
import com.art.model.supporting.enums.KinEnum;
import com.art.model.supporting.enums.OwnerType;
import com.art.model.supporting.filters.AppUserFilter;
import com.art.service.*;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigInteger;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    private final RoleService roleService;

    private final FacilityService facilityService;

    private final MoneyService moneyService;

    private final UsersAnnexToContractsService usersAnnexToContractsService;

    private final AccountService accountService;

    private final AppUserFilter filter = new AppUserFilter();

    public UserController(UserService userService, RoleService roleService,
                          FacilityService facilityService, MoneyService moneyService,
                          UsersAnnexToContractsService usersAnnexToContractsService, AccountService accountService) {
        this.userService = userService;
        this.roleService = roleService;
        this.facilityService = facilityService;
        this.moneyService = moneyService;
        this.usersAnnexToContractsService = usersAnnexToContractsService;
        this.accountService = accountService;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(path = Location.USERS_LIST)
    public ModelAndView usersList(@PageableDefault(size = 1000) @SortDefault Pageable pageable) {
        return prepareModel(filter);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(path = Location.USERS_LIST)
    public ModelAndView usersListFiltered(@ModelAttribute("filter") AppUserFilter filter) {
        return prepareModel(filter);
    }

    /**
     * Подготовить модель для страницы
     *
     * @param filter фильтры
     */
    private ModelAndView prepareModel(AppUserFilter filter) {
        ModelAndView model = new ModelAndView("user-list");
        Pageable pageable = new PageRequest(filter.getPageNumber(), filter.getPageSize());
        Page<AppUser> page = userService.findAll(filter, pageable);
        List<KinEnum> kins = new ArrayList<>(Arrays.asList(KinEnum.values()));
        model.addObject("page", page);
        model.addObject("filter", filter);
        model.addObject("userDTO", new UserDTO());
        model.addObject("kins", kins);

        return model;
    }

    /*
     * В профиль
     *
     * */

    @GetMapping(value = "/profile")
    public ModelAndView toProfile(Principal principal) {

        String title = "Личный кабинет";
        ModelAndView modelAndView = new ModelAndView("profile");
        AppUser user = userService.findByLoginWithAnnexes(principal.getName());
        Account account = accountService.findByOwnerId(user.getId(), OwnerType.INVESTOR);
        String accountNumber;
        if (null == account) {
            accountNumber = "";
        } else {
            accountNumber = account.getAccountNumber();
        }
        List<UsersAnnexToContracts> usersAnnexToContracts = usersAnnexToContractsService.findByUserId(user.getId());

        user.setPassword("");
        FileBucket fileModel = new FileBucket();
        int annexCnt = (int) usersAnnexToContracts.stream()
                .filter(a -> a.getAnnexRead() == 0)
                .count();

        int totalAnnex = usersAnnexToContracts.size();
        modelAndView.addObject("usersAnnexToContractsList", usersAnnexToContracts);
        modelAndView.addObject("totalAnnex", totalAnnex);
        modelAndView.addObject("annexCnt", annexCnt);
        modelAndView.addObject("fileBucket", fileModel);
        modelAndView.addObject("user", user);
        modelAndView.addObject("title", title);
        modelAndView.addObject("search", new SearchSummary());
        modelAndView.addObject("accountNumber", accountNumber);
        return modelAndView;
    }

    @PostMapping(value = "/profile")
    public ModelAndView profilePage(@ModelAttribute("user") UserDTO userDTO, Principal principal) {
        ModelAndView modelAndView = new ModelAndView("profile");
        AppUser user = new AppUser(userDTO);
        userService.updateProfile(user);
        user.setPassword(null);
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @PostMapping(value = "/admin-flows")
    public ModelAndView viewFlowsAdmin(@ModelAttribute("searchSummary") SearchSummary searchSummary) {
        ModelAndView view = new ModelAndView("viewFlows");
        if (SecurityUtils.isAdmin()) {
            if (searchSummary.getInvestor() != null || searchSummary.getLogin() != null) {
                BigInteger invId = new BigInteger(searchSummary.getInvestor());
                String login = searchSummary.getLogin();
                view.addObject("invId", invId);
                view.addObject("invLogin", login);
            }
        }
        return view;
    }

    @GetMapping(path = "/edit-user-{id}")
    public String editUser(@PathVariable Long id, ModelMap model) {
        String title = "Обновление данных пользователя";
        AppUser user = userService.findById(id);

        List<KinEnum> kins = new ArrayList<>(Arrays.asList(KinEnum.values()));
        model.addAttribute("user", user);
        model.addAttribute("edit", true);
        model.addAttribute("kins", kins);
        model.addAttribute("title", title);
        return "user-add";
    }

    /**
     * Удаление пользователя по ID.
     */
    @PostMapping(path = "/users/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    GenericResponse deleteUser(@RequestBody UserDTO userDTO) {
        GenericResponse response = new GenericResponse();
        AppUser user = userService.findById(userDTO.getId());
        List<Money> monies;
        monies = moneyService.findByInvestorId(user.getId());
        monies.forEach(ic -> moneyService.deleteById(ic.getId()));
        try {
            userService.deleteUser(user.getId());
            response.setMessage("Пользователь <b>" + user.getLogin() + "</b> успешно удалён.");
        } catch (Exception e) {
            response.setError("При удалении пользователя <b>" + user.getLogin() + "</b> произошла ошибка.");
        }
        return response;
    }

    /**
     * Страница создания пользователя
     */
    @GetMapping(path = Location.USERS_CREATE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String createUser(ModelMap model) {
        String title = "Создание пользователя";
        List<KinEnum> kins = new ArrayList<>(Arrays.asList(KinEnum.values()));
        AppUser user = new AppUser();
        model.addAttribute("user", user);
        model.addAttribute("kins", kins);
        model.addAttribute("title", title);
        return "user-add";
    }

    /**
     * Создание/обновление пользователя
     */
    @PostMapping(path = Location.USERS_SAVE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    ApiResponse saveUser(@RequestBody UserDTO userDTO) {
        AppUser user = new AppUser(userDTO);
        ApiResponse response;
        if (user.getId() == null) {
            response = userService.create(user);
        } else {
            response = userService.update(user);
        }
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

    @ResponseBody
    @PostMapping(path = Location.DEACTIVATE_USER)
    public ApiResponse deactivateUser(@RequestBody UserDTO dto) {
        return userService.deactivateUser(dto);
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
    public List<AppRole> initializeRoles() {
        return roleService.findAll();
    }

    @ModelAttribute("investors")
    public List<AppUser> initializeInvestors() {
        return userService.initializeInvestors();
    }

    @ModelAttribute("facilities")
    public List<Facility> initializeFacilities() {
        return facilityService.findAll();
    }

}
