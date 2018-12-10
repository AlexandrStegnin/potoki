package com.art.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class ScheduledService {

    private MarketingTreeService marketingTreeService;

    @Autowired
    public ScheduledService(final MarketingTreeService marketingTreeService) {
        this.marketingTreeService = marketingTreeService;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void calculateMarketingTree() {
        marketingTreeService.updateMarketingTree(BigInteger.ZERO);
    }
}
