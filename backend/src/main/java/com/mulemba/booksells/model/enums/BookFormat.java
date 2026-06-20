package com.mulemba.booksells.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum BookFormat {
    PHYSICAL,
    PDF,
    EPUB;

    @JsonCreator
    public static BookFormat from(String value) {
        if (value == null) return PDF;
        return BookFormat.valueOf(value.toUpperCase());
    }
}
