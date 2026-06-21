package com.mulemba.booksells.service;

import com.mulemba.booksells.dto.BookRequest;
import com.mulemba.booksells.dto.BookResponse;
import com.mulemba.booksells.dto.PageResponse;
import com.mulemba.booksells.exception.ResourceNotFoundException;
import com.mulemba.booksells.model.Book;
import com.mulemba.booksells.model.Category;
import com.mulemba.booksells.model.enums.BookType;
import com.mulemba.booksells.repository.BookRepository;
import com.mulemba.booksells.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BookService bookService;

    private Book testBook;
    private BookRequest bookRequest;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setId("bk-123");
        testBook.setTitle("O Vendedor de Passados");
        testBook.setCategoryId("cat-1");
        testBook.setPrice(new BigDecimal("5000"));
        testBook.setFormats(List.of(com.mulemba.booksells.model.enums.BookFormat.PHYSICAL));
        testBook.setType(BookType.PHYSICAL);
        testBook.setPublishedDate(java.time.LocalDate.now());

        bookRequest = new BookRequest(
                "O Vendedor de Passados", "aut-1", "cat-1", "Sinopse", new BigDecimal("5000"),
                null, null, null, BookType.PHYSICAL, List.of(com.mulemba.booksells.model.enums.BookFormat.PHYSICAL), 10, 200, "2024-01-01",
                false, true, "123456", "Editora", "sel-1", com.mulemba.booksells.model.enums.UserType.PUBLISHER, "Sede", com.mulemba.booksells.model.enums.BookConditions.NEW, null, "Luanda", "LUA", "923"
        );

        testCategory = new Category();
        testCategory.setId("cat-1");
        testCategory.setBooksCount(0);
    }

    @Test
    void getById_ShouldReturnBook_WhenFound() {
        when(bookRepository.findById("bk-123")).thenReturn(Optional.of(testBook));

        BookResponse response = bookService.getById("bk-123");

        assertNotNull(response);
        assertEquals("O Vendedor de Passados", response.title());
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(bookRepository.findById("invalid")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.getById("invalid"));
    }

    @Test
    void create_ShouldSaveBookAndIncrementCategory() {
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        when(categoryRepository.findById("cat-1")).thenReturn(Optional.of(testCategory));

        BookResponse response = bookService.create(bookRequest);

        assertNotNull(response);
        assertEquals("O Vendedor de Passados", response.title());
        
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(categoryRepository, times(1)).save(testCategory);
        assertEquals(1, testCategory.getBooksCount());
    }

    @Test
    void delete_ShouldRemoveBookAndDecrementCategory() {
        when(bookRepository.findById("bk-123")).thenReturn(Optional.of(testBook));
        testCategory.setBooksCount(5);
        when(categoryRepository.findById("cat-1")).thenReturn(Optional.of(testCategory));

        bookService.delete("bk-123");

        verify(bookRepository, times(1)).delete(testBook);
        verify(categoryRepository, times(1)).save(testCategory);
        assertEquals(4, testCategory.getBooksCount());
    }

    @Test
    void search_ShouldReturnPageResponse() {
        Page<Book> page = new PageImpl<>(List.of(testBook));
        when(bookRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(page);

        PageResponse<BookResponse> response = bookService.search(
                "Vendedor", "cat-1", null, "all", null, null, null, "newest", 1, 10
        );

        assertNotNull(response);
        assertEquals(1, response.content().size());
        assertEquals(1, response.totalElements());
    }
}
