package com.mulemba.booksells.service;

import com.mulemba.booksells.dto.AdminStatsResponse;
import com.mulemba.booksells.model.enums.OrderStatus;
import com.mulemba.booksells.repository.BookRepository;
import com.mulemba.booksells.repository.OrderRepository;
import com.mulemba.booksells.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import com.mulemba.booksells.dto.UserResponse;
import com.mulemba.booksells.model.User;
import com.mulemba.booksells.model.enums.UserRole;
import com.mulemba.booksells.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public AdminStatsResponse getStats() {
        BigDecimal revenue = orderRepository.findAll().stream()
                .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                .map(o -> o.getTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long pending = orderRepository.findAll().stream()
                .filter(o -> o.getStatus() == OrderStatus.PROCESSING || o.getStatus() == OrderStatus.PENDING)
                .count();

        return new AdminStatsResponse(
                bookRepository.count(),
                orderRepository.count(),
                userRepository.count(),
                revenue,
                pending
        );
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }

    public UserResponse updateUserRole(String userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(role.equalsIgnoreCase("admin") ? UserRole.ADMIN : UserRole.USER);
        return UserResponse.from(userRepository.save(user));
    }
}
