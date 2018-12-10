package com.art.model.supporting.filters;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MarketingTreeFilter extends AbstractFilter {

    private String investor;

    private String partner;

    private String kin;

}
