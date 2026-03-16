package com.github.polar.catalogservice.catalog.infrastructure;

import com.github.polar.catalogservice.catalog.domain.Book;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    long deleteByIsbn(String isbn);
}
