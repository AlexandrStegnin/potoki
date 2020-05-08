package com.art.model.supporting.view;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Класс для отображения представления
 * о доле каждого проекта во всех вложенных деньгах инвестора
 *
 * @author Alexandr Stegnin
 */
@Entity
@Immutable
@Table(name = "kind_project_on_all_invested_monies")
public class KindProjectOnAllMonies {

    @Id
    @Column(name = "FacilityId")
    private Long id;

    @Column(name = "facility")
    private String facility;

    @Column(name = "login")
    private String login;

    @Column(name = "percent")
    private BigDecimal percent;

    public String getFacility() {
        return facility;
    }

    public String getLogin() {
        return login;
    }

    public BigDecimal getPercent() {
        return percent;
    }
}
