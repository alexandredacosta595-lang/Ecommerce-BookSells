package com.mulemba.booksells.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum OrderStatus {
    PENDING,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED;

    @JsonCreator
    public static OrderStatus from(String value) {
        return OrderStatus.valueOf(value.toUpperCase());
    }
}
