package com.art.model.supporting.view;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author Alexandr Stegnin
 */

@Entity
@Immutable
@Table(name = "kind_on_project")
public class KindOnProject {

    @Id
    @Column(name = "FacilityId")
    private Long id;

    @Column(name = "facility")
    private String facility;

    @Column(name = "login")
    private String login;

    @Column(name = "given_cash")
    private BigDecimal givenCash;

    @Column(name = "project_coast")
    private BigDecimal projectCoast;

    @Column(name = "percent_on_project")
    private BigDecimal percent;

    public String getFacility() {
        return facility;
    }

    public String getLogin() {
        return login;
    }

    public BigDecimal getGivenCash() {
        return givenCash;
    }

    public BigDecimal getProjectCoast() {
        return projectCoast;
    }

    public BigDecimal getPercent() {
        return percent;
    }
}
