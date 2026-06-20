package com.mulemba.booksells.service;

import com.mulemba.booksells.dto.CreateReviewRequest;
import com.mulemba.booksells.dto.ReviewResponse;
import com.mulemba.booksells.model.Book;
import com.mulemba.booksells.model.Review;
import com.mulemba.booksells.model.User;
import com.mulemba.booksells.repository.BookRepository;
import com.mulemba.booksells.repository.ReviewRepository;
import com.mulemba.booksells.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final UserRepository userRepository;

    public List<ReviewResponse> getByBookId(String bookId) {
        return reviewRepository.findByBookIdOrderByDateDesc(bookId).stream()
                .map(ReviewResponse::from).toList();
    }

    @Transactional
    public ReviewResponse create(String bookId, String userId, CreateReviewRequest request) {
        Book book = bookService.findBook(bookId);
        User user = userRepository.findById(userId).orElseThrow();

        Review review = Review.builder()
                .bookId(bookId)
                .userId(userId)
                .userName(user.getName())
                .userAvatar(user.getAvatar())
                .rating(request.rating())
                .comment(request.comment().trim())
                .date(LocalDate.now())
                .build();

        reviewRepository.save(review);
        recalculateBookRating(book);
        return ReviewResponse.from(review);
    }

    private void recalculateBookRating(Book book) {
        List<Review> reviews = reviewRepository.findByBookIdOrderByDateDesc(book.getId());
        double avg = reviews.stream().mapToInt(Review::getRating).average().orElse(5.0);
        book.setRating(Math.round(avg * 10.0) / 10.0);
        book.setReviewsCount(reviews.size());
        bookRepository.save(book);
    }
}
