package com.art.model.supporting.view;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

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
    @Column(name = "id")
    private Long id;

    @Column(name = "date_sale")
    private LocalDate dateSale;

    @Column(name = "under_facility")
    private String underFacility;

    @Column(name = "profit")
    private BigDecimal profit;

    public LocalDate getDateSale() {
        return dateSale;
    }

    public String getUnderFacility() {
        return underFacility;
    }

    public BigDecimal getProfit() {
        return profit;
    }
}
