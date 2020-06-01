package com.art.model.supporting.filters;

import com.art.model.Facilities;
import com.art.model.Users;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CashFilter extends AbstractFilter {
    private Users investor;
    private List<Facilities> facilities;
    private int filtered = 0;
}
