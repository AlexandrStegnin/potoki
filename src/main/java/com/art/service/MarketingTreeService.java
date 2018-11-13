package com.art.service;

import com.art.model.*;
import com.art.model.supporting.StatusEnum;
import com.art.repository.MarketingTreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class MarketingTreeService implements MarketingTreeRepository {

    private static double CONSTANT_DAYS = 366 * 1.5;
    private static final String INVESTOR = "Инвестор";
    private static final String ALL = "all";

    private final UserService userService;
    private final InvestorsCashService cashService;

    @PersistenceContext(name = "persistanceUnit")
    EntityManager em;

    @Autowired
    public MarketingTreeService(UserService userService, InvestorsCashService cashService) {
        this.userService = userService;
        this.cashService = cashService;
    }

    @Override
    public boolean save(final MarketingTree marketingTree) {
        em.persist(marketingTree);
        return true;
    }

    @Override
    public boolean update(MarketingTree marketingTree) {
        em.merge(marketingTree);
        return true;
    }

    @Override
    public boolean calculate(String login) {
        long plusDays = (long) CONSTANT_DAYS;
        AtomicInteger serNumber = new AtomicInteger(1);
        List<Users> users = new ArrayList<>();
        List<InvestorsCash> cashes = new ArrayList<>();
        List<MarketingTree> trees = new ArrayList<>();
        if (login.equalsIgnoreCase(ALL)) {
            users = userService.findAllWithAllFields().stream()
                    .filter(u -> u.getUserStuff().getStuff().equalsIgnoreCase(INVESTOR) &&
                            !Objects.equals(null, u.getPartnerId()))
                    .collect(Collectors.toList());
            cashes = cashService.findAllWithAllFields().stream()
                    .filter(c -> c.getGivedCash().compareTo(BigDecimal.ZERO) > 0)
                    .collect(Collectors.toList());
            trees = findAll().stream().filter(t -> !Objects.equals(null, t.getPartner())).collect(Collectors.toList());
        } else {
            users.add(userService.findByLogin(login));
            if (!users.isEmpty()) {
                cashes = cashService.findByInvestorId(users.get(0).getId());
                MarketingTree marketingTree = findByInvestorId(users.get(0).getId());
                if (marketingTree != null) {
                    trees.add(findByInvestorId(users.get(0).getId()));
                }
            }
        }

        final Map<BigInteger, Date> cashMap = cashes.stream().collect(HashMap::new,
                (m, c) -> m.putIfAbsent(c.getInvestorId(), c.getDateGivedCash()),
                (m, u) -> {
                });

        users.forEach(u -> {
            if (cashMap.containsKey(u.getId())) {
                u.setFirstInvestmentDate(cashMap.get(u.getId()));
            }
        });

        final List<MarketingTree> finalTrees = trees;
        users.forEach(u -> {
            Date firstInvestment = u.getFirstInvestmentDate();
            if (firstInvestment != null) {
                LocalDate firstInvestmentDate = firstInvestment.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (!Objects.equals(null, firstInvestment)) {
                    long elapsedDays = ChronoUnit.DAYS.between(LocalDate.now(), firstInvestmentDate.plusDays(plusDays));
                    if (elapsedDays > 0) {
                        u.setDaysToDeactivate((int) elapsedDays);
                        u.setStatus(StatusEnum.ACTIVE);
                    } else {
                        u.setDaysToDeactivate(0);
                        u.setStatus(StatusEnum.NO_ACTIVE);
                    }
                }
            }
            if (!finalTrees.isEmpty()) {
                if (finalTrees.stream().noneMatch(t -> t.getInvestor().getId().equals(u.getId()))) {
                    populateTree(u, firstInvestment, serNumber);
                } else {
                    finalTrees.forEach(t -> {
                        if (u.getId().equals(t.getInvestor().getId())) {
                            serNumber.set(getSerialNumber(t.getPartner().getPartnerId(), firstInvestment));
                            t.setDaysToDeactivate(u.getDaysToDeactivate());
                            t.setInvStatus(u.getStatus());
                            t.setKin(u.getKin());
                            t.setSerNumber(serNumber.get());
                            t.setFirstInvestmentDate(u.getFirstInvestmentDate());
                        }
                    });
                }
            } else {
                populateTree(u, firstInvestment, serNumber);
            }
        });
        finalTrees.forEach(this::update);
        return true;
    }

    @Override
    public List<MarketingTree> findAll() {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<MarketingTree> marketingTreeCriteriaQuery = cb.createQuery(MarketingTree.class);
        final Root<MarketingTree> marketingTreeRoot = marketingTreeCriteriaQuery.from(MarketingTree.class);
        marketingTreeCriteriaQuery.select(marketingTreeRoot);
        return em.createQuery(marketingTreeCriteriaQuery).getResultList();
    }

    @Override
    public MarketingTree findByInvestorId(BigInteger investorId) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<MarketingTree> marketingTreeCriteriaQuery = cb.createQuery(MarketingTree.class);
        final Root<MarketingTree> marketingTreeRoot = marketingTreeCriteriaQuery.from(MarketingTree.class);
        marketingTreeCriteriaQuery.select(marketingTreeRoot);
        marketingTreeCriteriaQuery.where(cb.equal(marketingTreeRoot.get(MarketingTree_.investor).get(Users_.id), investorId));
        return em.createQuery(marketingTreeCriteriaQuery).getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public void removeByInvestorId(BigInteger id) {
        final CriteriaBuilder cb = this.em.getCriteriaBuilder();
        final CriteriaDelete<MarketingTree> delete = cb.createCriteriaDelete(MarketingTree.class);
        final Root<MarketingTree> marketingTreeRoot = delete.from(MarketingTree.class);
        delete.where(cb.equal(marketingTreeRoot.get(MarketingTree_.investor).get(Users_.id), id));
        this.em.createQuery(delete).executeUpdate();
    }

    @Override
    public List<MarketingTree> findByPartnerIdAndFirstInvestmentDate(BigInteger partnerId, Date firstInvestmentDate) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<MarketingTree> marketingTreeCriteriaQuery = cb.createQuery(MarketingTree.class);
        final Root<MarketingTree> marketingTreeRoot = marketingTreeCriteriaQuery.from(MarketingTree.class);
        marketingTreeCriteriaQuery.select(marketingTreeRoot);
        marketingTreeCriteriaQuery.where(cb.and(cb.equal(marketingTreeRoot.get(MarketingTree_.partner).get(Users_.id), partnerId),
                cb.equal(marketingTreeRoot.get(MarketingTree_.firstInvestmentDate), firstInvestmentDate)));
        return em.createQuery(marketingTreeCriteriaQuery).getResultList();
    }

    @Override
    public int getSerialNumber(BigInteger partnerId, Date firstInvestmentDate) {
        int serialNumber = 1;
        if (firstInvestmentDate == null) return serialNumber;
        final List<MarketingTree> trees = new ArrayList<>(findByPartnerIdAndFirstInvestmentDate(partnerId, firstInvestmentDate));
        if (!trees.isEmpty()) {
            serialNumber = trees.stream().mapToInt(MarketingTree::getSerNumber).max().orElse(0);
            serialNumber++;
        }
        return serialNumber;
    }

    @Override
    public void populateTree(Users u, Date firstInvestment, AtomicInteger serNumber) {
        MarketingTree tree = new MarketingTree();
        tree.setFirstInvestmentDate(u.getFirstInvestmentDate());
        tree.setKin(u.getKin());
        tree.setInvStatus(u.getStatus());
        tree.setDaysToDeactivate(u.getDaysToDeactivate());
        tree.setInvestor(u);
        tree.setPartner(userService.findById(u.getPartnerId()));
        serNumber.set(getSerialNumber(tree.getPartner().getId(), firstInvestment));
        tree.setSerNumber(serNumber.get());
        save(tree);
    }

    @Override
    public void updateMarketingTree(BigInteger invId){
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("updateMarketingTree");
        query.setParameter("invId", invId);
        query.execute();
    }
}
