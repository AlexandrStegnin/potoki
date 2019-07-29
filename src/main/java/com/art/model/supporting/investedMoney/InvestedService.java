package com.art.model.supporting.investedMoney;

import com.art.model.InvestorsCash;
import com.art.model.Rooms;
import com.art.service.InvestorsCashService;
import com.art.service.RoomsService;
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

    private static final Executor EXECUTOR = Executors.newFixedThreadPool(7);

    private final InvestorsCashService investorsCashService;

    private final RoomsService roomsService;

    public InvestedService(InvestorsCashService investorsCashService, RoomsService roomsService) {
        this.investorsCashService = investorsCashService;
        this.roomsService = roomsService;
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
        fillInvestedListForTables(investorsCashes, investorLogin, result);
        return result;
    }

    private void fillInvestedListForTables(List<InvestorsCash> investorsCashes, String investorLogin, List<Invested> investedList) {
        investorsCashes
                .stream()
                .filter(investorsCash -> investorsCash.getGivedCash().compareTo(BigDecimal.ZERO) > 0)
                .map(Invested::new)
                .forEach(invested -> {
                    if (!investedList.contains(invested)) {
                        investedList.add(invested);
                    } else {
                        Invested newInvested = investedList.get(investedList.indexOf(invested));
                        if (invested.getInvestorLogin().equalsIgnoreCase(investorLogin)) {
                            if (invested.getTypeClosingInvest() == null || !invested.getTypeClosingInvest().equalsIgnoreCase("Перепродажа доли")) {
                                newInvested.setIncomeCash((newInvested.getIncomeCash().add(invested.getGivenCash())).setScale(2, BigDecimal.ROUND_CEILING));
                            }
                            if (invested.getTypeClosingInvest() != null &&
                                    (invested.getTypeClosingInvest().equalsIgnoreCase("Вывод") ||
                                            invested.getTypeClosingInvest().equalsIgnoreCase("Вывод_комиссия") ||
                                            invested.getTypeClosingInvest().equalsIgnoreCase("Реинвестирование"))) {
                                newInvested.setCashing((newInvested.getCashing().add(invested.getGivenCash())).setScale(2, BigDecimal.ROUND_CEILING));
                            }
                            Collections.replaceAll(investedList, invested, newInvested);
                        }
                    }
                });
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
//        fillFacilitiesCoasts(invested);
        return invested
                .stream()
                .filter(element -> element.getMyCash().compareTo(BigDecimal.ZERO) > 0)
                .map(Invested::getMyCash)
                .collect(Collectors.toList());
    }

    private List<Invested> fillFacilitiesCoasts(List<Invested> investedList) {
        List<Rooms> rooms = roomsService.findAll();
        investedList.forEach(invested ->
                invested.setCoast(rooms.stream()
                        .filter(room -> room.getUnderFacility().getFacility().getFacility().equalsIgnoreCase(invested.getFacility()))
                        .map(Rooms::getCoast)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)));
        return investedList;
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

        CompletableFuture<List<Invested>> facilitiesCoastsFuture = investedFuture.thenComposeAsync(this::getFacilitiesCoastsFuture);

        CompletableFuture<List<BigDecimal>> sumsFuture = investedFuture.thenComposeAsync(this::getSumsFuture);

        CompletableFuture<Void> allCompletableFutures = CompletableFuture.allOf(investorsCashFuture, investedFuture, totalMoneyFuture, facilityWithMaxSumFuture,
                facilitiesListFuture, sumsFuture);

        CompletableFuture<InvestedMoney> investedMoneyCompletableFuture = allCompletableFutures.thenApply(v -> {
            InvestedMoney investedMoney = new InvestedMoney();
            investedMoney.setInvestor(INVESTOR_LOGIN);
            investedMoney.setInvestorsCashList(investorsCashFuture.join());
            investedMoney.setInvested(investedFuture.join());
            investedMoney.setTotalMoney(totalMoneyFuture.join());
            investedMoney.setFacilityWithMaxSum(facilityWithMaxSumFuture.join());
            investedMoney.setFacilitiesList(facilitiesListFuture.join());
            investedMoney.setSums(sumsFuture.join());
            facilitiesCoastsFuture.join();
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
        return CompletableFuture.supplyAsync(investorsCashService::getInvestedMoney, EXECUTOR);
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

    private CompletableFuture<List<Invested>> getFacilitiesCoastsFuture(List<Invested> investedList) {
        printThreadName(Thread.currentThread());
        return CompletableFuture.supplyAsync(() -> fillFacilitiesCoasts(investedList), EXECUTOR);
    }

    private void printThreadName(Thread thread) {
        System.out.println("CURRENT THREAD NAME === " + thread.getName());
    }


}
