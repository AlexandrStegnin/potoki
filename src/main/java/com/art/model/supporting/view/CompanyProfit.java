package com.art.model.supporting.view;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Сущность для отображения представления заработок компании для инвесторов
 *
 * @author Alexandr Stegnin
 */

@Entity
@Immutable
@Table(name = "company_profit")
public class CompanyProfit {

    @Id
    @Column(name = "uuid", insertable = false, updatable = false)
    private UUID id;

    @Column(name = "year_sale")
    private int yearSale;

    @Column(name = "profit")
    private BigDecimal profit;

    public int getYearSale() {
        return yearSale;
    }

    public BigDecimal getProfit() {
        return profit;
    }
}
