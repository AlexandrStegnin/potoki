package com.art.model.supporting.filters;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Alexandr Stegnin
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class InvShareFilter extends AbstractFilter {

    private Integer yearFrom;
    private Integer yearTo;

    private Integer monthFrom;
    private Integer monthTo;

}
