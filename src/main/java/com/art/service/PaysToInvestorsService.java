package com.art.service;

import com.art.model.supporting.PaysToInvestors;
import com.art.repository.PaysToInvestorsRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
@Repository
public class PaysToInvestorsService {

    @Resource(name = "paysToInvestorsRepository")
    private PaysToInvestorsRepository paysToInvestorsRepository;

    public List<PaysToInvestors> findByInvestorIdAndFacility(BigInteger investorId,
                                                             String facility) {
        return paysToInvestorsRepository.findByInvestorIdAndFacility(investorId,
                facility);
    }

    public List<PaysToInvestors> findAll() {
        return paysToInvestorsRepository.findAll();
    }

    public void saveList(List<PaysToInvestors> paysToInvestorsList) {
        paysToInvestorsRepository.save(paysToInvestorsList);
    }
}
