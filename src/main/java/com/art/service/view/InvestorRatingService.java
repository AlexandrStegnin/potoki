package com.art.service.view;

import com.art.model.supporting.filters.InvestorRatingFilter;
import com.art.model.supporting.view.InvestorRating;
import com.art.repository.view.InvestorRatingRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InvestorRatingService {

  InvestorRatingRepository investorRatingRepository;

  public List<InvestorRating> findAll() {
    return investorRatingRepository.findAll();
  }

  public void calculateRatings(InvestorRatingFilter filter) {
    investorRatingRepository.callInvestorsRatings(filter.getFirstRatingDate(), filter.getSecondRatingDate());
  }

}
