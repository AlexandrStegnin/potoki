package com.art.model.supporting.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * DTO для хранения информации о том, с кем заключён договор (ЮЛ/ФЛ)
 *
 * @author Alexandr Stegnin
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAgreementDTO {

    @NotBlank(message = "Название объекта должно быть указано")
    Long facilityId;

    @NotBlank(message = "С кем заключён договор должно быть заполнено")
    String concludedWith;

    @NotBlank(message = "От кого заключён договор должно быть заполнено")
    Long concludedFrom;

    @NotNull(message = "Налоговая ставка должна быть заполнена")
    Double taxRate;

}
