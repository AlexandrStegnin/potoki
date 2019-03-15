package com.art.repository;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

public interface CalculateInvestorShareRepository {

    List<Integer> getYearsFromInvCash();

    void calculateInvShare(Integer yearFrom, Integer yearTo);
}
