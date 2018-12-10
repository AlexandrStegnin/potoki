package com.art.specifications;

import com.art.model.MarketingTree;
import com.art.model.MarketingTree_;
import com.art.model.Users_;
import com.art.model.supporting.KinEnum;
import com.art.model.supporting.filters.MarketingTreeFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;

import static org.springframework.data.jpa.domain.Specifications.where;

@Component
public class MarketingTreeSpecification extends BaseSpecification<MarketingTree, MarketingTreeFilter> {
    @Override
    public Specification<MarketingTree> getFilter(MarketingTreeFilter filter) {
        return (root, query, cb) -> where(
                firstInvestmentDateBetween(filter.getFromDate(), filter.getToDate()))
                .and(investorEqual(filter.getInvestor()))
                .and(partnerEqual(filter.getPartner()))
                .and(kinEqual(filter.getKin() == null ? null : KinEnum.valueOf(filter.getKin())))
                .toPredicate(root, query, cb);
    }

    private static Specification<MarketingTree> firstInvestmentDateBetween(Date min, Date max) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (!Objects.equals(null, min) && !Objects.equals(null, max)) {
                return criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get(MarketingTree_.firstInvestmentDate), min),
                        criteriaBuilder.lessThanOrEqualTo(root.get(MarketingTree_.firstInvestmentDate), max)
                );
            } else if (!Objects.equals(null, min)) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(MarketingTree_.firstInvestmentDate), min);
            } else if (!Objects.equals(null, max)) {
                return criteriaBuilder.lessThanOrEqualTo(root.get(MarketingTree_.firstInvestmentDate), max);
            } else {
                return null;
            }
        }
        );
    }

    private static Specification<MarketingTree> investorEqual(String login) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (Objects.equals(null, login) || StringUtils.isEmpty(login) || "Выберите инвестора".equalsIgnoreCase(login)) {
                return null;
            } else {
                return criteriaBuilder.equal(root.get(MarketingTree_.investor).get(Users_.login), login);
            }
        }
        );
    }

    private static Specification<MarketingTree> partnerEqual(String login) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (Objects.equals(null, login) || StringUtils.isEmpty(login) || "Выберите партнёра".equalsIgnoreCase(login)) {
                return null;
            } else {
                return criteriaBuilder.equal(root.get(MarketingTree_.partner).get(Users_.login), login);
            }
        }
        );
    }

    private static Specification<MarketingTree> kinEqual(KinEnum kin) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (Objects.equals(null, kin) || "EMPTY".equalsIgnoreCase(kin.name())) {
                return null;
            } else {
                return criteriaBuilder.equal(root.get(MarketingTree_.kin), kin);
            }
        }
        );
    }
}
