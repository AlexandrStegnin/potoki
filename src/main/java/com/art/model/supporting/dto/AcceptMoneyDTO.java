package com.art.model.supporting.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Data
public class AcceptMoneyDTO {

    private List<Long> acceptedMoneyIds = new ArrayList<>();

}
