package com.art.service;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledService {

    private final MarketingTreeService marketingTreeService;

    @Autowired
    public ScheduledService(final MarketingTreeService marketingTreeService) {
        this.marketingTreeService = marketingTreeService;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    @SchedulerLock(name = "ScheduledService_calculateMarketingTree",// уникальное имя задачи
            lockAtLeastFor = "PT29M", // запускать не чаще, чем раз в 29 мин
            lockAtMostFor = "PT29M") // если нода "умерла" и не отпустила локу, то держит её не более 29 мин
    public void calculateMarketingTree() {
        marketingTreeService.updateMarketingTreeFromApp();
    }
}
