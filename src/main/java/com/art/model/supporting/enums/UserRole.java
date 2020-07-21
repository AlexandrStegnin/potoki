package com.art.model.supporting.enums;

import com.art.model.supporting.filters.Filterable;

/**
 * @author Alexandr Stegnin
 */

public enum UserRole implements Filterable {

    UNDEFINED(0, "Не определена", "ANONYMOUS"),
    ROLE_INVESTOR(1, "Инвестор", "ROLE_INVESTOR"),
    ROLE_MANAGER(2, "Управляющий", "ROLE_MANAGER"),
    ROLE_ADMIN(3, "Админ", "ROLE_ADMIN");

    private final int id;

    private final String title;

    private final String systemName;

    UserRole(int id, String title, String systemName) {
        this.id = id;
        this.title = title;
        this.systemName = systemName;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public static UserRole fromId(int id) {
        for (UserRole role : values()) {
            if (role.getId() == id) {
                return role;
            }
        }
        return UNDEFINED;
    }

    public static UserRole fromTitle(String title) {
        for (UserRole role : values()) {
            if (role.getTitle().equalsIgnoreCase(title)) {
                return role;
            }
        }
        return UNDEFINED;
    }

    public String getSystemName() {
        return systemName;
    }
}
