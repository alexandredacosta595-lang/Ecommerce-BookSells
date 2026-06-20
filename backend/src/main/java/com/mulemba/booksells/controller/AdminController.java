package com.mulemba.booksells.controller;

import com.mulemba.booksells.dto.AdminStatsResponse;
import com.mulemba.booksells.dto.OrderResponse;
import com.mulemba.booksells.dto.UserResponse;
import com.mulemba.booksells.service.AdminService;
import com.mulemba.booksells.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final OrderService orderService;

    @GetMapping("/stats")
    public AdminStatsResponse getStats() {
        return adminService.getStats();
    }

    @GetMapping("/orders")
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        return adminService.getAllUsers();
    }

    @PatchMapping("/users/{id}/role")
    public UserResponse updateUserRole(@PathVariable String id, @RequestParam String role) {
        return adminService.updateUserRole(id, role);
    }
}
