package com.art.service;

import com.art.model.InvestorsShare;
import com.art.model.Users;
import com.art.repository.InvestorsShareRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
@Repository
public class InvestorShareService {

    @Resource(name = "investorsShareRepository")
    private InvestorsShareRepository investorsShareRepository;

    public List<InvestorsShare> findAll() {
        return investorsShareRepository.findAll();
    }

    public InvestorsShare findById(BigInteger id) {
        return investorsShareRepository.findById(id);
    }

    public InvestorsShare update(InvestorsShare investorsShare) {
        return investorsShareRepository.saveAndFlush(investorsShare);
    }

    public void deleteById(BigInteger id) {
        investorsShareRepository.deleteById(id);
    }

    public void create(InvestorsShare investorsShare) {
        investorsShareRepository.saveAndFlush(investorsShare);
    }

    public List<InvestorsShare> findByInvestor(Users investor) {
        return investorsShareRepository.findByInvestor(investor);
    }
}
