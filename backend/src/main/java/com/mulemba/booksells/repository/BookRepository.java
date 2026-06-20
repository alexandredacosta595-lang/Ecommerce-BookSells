package com.mulemba.booksells.repository;

import com.mulemba.booksells.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookRepository extends JpaRepository<Book, String>, JpaSpecificationExecutor<Book> {
}
