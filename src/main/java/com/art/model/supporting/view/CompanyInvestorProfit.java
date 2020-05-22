package com.art.model.supporting.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Сущность для отображения представления заработок компании по годам и по всем инвесторам
 *
 * @author Alexandr Stegnin
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyInvestorProfit {

    private int yearSale;

    private BigDecimal profit;

    private String login;

    private BigDecimal investorProfit;

    public int getYearSale() {
        return yearSale;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public String getLogin() {
        return login;
    }

    public BigDecimal getInvestorProfit() {
        return investorProfit;
    }
}
