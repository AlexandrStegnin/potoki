package com.art.model.supporting.enums;

/**
 * @author Alexandr Stegnin
 */

public enum MoneyOperation {

    UNDEFINED(0, "UNDEFINED"),
    CREATE(1, "CREATE"),
    UPDATE(2, "UPDATE"),
    CLOSE(3, "CLOSE"),
    DOUBLE(4, "DOUBLE"),
    RESALE(5, "RESALE");

    private final int id;

    private final String title;

    MoneyOperation(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
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
