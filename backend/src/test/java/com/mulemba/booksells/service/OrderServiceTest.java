package com.mulemba.booksells.service;

import com.mulemba.booksells.dto.CreateOrderRequest;
import com.mulemba.booksells.dto.OrderResponse;
import com.mulemba.booksells.exception.BusinessException;
import com.mulemba.booksells.model.Book;
import com.mulemba.booksells.model.Order;
import com.mulemba.booksells.model.enums.BookFormat;
import com.mulemba.booksells.model.enums.OrderStatus;
import com.mulemba.booksells.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private BookService bookService;

    @Mock
    private CouponService couponService;

    @Mock
    private LibraryService libraryService;

    @InjectMocks
    private OrderService orderService;

    private Book testPhysicalBook;
    private Book testDigitalBook;
    private CreateOrderRequest.ShippingAddressRequest shippingReq;

    @BeforeEach
    void setUp() {
        testPhysicalBook = new Book();
        testPhysicalBook.setId("bk-1");
        testPhysicalBook.setTitle("Physical Book");
        testPhysicalBook.setPrice(new BigDecimal("10000")); // 10000 Kz
        testPhysicalBook.setStock(5);
        testPhysicalBook.setFormats(List.of(BookFormat.PHYSICAL, BookFormat.PDF));

        testDigitalBook = new Book();
        testDigitalBook.setId("bk-2");
        testDigitalBook.setTitle("Ebook Book");
        testDigitalBook.setPrice(new BigDecimal("5000")); // 5000 Kz
        testDigitalBook.setStock(0);
        testDigitalBook.setFormats(List.of(BookFormat.EPUB));

        shippingReq = new CreateOrderRequest.ShippingAddressRequest(
                "John Doe", "Street A", "Luanda", "LUA", "0000", "Angola", "999"
        );
    }

    @Test
    void createOrder_WithPhysicalBook_UnderThreshold_ShouldAddShippingFee() {
        // Physical book price: 10,000 Kz. Threshold is 50,000 Kz. Shipping should be 2000 Kz.
        CreateOrderRequest request = new CreateOrderRequest(
                List.of(new CreateOrderRequest.OrderItemRequest("bk-1", BookFormat.PHYSICAL, 1)),
                null, com.mulemba.booksells.model.enums.PaymentMethod.CARD, shippingReq, null
        );

        when(bookService.findBook("bk-1")).thenReturn(testPhysicalBook);
        when(couponService.calculateDiscount(any(), any())).thenReturn(BigDecimal.ZERO);
        when(orderRepository.count()).thenReturn(10L);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        OrderResponse response = orderService.createOrder("user-1", request);

        assertNotNull(response);
        assertEquals(new BigDecimal("10000"), response.subtotal());
        assertEquals(new BigDecimal("2000"), response.shippingCharge()); // Frete de 2000 Kz
        // Tax 8% of 10000 = 800
        assertEquals(new BigDecimal("800.00"), response.tax());
        // Total = 10000 + 2000 + 800 = 12800
        assertEquals(new BigDecimal("12800.00"), response.total());
        assertEquals(4, testPhysicalBook.getStock()); // Stock reduced
    }

    @Test
    void createOrder_WithPhysicalBook_OverThreshold_ShouldBeFreeShipping() {
        // Physical book price: 10,000 Kz * 6 = 60,000 Kz. Threshold is 50,000 Kz. Free shipping.
        CreateOrderRequest request = new CreateOrderRequest(
                List.of(new CreateOrderRequest.OrderItemRequest("bk-1", BookFormat.PHYSICAL, 6)),
                null, com.mulemba.booksells.model.enums.PaymentMethod.CARD, shippingReq, null
        );
        testPhysicalBook.setStock(10); // Ensure enough stock

        when(bookService.findBook("bk-1")).thenReturn(testPhysicalBook);
        when(couponService.calculateDiscount(any(), any())).thenReturn(BigDecimal.ZERO);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        OrderResponse response = orderService.createOrder("user-1", request);

        assertNotNull(response);
        assertEquals(new BigDecimal("60000"), response.subtotal());
        assertEquals(new BigDecimal("0"), response.shippingCharge()); // Frete Grátis
    }

    @Test
    void createOrder_DigitalBookOnly_ShouldNotAddShippingFee() {
        // Digital book price: 5,000 Kz. No physical items.
        CreateOrderRequest request = new CreateOrderRequest(
                List.of(new CreateOrderRequest.OrderItemRequest("bk-2", BookFormat.EPUB, 1)),
                null, com.mulemba.booksells.model.enums.PaymentMethod.CARD, shippingReq, null
        );

        when(bookService.findBook("bk-2")).thenReturn(testDigitalBook);
        when(couponService.calculateDiscount(any(), any())).thenReturn(BigDecimal.ZERO);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        OrderResponse response = orderService.createOrder("user-1", request);

        assertNotNull(response);
        assertEquals(new BigDecimal("5000"), response.subtotal());
        assertEquals(new BigDecimal("0"), response.shippingCharge()); // No shipping for digital
        verify(libraryService, times(1)).addToLibrary("user-1", "bk-2", BookFormat.EPUB);
    }

    @Test
    void createOrder_ShouldThrowException_WhenStockIsInsufficient() {
        // Physical book stock: 5. Requesting: 10.
        CreateOrderRequest request = new CreateOrderRequest(
                List.of(new CreateOrderRequest.OrderItemRequest("bk-1", BookFormat.PHYSICAL, 10)),
                null, com.mulemba.booksells.model.enums.PaymentMethod.CARD, shippingReq, null
        );

        when(bookService.findBook("bk-1")).thenReturn(testPhysicalBook);

        BusinessException ex = assertThrows(BusinessException.class, () -> orderService.createOrder("user-1", request));
        assertTrue(ex.getMessage().contains("Stock insuficiente"));
    }

    @Test
    void updateStatus_ShouldChangeStatusAndGenerateTracking() {
        Order order = Order.builder().id("ord-123").status(OrderStatus.PROCESSING).date(LocalDate.now()).paymentMethod(com.mulemba.booksells.model.enums.PaymentMethod.CARD).build();
        when(orderRepository.findById("ord-123")).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        OrderResponse response = orderService.updateStatus("ord-123", OrderStatus.SHIPPED);

        assertEquals("shipped", response.status());
        assertNotNull(response.trackingNumber()); // Tracking generated
    }
}
