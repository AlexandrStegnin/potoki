package com.art.service;

import com.art.model.AppUser;
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

//    @Cacheable(Constant.USERS_ANNEXES_CACHE_KEY)
    public List<UsersAnnexToContracts> findByUserAndAnnexName(Long userId, String annexName) {
        if (!annexName.endsWith(".pdf")) {
            annexName = annexName + ".pdf";
        }
        return userAnnexToContractsRepository.findByUserIdAndAnnex_AnnexName(userId, annexName);
    }

//    @Cacheable(Constant.USERS_ANNEXES_CACHE_KEY)
    public List<UsersAnnexToContracts> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<UsersAnnexToContracts> usersAnnexToContractsCriteriaQuery = cb.createQuery(UsersAnnexToContracts.class);
        Root<UsersAnnexToContracts> usersAnnexToContractsRoot = usersAnnexToContractsCriteriaQuery.from(UsersAnnexToContracts.class);
        usersAnnexToContractsCriteriaQuery.select(usersAnnexToContractsRoot);

        return em.createQuery(usersAnnexToContractsCriteriaQuery).getResultList();
    }

//    @Cacheable(Constant.USERS_ANNEXES_CACHE_KEY)
    public UsersAnnexToContracts findById(BigInteger id) {
        return this.em.find(UsersAnnexToContracts.class, id);
    }

//    @Cacheable(Constant.USERS_ANNEXES_CACHE_KEY)
    public List<UsersAnnexToContracts> findByUserId(Long userId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UsersAnnexToContracts> query = cb.createQuery(UsersAnnexToContracts.class);
        Root<UsersAnnexToContracts> root = query.from(UsersAnnexToContracts.class);
        query.select(root);
        query.where(cb.equal(root.get(UsersAnnexToContracts_.userId), userId));
        query.orderBy(cb.desc(root.get(UsersAnnexToContracts_.id)));
        return em.createQuery(query).getResultList();
    }

//    @Cacheable(Constant.USERS_ANNEXES_CACHE_KEY)
    public List<UsersAnnexToContracts> findByLogin(String login) {
        if (Objects.isNull(login)) {
            throw new RuntimeException("Необходимо передать логин пользователя");
        }
        AppUser user = userService.findByLogin(login);
        if (Objects.isNull(user)) {
            throw new RuntimeException("Пользователь с логином = [" + login + "] не найден");
        }
        return findByUserId(user.getId());
    }

//    @CacheEvict(Constant.USERS_ANNEXES_CACHE_KEY)
    public void deleteById(BigInteger id) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaDelete<UsersAnnexToContracts> delete = cb.createCriteriaDelete(UsersAnnexToContracts.class);
        Root<UsersAnnexToContracts> usersAnnexToContractsRoot = delete.from(UsersAnnexToContracts.class);
        delete.where(cb.equal(usersAnnexToContractsRoot.get(UsersAnnexToContracts_.id), id));
        this.em.createQuery(delete).executeUpdate();
    }

//    @CachePut(value = Constant.USERS_ANNEXES_CACHE_KEY, key = "#usersAnnexToContracts.id")
    public void update(UsersAnnexToContracts usersAnnexToContracts) {
        this.em.merge(usersAnnexToContracts);
    }

//    @CachePut(Constant.USERS_ANNEXES_CACHE_KEY)
    public void create(UsersAnnexToContracts usersAnnexToContracts) {
        this.em.persist(usersAnnexToContracts);
    }

    public boolean haveUnread(String login) {
        AppUser user = userService.findByLogin(login);
        if (Objects.nonNull(user)) {
            return haveUnread(user.getId());
        }
        return false;
    }

    private boolean haveUnread(Long userId) {
        return annexRepository.existsByUserIdAndDateReadIsNull(userId);
    }
}
