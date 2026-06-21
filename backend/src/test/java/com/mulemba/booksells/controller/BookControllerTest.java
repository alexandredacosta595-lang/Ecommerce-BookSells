package com.mulemba.booksells.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulemba.booksells.dto.BookRequest;
import com.mulemba.booksells.dto.BookResponse;
import com.mulemba.booksells.dto.PageResponse;
import com.mulemba.booksells.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    void search_ShouldReturnPageOfBooks() throws Exception {
        BookResponse book = new BookResponse(
                "bk-1", "A Morte do Velho", "aut-1", "cat-1", "Desc", new BigDecimal("3500"),
                5.0, null, null, null, "physical", List.of("physical"), 10, 150, "2024-01-01",
                false, true, 0, "123", "Editora", "sel-1", "author", "Sede", "new", null, "Luanda", "LUA", "923"
        );
        PageResponse<BookResponse> pageResponse = new PageResponse<>(List.of(book), 1, 10, 1L, 1);

        when(bookService.search(any(), any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/api/books")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("bk-1"))
                .andExpect(jsonPath("$.content[0].title").value("A Morte do Velho"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getById_ShouldReturnBook() throws Exception {
        BookResponse book = new BookResponse(
                "bk-1", "O Livro", "aut-1", "cat-1", "Desc", new BigDecimal("3500"),
                5.0, null, null, null, "physical", List.of("physical"), 10, 150, "2024-01-01",
                false, true, 0, "123", "Editora", "sel-1", "author", "Sede", "new", null, "Luanda", "LUA", "923"
        );

        when(bookService.getById("bk-1")).thenReturn(book);

        mockMvc.perform(get("/api/books/bk-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("O Livro"));
    }

    @Test
    void create_ShouldReturnCreatedBook() throws Exception {
        BookRequest request = new BookRequest(
                "Novo Livro", "aut-1", "cat-1", "Sinopse", new BigDecimal("5000"),
                null, null, null, com.mulemba.booksells.model.enums.BookType.PHYSICAL, List.of(com.mulemba.booksells.model.enums.BookFormat.PHYSICAL), 10, 200, "2024-01-01",
                false, true, "123456", "Editora", "sel-1", com.mulemba.booksells.model.enums.UserType.PUBLISHER, "Sede", com.mulemba.booksells.model.enums.BookConditions.NEW, null, "Luanda", "LUA", "923"
        );

        BookResponse response = new BookResponse(
                "bk-new", "Novo Livro", "aut-1", "cat-1", "Sinopse", new BigDecimal("5000"),
                0.0, null, null, null, "physical", List.of("physical"), 10, 200, "2024-01-01",
                false, true, 0, "123456", "Editora", "sel-1", "publisher", "Sede", "new", null, "Luanda", "LUA", "923"
        );

        when(bookService.create(any(BookRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("bk-new"))
                .andExpect(jsonPath("$.title").value("Novo Livro"));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(bookService).delete("bk-1");

        mockMvc.perform(delete("/api/books/bk-1"))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).delete("bk-1");
    }
}
