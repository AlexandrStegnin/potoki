package com.art.service;

import com.art.repository.MarketingTreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledService {

    private MarketingTreeRepository marketingTreeRepository;

    @Autowired
    public ScheduledService(final MarketingTreeRepository marketingTreeRepository) {
        this.marketingTreeRepository = marketingTreeRepository;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void calculateMarketingTree() {
        marketingTreeRepository.calculate("all");
    }
}
