package com.art.model.supporting.investedMoney;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alexandr Stegnin
 */

@Service
public class InvestedService {

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public List<Invested> getInvested(BigInteger investorId) {
        Query q = em.createNativeQuery("SELECT t.FACILITY, ROUND(SUM(givenCash), 2) as givenCash, " +
                "ROUND(SUM(myCash), 2) as myCash, ROUND(SUM(openCash), 2) as openCash, " +
                "ROUND(SUM(closedCash), 2) as closedCash, " +
                "IFNULL(ROUND(AVG(coast), 2), 0) coast, " +
                "FacilityId FROM (" +
                "SELECT f.FACILITY, FacilityId, InvestorId, SUM(GivedCash) givenCash, " +
                "CASE WHEN InvestorId = :investorId THEN SUM(GivedCash) ELSE 0 END myCash, " +
                "CASE WHEN InvestorId = :investorId AND TypeClosingInvestId IS NOT NULL THEN SUM(GivedCash) ELSE 0 END closedCash, " +
                "CASE WHEN InvestorId = :investorId AND TypeClosingInvestId IS NULL THEN SUM(GivedCash) ELSE 0 END openCash " +
                "FROM InvestorsCash ic " +
                "JOIN FACILITYES f ON  ic.FacilityId = f.ID " +
                "JOIN USERS u ON ic.InvestorId = u.Id " +
                "WHERE FacilityId IS NOT NULL " +
                "GROUP BY FacilityId, InvestorId, TypeClosingInvestId " +
                ") t " +
                "LEFT JOIN " +
                "(SELECT f.FACILITY, f.ID, SUM(Coast) coast FROM Rooms r " +
                "LEFT JOIN UnderFacilities uf on r.UnderFacilityId = uf.Id " +
                "LEFT JOIN FACILITYES f on uf.FacilityId = f.ID " +
                "GROUP BY f.FACILITY, f.ID " +
                "ORDER BY f.ID) cst on t.FacilityId = cst.ID " +
                "GROUP BY FACILITY, FacilityId " +
                "ORDER BY FacilityId");
        q.setParameter("investorId", investorId);
        List<Object[]> objList = q.getResultList();
        List<Invested> investedList = new ArrayList<>();
        objList.forEach(record -> investedList.add(
                new Invested((String) record[0],
                        (BigDecimal) record[1],
                        (BigDecimal) record[2],
                        (BigDecimal) record[3],
                        (BigDecimal) record[4],
                        (BigDecimal) record[5])));
        return investedList;
    }

    public BigDecimal getTotalMoney(BigInteger investorId) {
        Query q = em.createNativeQuery("SELECT SUM(GivedCash) givenCash FROM InvestorsCash " +
                "WHERE InvestorId = :investorId AND TypeClosingInvestId IS NULL");
        q.setParameter("investorId", investorId);
        return (BigDecimal) q.getSingleResult();
    }

    public String getFacilityWithMaxSum(BigInteger investorId) {
        Query q = em.createNativeQuery("SELECT FACILITY " +
                "FROM " +
                "(" +
                "SELECT FacilityId, FACILITY, SUM(GivedCash) GivenCash " +
                "FROM InvestorsCash ic " +
                "JOIN FACILITYES f on ic.FacilityId = f.ID " +
                "WHERE FacilityId IS NOT NULL AND InvestorId = :investorId " +
                "GROUP BY FacilityId, InvestorId " +
                ") t " +
                "GROUP BY FacilityId, FACILITY " +
                "ORDER BY GivenCash DESC " +
                "LIMIT 1");
        q.setParameter("investorId", investorId);
        return (String) q.getSingleResult();
    }

    public List<String> getFacilitiesList(List<Invested> invested) {
        return invested
                .stream()
                .filter(element -> element.getMyCash().compareTo(BigDecimal.ZERO) > 0)
                .map(Invested::getFacility)
                .collect(Collectors.toList());
    }

    public List<BigDecimal> getSums(List<Invested> invested) {
        return invested
                .stream()
                .filter(element -> element.getMyCash().compareTo(BigDecimal.ZERO) > 0)
                .map(Invested::getMyCash)
                .collect(Collectors.toList());
    }

}
