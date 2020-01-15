package com.art.model.supporting.filters;

import com.art.model.Users;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CashFilter extends AbstractFilter {
    private Users investor;
    private int filtered = 0;
}
