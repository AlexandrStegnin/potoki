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
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_agreement_generator")
  @SequenceGenerator(name = "user_agreement_generator", sequenceName = "user_agreement_id_seq")
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
   * Инвестор
   */
  @ManyToOne
  @JoinColumn(name = "concluded_from")
  AppUser concludedFrom;

  /**
   * Налоговая ставка (%)
   */
  @Column(name = "tax_rate")
  Double taxRate;

  /**
   * От кого заключен договор
   */
  @Column(name = "organization")
  String organization;

}
