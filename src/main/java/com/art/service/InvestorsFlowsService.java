package com.art.service;

import com.art.model.*;
import com.art.model.supporting.SearchSummary;
import com.art.repository.InvestorsFlowsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@Transactional
public class InvestorsFlowsService {
    public static final int PAGE_SIZE = 100;

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    private final InvestorsFlowsRepository investorsFlowsRepository;
    private final FacilityService facilityService;
    private final UnderFacilitiesService underFacilitiesService;
    private final UserService userService;

    @Autowired
    public InvestorsFlowsService(FacilityService facilityService,
                                 InvestorsFlowsRepository investorsFlowsRepository,
                                 UnderFacilitiesService underFacilitiesService,
                                 UserService userService) {
        this.facilityService = facilityService;
        this.investorsFlowsRepository = investorsFlowsRepository;
        this.underFacilitiesService = underFacilitiesService;
        this.userService = userService;
    }

    public List<InvestorsFlows> findAll() {
        return investorsFlowsRepository.findAll();
    }

    public Page<InvestorsFlows> findAll(Pageable pageable, SearchSummary filter) {
        return investorsFlowsRepository.findAll(pageable);
    }

    public List<InvestorsFlows> findByRoomId(BigInteger roomId) {
        return investorsFlowsRepository.findByRoomId(roomId);
    }

    public void delete() {
        investorsFlowsRepository.deleteAll();
    }

    public void saveList(List<InvestorsFlows> investorsFlowsList) {
        investorsFlowsRepository.save(investorsFlowsList);
    }

    public List<InvestorsFlows> findByInvestorId(Long investorId) {
        return investorsFlowsRepository.findByInvestorId(investorId);
    }

    public List<InvestorsFlows> findByIdIn(List<BigInteger> idList) {
        return investorsFlowsRepository.findByIdIn(idList);
    }

    public InvestorsFlows findById(BigInteger id) {
        return this.em.find(InvestorsFlows.class, id);
    }

    public void update(InvestorsFlows flows) {
        CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
        CriteriaUpdate<InvestorsFlows> update = criteriaBuilder.createCriteriaUpdate(InvestorsFlows.class);
        Root<InvestorsFlows> flowsRoot = update.from(InvestorsFlows.class);
        update.set(InvestorsFlows_.isReinvest, flows.getIsReinvest());
        update.where(criteriaBuilder.equal(flowsRoot.get(InvestorsFlows_.id), flows.getId()));
        this.em.createQuery(update).executeUpdate();
    }

    public void deleteByIdIn(List<BigInteger> idList) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<InvestorsFlows> query = cb.createCriteriaDelete(InvestorsFlows.class);
        Root<InvestorsFlows> root = query.from(InvestorsFlows.class);
        query.where(root.get(InvestorsFlows_.id).in(idList));
        em.createQuery(query).executeUpdate();
    }

    public List<InvestorsFlows> findByInvestorIdWithFacilitiesUnderFacilities(BigInteger investorId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsFlows> flowsCriteriaQuery = cb.createQuery(InvestorsFlows.class);
        Root<InvestorsFlows> flowsRoot = flowsCriteriaQuery.from(InvestorsFlows.class);
        flowsRoot.fetch(InvestorsFlows_.facility, JoinType.LEFT);
        //.fetch(Facilities_.underFacilities, JoinType.LEFT);
        flowsCriteriaQuery.select(flowsRoot).distinct(true);
        flowsCriteriaQuery.where(cb.equal(flowsRoot.get(InvestorsFlows_.investor).get(Users_.id), investorId));
        return em.createQuery(flowsCriteriaQuery).getResultList();
    }

    public List<InvestorsFlows> findByPageNumber(int pageNumber) {

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<InvestorsFlows> criteriaQuery = cb
                .createQuery(InvestorsFlows.class);
        Root<InvestorsFlows> from = criteriaQuery.from(InvestorsFlows.class);
        CriteriaQuery<InvestorsFlows> select = criteriaQuery.select(from);

        TypedQuery<InvestorsFlows> typedQuery = em.createQuery(select);
        typedQuery.setFirstResult(pageNumber - 1);
        typedQuery.setMaxResults(PAGE_SIZE - 1);
        return typedQuery.getResultList();
    }

    public Map<Integer, List<InvestorsFlows>> findAllWithPagination(int pageNumber, SearchSummary searchSummary) {
        List<Predicate> predicates = new ArrayList<>();

        Map<Integer, List<InvestorsFlows>> result = new HashMap<>(1);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InvestorsFlows> criteriaQuery = cb
                .createQuery(InvestorsFlows.class);

        Root<InvestorsFlows> from = criteriaQuery.from(InvestorsFlows.class);

        Facility facility = searchSummary.getFacility();
        if (!Objects.equals(null, facility)) {
            facility = facilityService.findById(facility.getId());
            predicates.add(cb.like(from.get(InvestorsFlows_.facility).get(Facility_.name), facility.getName()));
        }

        UnderFacilities underFacility = searchSummary.getUnderFacilities();
        if (!Objects.equals(null, underFacility)) {
            underFacility = underFacilitiesService.findById(underFacility.getId());
            predicates.add(cb.like(from.get(InvestorsFlows_.underFacilities)
                    .get(UnderFacilities_.underFacility), underFacility.getUnderFacility()));
        }

        Users investor = searchSummary.getUser();
        if (!Objects.equals(null, investor)) {
            investor = userService.findById(investor.getId());
            predicates.add(cb.like(from.get(InvestorsFlows_.investor)
                    .get(Users_.login), investor.getLogin()));
        }

        Date dateFrom = searchSummary.getDateStart();
        if (!Objects.equals(null, dateFrom)) {
            predicates.add(cb.greaterThanOrEqualTo(from.get(InvestorsFlows_.reportDate), dateFrom));
        }

        Date dateTo = searchSummary.getDateEnd();
        if (!Objects.equals(null, dateTo)) {
            predicates.add(cb.lessThanOrEqualTo(from.get(InvestorsFlows_.reportDate), dateTo));
        }

        CriteriaQuery<Long> countQuery = cb
                .createQuery(Long.class);
        countQuery.select(cb
                .count(countQuery.from(InvestorsFlows.class)));
        Long count = em.createQuery(countQuery)
                .getSingleResult();

        CriteriaQuery<InvestorsFlows> select = criteriaQuery.select(from);
        select.where(predicates.toArray(new Predicate[0]));
        TypedQuery<InvestorsFlows> typedQuery = em.createQuery(select);
        int pageCount = count.intValue() / PAGE_SIZE;
        typedQuery.setFirstResult(pageNumber - 1);
        typedQuery.setMaxResults(PAGE_SIZE - 1);
        result.put(pageCount, typedQuery.getResultList());
        return result;
    }

    public Page<InvestorsFlows> findAllFiltering(Pageable pageable, SearchSummary filters) {
        String investor = filters.getInvestor();
        String facility = filters.getFacilityStr();
        String underFacility = filters.getUnderFacility();
        LocalDate startDate = filters.getStartDate();
        LocalDate endDate = filters.getEndDate();
        Date start = null;
        Date end = null;
        if (!Objects.equals(null, startDate)) start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        if (!Objects.equals(null, endDate)) end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        if (!Objects.equals(null, investor) && investor.equalsIgnoreCase("Выберите инвестора")) investor = null;
        if (!Objects.equals(null, facility) && facility.equalsIgnoreCase("Выберите объект")) facility = null;
        if (!Objects.equals(null, underFacility) && underFacility.equalsIgnoreCase("Выберите подобъект")) underFacility = null;

        return investorsFlowsRepository.findFiltering(pageable, investor, facility, underFacility, start, end);
    }

    public void updateInvestorDemo() {
        Query q = em.createNativeQuery("{call UPDATE_INVESTOR_DEMO()}");
        q.executeUpdate();
    }
}
