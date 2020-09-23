package com.art.service;

import com.art.config.AppSecurityConfig;
import com.art.config.SecurityUtils;
import com.art.func.PersonalMailService;
import com.art.model.AppUser;
import com.art.model.AppUser_;
import com.art.model.UserProfile;
import com.art.model.UserProfile_;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.SendingMail;
import com.art.model.supporting.dto.UserDTO;
import com.art.model.supporting.enums.KinEnum;
import com.art.model.supporting.enums.UserRole;
import com.art.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final PersonalMailService personalMailService;

    private final AccountService accountService;

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, PersonalMailService personalMailService,
                       AccountService accountService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.personalMailService = personalMailService;
        this.accountService = accountService;
    }

//    @Cacheable(Constant.USERS_CACHE_KEY)
    public List<AppUser> findAll() {
        return userRepository.findAll();
    }

//    @CacheEvict(value = Constant.USERS_CACHE_KEY, key = "#id")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public AppUser findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public AppUser findById(Long id) {
        return userRepository.findOne(id);
    }

    public void changeUserPassword(AppUser user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.saveAndFlush(user);
    }

    public List<AppUser> initializeInvestors() {
        return getUsers("Выберите инвестора");
    }

    public List<AppUser> initializeMultipleInvestors() {
        return findAll();
    }

    public List<AppUser> initializePartners() {
        return getUsers("Выберите партнёра");
    }

    private List<AppUser> getUsers(String s) {
        AppUser partner = new AppUser();
        partner.setId(0L);
        partner.setLogin(s);
        List<AppUser> users = new ArrayList<>(0);
        users.add(partner);
        users.addAll(findAll());
        return users;
    }

    @Transactional
    public void confirm(Long userId) {
        AppUser investor = findById(userId);
        if (!investor.isConfirmed()) {
            investor.setConfirmed(true);
            update(investor);
        }
    }

    /*

    CRITERIA API

     */

    public AppUser findByLoginWithAnnexes(String login) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AppUser> usersCriteriaQuery = cb.createQuery(AppUser.class);
        Root<AppUser> usersRoot = usersCriteriaQuery.from(AppUser.class);
        usersCriteriaQuery.select(usersRoot).distinct(true);
        usersCriteriaQuery.where(cb.equal(usersRoot.get(AppUser_.login), login));
        return em.createQuery(usersCriteriaQuery).getSingleResult();
    }

//    @CachePut(value = Constant.USERS_CACHE_KEY, key = "#user?.id")
    public ApiResponse create(AppUser user) {
        ApiResponse response = new ApiResponse();
        if (loginIsBusy(user.getLogin())) {
            response.setError(String.format("Логин [%s] занят", user.getLogin()));
            response.setStatus(HttpStatus.PRECONDITION_FAILED.value());
            return response;
        }
        String password = generatePassword();
        user.setPassword(password);
        user.getProfile().setUser(user);
        user.getProfile().setEmail(user.getProfile().getEmail().toLowerCase());
        user.setLogin(user.getLogin().toLowerCase());
        userRepository.save(user);
        if (SecurityUtils.isUserInRole(user, UserRole.ROLE_INVESTOR)) {
            accountService.createAccount(user);
            sendWelcomeMessage(user, password);
        }
        response.setMessage("Пользователь успешно создан");
        return response;
    }

//    @CachePut(value = Constant.USERS_CACHE_KEY, key = "#user?.id")
    public void updateProfile(AppUser user) {
        AppUser dbUser = findById(user.getId());
        if (null != user.getPassword()) {
            dbUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (null != user.getProfile().getEmail()) {
            dbUser.getProfile().setEmail(user.getProfile().getEmail());
        }
        userRepository.save(dbUser);
    }

//    @CachePut(value = Constant.USERS_CACHE_KEY, key = "#user?.id")
    public ApiResponse update(AppUser user) {
        AppUser dbUser = findById(user.getId());
        dbUser.setPartnerId(user.getPartnerId());
        dbUser.setKin(user.getKin());
        dbUser.setRoles(user.getRoles());
        UserProfile profile = dbUser.getProfile();
        profile.setFirstName(user.getProfile().getFirstName());
        profile.setLastName(user.getProfile().getLastName());
        profile.setPatronymic(user.getProfile().getPatronymic());
        profile.setEmail(user.getProfile().getEmail());
        userRepository.save(dbUser);
        return new ApiResponse("Пользователь успешно обновлён");
    }

    private void sendWelcomeMessage(AppUser user, String password) {
        SendingMail mail = new SendingMail();
        mail.setSubject("Добро пожаловать в Доходный Дом КолесникЪ!");
        mail.setBody("Здравствуйте, уважаемый Инвестор!<br/>" +
                "Вам предоставлен доступ в личный кабинет Доходного Дома &#171;Колесникъ&#187; (https://www.ddkolesnik.com)<br/>" +
                "Наша видео инструкция поможет сориентироваться (https://youtu.be/nWtQdlP5GDU)<br/>" +
                "Данные для входа:<br/>" +
                "login: " + user.getLogin() + "<br/>" +
                "Пароль: " + password + "<br/>" +
                "С уважением,<br/>" +
                "Сергей Колесник.");
        String who = "ДД Колесникъ";
        Properties prop = getProp();
        personalMailService.sendEmails(user, mail, prop.getProperty("mail.kolesnik"), prop.getProperty("mail.kolesnikpwd"), who, null);
    }

    public AppUser findByLoginAndEmail(String login, String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AppUser> usersCriteriaQuery = cb.createQuery(AppUser.class);
        Root<AppUser> usersRoot = usersCriteriaQuery.from(AppUser.class);
        usersCriteriaQuery.select(usersRoot);
        usersCriteriaQuery.where(cb.and(cb.equal(usersRoot.get(AppUser_.login), login),
                cb.equal(usersRoot.get(AppUser_.profile).get(UserProfile_.email), email)));
        return em.createQuery(usersCriteriaQuery).getSingleResult();
    }

    public Properties getProp() {
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

    public List<AppUser> getForFindPartnerChild() {
        return userRepository.getForFindPartnerChild();
    }

    private String generatePassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Проверка занят ли логин
     *
     * @param login логин для проверки
     * @return статус
     */
    public boolean loginIsBusy(String login) {
        return userRepository.existsByLogin(login);
    }

    /**
     * Деактивировать пользователя, чтоб убрать из маркетингового дерева
     *
     * @param dto DTO для деактивации
     * @return ответ
     */
    public ApiResponse deactivateUser(UserDTO dto) {
        if (dto.getId() == null) {
            return new ApiResponse("Не задан id пользователя", HttpStatus.PRECONDITION_FAILED.value());
        }
        AppUser user = findById(dto.getId());
        if (user == null) {
            return new ApiResponse("Пользователь не найден", HttpStatus.PRECONDITION_FAILED.value());
        }
        user.setConfirmed(false);
        user.setKin(KinEnum.EMPTY);
        user.setPartnerId(null);
        update(user);
        return new ApiResponse("Пользователь успешно деактивирован");
    }

}
