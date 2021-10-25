package com.art.model.supporting.view;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * @author Alexandr Stegnin
 */
@Data
@Entity
@Immutable
@Table(name = "investor_rating")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvestorRating {

  @Id
  UUID id;
  @Column(name = "now_r_previous_r")
  String nowRPreviousR;
  @Column(name = "change_position")
  Integer changePosition;
  @Column(name = "login")
  String login;
  @Column(name = "first_rating_cash")
  BigDecimal firstRatingCash;
  @Column(name = "second_rating_cash")
  BigDecimal secondRatingCash;
  @Column(name = "difference_cash")
  BigDecimal differenceCash;
  @Column(name = "first_rating_position")
  Integer firstRatingPosition;
  @Column(name = "second_rating_position")
  Integer secondRatingPosition;
  @Column(name = "first_rating_date")
  @Temporal(TemporalType.TIMESTAMP)
  Date firstRatingDate;
  @Column(name = "second_rating_date")
  @Temporal(TemporalType.TIMESTAMP)
  Date secondRatingDate;

}
