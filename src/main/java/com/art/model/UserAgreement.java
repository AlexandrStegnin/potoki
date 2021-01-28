package com.art.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

/**
 * Сущность для хранения инфо о том, с кем заключён договор
 *
 * @author Alexandr Stegnin
 */

@Data
@Entity
@Table(name = "user_agreement")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    /**
     * ID объекта
     */
    @ManyToOne
    @JoinColumn(name = "facility_id")
    Facility facility;

    /**
     * С кем заключён договор (ЮЛ/ФЛ)
     */
    @Column(name = "concluded_with")
    String concludedWith;

    /**
     * От кого заключён договор (id инвестора)
     */
    @ManyToOne
    @JoinColumn(name = "concluded_from")
    AppUser concludedFrom;

    /**
     * Налоговая ставка (%)
     */
    @Column(name = "tax_rate")
    Double taxRate;

}
