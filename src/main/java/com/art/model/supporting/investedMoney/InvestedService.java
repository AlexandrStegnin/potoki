package com.art.model.supporting.investedMoney;

import com.art.model.InvestorsCash;
import com.art.service.InvestorsCashService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author Alexandr Stegnin
 */

@Service
public class InvestedService {

    private String INVESTOR_LOGIN;
    private BigInteger INVESTOR_ID;

    private static final Executor EXECUTOR = Executors.newFixedThreadPool(5);

    private final InvestorsCashService investorsCashService;

    public InvestedService(InvestorsCashService investorsCashService) {
        this.investorsCashService = investorsCashService;
    }

    private List<Invested> getInvestedList(List<InvestorsCash> investorsCashes, String investorLogin) {
        List<Invested> investedList = investorsCashes.stream()
                .filter(investorsCash -> investorsCash.getFacility() != null)
                .map(Invested::new)
                .collect(Collectors.toList());
        List<Invested> result = new ArrayList<>();
        investedList.forEach(invested -> {
            if (!result.contains(invested)) {
                result.add(invested);
            } else {
                Invested newInvested = result.get(result.indexOf(invested));
                newInvested.setGivenCash(newInvested.getGivenCash().add(invested.getGivenCash()));
                if (invested.getInvestorLogin().equalsIgnoreCase(investorLogin)) {
                    newInvested.setMyCash(newInvested.getMyCash().add(invested.getGivenCash()));
                    if (invested.getTypeClosingInvest() == null) {
                        newInvested.setOpenCash(newInvested.getOpenCash().add(invested.getGivenCash()));
                    } else {
                        newInvested.setClosedCash(newInvested.getClosedCash().add(invested.getGivenCash()));
                    }
                }
                Collections.replaceAll(result, invested, newInvested);
            }
        });
        return result;
    }

    public BigDecimal getTotalMoney(List<InvestorsCash> investorsCashes, BigInteger investorId) {
        return investorsCashes.stream()
                .filter(ic -> ic.getInvestor() != null && ic.getInvestor().getId().equals(investorId) && ic.getTypeClosingInvest() == null)
                .map(InvestorsCash::getGivedCash)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getFacilityWithMaxSum(List<Invested> investedList) {
        final String[] facility = {""};
        investedList.stream()
                .collect(Collectors.toMap(
                        Invested::getFacility,
                        Invested::getOpenCash))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(inv -> facility[0] = inv.getKey());
        return facility[0];
    }

    private List<String> getFacilitiesList(List<Invested> invested) {
        return invested
                .stream()
                .filter(element -> element.getMyCash().compareTo(BigDecimal.ZERO) > 0)
                .map(Invested::getFacility)
                .collect(Collectors.toList());
    }

    private List<BigDecimal> getSums(List<Invested> invested) {
        return invested
                .stream()
                .filter(element -> element.getMyCash().compareTo(BigDecimal.ZERO) > 0)
                .map(Invested::getMyCash)
                .collect(Collectors.toList());
    }

    public InvestedMoney getInvestedMoney(BigInteger investorId, String investorLogin) {
        long start = System.nanoTime();

        INVESTOR_LOGIN = investorLogin;
        INVESTOR_ID = investorId;

        CompletableFuture<List<InvestorsCash>> investorsCashFuture = getInvestorsCash();

        CompletableFuture<List<Invested>> investedFuture = investorsCashFuture.thenComposeAsync(this::getInvestedMoney);

        CompletableFuture<BigDecimal> totalMoneyFuture = investorsCashFuture.thenComposeAsync(this::getTotalMoney);

        CompletableFuture<String> facilityWithMaxSumFuture = investedFuture.thenComposeAsync(this::getFacilityWithMaxSumFuture);

        CompletableFuture<List<String>> facilitiesListFuture = investedFuture.thenComposeAsync(this::getFacilitiesListFuture);

        CompletableFuture<List<BigDecimal>> sumsFuture = investedFuture.thenComposeAsync(this::getSumsFuture);

        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(investorsCashFuture, investedFuture, totalMoneyFuture, facilityWithMaxSumFuture,
                facilitiesListFuture, sumsFuture);

        CompletableFuture<InvestedMoney> investedMoneyCompletableFuture = voidCompletableFuture.thenApply(v -> {
            InvestedMoney investedMoney = new InvestedMoney();
            investedMoney.setInvestor(INVESTOR_LOGIN);
            investedMoney.setInvestorsCashList(investorsCashFuture.join());
            investedMoney.setInvested(investedFuture.join());
            investedMoney.setTotalMoney(totalMoneyFuture.join());
            investedMoney.setFacilityWithMaxSum(facilityWithMaxSumFuture.join());
            investedMoney.setFacilitiesList(facilitiesListFuture.join());
            investedMoney.setSums(sumsFuture.join());
            return investedMoney;
        });
        InvestedMoney money = new InvestedMoney();
        try {
            money = investedMoneyCompletableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        long end = System.nanoTime();

        long duration = end - start;

        System.out.println("ESTIMATED TIME = " + Duration.ofNanos(duration).getSeconds() + " SECONDS");

        return money;
    }

    private CompletableFuture<List<InvestorsCash>> getInvestorsCash() {
        printThreadName(Thread.currentThread());
        return CompletableFuture.supplyAsync(() -> investorsCashService.findAll()
                .stream()
                .filter(invCash -> invCash.getFacility() != null &&
                        (invCash.getTypeClosingInvest() == null ||
                                !invCash.getTypeClosingInvest().getId().equals(new BigInteger("7"))))
                .collect(Collectors.toList()), EXECUTOR);
    }

    private CompletableFuture<List<Invested>> getInvestedMoney(List<InvestorsCash> investorsCashes) {
        printThreadName(Thread.currentThread());
        return CompletableFuture.supplyAsync(() -> getInvestedList(investorsCashes, INVESTOR_LOGIN), EXECUTOR);
    }


    private CompletableFuture<BigDecimal> getTotalMoney(List<InvestorsCash> investorsCashes) {
        printThreadName(Thread.currentThread());
        return CompletableFuture.supplyAsync(() -> getTotalMoney(investorsCashes, INVESTOR_ID), EXECUTOR);
    }

    private CompletableFuture<String> getFacilityWithMaxSumFuture(List<Invested> investedList) {
        printThreadName(Thread.currentThread());
        return CompletableFuture.supplyAsync(() -> getFacilityWithMaxSum(investedList), EXECUTOR);
    }

    private CompletableFuture<List<String>> getFacilitiesListFuture(List<Invested> investedList) {
        printThreadName(Thread.currentThread());
        return CompletableFuture.supplyAsync(() -> getFacilitiesList(investedList), EXECUTOR);
    }

    private CompletableFuture<List<BigDecimal>> getSumsFuture(List<Invested> investedList) {
        printThreadName(Thread.currentThread());
        return CompletableFuture.supplyAsync(() -> getSums(investedList), EXECUTOR);
    }

    private void printThreadName(Thread thread) {
        System.out.println("CURRENT THREAD NAME === " + thread.getName());
    }


}
