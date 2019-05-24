package com.art.repository;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

public interface CalculateInvestorShareRepository {

    List<Integer> getYearsFromInvCash();

    List<Integer> getMonths();

    void calculateInvShare(Integer yearFrom, Integer yearTo, Integer monthFrom, Integer monthTo);
}
