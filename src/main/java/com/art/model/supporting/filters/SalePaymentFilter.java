package com.art.model.supporting.filters;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SalePaymentFilter extends AbstractFilter {

    private String investor;

}
