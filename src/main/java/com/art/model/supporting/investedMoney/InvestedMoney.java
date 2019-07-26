package com.art.model.supporting.investedMoney;

import com.art.model.InvestorsCash;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Data
public class InvestedMoney {

    private String investor;

    private List<InvestorsCash> investorsCashes;

    private List<Invested> invested;

    private BigDecimal totalMoney;

    private String facilityWithMaxSum;

    private List<String> facilitiesList;

    private List<BigDecimal> sums;

}
