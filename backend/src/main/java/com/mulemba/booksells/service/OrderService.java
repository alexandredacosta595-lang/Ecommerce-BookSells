package com.mulemba.booksells.service;

import com.mulemba.booksells.dto.CreateOrderRequest;
import com.mulemba.booksells.dto.OrderResponse;
import com.mulemba.booksells.exception.BusinessException;
import com.mulemba.booksells.exception.ResourceNotFoundException;
import com.mulemba.booksells.model.*;
import com.mulemba.booksells.model.enums.BookFormat;
import com.mulemba.booksells.model.enums.OrderStatus;
import com.mulemba.booksells.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.08");
    private static final BigDecimal SHIPPING_FEE = new BigDecimal("4.99");
    private static final BigDecimal FREE_SHIPPING_THRESHOLD = new BigDecimal("50");

    private final OrderRepository orderRepository;
    private final BookService bookService;
    private final CouponService couponService;
    private final LibraryService libraryService;

    public List<OrderResponse> getUserOrders(String userId) {
        return orderRepository.findByUserIdOrderByDateDesc(userId).stream()
                .map(OrderResponse::from).toList();
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAllByOrderByDateDesc().stream()
                .map(OrderResponse::from).toList();
    }

    public OrderResponse getById(String orderId, String userId, boolean admin) {
        Order order = findOrder(orderId);
        if (!admin && !order.getUserId().equals(userId)) {
            throw new BusinessException("Pedido não pertence ao utilizador");
        }
        return OrderResponse.from(order);
    }

    @Transactional
    public OrderResponse createOrder(String userId, CreateOrderRequest request) {
        BigDecimal subtotal = BigDecimal.ZERO;
        boolean hasPhysical = false;
        List<OrderItem> items = new ArrayList<>();

        for (CreateOrderRequest.OrderItemRequest itemReq : request.items()) {
            Book book = bookService.findBook(itemReq.bookId());
            if (itemReq.quantity() <= 0) {
                throw new BusinessException("Quantidade inválida");
            }
            if (!book.getFormats().contains(itemReq.selectedFormat())) {
                throw new BusinessException("Formato não disponível para: " + book.getTitle());
            }
            if (itemReq.selectedFormat() == BookFormat.PHYSICAL && book.getStock() < itemReq.quantity()) {
                throw new BusinessException("Stock insuficiente para: " + book.getTitle());
            }

            BigDecimal lineTotal = book.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity()));
            subtotal = subtotal.add(lineTotal);

            if (itemReq.selectedFormat() == BookFormat.PHYSICAL) {
                hasPhysical = true;
                book.setStock(book.getStock() - itemReq.quantity());
            }

            OrderItem item = OrderItem.builder()
                    .bookId(book.getId())
                    .title(book.getTitle())
                    .price(book.getPrice())
                    .quantity(itemReq.quantity())
                    .selectedFormat(itemReq.selectedFormat())
                    .coverImage(book.getCoverImage())
                    .build();
            items.add(item);
        }

        BigDecimal discount = couponService.calculateDiscount(request.couponCode(), subtotal);
        BigDecimal afterDiscount = subtotal.subtract(discount).max(BigDecimal.ZERO);

        BigDecimal shipping = BigDecimal.ZERO;
        if (hasPhysical && afterDiscount.compareTo(FREE_SHIPPING_THRESHOLD) < 0) {
            shipping = SHIPPING_FEE;
        }

        BigDecimal tax = afterDiscount.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = afterDiscount.add(shipping).add(tax);

        String orderId = "ord-" + (1000 + orderRepository.count() + 1);

        Order order = Order.builder()
                .userId(userId)
                .subtotal(subtotal)
                .discount(discount)
                .shippingCharge(shipping)
                .tax(tax)
                .total(total)
                .date(LocalDate.now())
                .status(OrderStatus.PROCESSING)
                .paymentMethod(request.paymentMethod())
                .shippingAddress(mapShipping(request.shippingAddress()))
                .billingAddress(request.billingAddress() != null ? mapBilling(request.billingAddress()) : null)
                .items(new ArrayList<>())
                .build();

        for (OrderItem item : items) {
            item.setOrder(order);
            order.getItems().add(item);
        }

        Order saved = orderRepository.save(order);

        request.items().stream()
                .filter(i -> i.selectedFormat() != BookFormat.PHYSICAL)
                .forEach(i -> libraryService.addToLibrary(userId, i.bookId(), i.selectedFormat()));

        return OrderResponse.from(saved);
    }

    @Transactional
    public OrderResponse updateStatus(String orderId, OrderStatus status) {
        Order order = findOrder(orderId);
        order.setStatus(status);
        if (status == OrderStatus.SHIPPED && order.getTrackingNumber() == null) {
            order.setTrackingNumber("TRK-" + System.currentTimeMillis());
        }
        return OrderResponse.from(orderRepository.save(order));
    }

    private Order findOrder(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));
    }

    private Order.ShippingAddress mapShipping(CreateOrderRequest.ShippingAddressRequest req) {
        return Order.ShippingAddress.builder()
                .fullName(req.fullName())
                .street(req.street())
                .city(req.city())
                .state(req.state())
                .zipCode(req.zipCode())
                .country(req.country())
                .phone(req.phone())
                .build();
    }

    private Order.BillingAddress mapBilling(CreateOrderRequest.BillingAddressRequest req) {
        return Order.BillingAddress.builder()
                .street(req.street())
                .city(req.city())
                .state(req.state())
                .zipCode(req.zipCode())
                .country(req.country())
                .build();
    }
}
