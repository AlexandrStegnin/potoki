package com.art.model.supporting.investedMoney;

import com.art.model.InvestorsCash;
import com.art.model.Rooms;
import com.art.service.InvestorsCashService;
import com.art.service.RoomsService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
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
                if (invested.getInvestorLogin().equalsIgnoreCase(investorLogin)) {
                    BigDecimal myCash = invested.getMyCash() == null ? new BigDecimal(BigInteger.ZERO) : invested.getMyCash();
                    BigDecimal givenCash = invested.getGivenCash() == null ? new BigDecimal(BigInteger.ZERO) : invested.getGivenCash();
                    BigDecimal openCash = invested.getOpenCash() == null ? new BigDecimal(BigInteger.ZERO) : invested.getOpenCash();
                    BigDecimal closedCash = invested.getClosedCash() == null ? new BigDecimal(BigInteger.ZERO) : invested.getClosedCash();

                    invested.setMyCash(myCash.add(givenCash));
                    if (invested.getTypeClosingInvest() == null) {
                        invested.setOpenCash(openCash.add(givenCash));
                    } else {
                        invested.setClosedCash(closedCash.add(givenCash));
                    }
                }
                result.add(invested);
            } else {
                Invested newInvested = result.get(result.indexOf(invested));

                BigDecimal newMyCash = newInvested.getMyCash() == null ? new BigDecimal(BigInteger.ZERO) : newInvested.getMyCash();
                BigDecimal givenCash = invested.getGivenCash() == null ? new BigDecimal(BigInteger.ZERO) : invested.getGivenCash();
                BigDecimal newGivenCash = newInvested.getGivenCash() == null ? new BigDecimal(BigInteger.ZERO) : newInvested.getGivenCash();
                BigDecimal newOpenCash = newInvested.getOpenCash() == null ? new BigDecimal(BigInteger.ZERO) : newInvested.getOpenCash();
                BigDecimal newClosedCash = newInvested.getClosedCash() == null ? new BigDecimal(BigInteger.ZERO) : newInvested.getClosedCash();

                newInvested.setGivenCash(newGivenCash.add(givenCash));
                if (invested.getInvestorLogin().equalsIgnoreCase(investorLogin)) {
                    newInvested.setMyCash(newMyCash.add(givenCash));
                    if (invested.getTypeClosingInvest() == null) {
                        newInvested.setOpenCash(newOpenCash.add(givenCash));
                    } else {
                        newInvested.setClosedCash(newClosedCash.add(givenCash));
                    }
                }
                Collections.replaceAll(result, invested, newInvested);
            }
        });
        fillInvestedListForTables(investorsCashes, investorLogin, result);
        return result;
    }

    private void fillInvestedListForTables(List<InvestorsCash> investorsCashes, String investorLogin, List<Invested> investedList) {
        List<Invested> newInvestedList;
        newInvestedList = investorsCashes
                .stream()
                .filter(investorsCash -> investorsCash.getGivedCash() != null && investorsCash.getGivedCash().compareTo(BigDecimal.ZERO) > 0)
                .map(Invested::new)
                .collect(Collectors.toList());
        newInvestedList
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
                .filter(element -> element != null && element.getMyCash().compareTo(BigDecimal.ZERO) > 0)
                .map(Invested::getFacility)
                .collect(Collectors.toList());
    }

    private List<BigDecimal> getSums(List<Invested> invested) {
        return invested
                .stream()
                .filter(element -> element != null && element.getMyCash().compareTo(BigDecimal.ZERO) > 0)
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

        return money;
    }

    private CompletableFuture<List<InvestorsCash>> getInvestorsCash() {
        return CompletableFuture.supplyAsync(investorsCashService::getInvestedMoney, EXECUTOR);
    }

    private CompletableFuture<List<Invested>> getInvestedMoney(List<InvestorsCash> investorsCashes) {
        return CompletableFuture.supplyAsync(() -> getInvestedList(investorsCashes, INVESTOR_LOGIN), EXECUTOR);
    }


    private CompletableFuture<BigDecimal> getTotalMoney(List<InvestorsCash> investorsCashes) {
        return CompletableFuture.supplyAsync(() -> getTotalMoney(investorsCashes, INVESTOR_ID), EXECUTOR);
    }

    private CompletableFuture<String> getFacilityWithMaxSumFuture(List<Invested> investedList) {
        return CompletableFuture.supplyAsync(() -> getFacilityWithMaxSum(investedList), EXECUTOR);
    }

    private CompletableFuture<List<String>> getFacilitiesListFuture(List<Invested> investedList) {
        return CompletableFuture.supplyAsync(() -> getFacilitiesList(investedList), EXECUTOR);
    }

    private CompletableFuture<List<BigDecimal>> getSumsFuture(List<Invested> investedList) {
        return CompletableFuture.supplyAsync(() -> getSums(investedList), EXECUTOR);
    }

    private CompletableFuture<List<Invested>> getFacilitiesCoastsFuture(List<Invested> investedList) {
        return CompletableFuture.supplyAsync(() -> fillFacilitiesCoasts(investedList), EXECUTOR);
    }

}
