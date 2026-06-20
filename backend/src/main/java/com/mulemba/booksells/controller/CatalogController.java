package com.mulemba.booksells.controller;

import com.mulemba.booksells.dto.AuthorResponse;
import com.mulemba.booksells.dto.CategoryResponse;
import com.mulemba.booksells.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping("/api/categories")
    public List<CategoryResponse> getCategories() {
        return catalogService.getCategories();
    }

    @GetMapping("/api/authors")
    public List<AuthorResponse> getAuthors() {
        return catalogService.getAuthors();
    }

    @GetMapping("/api/authors/{id}")
    public AuthorResponse getAuthor(@PathVariable String id) {
        return catalogService.getAuthor(id);
    }
}
