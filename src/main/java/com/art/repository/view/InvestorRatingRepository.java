package com.art.repository.view;

import com.art.model.supporting.view.InvestorRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.UUID;

/**
 * @author Alexandr Stegnin
 */
@Repository
public interface InvestorRatingRepository extends JpaRepository<InvestorRating, UUID> {

  @Procedure(procedureName = "investors_ratings")
  void callInvestorsRatings(@Param("firs_rating_date") Date firstRatingDate,
                            @Param("second_rating_date") Date secondRatingDate);

}
