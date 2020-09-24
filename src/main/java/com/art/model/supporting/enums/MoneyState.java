package com.art.model.supporting.enums;

import lombok.Getter;

/**
 * @author Alexandr Stegnin
 */

@Getter
public enum MoneyState {

    ACTIVE(1, "Активная"),
    ARCHIVE(2, "Архивная"),
    MATCHING(3, "На согласовании");

    private final int id;

    private final String title;

    MoneyState(int id, String title) {
        this.id = id;
        this.title = title;
    }

}
