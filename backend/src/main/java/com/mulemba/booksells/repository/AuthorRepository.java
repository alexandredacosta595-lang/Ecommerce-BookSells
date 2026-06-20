package com.mulemba.booksells.repository;

import com.mulemba.booksells.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, String> {
}
