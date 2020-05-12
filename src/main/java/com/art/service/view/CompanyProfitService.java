package com.art.service.view;

import com.art.model.supporting.view.CompanyProfit;
import com.art.repository.view.CompanyProfitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Service
public class CompanyProfitService {

    private final CompanyProfitRepository companyProfitRepository;

    public CompanyProfitService(CompanyProfitRepository companyProfitRepository) {
        this.companyProfitRepository = companyProfitRepository;
    }

    public List<CompanyProfit> findAll() {
        return companyProfitRepository.findAll();
    }

}
