package com.art.specifications;

import com.art.model.*;
import com.art.model.supporting.filters.RentPaymentFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * @author Alexandr Stegnin
 */

@Component
public class RentPaymentSpecification extends BaseSpecification<RentPayment, RentPaymentFilter> {

    @Override
    public Specification<RentPayment> getFilter(RentPaymentFilter filter) {
        return (root, query, cb) -> where(
                dateGivenCashBetween(filter.getFromDate(), filter.getToDate()))
                .and(facilityEqual(filter.getFacility()))
                .and(underFacilityEqual(filter.getUnderFacility()))
                .and(loginEqual(filter.getInvestor()))
                .and(facilityIsNotNull())
                .toPredicate(root, query, cb);
    }

    private static Specification<RentPayment> dateGivenCashBetween(Date min, Date max) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (!Objects.equals(null, min) && !Objects.equals(null, max)) {
                return criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get(RentPayment_.dateReport), min),
                        criteriaBuilder.lessThanOrEqualTo(root.get(RentPayment_.dateReport), max)
                );
            } else if (!Objects.equals(null, min)) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(RentPayment_.dateReport), min);
            } else if (!Objects.equals(null, max)) {
                return criteriaBuilder.lessThanOrEqualTo(root.get(RentPayment_.dateReport), max);
            } else {
                return null;
            }
        }
        );
    }

    private static Specification<RentPayment> facilityEqual(String name) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (Objects.equals(null, name) || StringUtils.isEmpty(name) || "Выберите объект".equalsIgnoreCase(name.trim())) {
                return null;
            } else {
                return criteriaBuilder.equal(root.get(RentPayment_.facility).get(Facility_.name), name);
            }
        }
        );
    }

    private static Specification<RentPayment> underFacilityEqual(String underFacility) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (Objects.equals(null, underFacility) || StringUtils.isEmpty(underFacility) || "Выберите подобъект".equalsIgnoreCase(underFacility.trim())
                    || "Без подобъекта".equalsIgnoreCase(underFacility.trim())) {
                return null;
            } else {
                return criteriaBuilder.equal(root.get(RentPayment_.underFacility).get(UnderFacility_.name), underFacility);
            }
        }
        );
    }

    private static Specification<RentPayment> loginEqual(String login) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (login == null || login.trim().equalsIgnoreCase("Выберите инвестора")) {
                return null;
            } else {
                return criteriaBuilder.equal(root.get(RentPayment_.investor).get(AppUser_.login), login);
            }
        }
        );
    }

    private static Specification<RentPayment> facilityIsNotNull() {
        return ((root, criteriaQuery, criteriaBuilder) ->
                root.get(RentPayment_.facility).get(Facility_.name).isNotNull()
        );
    }

}
