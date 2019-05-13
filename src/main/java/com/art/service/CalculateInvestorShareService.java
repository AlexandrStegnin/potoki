package com.art.service;

import com.art.repository.CalculateInvestorShareRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

/**
 * @author Alexandr Stegnin
 */

@Service
public class CalculateInvestorShareService implements CalculateInvestorShareRepository {

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public List<Integer> getYearsFromInvCash() {
        return (List<Integer>) em.createNativeQuery(
                "SELECT YEAR(c.DateGivedCash) FROM InvestorsCash c GROUP BY YEAR(c.DateGivedCash) ORDER BY YEAR(c.DateGivedCash)")
                .getResultList();
    }

    @Transactional
    public void calculateInvShare(Integer yearFrom, Integer yearTo) {
        if (Objects.equals(null, yearFrom) && Objects.equals(null, yearTo)) {
            List<LocalDate> dateRange = getDatesFromYearsRange(getYearsFromInvCash());
            Query truncate = em.createNativeQuery("TRUNCATE TABLE InvestorShare");
            truncate.executeUpdate();
            Query q = em.createNativeQuery("{call CALCULATEINVESTORSHARE(:REP_DATE)}");
            dateRange.forEach(date -> {
                q.setParameter("REP_DATE", date.toString());
                q.executeUpdate();
            });
        } else {
            Query q = em.createNativeQuery("{call CalculateInvShareByPeriod(:YEAR_FROM, :YEAR_TO)}");
            q.setParameter("YEAR_FROM", yearFrom);
            q.setParameter("YEAR_TO", yearTo);
            q.executeUpdate();
        }
    }

    private List<LocalDate> getDatesFromYearsRange(List<Integer> yearsList) {
        LocalDate start = LocalDate.ofYearDay(yearsList.get(0), 31);
        LocalDate end = LocalDate.now().with(lastDayOfMonth());
        return Stream.iterate(start, date -> date.plusMonths(1).with(lastDayOfMonth()))
                .limit(ChronoUnit.MONTHS.between(start, end))
                .collect(Collectors.toList());
    }

}
