package com.art.service;

import com.art.model.AppUser;
import com.art.model.Facility;
import com.art.model.UserAgreement;
import com.art.model.supporting.dto.UserAgreementDTO;
import com.art.model.supporting.filters.UserAgreementFilter;
import com.art.repository.FacilityRepository;
import com.art.repository.UserAgreementRepository;
import com.art.repository.UserRepository;
import com.art.specifications.UserAgreementSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

/**
 * @author Alexandr Stegnin
 */

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserAgreementService {

    UserAgreementSpecification specification;

    UserAgreementRepository userAgreementRepository;

    FacilityRepository facilityRepository;

    UserRepository userRepository;

    public UserAgreement findById(Long id) {
        UserAgreement agreement = userAgreementRepository.getOne(id);
        if (agreement == null) {
            throw new EntityNotFoundException("Информация о заключённом договоре не найдена");
        }
        return agreement;
    }

    public UserAgreement create(UserAgreement userAgreement) {
        return userAgreementRepository.save(userAgreement);
    }

    public UserAgreement update(UserAgreement userAgreement) {
        return userAgreementRepository.save(userAgreement);
    }

    public void delete(UserAgreement userAgreement) {
        userAgreementRepository.delete(userAgreement);
    }

    /**
     * Создать запись о том, с кем заключён договор на основе DTO
     *
     * @param dto DTO записи
     * @return созданная запись
     */
    private UserAgreement create(UserAgreementDTO dto) {
        Facility facility = facilityRepository.findOne(dto.getFacilityId());
        AppUser investor = userRepository.findOne(dto.getConcludedFrom());
        if (investor == null) {
            throw new EntityNotFoundException("Пользователь не найден");
        }
        UserAgreement userAgreement = new UserAgreement();
        userAgreement.setFacility(facility);
        userAgreement.setConcludedWith(dto.getConcludedWith());
        userAgreement.setTaxRate(dto.getTaxRate());
        userAgreement.setConcludedFrom(investor);
        return userAgreementRepository.save(userAgreement);
    }

    /**
     * Обновить информацию о том, с кем заключён договор на основе DTO
     *
     * @param dto DTO
     * @return обновлённая информация
     */
    public UserAgreement update(UserAgreementDTO dto) {
        Facility facility = facilityRepository.findOne(dto.getFacilityId());
        dto.setConcludedFrom(dto.getConcludedFrom());
        AppUser investor = userRepository.findOne(dto.getConcludedFrom());
        if (investor == null) {
            throw new EntityNotFoundException("Пользователь не найден");
        }
        UserAgreement userAgreement = userAgreementRepository.findByFacilityIdAndConcludedFrom(facility.getId(), investor.getId());
        if (userAgreement == null) {
            return create(dto);
        }
        userAgreement.setConcludedFrom(investor);
        userAgreement.setTaxRate(dto.getTaxRate());
        userAgreement.setConcludedWith(dto.getConcludedWith());
        return userAgreementRepository.save(userAgreement);
    }

    /**
     * Найти инфо о том, с кем заключён договор
     *
     * @param investor инвестор
     * @param facility объект
     * @return найденная информация
     */
    public UserAgreement findByInvestorAndFacility(AppUser investor, Facility facility) {
        return userAgreementRepository.findByFacilityIdAndConcludedFrom(facility.getId(), investor.getId());
    }

    public Page<UserAgreement> findAll(UserAgreementFilter filter, Pageable pageable) {
        return userAgreementRepository.findAll(specification.getFilter(filter), pageable);
    }

}
