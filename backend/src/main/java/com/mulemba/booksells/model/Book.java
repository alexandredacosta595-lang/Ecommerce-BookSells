package com.mulemba.booksells.model;

import com.mulemba.booksells.model.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "livros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String authorId;

    @Column(nullable = false)
    private String categoryId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private double rating;

    private String coverImage;

    private String ebookFileUrl;

    @Column(nullable = false)
    private String coverColor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookType type;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "book_formats", joinColumns = @JoinColumn(name = "book_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "format")
    @Builder.Default
    private List<BookFormat> formats = new ArrayList<>();

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private int pages;

    @Column(nullable = false)
    private LocalDate publishedDate;

    private boolean bestSeller;
    private boolean newRelease;

    @Column(nullable = false)
    private int reviewsCount;

    private String isbn;
    private String publisher;

    private String sellerId;
    @Enumerated(EnumType.STRING)
    private UserType sellerType;
    private String sellerName;
    @Enumerated(EnumType.STRING)
    private BookConditions conditions;
    private String conditionNotes;
    private String city;
    private String state;
    private String whatsapp;
}
