package com.mulemba.booksells.controller;

import com.mulemba.booksells.dto.CreateOrderRequest;
import com.mulemba.booksells.dto.OrderResponse;
import com.mulemba.booksells.model.enums.OrderStatus;
import com.mulemba.booksells.security.SecurityUtils;
import com.mulemba.booksells.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderResponse> getMyOrders() {
        return orderService.getUserOrders(SecurityUtils.getCurrentUserId());
    }

    @GetMapping("/{id}")
    public OrderResponse getById(@PathVariable String id) {
        return orderService.getById(id, SecurityUtils.getCurrentUserId(), SecurityUtils.isAdmin());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.createOrder(SecurityUtils.getCurrentUserId(), request);
    }

    @PatchMapping("/{id}/status")
    public OrderResponse updateStatus(@PathVariable String id, @RequestBody Map<String, String> body) {
        OrderStatus status = OrderStatus.valueOf(body.get("status").toUpperCase());
        return orderService.updateStatus(id, status);
    }
}
