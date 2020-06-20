package com.art.model.supporting.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

/**
 * Модель для работы с приложениями к договорам через скрипты (jQuery)
 *
 * @author Alexandr Stegnin
 */

@Getter
@Setter
public class AnnexModel {

    private List<BigInteger> annexIdList;

}
