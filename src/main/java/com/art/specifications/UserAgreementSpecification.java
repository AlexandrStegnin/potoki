package com.art.specifications;

import com.art.model.UserAgreement;
import com.art.model.UserAgreement_;
import com.art.model.supporting.filters.UserAgreementFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * @author Alexandr Stegnin
 */

@Component
public class UserAgreementSpecification extends BaseSpecification<UserAgreement, UserAgreementFilter>{

    @Override
    public Specification<UserAgreement> getFilter(UserAgreementFilter filter) {
        return (root, query, cb) -> where(
                investorIdIn(filter.getInvestorsId()))
                .and(facilityIdIn(filter.getFacilitiesId()))
                .toPredicate(root, query, cb);
    }

    private static Specification<UserAgreement> investorIdIn(List<Long> investorsId) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (investorsId == null || investorsId.size() == 0) {
                return null;
            }
            return root.get(UserAgreement_.concludedFrom).in(investorsId);
        }
        );
    }

    private static Specification<UserAgreement> facilityIdIn(List<Long> facilitiesId) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (facilitiesId == null || facilitiesId.size() == 0) {
                return null;
            }
            return root.get(UserAgreement_.facility).in(facilitiesId);
        }
        );
    }

}
