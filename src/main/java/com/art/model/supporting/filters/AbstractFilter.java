package com.art.model.supporting.filters;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public abstract class AbstractFilter {

    private Date fromDate;

    private Date toDate;

    private String facility;

    private String underFacility;

    private List<String> investors;

    private boolean allRows = false;

    private int pageNumber = 0;

    private int pageSize = 100;

    private int total;

}
