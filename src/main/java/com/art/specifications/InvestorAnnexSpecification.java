package com.art.specifications;

import com.art.model.supporting.filters.InvestorAnnexFilter;
import com.art.model.supporting.view.InvestorAnnex;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * @author Alexandr Stegnin
 */

@Component
public class InvestorAnnexSpecification extends BaseSpecification<InvestorAnnex, InvestorAnnexFilter> {

    @Override
    public Specification<InvestorAnnex> getFilter(InvestorAnnexFilter filter) {
        return (root, query, cb) -> where(
                investorEqual(filter.getInvestor()))
                .toPredicate(root, query, cb);
    }

    private static Specification<InvestorAnnex> investorEqual(String investor) {
        if (investor == null || "Выберите инвестора".equalsIgnoreCase(investor)) {
            return null;
        } else {
            return ((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("investor"), investor)
            );
        }
    }

}
