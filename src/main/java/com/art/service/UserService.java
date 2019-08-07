package com.art.service;

import com.art.config.AppSecurityConfig;
import com.art.func.PersonalMailService;
import com.art.model.Facilities_;
import com.art.model.MailingGroups;
import com.art.model.Users;
import com.art.model.Users_;
import com.art.model.supporting.SendingMail;
import com.art.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.*;

@Service
@Transactional
@Repository
public class UserService {
    @Resource(name = "userRepository")
    private UserRepository userRepository;

    @Resource(name = "passwordEncoder")
    private PasswordEncoder passwordEncoder;

    @Resource(name = "stuffService")
    private StuffService stuffService;

    @Resource(name = "personalMailService")
    private PersonalMailService personalMailService;

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    public List<Users> findAll() {
        return userRepository.findAll();
    }

    public Users login(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public void deleteUser(BigInteger id) {
        userRepository.deleteById(id);
    }

    public Users findByLogin(String login) {
        return userRepository.findByLogin(login);
    }


    public List<Users> findRentors(BigInteger managerId) {
        List<Users> usersList = new ArrayList<>(0);
        usersList.addAll(userRepository.findByStuffIdOrderByLastName(managerId));
        return usersList;
    }

    public List<Users> findByIdIn(List<BigInteger> idList) {
        return userRepository.findByIdIn(idList);
    }

    public void changeUserPassword(Users user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.saveAndFlush(user);
    }

    public void updateList(List<Users> users) {
        userRepository.save(users);
    }

    public Users findByLastName(String lastName) {
        return userRepository.findByLastName(lastName);
    }

    public List<Users> initializeInvestors() {
        return getUsers("Выберите инвестора");
    }

    public List<Users> initializePartners() {
        return getUsers("Выберите партнёра");
    }

    private List<Users> getUsers(String s) {
        Users partner = new Users();
        partner.setId(new BigInteger("0"));
        partner.setLogin(s);
        List<Users> users = new ArrayList<>(0);
        users.add(partner);
        users.addAll(userRepository.findByStuffIdOrderByLastName(stuffService.findByStuff("Инвестор").getId()));
        return users;
    }

    public List<Users> findByMailingGroups(MailingGroups mailingGroups) {
        return userRepository.findByMailingGroups(mailingGroups);
    }

    /*

    CRITERIA API

     */

    public Users findById(BigInteger id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersCriteriaQuery.select(usersRoot);
        usersCriteriaQuery.where(cb.equal(usersRoot.get(Users_.id), id));
        return em.createQuery(usersCriteriaQuery).getSingleResult();
    }

    public Users findByIdWithAnnexesAndFacilities(BigInteger id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersRoot.fetch(Users_.facilities, JoinType.LEFT);
        usersRoot.fetch(Users_.usersAnnexToContractsList, JoinType.LEFT);
        usersCriteriaQuery.select(usersRoot).distinct(true);
        usersCriteriaQuery.where(cb.equal(usersRoot.get(Users_.id), id));
        return em.createQuery(usersCriteriaQuery).getSingleResult();
    }

    public Users findByIdWithStuffs(BigInteger id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersRoot.fetch(Users_.userStuff, JoinType.LEFT);
        usersCriteriaQuery.select(usersRoot).distinct(true);
        usersCriteriaQuery.where(cb.equal(usersRoot.get(Users_.id), id));
        return em.createQuery(usersCriteriaQuery).getSingleResult();
    }

    public Users findByIdWithStuffsAndMailingGroupsAndFacilities(BigInteger id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersRoot.fetch(Users_.userStuff, JoinType.LEFT);
        usersRoot.fetch(Users_.mailingGroups, JoinType.LEFT);
        usersRoot.fetch(Users_.facilities, JoinType.LEFT);
        usersCriteriaQuery.select(usersRoot).distinct(true);
        usersCriteriaQuery.where(cb.equal(usersRoot.get(Users_.id), id));
        return em.createQuery(usersCriteriaQuery).getSingleResult();
    }

    public List<Users> findAllWithStuffs() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersRoot.fetch(Users_.userStuff, JoinType.LEFT);
        usersCriteriaQuery.select(usersRoot).distinct(true);
        return em.createQuery(usersCriteriaQuery).getResultList();
    }

    public List<Users> findByIdInWithMailingGroups(List<BigInteger> idList) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersRoot.fetch(Users_.mailingGroups, JoinType.LEFT);
        usersCriteriaQuery.select(usersRoot).distinct(true);
        usersCriteriaQuery.where(usersRoot.get(Users_.id).in(idList));
        return em.createQuery(usersCriteriaQuery).getResultList();
    }

    public List<Users> findAllWithFacilitiesAndUnderFacilities() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);

        usersRoot.fetch(Users_.facilities, JoinType.LEFT)
                .fetch(Facilities_.underFacilities, JoinType.LEFT);
        usersCriteriaQuery.select(usersRoot).distinct(true);

        return em.createQuery(usersCriteriaQuery).getResultList();
    }

    public Users findByIdWithFacilities(BigInteger id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersRoot.fetch(Users_.facilities, JoinType.LEFT);
        usersCriteriaQuery.select(usersRoot).distinct(true);
        usersCriteriaQuery.where(cb.equal(usersRoot.get(Users_.id), id));
        return em.createQuery(usersCriteriaQuery).getSingleResult();
    }

    public Users findByLoginWithAnnexes(String login) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersRoot.fetch(Users_.usersAnnexToContractsList, JoinType.LEFT);
        usersCriteriaQuery.select(usersRoot).distinct(true);
        usersCriteriaQuery.where(cb.equal(usersRoot.get(Users_.login), login));
        return em.createQuery(usersCriteriaQuery).getSingleResult();
    }

    public void create(Users user) {
        sendWelcomeMessage(user);
        this.em.merge(user);
    }

    public Users findWithAllFields(BigInteger id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersRoot.fetch(Users_.userStuff, JoinType.LEFT);
        usersRoot.fetch(Users_.mailingGroups, JoinType.LEFT);
        usersRoot.fetch(Users_.facilities, JoinType.LEFT);
        usersRoot.fetch(Users_.usersAnnexToContractsList, JoinType.LEFT);
        usersRoot.fetch(Users_.emails, JoinType.LEFT);
        usersCriteriaQuery.select(usersRoot).distinct(true);
        usersCriteriaQuery.where(cb.equal(usersRoot.get(Users_.id), id));
        return em.createQuery(usersCriteriaQuery).getSingleResult();
    }

    public void update(Users user) {
        Users updUser = findWithAllFields(user.getId());
        user.setLogin(updUser.getLogin());
        if (!Objects.equals(user.getPassword(), null) && !Objects.equals(user.getPassword(), "")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(updUser.getPassword());
        }
        if (Objects.equals(null, user.getLastName())) {
            user.setLastName(updUser.getLastName());
        }
        if (Objects.equals(null, user.getFirst_name())) {
            user.setFirst_name(updUser.getFirst_name());
        }
        if (Objects.equals(null, user.getMiddle_name())) {
            user.setMiddle_name(updUser.getMiddle_name());
        }
        if (Objects.equals(null, user.getState())) {
            user.setState(updUser.getState());
        }
        if (Objects.equals(null, user.getRoles())) {
            user.setRoles(updUser.getRoles());
        }
        if (Objects.equals(null, user.getUserStuff())) {
            user.setUserStuff(updUser.getUserStuff());
        }
        if (Objects.equals(null, user.getMailingGroups())) {
            user.setMailingGroups(updUser.getMailingGroups());
        }
        if (Objects.equals(null, user.getFacilities())) {
            user.setFacilities(updUser.getFacilities());
        }
        if (Objects.equals(null, user.getUsersAnnexToContractsList())) {
            user.setUsersAnnexToContractsList(updUser.getUsersAnnexToContractsList());
        }
        if (Objects.equals(null, user.getEmails())) {
            user.setEmails(updUser.getEmails());
        }
        if (!user.getEmail().equalsIgnoreCase(updUser.getEmail())) {
            sendWelcomeMessage(user);
        }
        if (Objects.equals(null, user.getPartnerId())) {
            user.setPartnerId(updUser.getPartnerId());
            user.setKin(updUser.getKin());
        }

        this.em.merge(user);

    }

    private void sendWelcomeMessage(Users user) {
        String uuid = UUID.randomUUID().toString().substring(0, 8);
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
            user.setPassword(passwordEncoder.encode(uuid));
        }
    }

    public Users findByLoginAndEmail(String login, String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersCriteriaQuery.select(usersRoot);
        usersCriteriaQuery.where(cb.and(cb.equal(usersRoot.get(Users_.login), login),
                cb.equal(usersRoot.get(Users_.email), email)));
        return em.createQuery(usersCriteriaQuery).getSingleResult();
    }

    public List<Users> findAllWithAllFields() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersRoot.fetch(Users_.userStuff, JoinType.LEFT);
        usersRoot.fetch(Users_.mailingGroups, JoinType.LEFT);
        usersRoot.fetch(Users_.facilities, JoinType.LEFT);
        usersRoot.fetch(Users_.usersAnnexToContractsList, JoinType.LEFT);
        usersRoot.fetch(Users_.emails, JoinType.LEFT);
        usersCriteriaQuery.select(usersRoot).distinct(true);
        return em.createQuery(usersCriteriaQuery).getResultList();
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

}
