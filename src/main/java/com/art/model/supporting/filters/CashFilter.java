package com.art.model.supporting.filters;

import com.art.model.Facility;
import com.art.model.UnderFacility;
import com.art.model.AppUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CashFilter extends AbstractFilter {
    private AppUser investor;
    private List<Facility> facilities;
    private List<UnderFacility> underFacilities;
    private boolean fromApi;
    private int filtered = 0;
}
