package com.art.model.supporting.investedMoney;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Alexandr Stegnin
 */

@Data
@AllArgsConstructor
public class Invested {

    private String facility;

    private BigDecimal givenCash;

    private BigDecimal myCash;

    private BigDecimal openCash;

    private BigDecimal closedCash;

    private BigDecimal coast;

}
