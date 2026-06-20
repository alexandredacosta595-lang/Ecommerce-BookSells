package com.mulemba.booksells.repository;

import com.mulemba.booksells.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUserIdOrderByDateDesc(String userId);
    List<Order> findAllByOrderByDateDesc();
}
