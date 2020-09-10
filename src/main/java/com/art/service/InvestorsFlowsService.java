package com.art.service;

import com.art.model.InvestorsFlows;
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
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class InvestorsFlowsService {

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    private final InvestorsFlowsRepository investorsFlowsRepository;

    @Autowired
    public InvestorsFlowsService(InvestorsFlowsRepository investorsFlowsRepository) {
        this.investorsFlowsRepository = investorsFlowsRepository;
    }

//    @Cacheable(Constant.INVESTOR_FLOWS_CACHE_KEY)
    public List<InvestorsFlows> findAll() {
        return investorsFlowsRepository.findAll();
    }

    public List<InvestorsFlows> findByRoomId(Long roomId) {
        return investorsFlowsRepository.findByRoomId(roomId);
    }

//    @CacheEvict(Constant.INVESTOR_FLOWS_CACHE_KEY)
    public void delete() {
        investorsFlowsRepository.deleteAll();
    }

//    @CachePut(Constant.INVESTOR_FLOWS_CACHE_KEY)
    public void saveList(List<InvestorsFlows> investorsFlowsList) {
        investorsFlowsRepository.save(investorsFlowsList);
    }

//    @Cacheable(Constant.INVESTOR_FLOWS_CACHE_KEY)
    public List<InvestorsFlows> findByIdIn(List<Long> idList) {
        return investorsFlowsRepository.findByIdIn(idList);
    }

//    @Cacheable(Constant.INVESTOR_FLOWS_CACHE_KEY)
    public InvestorsFlows findById(BigInteger id) {
        return this.em.find(InvestorsFlows.class, id);
    }

//    @CachePut(value = Constant.INVESTOR_FLOWS_CACHE_KEY, key = "#flows.id")
    public void update(InvestorsFlows flows) {
        investorsFlowsRepository.save(flows);
    }

//    @CacheEvict(Constant.INVESTOR_FLOWS_CACHE_KEY)
    public void deleteByIdIn(List<Long> idList) {
        idList.forEach(investorsFlowsRepository::delete);
    }

//    @Cacheable(Constant.INVESTOR_FLOWS_CACHE_KEY)
    public Page<InvestorsFlows> findAllFiltering(Pageable pageable, SearchSummary filters) {
        String investor = filters.getInvestor();
        String facility = filters.getFacilityStr();
        String underFacility = filters.getUnderFacilityStr();
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
