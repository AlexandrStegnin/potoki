package com.art.model.supporting.filters;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Alexandr Stegnin
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AccountTransactionFilter extends AbstractFilter {

    private String investor;

    private String facility;

    private Long salePaymentId;

}
