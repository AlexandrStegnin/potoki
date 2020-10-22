package com.art.specifications;

import com.art.config.application.Constant;
import com.art.model.AccountTransaction;
import com.art.model.AccountTransaction_;
import com.art.model.Account_;
import com.art.model.supporting.enums.CashType;
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
                ownerEqual(filter.getOwner()))
                .and(parentPayerEqual(filter.getParentPayer()))
                .and(payerEqual(filter.getPayer()))
                .and(cashTypeEqual(filter.getCashType()))
                .and(cashNotNull())
                .toPredicate(root, query, cb);
    }

    private static Specification<AccountTransaction> ownerEqual(String ownerName) {
        if (ownerName == null || ownerName.startsWith(Constant.CHOOSE_FILTER_PREFIX)) {
            return null;
        } else {
            return ((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(AccountTransaction_.owner).get(Account_.ownerName), ownerName)
            );
        }
    }

    private static Specification<AccountTransaction> accIdEqual(Long accId) {
        if (accId == null) {
            return null;
        } else {
            return ((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(AccountTransaction_.owner).get(Account_.id), accId)
            );
        }
    }

    private static Specification<AccountTransaction> recipientEqual(String recipientName) {
        if (recipientName == null || recipientName.startsWith(Constant.CHOOSE_FILTER_PREFIX)) {
            return null;
        } else {
            return ((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(AccountTransaction_.payer).get(Account_.ownerName), recipientName)
            );
        }
    }

    private static Specification<AccountTransaction> cashTypeEqual(String cashTypeTitle) {
        if (cashTypeTitle == null || cashTypeTitle.startsWith(Constant.CHOOSE_FILTER_PREFIX)) {
            return null;
        } else {
            CashType cashType = CashType.fromTitle(cashTypeTitle);
            return ((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(AccountTransaction_.cashType), cashType)
            );
        }
    }

    private static Specification<AccountTransaction> parentPayerEqual(String parentPayer) {
        if (parentPayer == null || parentPayer.startsWith(Constant.CHOOSE_FILTER_PREFIX)) {
            return null;
        } else {
            return ((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(AccountTransaction_.payer).get(Account_.parentAccount).get(Account_.ownerName), parentPayer)
            );
        }
    }

    private static Specification<AccountTransaction> payerEqual(String payerName) {
        if (payerName == null ||payerName.startsWith(Constant.CHOOSE_FILTER_PREFIX)) {
            return null;
        } else {
            return ((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(AccountTransaction_.payer).get(Account_.ownerName), payerName)
            );
        }
    }

    private static Specification<AccountTransaction> cashNotNull() {
        return ((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get(AccountTransaction_.cash))
        );
    }

    public Specification<AccountTransaction> getDetailsFilter(AccountTransactionFilter filter) {
        return (root, query, cb) -> where(
                accIdEqual(filter.getAccountId()))
                .and(parentPayerEqual(filter.getParentPayer()))
                .and(payerEqual(filter.getPayer()))
                .toPredicate(root, query, cb);
    }

}
