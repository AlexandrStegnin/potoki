package com.art.service;

import com.art.model.InvestorsTypes;
import com.art.repository.InvestorsTypesRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Repository
public class InvestorsTypesService {
    @Resource(name = "investorsTypesRepository")
    private InvestorsTypesRepository investorsTypesRepository;

    public List<InvestorsTypes> findAll() {
        return investorsTypesRepository.findAll();
    }

    public InvestorsTypes findById(BigInteger id) {
        return investorsTypesRepository.findOne(id);
    }

    public InvestorsTypes update(InvestorsTypes investorsTypes) {
        return investorsTypesRepository.saveAndFlush(investorsTypes);
    }

    public void deleteById(BigInteger id) {
        investorsTypesRepository.delete(id);
    }

    public void create(InvestorsTypes investorsTypes) {
        investorsTypesRepository.saveAndFlush(investorsTypes);
    }

    public InvestorsTypes findByInvestorsTypes(String investorsType) {
        return investorsTypesRepository.findByInvestorsType(investorsType);
    }

    public List<InvestorsTypes> initializeInvestorsTypes() {
        List<InvestorsTypes> investorsTypesList = new ArrayList<>(0);
        InvestorsTypes investorsTypes = new InvestorsTypes();
        investorsTypes.setId(new BigInteger("0"));
        investorsTypes.setInvestorsType("Выберите вид инвестора");
        investorsTypesList.add(investorsTypes);
        investorsTypesList.addAll(investorsTypesRepository.findAll());
        return investorsTypesList;
    }
}
