package com.art.model.supporting;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Класс для хранения в базе фильтров,
 * которые в последний раз использовались
 *
 * @author Alexandr Stegnin
 */

@Data
@Entity
@Table(name = "app_filter")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppFilter {

    @Id
    Long id;

    @Column(name = "user_id")
    Long userId;

    @Column(name = "page_id")
    Integer pageId;

    @Column(name = "text")
    String text;

}
