package com.art.specifications;

import com.art.model.*;
import com.art.model.supporting.filters.CashFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.springframework.data.jpa.domain.Specifications.where;

@Component
public class InvestorsCashSpecification extends BaseSpecification<InvestorsCash, CashFilter> {

    @Override
    public Specification<InvestorsCash> getFilter(CashFilter request) {
        return (root, query, cb) -> {
//            query.distinct(true); //Important because of the join in the child entities specifications
            return where(
                    dateGivenCashBetween(request.getFromDate(), request.getToDate()))
                    .and(facilityEqual(request.getFacility()))
                    .and(underFacilityEqual(request.getUnderFacility()))
                    .and(loginIn(request.getInvestors()))
                    .and(facilityIsNotNull())
                    .toPredicate(root, query, cb);
        };

    }

    private static Specification<InvestorsCash> dateGivenCashBetween(Date min, Date max) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (!Objects.equals(null, min) && !Objects.equals(null, max)) {
                return criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get(InvestorsCash_.dateGivedCash), min),
                        criteriaBuilder.lessThanOrEqualTo(root.get(InvestorsCash_.dateGivedCash), max)
                );
            } else if (!Objects.equals(null, min)) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(InvestorsCash_.dateGivedCash), min);
            } else if (!Objects.equals(null, max)) {
                return criteriaBuilder.lessThanOrEqualTo(root.get(InvestorsCash_.dateGivedCash), max);
            } else {
                return null;
            }
        }
        );
    }

    private static Specification<InvestorsCash> facilityEqual(String facility) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (Objects.equals(null, facility) || StringUtils.isEmpty(facility)) {
                return null;
            } else {
                return criteriaBuilder.equal(root.get(InvestorsCash_.facility).get(Facilities_.facility), facility);
            }
        }
        );
    }

    private static Specification<InvestorsCash> underFacilityEqual(String underFacility) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (Objects.equals(null, underFacility) || StringUtils.isEmpty(underFacility)) {
                return null;
            } else {
                return criteriaBuilder.equal(root.get(InvestorsCash_.underFacility).get(UnderFacilities_.underFacility), underFacility);
            }
        }
        );
    }

    private static Specification<InvestorsCash> loginIn(List<String> loginList) {
        if (loginList == null || loginList.size() == 0) {
            return null;
        } else {
            return ((root, criteriaQuery, criteriaBuilder) ->
                    root.get(InvestorsCash_.investor).get(Users_.login).in(loginList)
            );
        }
    }

    private static Specification<InvestorsCash> facilityIsNotNull() {
        return ((root, criteriaQuery, criteriaBuilder) ->
                root.get(InvestorsCash_.facility).get(Facilities_.facility).isNotNull()
        );
    }

}
