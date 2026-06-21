package com.mulemba.booksells.dto;

import com.mulemba.booksells.model.Order;
import com.mulemba.booksells.model.enums.BookFormat;
import com.mulemba.booksells.model.enums.OrderStatus;
import com.mulemba.booksells.model.enums.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(
        String id,
        String userId,
        List<OrderItemResponse> items,
        BigDecimal total,
        BigDecimal subtotal,
        BigDecimal tax,
        BigDecimal shippingCharge,
        BigDecimal discount,
        String date,
        String status,
        ShippingAddressResponse shippingAddress,
        BillingAddressResponse billingAddress,
        String paymentMethod,
        String trackingNumber
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getItems().stream().map(OrderItemResponse::from).toList(),
                order.getTotal(),
                order.getSubtotal(),
                order.getTax(),
                order.getShippingCharge(),
                order.getDiscount(),
                order.getDate().toString(),
                mapStatus(order.getStatus()),
                ShippingAddressResponse.from(order.getShippingAddress()),
                order.getBillingAddress() != null ? BillingAddressResponse.from(order.getBillingAddress()) : null,
                mapPayment(order.getPaymentMethod()),
                order.getTrackingNumber()
        );
    }

    private static String mapStatus(OrderStatus status) {
        return status.name().toLowerCase();
    }

    private static String mapPayment(PaymentMethod method) {
        return switch (method) {
            case CARD -> "card";
            case PAYPAL -> "paypal";
            case GPLAY -> "gplay";
        };
    }

    public record OrderItemResponse(
            String bookId,
            String title,
            BigDecimal price,
            int quantity,
            String selectedFormat,
            String coverImage
    ) {
        public static OrderItemResponse from(com.mulemba.booksells.model.OrderItem item) {
            return new OrderItemResponse(
                    item.getBookId(),
                    item.getTitle(),
                    item.getPrice(),
                    item.getQuantity(),
                    mapFormat(item.getSelectedFormat()),
                    item.getCoverImage()
            );
        }

        private static String mapFormat(BookFormat format) {
            return switch (format) {
                case PHYSICAL -> "physical";
                case PDF -> "pdf";
                case EPUB -> "epub";
            };
        }
    }

    public record ShippingAddressResponse(
            String fullName,
            String street,
            String city,
            String state,
            String zipCode,
            String country,
            String phone
    ) {
        public static ShippingAddressResponse from(Order.ShippingAddress address) {
            if (address == null) return null;
            return new ShippingAddressResponse(
                    address.getFullName(),
                    address.getStreet(),
                    address.getCity(),
                    address.getState(),
                    address.getZipCode(),
                    address.getCountry(),
                    address.getPhone()
            );
        }
    }

    public record BillingAddressResponse(
            String street,
            String city,
            String state,
            String zipCode,
            String country
    ) {
        public static BillingAddressResponse from(Order.BillingAddress address) {
            if (address == null) return null;
            return new BillingAddressResponse(
                    address.getStreet(),
                    address.getCity(),
                    address.getState(),
                    address.getZipCode(),
                    address.getCountry()
            );
        }
    }
}
