package com.art.model.supporting.filters;

import com.art.model.Facility;
import com.art.model.UnderFacilities;
import com.art.model.Users;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CashFilter extends AbstractFilter {
    private Users investor;
    private List<Facility> facilities;
    private List<UnderFacilities> underFacilities;
    private int filtered = 0;
}
