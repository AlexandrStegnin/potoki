package com.art.model;

import com.art.model.supporting.enums.KinEnum;
import com.art.model.supporting.enums.StatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@Table(name = "marketing_tree")
public class MarketingTree implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "marketing_tree_generator")
  @SequenceGenerator(name = "marketing_tree_generator", sequenceName = "marketing_tree_id_seq")
  @Column(name = "id")
  private Long id;

  @OneToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "partner_id", referencedColumnName = "id")
  private AppUser partner;

  @OneToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "investor_id", referencedColumnName = "id")
  private AppUser investor;

  @Column(name = "kin")
  @Enumerated(EnumType.STRING)
  private KinEnum kin;

  @Column(name = "first_investment_date")
  private Date firstInvestmentDate;

  @Column(name = "inv_status")
  @Enumerated(EnumType.STRING)
  private StatusEnum invStatus;

  @Column(name = "days_to_deactivate")
  private int daysToDeactivate;

  @Column(name = "ser_number")
  private int serNumber;

}
