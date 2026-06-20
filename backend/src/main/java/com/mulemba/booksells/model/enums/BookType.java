package com.mulemba.booksells.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum BookType {
    PHYSICAL,
    DIGITAL,
    BOTH;

    @JsonCreator
    public static BookType from(String value) {
        if (value == null) return BOTH;
        return BookType.valueOf(value.toUpperCase());
    }
}
