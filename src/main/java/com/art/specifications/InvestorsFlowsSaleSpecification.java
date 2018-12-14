package com.art.specifications;

import com.art.model.*;
import com.art.model.supporting.filters.FlowsSaleFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.springframework.data.jpa.domain.Specifications.where;

@Component
public class InvestorsFlowsSaleSpecification extends BaseSpecification<InvestorsFlowsSale, FlowsSaleFilter> {

    @Override
    public Specification<InvestorsFlowsSale> getFilter(FlowsSaleFilter filter) {
        return (root, query, cb) -> where(
                dateGivenCashBetween(filter.getFromDate(), filter.getToDate()))
                .and(facilityEqual(filter.getFacility()))
                .and(underFacilityEqual(filter.getUnderFacility()))
                .and(loginIn(filter.getInvestors()))
                .and(facilityIsNotNull())
                .toPredicate(root, query, cb);
    }

    private static Specification<InvestorsFlowsSale> dateGivenCashBetween(Date min, Date max) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (!Objects.equals(null, min) && !Objects.equals(null, max)) {
                return criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get(InvestorsFlowsSale_.dateGived), min),
                        criteriaBuilder.lessThanOrEqualTo(root.get(InvestorsFlowsSale_.dateGived), max)
                );
            } else if (!Objects.equals(null, min)) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(InvestorsFlowsSale_.dateGived), min);
            } else if (!Objects.equals(null, max)) {
                return criteriaBuilder.lessThanOrEqualTo(root.get(InvestorsFlowsSale_.dateGived), max);
            } else {
                return null;
            }
        }
        );
    }

    private static Specification<InvestorsFlowsSale> facilityEqual(String facility) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (Objects.equals(null, facility) || StringUtils.isEmpty(facility) || "Выберите объект".equalsIgnoreCase(facility.trim())) {
                return null;
            } else {
                return criteriaBuilder.equal(root.get(InvestorsFlowsSale_.facility).get(Facilities_.facility), facility);
            }
        }
        );
    }

    private static Specification<InvestorsFlowsSale> underFacilityEqual(String underFacility) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (Objects.equals(null, underFacility) || StringUtils.isEmpty(underFacility) || "Выберите подобъект".equalsIgnoreCase(underFacility.trim())) {
                return null;
            } else {
                return criteriaBuilder.equal(root.get(InvestorsFlowsSale_.underFacility).get(UnderFacilities_.underFacility), underFacility);
            }
        }
        );
    }

    private static Specification<InvestorsFlowsSale> loginIn(List<String> loginList) {
        if (loginList == null || loginList.size() == 0 || loginList.get(0).trim().equalsIgnoreCase("Выберите инвестора")) {
            return null;
        } else {
            return ((root, criteriaQuery, criteriaBuilder) ->
                    root.get(InvestorsFlowsSale_.investor).get(Users_.login).in(loginList)
            );
        }
    }

    private static Specification<InvestorsFlowsSale> facilityIsNotNull() {
        return ((root, criteriaQuery, criteriaBuilder) ->
                root.get(InvestorsFlowsSale_.facility).get(Facilities_.facility).isNotNull()
        );
    }

}
