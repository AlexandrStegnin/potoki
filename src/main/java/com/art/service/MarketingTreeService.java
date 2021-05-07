package com.art.service;

import com.art.model.MarketingTree;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.filters.MarketingTreeFilter;
import com.art.repository.MarketingTreeRepository;
import com.art.specifications.MarketingTreeSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MarketingTreeService {

    private final MarketingTreeSpecification specification;

    private final MarketingTreeRepository marketingTreeRepository;

    @Autowired
    public MarketingTreeService(MarketingTreeSpecification specification, MarketingTreeRepository marketingTreeRepository) {
        this.marketingTreeRepository = marketingTreeRepository;
        this.specification = specification;
    }

    public Page<MarketingTree> findAll(MarketingTreeFilter filters, Pageable pageable) {
        return marketingTreeRepository.findAll(
                specification.getFilter(filters),
                pageable
        );
    }

    public ApiResponse calculate() {
        marketingTreeRepository.calculateMarketingTree();
        return new ApiResponse("Обновление маркетингового дерева завершено");
    }

}
