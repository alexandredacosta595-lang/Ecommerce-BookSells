package com.mulemba.booksells.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UserType {
    READER,
    BOOKSTORE,
    PUBLISHER,
    AUTHOR;

    @JsonCreator
    public static UserType from(String value) {
        if (value == null) return READER;
        return UserType.valueOf(value.toUpperCase());
    }
}
