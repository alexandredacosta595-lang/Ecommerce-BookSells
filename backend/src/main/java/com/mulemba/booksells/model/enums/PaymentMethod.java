package com.mulemba.booksells.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PaymentMethod {
    CARD,
    PAYPAL,
    GPLAY;

    @JsonCreator
    public static PaymentMethod from(String value) {
        if (value == null) return CARD;
        return PaymentMethod.valueOf(value.toUpperCase());
    }
}
