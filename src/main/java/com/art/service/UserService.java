package com.art.service;

import com.art.config.AppSecurityConfig;
import com.art.config.SecurityUtils;
import com.art.config.exception.ApiException;
import com.art.config.exception.EntityNotFoundException;
import com.art.func.PersonalMailService;
import com.art.model.*;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.SendingMail;
import com.art.model.supporting.dto.EmailDTO;
import com.art.model.supporting.dto.UserDTO;
import com.art.model.supporting.enums.KinEnum;
import com.art.model.supporting.enums.UserRole;
import com.art.model.supporting.filters.AppUserFilter;
import com.art.repository.MarketingTreeRepository;
import com.art.repository.UserRepository;
import com.art.specifications.AppUserSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {

  final UserRepository userRepository;
  final PasswordEncoder passwordEncoder;
  final PersonalMailService personalMailService;
  final AccountService accountService;
  final MarketingTreeRepository marketingTreeRepository;
  final AppUserSpecification specification;

  @PersistenceContext(name = "persistanceUnit")
  EntityManager em;

  public List<AppUser> findAll() {
    return userRepository.findAll();
  }

  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }

  public AppUser findByLogin(String login) {
    return userRepository.findByLogin(login);
  }

  public AppUser findById(Long id) {
    return userRepository.findOne(id);
  }

  public UserDTO find(UserDTO dto) {
    AppUser user = userRepository.findOne(dto.getId());
    if (user == null) {
      throw new EntityNotFoundException("Пользователь не найден");
    }
    return new UserDTO(user);
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

  public ApiResponse create(AppUser user) {
    ApiResponse response = new ApiResponse();
    if (loginIsBusy(user.getLogin())) {
      response.setError(String.format("Логин [%s] занят", user.getLogin()));
      response.setStatus(HttpStatus.PRECONDITION_FAILED.value());
      return response;
    }
    String password = generatePassword();
    user.setPassword(passwordEncoder.encode(password));
    user.getProfile().setUser(user);
    user.getProfile().setEmail(user.getProfile().getEmail().toLowerCase());
    user.setLogin(user.getLogin().toLowerCase());
    userRepository.save(user);
    if (SecurityUtils.isUserInRole(user, UserRole.ROLE_INVESTOR)) {
      Account account = accountService.createAccount(user);
      if (account != null) {
        sendWelcomeMessage(user, password);
      }
    }
    response.setMessage("Пользователь успешно создан");
    return response;
  }

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

  public ApiResponse save(UserDTO dto) {
    AppUser user = new AppUser(dto);
    if (Objects.isNull(user.getId())) {
      return create(user);
    } else {
      return update(user);
    }
  }

  public ApiResponse update(AppUser user) {
    AppUser dbUser = findById(user.getId());
    dbUser.setPartner(user.getPartner());
    dbUser.setKin(user.getKin());
    dbUser.setRole(user.getRole());
    dbUser.setPhone(user.getPhone());
    dbUser.setBitrixId(user.getBitrixId());
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
        "Вам предоставлен доступ в личный кабинет Доходного Дома &#171;КолесникЪ&#187; (https://www.lk.ddkolesnik.com)<br/>" +
        "Данные для входа:<br/>" +
        "login: " + user.getLogin() + "<br/>" +
        "Пароль: " + password + "<br/>" +
        "С уважением, Колесник.Инвестиции");
    String who = "ДД КолесникЪ";
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
    user.setPartner(null);
    user.getProfile().setLocked(true);
    update(user);
    marketingTreeRepository.deleteByInvestorId(user.getId());
    return new ApiResponse("Пользователь успешно деактивирован");
  }

  public Page<AppUser> findAll(AppUserFilter filter, Pageable pageable) {
    return userRepository.findAll(
        specification.getFilter(filter),
        pageable
    );
  }

  public List<AppUser> getInvestors() {
    return userRepository.getInvestors();
  }

  public List<AppUser> getSellers() {
    return userRepository.getSellers();
  }

  public List<AppUser> initializeSellers() {
    List<AppUser> appUsers = new ArrayList<>(200);
    AppUser appUser = new AppUser();
    appUser.setId(0L);
    appUser.setLogin("Выберите продавца");
    appUsers.add(appUser);
    appUsers.addAll(getSellers());
    return appUsers;
  }

  public ApiResponse sendWelcomeMessage(EmailDTO emailDTO) {
    if (Objects.isNull(emailDTO)
        || Objects.isNull(emailDTO.getUser())
        || Objects.isNull(emailDTO.getUser().getId())) {
      throw new ApiException("Для отправки email не указан пользователь", HttpStatus.BAD_REQUEST);
    }
    AppUser user = findById(emailDTO.getUser().getId());
    if (Objects.isNull(user)) {
      throw new ApiException("Пользователь не найден", HttpStatus.NOT_FOUND);
    }
    String password = generatePassword();
    user.setPassword(passwordEncoder.encode(password));
    sendWelcomeMessage(user, password);
    userRepository.save(user);
    return new ApiResponse("Письмо успешно отправлено пользователю " + user.getProfile().getEmail());
  }
}
