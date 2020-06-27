package com.art.model.supporting.enums;

/**
 * @author Alexandr Stegnin
 */

public enum TransactionType {

    CREATE(1, "Создание"),
    UPDATE(2, "Изменение"),
    DIVIDE(3, "Разделение"),
    REINVESTMENT_SALE(4, "Реинвестирование с продажи"),
    REINVESTMENT_RENT(7, "Реинвестирование с аренды"),
    CLOSING(5, "Закрытие. Вывод"),
    CLOSING_RESALE(6, "Закрытие. Перепродажа доли"),
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
