package com.art.specifications;

import com.art.model.*;
import com.art.model.supporting.filters.SalePaymentFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.springframework.data.jpa.domain.Specifications.where;

@Component
public class SalePaymentSpecification extends BaseSpecification<SalePayment, SalePaymentFilter> {

    @Override
    public Specification<SalePayment> getFilter(SalePaymentFilter filter) {
        return (root, query, cb) -> where(
                dateGivenCashBetween(filter.getFromDate(), filter.getToDate()))
                .and(facilityEqual(filter.getFacility()))
                .and(underFacilityEqual(filter.getUnderFacility()))
                .and(loginIn(filter.getInvestors()))
                .and(facilityIsNotNull())
                .toPredicate(root, query, cb);
    }

    private static Specification<SalePayment> dateGivenCashBetween(Date min, Date max) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (!Objects.equals(null, min) && !Objects.equals(null, max)) {
                return criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get(SalePayment_.dateGiven), min),
                        criteriaBuilder.lessThanOrEqualTo(root.get(SalePayment_.dateGiven), max)
                );
            } else if (!Objects.equals(null, min)) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(SalePayment_.dateGiven), min);
            } else if (!Objects.equals(null, max)) {
                return criteriaBuilder.lessThanOrEqualTo(root.get(SalePayment_.dateGiven), max);
            } else {
                return null;
            }
        }
        );
    }

    private static Specification<SalePayment> facilityEqual(String name) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (Objects.equals(null, name) || StringUtils.isEmpty(name) || "Выберите объект".equalsIgnoreCase(name.trim())) {
                return null;
            } else {
                return criteriaBuilder.equal(root.get(SalePayment_.facility).get(Facility_.name), name);
            }
        }
        );
    }

    private static Specification<SalePayment> underFacilityEqual(String underFacility) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (Objects.equals(null, underFacility) || StringUtils.isEmpty(underFacility) || "Выберите подобъект".equalsIgnoreCase(underFacility.trim())
            || "Без подобъекта".equalsIgnoreCase(underFacility.trim())) {
                return null;
            } else {
                return criteriaBuilder.equal(root.get(SalePayment_.underFacility).get(UnderFacility_.name), underFacility);
            }
        }
        );
    }

    private static Specification<SalePayment> loginIn(List<String> loginList) {
        if (loginList == null || loginList.size() == 0 || loginList.get(0).trim().equalsIgnoreCase("Выберите инвестора")) {
            return null;
        } else {
            return ((root, criteriaQuery, criteriaBuilder) ->
                    root.get(SalePayment_.investor).get(AppUser_.login).in(loginList)
            );
        }
    }

    private static Specification<SalePayment> facilityIsNotNull() {
        return ((root, criteriaQuery, criteriaBuilder) ->
                root.get(SalePayment_.facility).get(Facility_.name).isNotNull()
        );
    }

}
