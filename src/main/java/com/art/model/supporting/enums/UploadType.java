package com.art.model.supporting.enums;

/**
 * Вид загружаемого Excel файла
 *
 * @author Alexandr Stegnin
 */

public enum UploadType {

    RENT(1, "Выплаты с аренды"),
    SALE(2, "Выплаты с продажи");

    private final int id;

    private final String title;

    UploadType(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
