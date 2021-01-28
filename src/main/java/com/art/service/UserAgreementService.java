package com.art.service;

import com.art.model.UserAgreement;
import com.art.model.supporting.filters.UserAgreementFilter;
import com.art.repository.UserAgreementRepository;
import com.art.specifications.UserAgreementSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author Alexandr Stegnin
 */

@Service
public class UserAgreementService {

    private final UserAgreementSpecification specification;

    private final UserAgreementRepository userAgreementRepository;

    public UserAgreementService(UserAgreementSpecification specification,
                                UserAgreementRepository userAgreementRepository) {
        this.specification = specification;
        this.userAgreementRepository = userAgreementRepository;
    }

    public Page<UserAgreement> findAll(UserAgreementFilter filter, Pageable pageable) {
        return userAgreementRepository.findAll(specification.getFilter(filter), pageable);
    }

}
