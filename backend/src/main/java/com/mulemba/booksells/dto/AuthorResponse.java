package com.mulemba.booksells.dto;

import com.mulemba.booksells.model.Author;

public record AuthorResponse(
        String name,
        String bio,
        String avatar,
        int booksCount
) {
    public static AuthorResponse from(Author author) {
        return new AuthorResponse(
                author.getName(),
                author.getBio(),
                author.getAvatar(),
                author.getBooksCount()
        );
    }
}
