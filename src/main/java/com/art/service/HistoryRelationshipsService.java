package com.art.service;

import com.art.model.HistoryRelationships;
import com.art.model.supporting.InvestorsSummary;
import com.art.repository.HistoryRelationshipsRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
@Repository
public class HistoryRelationshipsService {

    @Resource(name = "historyRelationshipsRepository")
    private HistoryRelationshipsRepository historyRelationshipsRepository;

    public List<HistoryRelationships> findAll() {
        return historyRelationshipsRepository.findAll();
    }

    public HistoryRelationships create(HistoryRelationships relationships) {
        return historyRelationshipsRepository.saveAndFlush(relationships);
    }

    public List<HistoryRelationships> findByManagerIdOrRentorId(BigInteger managerId, BigInteger rentorId) {
        return historyRelationshipsRepository.findByManagerIdOrRentorId(managerId, rentorId);
    }

    public HistoryRelationships findById(BigInteger id) {
        return historyRelationshipsRepository.findOne(id);
    }

    public HistoryRelationships update(HistoryRelationships relationships) {
        return historyRelationshipsRepository.saveAndFlush(relationships);
    }

    public void deleteById(BigInteger id) {
        historyRelationshipsRepository.delete(id);
    }


    public List<InvestorsSummary> getInvestorsSummary(BigInteger investorId) {
        return historyRelationshipsRepository.getInvestorsSummary(investorId);
    }

    public List<InvestorsSummary> getInvestorsSummaryWithFacility(BigInteger investorId, String facility) {
        return historyRelationshipsRepository.getInvestorsSummaryWithFacility(investorId, facility);
    }

}
