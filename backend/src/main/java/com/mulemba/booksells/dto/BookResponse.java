package com.mulemba.booksells.dto;

import com.mulemba.booksells.model.Book;
import com.mulemba.booksells.model.enums.BookConditions;
import com.mulemba.booksells.model.enums.BookFormat;
import com.mulemba.booksells.model.enums.BookType;

import java.math.BigDecimal;
import java.util.List;

public record BookResponse(
        String id,
        String title,
        String authorId,
        String categoryId,
        String description,
        BigDecimal price,
        double rating,
        String coverImage,
        String coverColor,
        String ebookFileUrl,
        String type,
        List<String> formats,
        int stock,
        int pages,
        String publishedDate,
        Boolean bestSeller,
        Boolean newRelease,
        int reviewsCount,
        String isbn,
        String publisher,
        String sellerId,
        String sellerType,
        String sellerName,
        String condition,
        String conditionNotes,
        String city,
        String state,
        String whatsapp
) {
    public static BookResponse from(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthorId(),
                book.getCategoryId(),
                book.getDescription(),
                book.getPrice(),
                book.getRating(),
                book.getCoverImage(),
                book.getCoverColor(),
                book.getEbookFileUrl(),
                mapType(book.getType()),
                book.getFormats().stream().map(BookResponse::mapFormat).toList(),
                book.getStock(),
                book.getPages(),
                book.getPublishedDate().toString(),
                book.isBestSeller(),
                book.isNewRelease(),
                book.getReviewsCount(),
                book.getIsbn(),
                book.getPublisher(),
                book.getSellerId(),
                book.getSellerType() != null ? book.getSellerType().name().toLowerCase() : null,
                book.getSellerName(),
                book.getConditions() != null ? mapCondition(book.getConditions()) : null,
                book.getConditionNotes(),
                book.getCity(),
                book.getState(),
                book.getWhatsapp()
        );
    }

    private static String mapType(BookType type) {
        return switch (type) {
            case PHYSICAL -> "physical";
            case DIGITAL -> "digital";
            case BOTH -> "both";
        };
    }

    private static String mapFormat(BookFormat format) {
        return switch (format) {
            case PHYSICAL -> "physical";
            case PDF -> "pdf";
            case EPUB -> "epub";
        };
    }

    private static String mapCondition(BookConditions condition) {
        return switch (condition) {
            case NEW -> "new";
            case USED_LIKE_NEW -> "used_like_new";
            case USED_VERY_GOOD -> "used_very_good";
            case USED_GOOD -> "used_good";
            case USED_ACCEPTABLE -> "used_acceptable";
        };
    }
}
