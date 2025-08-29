package com.github.polar.catalogservice.domain;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> listAllBooks();

    Optional<Book> findById(String isbn);

    boolean save(Book book);

    boolean delete(String isbn);
}
