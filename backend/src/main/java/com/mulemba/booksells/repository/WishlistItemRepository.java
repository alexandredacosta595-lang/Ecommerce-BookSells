package com.mulemba.booksells.repository;

import com.mulemba.booksells.model.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, String> {
    List<WishlistItem> findByUserId(String userId);
    Optional<WishlistItem> findByUserIdAndBookId(String userId, String bookId);
    boolean existsByUserIdAndBookId(String userId, String bookId);
}
