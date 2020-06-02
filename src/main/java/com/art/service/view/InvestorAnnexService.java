package com.art.service.view;

import com.art.model.supporting.view.InvestorAnnex;
import com.art.repository.view.InvestorAnnexRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Service
public class InvestorAnnexService {

    private final InvestorAnnexRepository annexRepository;

    public InvestorAnnexService(InvestorAnnexRepository annexRepository) {
        this.annexRepository = annexRepository;
    }

    public List<InvestorAnnex> findAll() {
        return annexRepository.findAll();
    }

}
