package com.mulemba.booksells.service;

import com.mulemba.booksells.dto.*;
import com.mulemba.booksells.exception.ResourceNotFoundException;
import com.mulemba.booksells.model.Book;
import com.mulemba.booksells.model.enums.BookType;
import com.mulemba.booksells.repository.BookRepository;
import com.mulemba.booksells.repository.CategoryRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    public PageResponse<BookResponse> search(
            String search,
            String categoryId,
            String authorId,
            String type,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Double minRating,
            String sortBy,
            int page,
            int size
    ) {
        Specification<Book> spec = buildSpec(search, categoryId, authorId, type, minPrice, maxPrice, minRating);
        Sort sort = buildSort(sortBy);
        Page<Book> result = bookRepository.findAll(spec, PageRequest.of(Math.max(page - 1, 0), size, sort));

        return new PageResponse<>(
                result.getContent().stream().map(BookResponse::from).toList(),
                page,
                size,
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    public BookResponse getById(String id) {
        return BookResponse.from(findBook(id));
    }

    public List<BookResponse> getAll() {
        return bookRepository.findAll().stream().map(BookResponse::from).toList();
    }

    @Transactional
    public BookResponse create(BookRequest request) {
        Book book = mapRequestToEntity(request);
        book.setReviewsCount(0);
        book.setRating(5.0);
        Book saved = bookRepository.save(book);
        incrementCategoryCount(saved.getCategoryId(), 1);
        return BookResponse.from(saved);
    }

    @Transactional
    public BookResponse update(String id, BookRequest request) {
        Book book = findBook(id);
        String oldCategory = book.getCategoryId();

        applyRequest(book, request);
        Book saved = bookRepository.save(book);

        if (!oldCategory.equals(saved.getCategoryId())) {
            incrementCategoryCount(oldCategory, -1);
            incrementCategoryCount(saved.getCategoryId(), 1);
        }
        return BookResponse.from(saved);
    }

    @Transactional
    public void delete(String id) {
        Book book = findBook(id);
        bookRepository.delete(book);
        incrementCategoryCount(book.getCategoryId(), -1);
    }

    public Book findBook(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado"));
    }

    private Specification<Book> buildSpec(
            String search, String categoryId, String authorId, String type,
            BigDecimal minPrice, BigDecimal maxPrice, Double minRating
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.isBlank()) {
                String pattern = "%" + search.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), pattern),
                        cb.like(cb.lower(root.get("description")), pattern)
                ));
            }
            if (categoryId != null && !categoryId.isBlank()) {
                predicates.add(cb.equal(root.get("categoryId"), categoryId));
            }
            if (authorId != null && !authorId.isBlank()) {
                predicates.add(cb.equal(root.get("authorId"), authorId));
            }
            if (type != null && !type.isBlank() && !"all".equalsIgnoreCase(type)) {
                BookType bookType = switch (type.toLowerCase()) {
                    case "physical" -> BookType.PHYSICAL;
                    case "digital" -> BookType.DIGITAL;
                    default -> BookType.BOTH;
                };
                if ("physical".equalsIgnoreCase(type)) {
                    predicates.add(cb.or(
                            cb.equal(root.get("type"), BookType.PHYSICAL),
                            cb.equal(root.get("type"), BookType.BOTH)
                    ));
                } else if ("digital".equalsIgnoreCase(type)) {
                    predicates.add(cb.or(
                            cb.equal(root.get("type"), BookType.DIGITAL),
                            cb.equal(root.get("type"), BookType.BOTH)
                    ));
                } else {
                    predicates.add(cb.equal(root.get("type"), bookType));
                }
            }
            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            if (minRating != null && minRating > 0) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("rating"), minRating));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Sort buildSort(String sortBy) {
        if (sortBy == null) return Sort.by(Sort.Direction.DESC, "bestSeller");
        return switch (sortBy) {
            case "price-low" -> Sort.by(Sort.Direction.ASC, "price");
            case "price-high" -> Sort.by(Sort.Direction.DESC, "price");
            case "rating" -> Sort.by(Sort.Direction.DESC, "rating");
            case "newest" -> Sort.by(Sort.Direction.DESC, "publishedDate");
            default -> Sort.by(Sort.Direction.DESC, "bestSeller");
        };
    }

    private Book mapRequestToEntity(BookRequest request) {
        Book book = new Book();
        applyRequest(book, request);
        return book;
    }

    private void applyRequest(Book book, BookRequest request) {
        book.setTitle(request.title());
        book.setAuthorId(request.authorId());
        book.setCategoryId(request.categoryId());
        book.setDescription(request.description());
        book.setPrice(request.price());
        book.setCoverImage(request.coverImage());
        book.setEbookFileUrl(request.ebookFileUrl());
        book.setCoverColor(request.coverColor() != null ? request.coverColor() : "from-blue-600 to-indigo-805");
        book.setType(request.type());
        book.setFormats(request.formats() != null ? new ArrayList<>(request.formats()) : new ArrayList<>());
        book.setStock(request.stock() != null ? request.stock() : 0);
        book.setPages(request.pages() != null ? request.pages() : 0);
        book.setPublishedDate(request.publishedDate() != null
                ? LocalDate.parse(request.publishedDate()) : LocalDate.now());
        book.setBestSeller(Boolean.TRUE.equals(request.bestSeller()));
        book.setNewRelease(Boolean.TRUE.equals(request.newRelease()));
        book.setIsbn(request.isbn());
        book.setPublisher(request.publisher());
        book.setSellerId(request.sellerId());
        book.setSellerType(request.sellerType());
        book.setSellerName(request.sellerName());
        book.setConditions(request.condition());
        book.setConditionNotes(request.conditionNotes());
        book.setCity(request.city());
        book.setState(request.state());
        book.setWhatsapp(request.whatsapp());
    }

    private void incrementCategoryCount(String categoryId, int delta) {
        categoryRepository.findById(categoryId).ifPresent(cat -> {
            cat.setBooksCount(Math.max(0, cat.getBooksCount() + delta));
            categoryRepository.save(cat);
        });
    }
}
