package com.art.model.supporting;

/**
 * @author Alexandr Stegnin
 */

public enum TransactionType {

    CREATE(1, "Создание"),
    UPDATE(2, "Изменение"),
    DIVIDE(3, "Разделение"),
    REINVESTMENT(4, "Реинвестирование"),
    CLOSING(5, "Закрытие"),
    UNDEFINED(0, "Не определено");

    private final int id;

    private final String title;

    TransactionType(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public static TransactionType fromTitle(String title) {
        for (TransactionType value : values()) {
            if (value.title.equalsIgnoreCase(title)) {
                return value;
            }
        }
        return UNDEFINED;
    }

}
