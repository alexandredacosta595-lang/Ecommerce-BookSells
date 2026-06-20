package com.mulemba.booksells.dto;

import com.mulemba.booksells.model.enums.BookFormat;
import com.mulemba.booksells.model.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(
        @NotEmpty List<@Valid OrderItemRequest> items,
        String couponCode,
        @NotNull PaymentMethod paymentMethod,
        @NotNull @Valid ShippingAddressRequest shippingAddress,
        BillingAddressRequest billingAddress
) {
    public record OrderItemRequest(
            @NotNull String bookId,
            @NotNull BookFormat selectedFormat,
            int quantity
    ) {}

    public record ShippingAddressRequest(
            String fullName,
            String street,
            String city,
            String state,
            String zipCode,
            String country,
            String phone
    ) {}

    public record BillingAddressRequest(
            String street,
            String city,
            String state,
            String zipCode,
            String country
    ) {}
}
