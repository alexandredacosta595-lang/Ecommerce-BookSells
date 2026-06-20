package com.mulemba.booksells.dto;

import com.mulemba.booksells.model.enums.BookConditions;
import com.mulemba.booksells.model.enums.BookFormat;
import com.mulemba.booksells.model.enums.BookType;

import java.math.BigDecimal;
import java.util.List;

public record BookRequest(
        String title,
        String authorId,
        String categoryId,
        String description,
        BigDecimal price,
        String coverImage,
        String coverColor,
        String ebookFileUrl,
        BookType type,
        List<BookFormat> formats,
        Integer stock,
        Integer pages,
        String publishedDate,
        Boolean bestSeller,
        Boolean newRelease,
        String isbn,
        String publisher,
        String sellerId,
        com.mulemba.booksells.model.enums.UserType sellerType,
        String sellerName,
        BookConditions condition,
        String conditionNotes,
        String city,
        String state,
        String whatsapp
) {}
