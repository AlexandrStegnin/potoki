package com.art.specifications;

import com.art.model.*;
import com.art.model.supporting.filters.CashFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.springframework.data.jpa.domain.Specifications.where;

@Component
public class InvestorCashSpecification extends BaseSpecification<InvestorCash, CashFilter> {

    @Override
    public Specification<InvestorCash> getFilter(CashFilter filter) {
        return (root, query, cb) -> where(
                dateGivenCashBetween(filter.getFromDate(), filter.getToDate()))
                .and(facilityIn(filter.getFacilities()))
                .and(underFacilityIn(filter.getUnderFacilities()))
                .and(loginIn(filter.getInvestors()))
                .and(facilityIsNotNull())
                .and(is1C(filter.isFromApi()))
                .toPredicate(root, query, cb);
    }

    public Specification<InvestorCash> getFilterForCashing(CashFilter filter) {
        return (root, query, cb) -> where(
                investorEqual(filter.getInvestor()))
                .and(givenCashGreaterThan(BigDecimal.ZERO))
                .and(notClosing())
                .and(facilityEqual(filter.getFacility()))
                .and(underFacilityEqual(filter.getUnderFacility()))
                .toPredicate(root, query, cb);
    }

    private static Specification<InvestorCash> dateGivenCashBetween(Date min, Date max) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (!Objects.equals(null, min) && !Objects.equals(null, max)) {
                return criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get(InvestorCash_.dateGiven), min),
                        criteriaBuilder.lessThanOrEqualTo(root.get(InvestorCash_.dateGiven), max)
                );
            } else if (!Objects.equals(null, min)) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(InvestorCash_.dateGiven), min);
            } else if (!Objects.equals(null, max)) {
                return criteriaBuilder.lessThanOrEqualTo(root.get(InvestorCash_.dateGiven), max);
            } else {
                return null;
            }
        }
        );
    }

    private static Specification<InvestorCash> facilityEqual(String name) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (Objects.equals(null, name) || StringUtils.isEmpty(name) || "Выберите объект".equalsIgnoreCase(name.trim())) {
                return null;
            } else {
                return criteriaBuilder.equal(root.get(InvestorCash_.facility).get(Facility_.name), name);
            }
        }
        );
    }

    private static Specification<InvestorCash> underFacilityEqual(String underFacility) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (Objects.equals(null, underFacility) || StringUtils.isEmpty(underFacility) || "Выберите подобъект".equalsIgnoreCase(underFacility.trim())) {
                return null;
            } else if ("Без подобъекта".equalsIgnoreCase(underFacility)) {
                return criteriaBuilder.isNull(root.get(InvestorCash_.underFacility));
            } else {
                return criteriaBuilder.equal(root.get(InvestorCash_.underFacility).get(UnderFacility_.name), underFacility);
            }
        }
        );
    }

    private static Specification<InvestorCash> loginIn(List<String> loginList) {
        if (loginList == null || loginList.size() == 0 || loginList.get(0).trim().equalsIgnoreCase("Выберите инвестора")) {
            return null;
        } else {
            return ((root, criteriaQuery, criteriaBuilder) ->
                    root.get(InvestorCash_.investor).get(AppUser_.login).in(loginList)
            );
        }
    }

    private static Specification<InvestorCash> facilityIsNotNull() {
        return ((root, criteriaQuery, criteriaBuilder) ->
                root.get(InvestorCash_.facility).get(Facility_.name).isNotNull()
        );
    }

    private static Specification<InvestorCash> investorEqual(AppUser investor) {
        if (investor == null) {
            return null;
        } else {
            return ((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(InvestorCash_.investor).get(AppUser_.id), investor.getId())
            );
        }
    }

    private static Specification<InvestorCash> givenCashGreaterThan(BigDecimal limit) {
        if (limit == null) {
            return null;
        } else {
            return ((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.gt(root.get(InvestorCash_.givenCash), limit)
            );
        }
    }

    private static Specification<InvestorCash> notClosing() {
        return ((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get(InvestorCash_.typeClosing))
        );
    }

    private static Specification<InvestorCash> notResale(final Long typeClosingId) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.notEqual(root
                        .get(InvestorCash_.typeClosing)
                        .get(TypeClosing_.id), typeClosingId);
    }

    public Specification<InvestorCash> getInvestedMoney(final Long typeClosingId) {
        return (root, query, cb) -> where(
                facilityIsNotNull())
                .and(notClosing())
                .or(notResale(typeClosingId))
                .toPredicate(root, query.orderBy(cb.asc(root.get(InvestorCash_.dateGiven))), cb);
    }

    private static Specification<InvestorCash> facilityIn(List<Facility> facilities) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (null == facilities) {
                return null;
            }
            if (facilities.size() == 0) {
                return null;
            }
            Facility undefinedFacility = facilities.stream()
                    .filter(facility -> facility != null && facility.getName().equalsIgnoreCase("Выберите объект"))
                    .findFirst()
                    .orElse(null);
            if (null != undefinedFacility) {
                return null;
            }
            return root.get(InvestorCash_.facility).in(facilities);
        }
        );
    }

    private static Specification<InvestorCash> underFacilityIn(List<UnderFacility> underFacilities) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (null == underFacilities) {
                return null;
            }
            if (underFacilities.size() == 0) {
                return null;
            } else if (null == underFacilities.get(0)) {
                return null;
            }
            UnderFacility undefinedUnderFacility = underFacilities.stream()
                    .filter(undefinedFacility -> undefinedFacility.getName().equalsIgnoreCase("Выберите подобъект"))
                    .findFirst()
                    .orElse(null);
            if (null != undefinedUnderFacility) {
                undefinedUnderFacility = underFacilities.stream()
                        .filter(undefinedFacility -> undefinedFacility.getName().equalsIgnoreCase("Без подобъекта"))
                        .findFirst()
                        .orElse(null);
                if (null != undefinedUnderFacility) {
                    return null;
                }
            }
            return root.get(InvestorCash_.underFacility).in(underFacilities);
        }
        );
    }

    private static Specification<InvestorCash> is1C(boolean is1C) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (!is1C) {
                return null;
            }
            return root.get(InvestorCash_.transactionUUID).isNotNull();
        }
        );
    }
}
