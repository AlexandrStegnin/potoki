package com.art.model.supporting;

/**
 * Вид денег для транзакций
 *
 * @author Alexandr Stegnin
 */

public enum CashType {

    NEW(1, "Новая сумма"),
    OLD(2, "Старая сумма"),
    UNDEFINED(0, "Не определено");

    private final int id;

    private final String title;

    CashType(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public static CashType fromTitle(String title) {
        for (CashType value : values()) {
            if (value.title.equalsIgnoreCase(title)) {
                return value;
            }
        }
        return UNDEFINED;
    }

}
