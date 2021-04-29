package com.art.model.supporting;

import com.art.model.supporting.enums.AppPage;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "user_id")
    Long userId;

    @Column(name = "page_id")
    Integer pageId;

    @Column(name = "text")
    String text;

    public void setPageId(AppPage page) {
        this.pageId = page.getId();
    }

}
