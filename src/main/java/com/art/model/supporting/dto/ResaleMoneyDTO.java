package com.art.model.supporting.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

/**
 * @author Alexandr Stegnin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class ResaleMoneyDTO extends MoneyDTO {

  Long id;
  Long buyerId;
  Date dateClose;
  Long typeCloseId;
  Date realDateGiven;

}
