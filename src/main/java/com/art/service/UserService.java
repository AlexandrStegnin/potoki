package com.art.service;

import com.art.model.*;
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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Repository
public class UserService {
    //@Autowired
    @Resource(name = "userRepository")
    private UserRepository userRepository;

    //@Autowired
    @Resource(name = "passwordEncoder")
    private PasswordEncoder passwordEncoder;

    @Resource(name = "stuffService")
    private StuffService stuffService;

    @Resource(name = "passwordResetTokenService")
    private PasswordResetTokenService passwordResetTokenService;

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    public List<Users> findAll() {
        return userRepository.findAll();
    }

    /*
    public Users create(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }


    public Users findUserById(BigInteger id) {
        return userRepository.findOne(id);
    }
    */
    public Users login(String email, String password) {
        return userRepository.findByEmailAndPassword(email,password);
    }

    /*
    public Users update(Users user) {
        Users updUser = findUserById(user.getId());
        user.setLogin(updUser.getLogin());
        if(!Objects.equals(user.getPassword(), null) && !Objects.equals(user.getPassword(), "")){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }else{
            user.setPassword(updUser.getPassword());
        }
        return userRepository.saveAndFlush(user);

    }
    */

    public void deleteUser(BigInteger id) {
        userRepository.deleteById(id);
    }

    public Users findByLogin(String login) {
        return userRepository.findByLogin(login);
    }


    public List<Users> findRentors(BigInteger managerId){
        List<Users> usersList = new ArrayList<>(0);
        /*Users user = new Users();
        usersList.add(user);*/
        usersList.addAll(userRepository.findByStuffId(managerId));
        return usersList;
    }

    public List<Users> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void createPasswordResetTokenForUser(Users user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordResetTokenService.create(myToken);
    }

    public List<Users> findByIdIn(List<BigInteger> idList){
        return userRepository.findByIdIn(idList);
    }

    public void changeUserPassword(Users user, String password){
        user.setPassword(passwordEncoder.encode(password));
        userRepository.saveAndFlush(user);

    }

    public void updateList(List<Users> users){
        userRepository.save(users);
    }

    public Users findByLastName(String lastName){
        return userRepository.findByLastName(lastName);
    }

    public List<Users> findByStuffId(BigInteger stuffId){
        return userRepository.findByStuffId(stuffId);
    }

    public List<Users> initializeInvestors(){
        Users investor = new Users();
        investor.setId(new BigInteger("0"));
        investor.setLogin("Выберите инвестора");
        List<Users> users = new ArrayList<>(0);
        users.add(investor);
        users.addAll(userRepository.findByStuffId(stuffService.findByStuff("Инвестор").getId()));
        return users;
    }

    public List<Users> findByMailingGroups(MailingGroups mailingGroups){
        return userRepository.findByMailingGroups(mailingGroups);
    }

    /*

    CRITERIA API

     */

    public Users findById(BigInteger id){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersCriteriaQuery.select(usersRoot);
        usersCriteriaQuery.where(cb.equal(usersRoot.get(Users_.id), id));
        return em.createQuery(usersCriteriaQuery).getSingleResult();
    }

    public Users findByIdWithAnnexes(BigInteger id){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersRoot.fetch(Users_.usersAnnexToContractsList, JoinType.LEFT);
        usersCriteriaQuery.select(usersRoot).distinct(true);
        usersCriteriaQuery.where(cb.equal(usersRoot.get(Users_.id), id));
        return em.createQuery(usersCriteriaQuery).getSingleResult();
    }

    public Users findByIdWithAnnexesAndFacilities(BigInteger id){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersRoot.fetch(Users_.usersAnnexToContractsList, JoinType.LEFT);
        usersRoot.fetch(Users_.facilityes, JoinType.LEFT);
        usersCriteriaQuery.select(usersRoot).distinct(true);
        usersCriteriaQuery.where(cb.equal(usersRoot.get(Users_.id), id));
        return em.createQuery(usersCriteriaQuery).getSingleResult();
    }

    public Users findByIdWithStuffs(BigInteger id){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersRoot.fetch(Users_.userStuff, JoinType.LEFT);
        usersCriteriaQuery.select(usersRoot).distinct(true);
        usersCriteriaQuery.where(cb.equal(usersRoot.get(Users_.id), id));
        return em.createQuery(usersCriteriaQuery).getSingleResult();
    }

    public Users findByIdWithMailingGroups(BigInteger id){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersRoot.fetch(Users_.mailingGroups, JoinType.LEFT);
        usersCriteriaQuery.select(usersRoot).distinct(true);
        usersCriteriaQuery.where(cb.equal(usersRoot.get(Users_.id), id));
        return em.createQuery(usersCriteriaQuery).getSingleResult();
    }

    public Users findByIdWithStuffsAndMailingGroupsAndFacilities(BigInteger id){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersRoot.fetch(Users_.userStuff, JoinType.LEFT);
        usersRoot.fetch(Users_.mailingGroups, JoinType.LEFT);
        usersRoot.fetch(Users_.facilityes, JoinType.LEFT);
        usersCriteriaQuery.select(usersRoot).distinct(true);
        usersCriteriaQuery.where(cb.equal(usersRoot.get(Users_.id), id));
        return em.createQuery(usersCriteriaQuery).getSingleResult();
    }

    public List<Users> findAllWithStuffs(){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersRoot.fetch(Users_.userStuff, JoinType.LEFT);
        usersCriteriaQuery.select(usersRoot).distinct(true);
        return em.createQuery(usersCriteriaQuery).getResultList();
    }

    public List<Users> findByIdInWithMailingGroups(List<BigInteger> idList){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersRoot.fetch(Users_.mailingGroups, JoinType.LEFT);
        usersCriteriaQuery.select(usersRoot).distinct(true);
        usersCriteriaQuery.where(usersRoot.get(Users_.id).in(idList));
        return em.createQuery(usersCriteriaQuery).getResultList();
    }

    public List<Users> findAllWithFacilities(){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersRoot.fetch(Users_.facilityes, JoinType.LEFT);
        usersCriteriaQuery.select(usersRoot).distinct(true);
        return em.createQuery(usersCriteriaQuery).getResultList();
    }

    public List<Users> findAllWithFacilitiesAndUnderFacilities(){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);

        usersRoot.fetch(Users_.facilityes, JoinType.LEFT)
                    .fetch(Facilities_.underFacilities, JoinType.LEFT);
        usersCriteriaQuery.select(usersRoot).distinct(true);

        return em.createQuery(usersCriteriaQuery).getResultList();
    }

    public Users findByIdWithFacilities(BigInteger id){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersRoot.fetch(Users_.facilityes, JoinType.LEFT);
        usersCriteriaQuery.select(usersRoot).distinct(true);
        usersCriteriaQuery.where(cb.equal(usersRoot.get(Users_.id), id));
        return em.createQuery(usersCriteriaQuery).getSingleResult();
    }

    public Users findByLoginWithFacilitiesAndUnderFacilitiesAndAnnexes(String login){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersRoot.fetch(Users_.facilityes, JoinType.LEFT)
                .fetch(Facilities_.underFacilities, JoinType.LEFT);
        usersRoot.fetch(Users_.usersAnnexToContractsList, JoinType.LEFT);
        usersCriteriaQuery.select(usersRoot).distinct(true);
        usersCriteriaQuery.where(cb.equal(usersRoot.get(Users_.login), login));
        return em.createQuery(usersCriteriaQuery).getSingleResult();
    }

    public Users findByLoginWithAnnexes(String login){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersRoot.fetch(Users_.usersAnnexToContractsList, JoinType.LEFT);
        usersCriteriaQuery.select(usersRoot).distinct(true);
        usersCriteriaQuery.where(cb.equal(usersRoot.get(Users_.login), login));
        return em.createQuery(usersCriteriaQuery).getSingleResult();
    }

    public void create(Users users){
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        this.em.merge(users);
    }

    public Users findWithAllFields(BigInteger id){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersRoot.fetch(Users_.userStuff, JoinType.LEFT);
        usersRoot.fetch(Users_.mailingGroups, JoinType.LEFT);
        usersRoot.fetch(Users_.facilityes, JoinType.LEFT);
        usersRoot.fetch(Users_.usersAnnexToContractsList, JoinType.LEFT);
        usersRoot.fetch(Users_.emails, JoinType.LEFT);
        usersCriteriaQuery.select(usersRoot).distinct(true);
        usersCriteriaQuery.where(cb.equal(usersRoot.get(Users_.id), id));
        return em.createQuery(usersCriteriaQuery).getSingleResult();
    }

    public void update(Users user){
        Users updUser = findWithAllFields(user.getId());
        user.setLogin(updUser.getLogin());
        if(!Objects.equals(user.getPassword(), null) && !Objects.equals(user.getPassword(), "")){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }else{
            user.setPassword(updUser.getPassword());
        }
        if(Objects.equals(null, user.getLastName())){
            user.setLastName(updUser.getLastName());
        }
        if(Objects.equals(null, user.getFirst_name())){
            user.setFirst_name(updUser.getFirst_name());
        }
        if(Objects.equals(null, user.getMiddle_name())){
            user.setMiddle_name(updUser.getMiddle_name());
        }
        if(Objects.equals(null, user.getState())){
            user.setState(updUser.getState());
        }
        if(Objects.equals(null, user.getRoles())){
            user.setRoles(updUser.getRoles());
        }
        if(Objects.equals(null, user.getUserStuff())){
            user.setUserStuff(updUser.getUserStuff());
        }
        if(Objects.equals(null, user.getMailingGroups())){
            user.setMailingGroups(updUser.getMailingGroups());
        }
        if(Objects.equals(null, user.getFacilityes())){
            user.setFacilityes(updUser.getFacilityes());
        }
        if(Objects.equals(null, user.getUsersAnnexToContractsList())){
            user.setUsersAnnexToContractsList(updUser.getUsersAnnexToContractsList());
        }
        if(Objects.equals(null, user.getEmails())){
            user.setEmails(updUser.getEmails());
        }

        this.em.merge(user);

    }

    public Users findByLoginAndEmail(String login, String email){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> usersCriteriaQuery = cb.createQuery(Users.class);
        Root<Users> usersRoot = usersCriteriaQuery.from(Users.class);
        usersCriteriaQuery.select(usersRoot);
        usersCriteriaQuery.where(cb.and(cb.equal(usersRoot.get(Users_.login), login),
                cb.equal(usersRoot.get(Users_.email), email)));
        return em.createQuery(usersCriteriaQuery).getSingleResult();
    }


}
