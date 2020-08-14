package com.art.model.supporting.enums;

/**
 * @author Alexandr Stegnin
 */

public enum MoneyOperation {

    UNDEFINED(0, "UNDEFINED", "Не определена"),
    CREATE(1, "CREATE", "Создание"),
    UPDATE(2, "UPDATE", "Обновление"),
    CLOSE(3, "CLOSE", "ЗАКРЫТИЕ"),
    DOUBLE(4, "DOUBLE", "Разделение"),
    RESALE(5, "RESALE", "Перепродажа доли"),
    CASHING(6, "CASHING", "Вывод");

    private final int id;

    private final String title;

    private final String description;

    MoneyOperation(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public static MoneyOperation fromId(int id) {
        for (MoneyOperation operation : values()) {
            if (operation.getId() == id) {
                return operation;
            }
        }
        return UNDEFINED;
    }

    public static MoneyOperation fromTitle(String title) {
        for (MoneyOperation operation : values()) {
            if (operation.getTitle().equalsIgnoreCase(title)) {
                return operation;
            }
        }
        return UNDEFINED;
    }

}
