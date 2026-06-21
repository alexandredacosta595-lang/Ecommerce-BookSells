package com.mulemba.booksells.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulemba.booksells.dto.CreateOrderRequest;
import com.mulemba.booksells.dto.OrderResponse;
import com.mulemba.booksells.security.SecurityUtils;
import com.mulemba.booksells.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private MockedStatic<SecurityUtils> mockedSecurityUtils;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class);
        mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn("usr-123");
        mockedSecurityUtils.when(SecurityUtils::isAdmin).thenReturn(false);
    }

    @AfterEach
    void tearDown() {
        mockedSecurityUtils.close();
    }

    @Test
    void getMyOrders_ShouldReturnList() throws Exception {
        OrderResponse order = new OrderResponse(
                "ord-1", "usr-123", Collections.emptyList(), new BigDecimal("108"), new BigDecimal("100"),
                new BigDecimal("8"), BigDecimal.ZERO, BigDecimal.ZERO,
                "2024-01-01", "processing", null, null, "card", null
        );

        when(orderService.getUserOrders("usr-123")).thenReturn(List.of(order));

        mockMvc.perform(get("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("ord-1"));
    }

    @Test
    void getById_ShouldReturnOrder() throws Exception {
        OrderResponse order = new OrderResponse(
                "ord-1", "usr-123", Collections.emptyList(), new BigDecimal("108"), new BigDecimal("100"),
                new BigDecimal("8"), BigDecimal.ZERO, BigDecimal.ZERO,
                "2024-01-01", "processing", null, null, "card", null
        );

        when(orderService.getById("ord-1", "usr-123", false)).thenReturn(order);

        mockMvc.perform(get("/api/orders/ord-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("ord-1"));
    }

    @Test
    void create_ShouldReturnCreatedOrder() throws Exception {
        CreateOrderRequest.ShippingAddressRequest shippingReq = new CreateOrderRequest.ShippingAddressRequest(
                "Name", "Street", "City", "State", "0000", "Country", "999"
        );
        CreateOrderRequest.OrderItemRequest itemReq = new CreateOrderRequest.OrderItemRequest("bk-1", com.mulemba.booksells.model.enums.BookFormat.PHYSICAL, 1);
        CreateOrderRequest request = new CreateOrderRequest(
                List.of(itemReq), null, com.mulemba.booksells.model.enums.PaymentMethod.CARD, shippingReq, null
        );

        OrderResponse order = new OrderResponse(
                "ord-1", "usr-123", Collections.emptyList(), new BigDecimal("108"), new BigDecimal("100"),
                new BigDecimal("8"), BigDecimal.ZERO, BigDecimal.ZERO,
                "2024-01-01", "processing", null, null, "card", null
        );

        when(orderService.createOrder(eq("usr-123"), any(CreateOrderRequest.class))).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("ord-1"));
    }

    @Test
    void updateStatus_ShouldReturnUpdatedOrder() throws Exception {
        OrderResponse order = new OrderResponse(
                "ord-1", "usr-123", Collections.emptyList(), new BigDecimal("108"), new BigDecimal("100"),
                new BigDecimal("8"), BigDecimal.ZERO, BigDecimal.ZERO,
                "2024-01-01", "shipped", null, null, "card", "TRK-123"
        );

        when(orderService.updateStatus(eq("ord-1"), any())).thenReturn(order);

        mockMvc.perform(patch("/api/orders/ord-1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("status", "SHIPPED"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("shipped"));
    }
}
