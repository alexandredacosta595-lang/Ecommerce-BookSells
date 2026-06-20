package com.mulemba.booksells.dto;

import com.mulemba.booksells.model.Category;

public record CategoryResponse(
        String name,
        String slug,
        String iconName,
        int booksCount
) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.getName(),
                category.getSlug(),
                category.getIconName(),
                category.getBooksCount()
        );
    }
}
