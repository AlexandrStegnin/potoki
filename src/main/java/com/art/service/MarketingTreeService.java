package com.art.service;

import com.art.model.MarketingTree;
import com.art.model.Users;
import com.art.model.supporting.StatusEnum;
import com.art.model.supporting.dto.MarketingTreeDTO;
import com.art.model.supporting.filters.MarketingTreeFilter;
import com.art.repository.MarketingTreeRepository;
import com.art.specifications.MarketingTreeSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class MarketingTreeService {

    private final static String KOLESNIK_LOGIN = "investor077";
    private final static String PANTYA_LOGIN = "investor008";

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    private final EntityManagerFactory emf;
    private final MarketingTreeSpecification specification;
    private final MarketingTreeRepository marketingTreeRepository;

    private BigInteger pantyaId;
    private BigInteger kolesnikId;
    private final List<Users> users;
    private static AtomicReference<BigInteger> parentPartnerId = new AtomicReference<>(BigInteger.ZERO);
    private static Set<BigInteger> partnerChild = new HashSet<>();
    private List<MarketingTreeDTO> marketingTreeDTOList = new ArrayList<>();

    @Autowired
    public MarketingTreeService(MarketingTreeSpecification specification, MarketingTreeRepository marketingTreeRepository,
                                EntityManagerFactory emf, UserService userService) {
        this.marketingTreeRepository = marketingTreeRepository;
        this.specification = specification;
        this.emf = emf;
        this.pantyaId = userService.findByLogin(PANTYA_LOGIN).getId();
        this.kolesnikId = userService.findByLogin(KOLESNIK_LOGIN).getId();
        this.users = userService.getForFindPartnerChild();
    }

    public List<MarketingTree> findAll() {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<MarketingTree> marketingTreeCriteriaQuery = cb.createQuery(MarketingTree.class);
        final Root<MarketingTree> marketingTreeRoot = marketingTreeCriteriaQuery.from(MarketingTree.class);
        marketingTreeCriteriaQuery.select(marketingTreeRoot);
        return em.createQuery(marketingTreeCriteriaQuery).getResultList();
    }

    public Page<MarketingTree> findAll(MarketingTreeFilter filters, Pageable pageable) {
        return marketingTreeRepository.findAll(
                specification.getFilter(filters),
                pageable
        );
    }

    public String updateMarketingTreeFromApp() {
        CompletableFuture<Void> getMarketingTreeDTOListFuture = CompletableFuture
                .runAsync(this::fillMarketingTreeDTOList);
        getMarketingTreeDTOListFuture.thenRunAsync(this::setDaysToDeactivate);
        getMarketingTreeDTOListFuture.thenRunAsync(this::setSerialNumbers);

        getMarketingTreeDTOListFuture.thenRunAsync(() -> findPartnerChild(kolesnikId, users))
                .thenRunAsync(this::fillParentPartner);

        getMarketingTreeDTOListFuture.join();

        truncateTree();
        updateTree();

        return "Маркетинговое дерево успешно обновлено";
    }

    private void fillMarketingTreeDTOList() {
        marketingTreeDTOList = marketingTreeRepository.findGroupedCash();
    }

    private void setDaysToDeactivate() {
        marketingTreeDTOList.forEach(marketingTreeDTO -> {
            long daysToAdd = (new Double(366 * 1.5)).longValue();
            Date minDate = marketingTreeDTO.getFirstInvestmentDate();
            LocalDate minDateInLocalDate = minDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate inFuture = minDateInLocalDate.plusDays(daysToAdd);
            LocalDate current = LocalDate.now();
            int diff = (int) DAYS.between(current, inFuture);
            int daysToDeactivate = Math.max(diff, 0);
            marketingTreeDTO.setDaysToDeactivate(daysToDeactivate);
            marketingTreeDTO.setInvStatus(setStatus(daysToDeactivate));
        });
    }

    private String setStatus(int daysToDeactivate) {
        return daysToDeactivate > 0 ?
                StatusEnum.ACTIVE.name() :
                StatusEnum.NO_ACTIVE.name();
    }

    private void setSerialNumbers() {
        final BigInteger[] firstPartnerId = {marketingTreeDTOList.get(0).getPartnerId()};
        final AtomicInteger[] serialNumber = {new AtomicInteger(1)};
        marketingTreeDTOList.forEach(marketingTreeDTO -> {
            BigInteger currPartnerId = marketingTreeDTO.getPartnerId();
            if (firstPartnerId[0].equals(currPartnerId)) {
                marketingTreeDTO.setSerNumber(serialNumber[0].get());
                serialNumber[0].getAndIncrement();
            } else {
                firstPartnerId[0] = currPartnerId;
                serialNumber[0].set(1);
                marketingTreeDTO.setSerNumber(serialNumber[0].get());
                serialNumber[0].getAndIncrement();
            }
        });
    }

    private void truncateTree() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        Query query = entityManager.createNativeQuery(
                "TRUNCATE TABLE MarketingTree;");
        tx.begin();
        query.executeUpdate();
        tx.commit();
        entityManager.close();
    }

    private void updateTree() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        marketingTreeDTOList.forEach(marketingTreeDTO -> {
            Query query = entityManager.createNativeQuery(
                    "INSERT INTO MarketingTree (InvestorId, PartnerId, Kin, FirstInvestmentDate, " +
                            "InvStatus, DaysToDeactivate, SerNumber, ParentPartnerId) " +
                            "VALUES (:investorId, :partnerId, :kin, :firstInvestmentDate, :invStatus, :daysToDeactivate, " +
                            ":serNumber, :parentPartnerId)");
            tx.begin();
            query
                    .setParameter("investorId", marketingTreeDTO.getInvestorId())
                    .setParameter("partnerId", marketingTreeDTO.getPartnerId())
                    .setParameter("kin", marketingTreeDTO.getKin().name())
                    .setParameter("firstInvestmentDate", marketingTreeDTO.getFirstInvestmentDate())
                    .setParameter("invStatus", marketingTreeDTO.getInvStatus())
                    .setParameter("daysToDeactivate", marketingTreeDTO.getDaysToDeactivate())
                    .setParameter("serNumber", marketingTreeDTO.getSerNumber())
                    .setParameter("parentPartnerId", marketingTreeDTO.getParentPartnerId())
                    .executeUpdate();
            tx.commit();
        });
        entityManager.close();
    }

    private void findParentPartner(BigInteger investorId, List<Users> users) {
        Map<BigInteger, Set<BigInteger>> result =
                users.stream().collect(
                        Collectors.groupingBy(Users::getPartnerId,
                                Collectors.mapping(Users::getId, Collectors.toSet())
                        )
                );
        result.forEach((k, v) -> {
            if (v.contains(investorId)) {
                parentPartnerId.set(k);
                findParentPartner(k, users);
            }
        });
    }

    private void findPartnerChild(BigInteger partnerId, List<Users> availableUsers) {
        Queue<BigInteger> queue = new LinkedList<>();
        queue.add(partnerId);
        while (!queue.isEmpty()) {
            partnerId = queue.poll();
            if (partnerId == null) break;
            BigInteger finalPartnerId = partnerId;
            List<Users> users = availableUsers.stream()
                    .filter(user -> user.getPartnerId().compareTo(finalPartnerId) == 0)
                    .filter(user -> !queue.contains(user.getId()))
                    .filter(user -> !partnerChild.contains(user.getId()))
                    .filter(user -> user.getPartnerId().compareTo(pantyaId) != 0)
                    .collect(Collectors.toList());
            if (users.size() > 0) {
                queue.addAll(users.stream().map(Users::getId).collect(Collectors.toList()));
                partnerChild.addAll(users.stream().map(Users::getId).collect(Collectors.toList()));
            }
        }
    }

    private void fillParentPartner() {
        marketingTreeDTOList.forEach(marketingTreeDTO -> {
            if (partnerChild.contains(marketingTreeDTO.getInvestorId()) ||
                    partnerChild.contains(marketingTreeDTO.getPartnerId())) {
                marketingTreeDTO.setParentPartnerId(kolesnikId);
            } else {
                marketingTreeDTO.setParentPartnerId(pantyaId);
            }
        });
    }

}
