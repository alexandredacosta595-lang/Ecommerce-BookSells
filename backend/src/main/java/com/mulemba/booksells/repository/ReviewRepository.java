package com.mulemba.booksells.repository;

import com.mulemba.booksells.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, String> {
    List<Review> findByBookIdOrderByDateDesc(String bookId);
}
