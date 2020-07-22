package com.art.controllers;

import com.art.config.application.Location;
import com.art.model.Account;
import com.art.model.Facility;
import com.art.model.InvestorsCash;
import com.art.model.UnderFacility;
import com.art.model.supporting.ApiResponse;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.SearchSummary;
import com.art.model.supporting.dto.FacilityDTO;
import com.art.model.supporting.enums.OwnerType;
import com.art.service.AccountService;
import com.art.service.FacilityService;
import com.art.service.InvestorsCashService;
import com.art.service.UnderFacilityService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Transactional
public class FacilityController {

    private static final String REDIRECT_URL = Location.FACILITIES_LIST;

    private final FacilityService facilityService;

    private final UnderFacilityService underFacilityService;

    private final InvestorsCashService investorsCashService;

    private final AccountService accountService;

    public FacilityController(FacilityService facilityService, UnderFacilityService underFacilityService,
                              InvestorsCashService investorsCashService, AccountService accountService) {
        this.facilityService = facilityService;
        this.underFacilityService = underFacilityService;
        this.investorsCashService = investorsCashService;
        this.accountService = accountService;
    }

    /**
     * Создание объекта
     */
    @GetMapping(path = Location.FACILITIES_CREATE)
    public String newFacility(ModelMap model) {
        Facility facility = new Facility();
        String accountNumber = "";
        model.addAttribute("newFacility", facility);
        model.addAttribute("accountNumber", accountNumber);
        model.addAttribute("edit", false);
        return "facility-add";
    }

    @PostMapping(path = Location.FACILITIES_CREATE)
    @ResponseBody
    public ApiResponse createFacility(@RequestBody FacilityDTO facilityDTO) {
        Facility newFacility = new Facility(facilityDTO);
        return facilityService.create(newFacility);
    }

    @GetMapping(path = Location.FACILITIES_LIST)
    public String adminFacility(ModelMap model) {
        List<Facility> facilities = facilityService.findAll();
        model.addAttribute("facilities", facilities);
        return "facility-list";
    }

    /**
     * Создание страницы с объектом редактирования
     */
    @GetMapping(path = Location.FACILITIES_EDIT)
    public String editFacility(@PathVariable Long id, ModelMap model) {
        Facility facility = facilityService.findById(id);
        Account account = accountService.findByOwnerId(id, OwnerType.FACILITY);
        String accountNumber = "";
        if (account != null) {
            accountNumber = account.getAccountNumber();
        }
        model.addAttribute("newFacility", facility);
        model.addAttribute("accountNumber", accountNumber);
        model.addAttribute("edit", true);
        return "facility-add";
    }

    /**
     * Обновление объекта в базе данных
     */
    @PostMapping(path = Location.FACILITIES_EDIT)
    public String updateFacility(@ModelAttribute("newFacility") Facility facility, BindingResult result, ModelMap model) {
        String ret = "списку объектов.";
        if (result.hasErrors()) {
            return "facility-add";
        }
        facilityService.update(facility);

        model.addAttribute("success", "Данные объекта " + facility.getName() + " успешно обновлены.");
        model.addAttribute("redirectUrl", REDIRECT_URL);
        model.addAttribute("ret", ret);
        return "registration-success";
    }

    /**
     * Удаление объекта по ID.
     */

    @PostMapping(path = Location.FACILITIES_DELETE, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse deleteFacility(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        Facility facility = facilityService.findById(Long.valueOf(searchSummary.getFacilityStr()));
        List<InvestorsCash> investorsCashes = investorsCashService.findByFacilityId(facility.getId());
        if (investorsCashes.size() > 0) {
            response.setMessage(String.format("В объект [%s] вложены деньги, необходимо перераспределить их", facility.getName()));
            return response;
        }
        try {
            List<UnderFacility> underFacilities = underFacilityService.findByFacilityId(facility.getId());
            underFacilities.forEach(underFacility -> underFacilityService.deleteById(underFacility.getId()));
            facilityService.deleteById(facility.getId());
            response.setMessage("Объект " + facility.getName() + " успешно удалён.");
        } catch (Exception e) {
            response.setError("При удалении объекта " + facility.getName() + " произошла ошибка.");
        }
        return response;
    }

}
