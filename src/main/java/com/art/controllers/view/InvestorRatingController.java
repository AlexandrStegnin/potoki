package com.art.controllers.view;

import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.filters.InvestorRatingFilter;
import com.art.model.supporting.view.InvestorRating;
import com.art.service.view.InvestorRatingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author Alexandr Stegnin
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("investors/rating")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InvestorRatingController {

  InvestorRatingService ratingService;

  @GetMapping
  public String getRatings(Model model) {
    List<InvestorRating> ratings = ratingService.findAll();
    Date firstRatingDate = null;
    Date secondRatingDate = null;
    if (!ratings.isEmpty()) {
      firstRatingDate = ratings.get(0).getFirstRatingDate();
      secondRatingDate = ratings.get(0).getSecondRatingDate();
    }
    model.addAttribute("ratings", ratings);
    model.addAttribute("firstRatingDate", firstRatingDate);
    model.addAttribute("secondRatingDate", secondRatingDate);
    model.addAttribute("filter", new InvestorRatingFilter());
    return "investors-ratings";
  }

  @ResponseBody
  @PostMapping("/calculate")
  public ApiResponse calculateRatings(@RequestBody InvestorRatingFilter filter) {
    ratingService.calculateRatings(filter);
    return new ApiResponse("Рейтинги пересчитаны");
  }

}
