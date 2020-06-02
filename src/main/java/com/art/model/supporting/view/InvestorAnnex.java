package com.art.model.supporting.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс для отображения представления "приложения к договорам инвесторов"
 *
 * @author Alexandr Stegnin
 */

@Data
@Entity
@Immutable
@Table(name = "investor_annexes")
public class InvestorAnnex {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "investor")
    private String investor;

    @Column(name = "annex_name")
    private String annexName;

    @Column(name = "annex_read")
    private Integer read;

    @Column(name = "date_load")
    private Date dateLoad;

    @Column(name = "loaded_by")
    private String loader;

    public String getDateLoad() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            localDate = format.format(dateLoad);
        } catch (Exception ignored) {
        }

        return localDate;
    }

}
