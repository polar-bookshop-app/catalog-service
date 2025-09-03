package com.github.polar.catalogservice.dao;

import com.github.polar.catalogservice.domain.Book;
import com.github.polar.catalogservice.domain.BookRepository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryBookRepository implements BookRepository {

    private final Map<String, Book> allBooks = new ConcurrentHashMap<>();

    @Override
    public List<Book> listAllBooks() {
        return new ArrayList<>(allBooks.values());
    }

    @Override
    public Optional<Book> findById(String isbn) {
        return Optional.ofNullable(allBooks.getOrDefault(isbn, null));
    }

    @Override
    public boolean save(Book book) {
        return allBooks.put(book.isbn(), book) == null;
    }

    @Override
    public boolean delete(String isbn) {
        return allBooks.remove(isbn) != null;
    }
}
