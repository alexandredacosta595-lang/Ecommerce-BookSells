package com.mulemba.booksells.service;

import com.mulemba.booksells.dto.AuthorResponse;
import com.mulemba.booksells.dto.CategoryResponse;
import com.mulemba.booksells.exception.ResourceNotFoundException;
import com.mulemba.booksells.model.Author;
import com.mulemba.booksells.model.Category;
import com.mulemba.booksells.repository.AuthorRepository;
import com.mulemba.booksells.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;

    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll().stream().map(CategoryResponse::from).toList();
    }

    public List<AuthorResponse> getAuthors() {
        return authorRepository.findAll().stream().map(AuthorResponse::from).toList();
    }

    public AuthorResponse getAuthor(String id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado"));
        return AuthorResponse.from(author);
    }
}
