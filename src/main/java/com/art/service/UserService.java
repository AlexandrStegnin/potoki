package com.art.service;

import com.art.config.AppSecurityConfig;
import com.art.config.SecurityUtils;
import com.art.func.PersonalMailService;
import com.art.model.*;
import com.art.model.supporting.SendingMail;
import com.art.model.supporting.enums.UserRole;
import com.art.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
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

    public List<Users> findAll() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Users findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public Users findById(Long id) {
        return userRepository.findOne(id);
    }

    public void changeUserPassword(Users user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.saveAndFlush(user);
    }

    public List<Users> initializeInvestors() {
        return getUsers("Выберите инвестора");
    }

    public List<Users> initializeMultipleInvestors() {
        return userRepository.findAll();
    }

    public List<Users> initializePartners() {
        return getUsers("Выберите партнёра");
    }

    private List<Users> getUsers(String s) {
        Users partner = new Users();
        partner.setId(Long.valueOf("0"));
        partner.setLogin(s);
        List<Users> users = new ArrayList<>(0);
        users.add(partner);
        users.addAll(userRepository.findAll());
        return users;
    }

    @Transactional
    public void confirm(Long userId) {
        Users investor = findById(userId);
        if (!investor.isConfirmed()) {
            investor.setConfirmed(true);
            update(investor);
        }
    }

    /*

    CRITERIA API

     */

    public List<Users> findAllWithFacilitiesAndUnderFacilities() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);

        usersRoot.fetch(Users_.facilities, JoinType.LEFT)
                .fetch(Facility_.underFacilities, JoinType.LEFT);
        usersCriteriaQuery.select(usersRoot).distinct(true);

        return em.createQuery(usersCriteriaQuery).getResultList();
    }

    public Users findByLoginWithAnnexes(String login) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersCriteriaQuery.select(usersRoot).distinct(true);
        usersCriteriaQuery.where(cb.equal(usersRoot.get(Users_.login), login));
        return em.createQuery(usersCriteriaQuery).getSingleResult();
    }

    public void create(Users user) {
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
    }

    public void updateProfile(Users user) {
        Users dbUser = findById(user.getId());
        if (null != user.getPassword()) {
            dbUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (null != user.getProfile().getEmail()) {
            dbUser.getProfile().setEmail(user.getProfile().getEmail());
        }
        userRepository.save(dbUser);
    }

    public void update(Users user) {
        Users dbUser = findById(user.getId());
        dbUser.setPartnerId(user.getPartnerId());
        dbUser.setKin(user.getKin());
        dbUser.setRoles(user.getRoles());
        UserProfile profile = dbUser.getProfile();
        profile.setFirstName(user.getProfile().getFirstName());
        profile.setLastName(user.getProfile().getLastName());
        profile.setPatronymic(user.getProfile().getPatronymic());
        profile.setEmail(user.getProfile().getEmail());
        userRepository.save(dbUser);
    }

    private void sendWelcomeMessage(Users user, String password) {
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

    public Users findByLoginAndEmail(String login, String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersCriteriaQuery.select(usersRoot);
        usersCriteriaQuery.where(cb.and(cb.equal(usersRoot.get(Users_.login), login),
                cb.equal(usersRoot.get(Users_.profile).get(UserProfile_.email), email)));
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

    public List<Users> getForFindPartnerChild() {
        return userRepository.getForFindPartnerChild();
    }

    private String generatePassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

}
