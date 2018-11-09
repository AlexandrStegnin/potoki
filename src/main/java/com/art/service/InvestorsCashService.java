package com.art.service;

import com.art.model.*;
import com.art.model.supporting.InvestorsTotalSum;
import com.art.model.supporting.PaginationResult;
import com.art.repository.InvestorsCashRepository;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional
@Repository
public class InvestorsCashService {

    @Resource(name = "investorsCashRepository")
    private InvestorsCashRepository investorsCashRepository;

    public List<InvestorsCash> findAll() {
        return investorsCashRepository.findAll();
    }

    public InvestorsCash findById(BigInteger id) {
        return investorsCashRepository.findById(id);
    }

    public InvestorsCash update(InvestorsCash investorsCash) {
        return investorsCashRepository.saveAndFlush(investorsCash);
    }

    public void deleteById(BigInteger id) {
        investorsCashRepository.deleteById(id);
    }


    public List<InvestorsTotalSum> getInvestorsTotalSum(BigInteger investorId) {
        return investorsCashRepository.getInvestorsTotalSum(investorId);
    }

    public List<InvestorsTotalSum> getInvestorsTotalSumDetails(BigInteger investorId) {
        return investorsCashRepository.getInvestorsTotalSumDetails(investorId);
    }

    public List<InvestorsTotalSum> getInvestorsCashSums(BigInteger investorId) {
        return investorsCashRepository.getInvestorsCashSums(investorId);
    }

    public List<InvestorsTotalSum> getInvestorsCashSumsDetails(BigInteger investorId) {
        return investorsCashRepository.getInvestorsCashSumsDetails(investorId);
    }

    public List<InvestorsCash> findAllOrderByDateGivedCashAsc() {
        return investorsCashRepository.findAllByOrderByDateGivedCashAsc();
    }

    public void saveAll(List<InvestorsCash> investorsCashes) {
        investorsCashRepository.save(investorsCashes);
    }

    public List<InvestorsCash> findByRoomId(BigInteger roomId) {
        return investorsCashRepository.findByRoomId(roomId);
    }

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    public InvestorsCash create(InvestorsCash investorsCash) {
        InvestorsCash cash = this.em.merge(investorsCash);
        return cash;
    }

    public PaginationResult getAllWithPageable(int pageNumber, Map<String, String> search) {
        String facilityId = search.get("facility");
        String underFacility = search.get("underFacility");
        String investor = search.get("investor");

        String dateBegin = search.get("dateBegin");

        Date dateBeg = null;
        if (!Objects.equals(null, dateBegin) && !Objects.equals(0, dateBegin.length())) {
            dateBeg = new Date(Long.parseLong(dateBegin));
        }

        String dateEnd = search.get("dateEnd");

        Date dateEnded = null;
        if (!Objects.equals(null, dateEnd) && !Objects.equals(0, dateEnd.length())) {
            dateEnded = new Date(Long.parseLong(dateEnd));
        }
        String sql = "SELECT ic FROM InvestorsCash ic ";
        StringBuilder builder = new StringBuilder("WHERE ");
        if (Objects.equals(null, facilityId) || Objects.equals(0, facilityId.length())) {
            builder.append("ic.facility is not null ");
        } else {
            builder.append("ic.facility.id = ").append(facilityId).append(" ");
        }
        if (!Objects.equals(null, underFacility) && !Objects.equals(0, underFacility.length())) {
            builder.append("AND underFacility.underFacility = '").append(underFacility).append("' ");
        }
        if (!Objects.equals(null, investor) && !Objects.equals(0, investor.length())) {
            builder.append("AND investor.id = ").append(investor).append(" ");
        }
        if (!Objects.equals(null, dateBegin) && !Objects.equals(0, dateBegin.length())) {
            builder.append("AND dateGivedCash >= '").append(dateBeg).append("' ");
        }
        if (!Objects.equals(null, dateEnd) && !Objects.equals(0, dateBegin.length())) {
            builder.append("AND dateGivedCash <= '").append(dateEnded).append("' ");
        }

        String orderBy = "order by ic.id";
        sql = sql.concat(builder.toString()).concat(orderBy);

        Session session = em.unwrap(Session.class);
        Query query = session.createQuery(sql);
        int maxResult = 100;
        int maxNavigationResult = 20;
        return new PaginationResult<InvestorsCash>(query, pageNumber, maxResult, maxNavigationResult);
    }

    public List<InvestorsCash> findByIdIn(List<BigInteger> idList) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsCash> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashRoot.fetch(InvestorsCash_.typeClosingInvest, JoinType.LEFT);
        investorsCashCriteriaQuery.select(investorsCashRoot);
        investorsCashCriteriaQuery.where(investorsCashRoot.get(InvestorsCash_.id).in(idList));
        return em.createQuery(investorsCashCriteriaQuery).getResultList();
    }

    public List<InvestorsCash> findBySource(String source) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsCash> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashRoot.fetch(InvestorsCash_.typeClosingInvest, JoinType.LEFT);
        investorsCashCriteriaQuery.select(investorsCashRoot);
        investorsCashCriteriaQuery.where(cb.equal(investorsCashRoot.get(InvestorsCash_.source), source));
        return em.createQuery(investorsCashCriteriaQuery).getResultList();
    }

    public List<InvestorsCash> findBySourceId(BigInteger sourceId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsCash> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashRoot.fetch(InvestorsCash_.typeClosingInvest, JoinType.LEFT);
        investorsCashCriteriaQuery.select(investorsCashRoot);
        investorsCashCriteriaQuery.where(cb.equal(investorsCashRoot.get(InvestorsCash_.sourceId), sourceId));
        return em.createQuery(investorsCashCriteriaQuery).getResultList();
    }

    public List<InvestorsCash> findByInvestorAndFacility(BigInteger investorId, BigInteger facilityId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsCash> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashCriteriaQuery.select(investorsCashRoot);
        investorsCashCriteriaQuery.where(cb.and(cb.equal(investorsCashRoot.get(InvestorsCash_.investorId), investorId),
                cb.equal(investorsCashRoot.get(InvestorsCash_.facilityId), facilityId),
                cb.gt(investorsCashRoot.get(InvestorsCash_.givedCash), 0),
                cb.isNull(investorsCashRoot.get(InvestorsCash_.typeClosingInvest))));
        return em.createQuery(investorsCashCriteriaQuery).getResultList();
    }

    public List<InvestorsCash> findByInvestorAndFacilityAndUnderFacility(BigInteger investorId, BigInteger facilityId, BigInteger underFacilityId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsCash> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashCriteriaQuery.select(investorsCashRoot);
        investorsCashCriteriaQuery.where(
                cb.and(
                        cb.equal(investorsCashRoot.get(InvestorsCash_.investorId), investorId),
                        cb.equal(investorsCashRoot.get(InvestorsCash_.facilityId), facilityId),
                        cb.equal(investorsCashRoot.get(InvestorsCash_.underFacility).get(UnderFacilities_.id), underFacilityId),
                        cb.gt(investorsCashRoot.get(InvestorsCash_.givedCash), 0),
                        cb.isNull(investorsCashRoot.get(InvestorsCash_.typeClosingInvest))));
        return em.createQuery(investorsCashCriteriaQuery).getResultList();
    }

    public List<InvestorsCash> findByInvestorId(BigInteger investorId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsCash> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashRoot.fetch(InvestorsCash_.underFacility, JoinType.LEFT);
        investorsCashCriteriaQuery.select(investorsCashRoot).distinct(true);
        investorsCashCriteriaQuery.where(cb.equal(investorsCashRoot.get(InvestorsCash_.investorId), investorId));
        investorsCashCriteriaQuery.orderBy(cb.asc(investorsCashRoot.get(InvestorsCash_.dateGivedCash)));
        return em.createQuery(investorsCashCriteriaQuery).getResultList();
    }

    public Facilities getSumCash(BigInteger investorId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsCash> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashCriteriaQuery.multiselect(investorsCashRoot.get(InvestorsCash_.facility),
                cb.sum(investorsCashRoot.get(InvestorsCash_.givedCash)));
        investorsCashCriteriaQuery.where(cb.equal(investorsCashRoot.get(InvestorsCash_.investor).get(Users_.id), investorId));
        investorsCashCriteriaQuery.groupBy(investorsCashRoot.get(InvestorsCash_.facility));
        //investorsCashCriteriaQuery.orderBy(cb.desc(investorsCashRoot.get(InvestorsCash_.givedCash)));
        List<InvestorsCash> cash = em.createQuery(investorsCashCriteriaQuery).getResultList();
        final Facilities[] facility = {new Facilities()};
        final BigDecimal[] max = {new BigDecimal(BigInteger.ZERO)};
        cash.forEach(c -> {
            c.setGivedCash(c.getGivedCash().stripTrailingZeros());
            if (c.getGivedCash().compareTo(max[0]) > 0) {
                max[0] = c.getGivedCash();
                facility[0] = c.getFacility();
            }
        });
        return facility[0];
    }

    public List<InvestorsCash> findAllWithAllFields() {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<InvestorsCash> investorsCashCriteriaQuery = cb.createQuery(InvestorsCash.class);
        Root<InvestorsCash> investorsCashRoot = investorsCashCriteriaQuery.from(InvestorsCash.class);
        investorsCashRoot.fetch(InvestorsCash_.facility, JoinType.LEFT);
        investorsCashCriteriaQuery.select(investorsCashRoot).distinct(true);
        investorsCashCriteriaQuery.where(cb.isNotNull(investorsCashRoot.get(InvestorsCash_.facility)));
        investorsCashCriteriaQuery.orderBy(cb.asc(investorsCashRoot.get(InvestorsCash_.dateGivedCash)));
        List<InvestorsCash> cash = em.createQuery(investorsCashCriteriaQuery).getResultList();
        cash.forEach(c -> {
            Hibernate.initialize(c.getInvestor());
            Hibernate.initialize(c.getFacility());
            Hibernate.initialize(c.getUnderFacility());
            Hibernate.initialize(c.getRoom());
        });

        return cash;
    }
}
