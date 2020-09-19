package com.art.model.supporting.filters;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Фильтр для выплат по аренде
 *
 * @author Alexandr Stegnin
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class RentPaymentFilter extends AbstractFilter {

    private String investor;

}
