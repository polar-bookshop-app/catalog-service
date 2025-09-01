package com.github.polar.catalogservice.dao;

import com.github.polar.catalogservice.domain.Book;
import com.github.polar.catalogservice.domain.BookRepository;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryBookRepository implements BookRepository {

    private final Map<String, Book> allBooks = new ConcurrentHashMap<>();

    public InMemoryBookRepository() {
        allBooks.put(
                "1449373321",
                new Book(
                        "1449373321",
                        "Designing Data-Intensive Applications: The Big Ideas Behind Reliable, Scalable, and Maintainable Systems",
                        "Martin Kleppmann",
                        new BigDecimal("59.99")));

        allBooks.put(
                "0321842685",
                new Book(
                        "0321842685",
                        "Hacker's Delight 2nd Edition",
                        "Henry Warren",
                        new BigDecimal("47.68")));

        allBooks.put(
                "0321349601",
                new Book(
                        "0321349601",
                        "Java Concurrency in Practice",
                        "Brian Goetz, Tim Peierls, Joshua Bloch, Joseph Bowbeer David Holmes",
                        new BigDecimal("28.00")));

        allBooks.put(
                "0134685997",
                new Book(
                        "0134685997",
                        "Effective Java 3rd Edition",
                        "Joshua Bloch",
                        new BigDecimal("43.86")));
    }

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
