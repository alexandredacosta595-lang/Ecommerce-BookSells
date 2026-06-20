package com.mulemba.booksells.repository;

import com.mulemba.booksells.model.LibraryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibraryItemRepository extends JpaRepository<LibraryItem, String> {
    List<LibraryItem> findByUserId(String userId);
    Optional<LibraryItem> findByUserIdAndBookId(String userId, String bookId);
}
