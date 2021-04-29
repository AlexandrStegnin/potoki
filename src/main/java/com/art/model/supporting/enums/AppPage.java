package com.art.model.supporting.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * @author Alexandr Stegnin
 */

@Getter
public enum AppPage {

    MONEY(1, "Деньги инвесторов", "money"),
    USERS(2, "Пользователи", "user-list"),
    SALE_PAYMENTS(3, "Выплаты с продажи", "sale-payment-list");

    private final int id;

    private final String title;

    private final String page;

    AppPage(int id, String title, String page) {
        this.id = id;
        this.title = title;
        this.page = page;
    }

    public static AppPage fromId(Integer id) {
        if (Objects.isNull(id)) {
            return null;
        }
        for (AppPage page : values()) {
            if (page.getId() == id) {
                return page;
            }
        }
        return null;
    }

}
