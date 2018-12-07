package com.art.specifications;

import com.art.model.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class InvestorsCashSpecification {
    public static Specification<InvestorsCash> dateGivenCashBetween(Date min, Date max) {
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

    public static Specification<InvestorsCash> facilityEqual(String facility) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (Objects.equals(null, facility) || StringUtils.isEmpty(facility)) {
                return null;
            } else {
                return criteriaBuilder.equal(root.get(InvestorsCash_.facility).get(Facilities_.facility), facility);
            }
        }
        );
    }

    public static Specification<InvestorsCash> underFacilityEqual(String underFacility) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (Objects.equals(null, underFacility) || StringUtils.isEmpty(underFacility)) {
                return null;
            } else {
                return criteriaBuilder.equal(root.get(InvestorsCash_.underFacility).get(UnderFacilities_.underFacility), underFacility);
            }
        }
        );
    }

    public static Specification<InvestorsCash> loginIn(List<String> logins) {
        if (logins.size() == 0) {
            return null;
        } else {
            return ((root, criteriaQuery, criteriaBuilder) ->
                    root.get(InvestorsCash_.investor).get(Users_.login).in(logins)
            );
        }
    }

    public static Specification<InvestorsCash> facilityIsNotNull() {
        return ((root, criteriaQuery, criteriaBuilder) ->
                root.get(InvestorsCash_.facility).get(Facilities_.facility).isNotNull()
        );
    }

}
