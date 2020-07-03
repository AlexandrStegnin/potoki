package com.art.model.supporting.enums;

/**
 * @author Alexandr Stegnin
 */

public enum UserStatus {

    ALL(1, "Все"),
    CONFIRMED(2, "Подтверждён"),
    NOT_CONFIRMED(3, "Не подтверждён");

    private final int id;

    private final String title;

    UserStatus(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public static UserStatus fromId(int id) {
        for (UserStatus status : values()) {
            if (status.getId() == id) {
                return status;
            }
        }
        return ALL;
    }

    public static UserStatus fromTitle(String title) {
        for (UserStatus status : values()) {
            if (status.getTitle().equalsIgnoreCase(title)) {
                return status;
            }
        }
        return ALL;
    }

}
