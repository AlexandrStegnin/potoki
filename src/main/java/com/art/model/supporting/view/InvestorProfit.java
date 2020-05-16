package com.art.model.supporting.view;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Сущность для отображения представления заработок компании в разрезе инвесторов
 *
 * @author Alexandr Stegnin
 */

@Entity
@Immutable
@Table(name = "investor_profit")
public class InvestorProfit {

    @Id
    @Column(name = "uuid", insertable = false, updatable = false)
    private UUID id;

    @Column(name = "year_sale")
    private int yearSale;

    @Column(name = "profit")
    private BigDecimal profit;

    @Column(name = "login")
    private String login;

    public int getYearSale() {
        return yearSale;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public String getLogin() {
        return login;
    }
}
