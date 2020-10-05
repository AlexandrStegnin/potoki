package com.art.model;

import com.art.model.supporting.enums.CashType;

import java.math.BigDecimal;

/**
 * @author Alexandr Stegnin
 */

public interface Cash {

    Long getId();

    BigDecimal getGivenCash();

    CashType getCashType();

}
