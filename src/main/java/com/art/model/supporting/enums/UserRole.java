package com.art.model.supporting.enums;

/**
 * @author Alexandr Stegnin
 */

public enum UserRole {

    UNDEFINED(0, "Не определена"),
    ROLE_INVESTOR(1, "Инвестор"),
    ROLE_ADMIN(2, "Админ");

    private final int id;

    private final String name;

    UserRole(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static UserRole fromId(int id) {
        for (UserRole role : values()) {
            if (role.getId() == id) {
                return role;
            }
        }
        return UNDEFINED;
    }

    public static UserRole fromName(String name) {
        for (UserRole role : values()) {
            if (role.getName().equalsIgnoreCase(name)) {
                return role;
            }
        }
        return UNDEFINED;
    }

}
