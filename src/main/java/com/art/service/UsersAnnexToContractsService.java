package com.art.service;

import com.art.model.AnnexToContracts;
import com.art.model.Users;
import com.art.model.UsersAnnexToContracts;
import com.art.model.UsersAnnexToContracts_;
import com.art.repository.UserAnnexRepository;
import com.art.repository.UserAnnexToContractsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class UsersAnnexToContractsService {

    private final UserAnnexToContractsRepository userAnnexToContractsRepository;

    private final UserAnnexRepository annexRepository;

    private final UserService userService;

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    public UsersAnnexToContractsService(UserAnnexToContractsRepository userAnnexToContractsRepository,
                                        UserAnnexRepository annexRepository, UserService userService) {
        this.userAnnexToContractsRepository = userAnnexToContractsRepository;
        this.annexRepository = annexRepository;
        this.userService = userService;
    }

    public List<UsersAnnexToContracts> findByUserAndAnnexName(Long userId, String annexName) {
        if (!annexName.endsWith(".pdf")) {
            annexName = annexName + ".pdf";
        }
        return userAnnexToContractsRepository.findByUserIdAndAnnex_AnnexName(userId, annexName);
    }

    public List<UsersAnnexToContracts> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<UsersAnnexToContracts> usersAnnexToContractsCriteriaQuery = cb.createQuery(UsersAnnexToContracts.class);
        Root<UsersAnnexToContracts> usersAnnexToContractsRoot = usersAnnexToContractsCriteriaQuery.from(UsersAnnexToContracts.class);
        usersAnnexToContractsCriteriaQuery.select(usersAnnexToContractsRoot);

        return em.createQuery(usersAnnexToContractsCriteriaQuery).getResultList();
    }

    public UsersAnnexToContracts findById(BigInteger id) {
        return this.em.find(UsersAnnexToContracts.class, id);
    }

    public List<UsersAnnexToContracts> findByUserIdAndAnnex(BigInteger userId, AnnexToContracts annex) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<UsersAnnexToContracts> usersAnnexToContractsCriteriaQuery = cb.createQuery(UsersAnnexToContracts.class);
        Root<UsersAnnexToContracts> usersAnnexToContractsRoot = usersAnnexToContractsCriteriaQuery.from(UsersAnnexToContracts.class);
        usersAnnexToContractsCriteriaQuery.select(usersAnnexToContractsRoot);
        usersAnnexToContractsCriteriaQuery.where(cb.equal(usersAnnexToContractsRoot.get(UsersAnnexToContracts_.userId), userId),
                cb.equal(usersAnnexToContractsRoot.get(UsersAnnexToContracts_.annex), annex));

        return em.createQuery(usersAnnexToContractsCriteriaQuery).getResultList();

    }

    public List<UsersAnnexToContracts> findByUserId(Long userId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<UsersAnnexToContracts> usersAnnexToContractsCriteriaQuery = cb.createQuery(UsersAnnexToContracts.class);
        Root<UsersAnnexToContracts> usersAnnexToContractsRoot = usersAnnexToContractsCriteriaQuery.from(UsersAnnexToContracts.class);
        usersAnnexToContractsCriteriaQuery.select(usersAnnexToContractsRoot);
        usersAnnexToContractsCriteriaQuery.where(cb.equal(usersAnnexToContractsRoot.get(UsersAnnexToContracts_.userId), userId));

        return em.createQuery(usersAnnexToContractsCriteriaQuery).getResultList();

    }

    public List<UsersAnnexToContracts> findByLogin(String login) {
        if (Objects.isNull(login)) {
            throw new RuntimeException("Необходимо передать логин пользователя");
        }
        Users user = userService.findByLogin(login);
        if (Objects.isNull(user)) {
            throw new RuntimeException("Пользователь с логином = [" + login + "] не найден");
        }
        return findByUserId(user.getId());
    }

    public void deleteById(BigInteger id) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaDelete<UsersAnnexToContracts> delete = cb.createCriteriaDelete(UsersAnnexToContracts.class);
        Root<UsersAnnexToContracts> usersAnnexToContractsRoot = delete.from(UsersAnnexToContracts.class);
        delete.where(cb.equal(usersAnnexToContractsRoot.get(UsersAnnexToContracts_.id), id));
        this.em.createQuery(delete).executeUpdate();
    }

    public void deleteByAnnex(AnnexToContracts annex) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaDelete<UsersAnnexToContracts> delete = cb.createCriteriaDelete(UsersAnnexToContracts.class);
        Root<UsersAnnexToContracts> usersAnnexToContractsRoot = delete.from(UsersAnnexToContracts.class);
        delete.where(cb.equal(usersAnnexToContractsRoot.get(UsersAnnexToContracts_.annex), annex));
        this.em.createQuery(delete).executeUpdate();
    }

    public void update(UsersAnnexToContracts usersAnnexToContracts) {
        this.em.merge(usersAnnexToContracts);
    }

    public void create(UsersAnnexToContracts usersAnnexToContracts) {
        this.em.persist(usersAnnexToContracts);
    }

    public boolean haveUnread(String login) {
        Users user = userService.findByLogin(login);
        if (Objects.nonNull(user)) {
            return haveUnread(user.getId());
        }
        return false;
    }

    private boolean haveUnread(Long userId) {
        return annexRepository.existsByUserIdAndDateReadIsNull(userId);
    }
}
