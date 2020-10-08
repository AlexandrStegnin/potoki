package com.art.specifications;

import com.art.model.*;
import com.art.model.supporting.filters.AccountTransactionFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * @author Alexandr Stegnin
 */

@Component
public class AccountTransactionSpecification extends BaseSpecification<AccountTransaction, AccountTransactionFilter> {

    @Override
    public Specification<AccountTransaction> getFilter(AccountTransactionFilter filter) {
        return (root, query, cb) -> where(
                investorEqual(filter.getInvestor()))
                .toPredicate(root, query, cb);
    }

    private static Specification<AccountTransaction> investorEqual(String investor) {
        if (investor == null || "Выберите инвестора".equalsIgnoreCase(investor)) {
            return null;
        } else {
            return ((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(AccountTransaction_.owner).get(Account_.accountNumber), investor)
            );
        }
    }

}
