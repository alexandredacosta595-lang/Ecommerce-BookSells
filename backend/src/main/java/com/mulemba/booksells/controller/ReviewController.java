package com.mulemba.booksells.controller;

import com.mulemba.booksells.dto.CreateReviewRequest;
import com.mulemba.booksells.dto.ReviewResponse;
import com.mulemba.booksells.security.SecurityUtils;
import com.mulemba.booksells.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/book/{bookId}")
    public List<ReviewResponse> getByBook(@PathVariable String bookId) {
        return reviewService.getByBookId(bookId);
    }

    @PostMapping("/book/{bookId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse create(@PathVariable String bookId, @Valid @RequestBody CreateReviewRequest request) {
        return reviewService.create(bookId, SecurityUtils.getCurrentUserId(), request);
    }
}
