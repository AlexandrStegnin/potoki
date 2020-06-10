package com.art.specifications;

import com.art.model.TransactionLog;
import com.art.model.TransactionLog_;
import com.art.model.supporting.TransactionType;
import com.art.model.supporting.filters.TxLogFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * @author Alexandr Stegnin
 */

@Component
public class TransactionLogSpecification extends BaseSpecification<TransactionLog, TxLogFilter> {

    @Override
    public Specification<TransactionLog> getFilter(TxLogFilter filter) {
        return (root, query, cb) -> where(
                txDateEqual(filter.getTxDate()))
                .and(txTypeEqual(filter.getType()))
                .and(creatorEqual(filter.getCreator()))
                .toPredicate(root, query, cb);
    }

    private static Specification<TransactionLog> txDateEqual(Date txDate) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (null == txDate) {
                return null;
            }
            return criteriaBuilder.equal(root.get(TransactionLog_.txDate), txDate);
        }
        );
    }

    private static Specification<TransactionLog> txTypeEqual(String type) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (null == type || type.isEmpty() || "Вид операции".equalsIgnoreCase(type)) {
                return null;
            }
            TransactionType txType = TransactionType.fromTitle(type);
            return criteriaBuilder.equal(root.get(TransactionLog_.type), txType);
        }
        );
    }

    private static Specification<TransactionLog> creatorEqual(String creator) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            if (null == creator || creator.isEmpty() || "Кем создана".equalsIgnoreCase(creator)) {
                return null;
            }
            return criteriaBuilder.equal(root.get(TransactionLog_.createdBy), creator);
        }
        );
    }

}
