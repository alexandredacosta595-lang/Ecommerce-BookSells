package com.mulemba.booksells.dto;

import com.mulemba.booksells.model.Review;

public record ReviewResponse(

        String bookId,
        String userName,
        String userAvatar,
        int rating,
        String comment,
        String date
) {
    public static ReviewResponse from(Review review) {
        return new ReviewResponse(

                review.getBookId(),
                review.getUserName(),
                review.getUserAvatar(),
                review.getRating(),
                review.getComment(),
                review.getDate().toString()
        );
    }
}
