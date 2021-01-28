package com.art.controllers;

import com.art.config.application.Location;
import com.art.model.AppUser;
import com.art.model.Facility;
import com.art.model.UserAgreement;
import com.art.model.supporting.filters.UserAgreementFilter;
import com.art.service.FacilityService;
import com.art.service.UserAgreementService;
import com.art.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserAgreementController {

    UserAgreementService userAgreementService;

    UserService userService;

    FacilityService facilityService;

    UserAgreementFilter filter = new UserAgreementFilter();

    @GetMapping(path = Location.USER_AGREEMENTS_LIST)
    public ModelAndView getAgreements(@PageableDefault(size = 1000) @SortDefault Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("agreements-list");
        Page<UserAgreement> page = userAgreementService.findAll(filter, pageable);
        modelAndView.addObject("page", page);
        modelAndView.addObject("filter", filter);
        return modelAndView;
    }

    @PostMapping(path = Location.USER_AGREEMENTS_LIST)
    public ModelAndView moneyPageable(@ModelAttribute(value = "filter") UserAgreementFilter filter) {
        Pageable pageable;
        if (filter.isAllRows()) {
            pageable = new PageRequest(0, Integer.MAX_VALUE);
        } else {
            pageable = new PageRequest(filter.getPageNumber(), filter.getPageSize());
        }
        ModelAndView modelAndView = new ModelAndView("agreements-list");
        Page<UserAgreement> page = userAgreementService.findAll(filter, pageable);
        modelAndView.addObject("page", page);
        modelAndView.addObject("filter", filter);
        return modelAndView;
    }

    @ModelAttribute("investors")
    public List<AppUser> initializeInvestors() {
        return userService.initializeMultipleInvestors();
    }

    @ModelAttribute("facilities")
    public List<Facility> initializeFacilities() {
        return facilityService.initializeFacilitiesForMultiple();
    }

}
