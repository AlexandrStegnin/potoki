package com.art.service;

import com.art.model.InvestorsExpenses;
import com.art.model.supporting.InvestorsPlanSale;
import com.art.repository.InvestorsExpensesRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
@Repository
public class InvestorsExpensesService {

    @Resource(name = "investorsExpensesRepository")
    private InvestorsExpensesRepository investorsExpensesRepository;

    public InvestorsExpenses create(InvestorsExpenses investorsExpenses){
        return investorsExpensesRepository.saveAndFlush(investorsExpenses);
    }

    public List<InvestorsExpenses> findAll(){
        return investorsExpensesRepository.findAll();
    }

    public InvestorsExpenses findById(BigInteger id){
        return investorsExpensesRepository.findOne(id);
    }

    public InvestorsExpenses update(InvestorsExpenses investorsExpenses){
        return investorsExpensesRepository.saveAndFlush(investorsExpenses);
    }

    public void deleteById(BigInteger id){
        investorsExpensesRepository.delete(id);
    }

    public List<InvestorsPlanSale> getInvestorsPlanSale(BigInteger investorId){
        return investorsExpensesRepository.getInvestorsPlanSale(investorId);
    }

    public List<InvestorsExpenses> findByInvestorId(BigInteger investorId){
        return investorsExpensesRepository.findByInvestor_Id(investorId);
    }

}
