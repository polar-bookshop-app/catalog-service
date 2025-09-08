package com.github.polar.catalogservice.demo;

import com.github.polar.catalogservice.domain.Book;
import com.github.polar.catalogservice.domain.BookRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Loads a set of sample books into the database, but only when running in the local (demo)
 * environment.
 */
@Component
@ConditionalOnProperty(name = "catalog.service.testdata.enabled", havingValue = "true")
public class BookDataLoader {

    private final BookRepository bookRepository;

    public BookDataLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadData() {

        bookRepository.deleteAll();

        var book1 =
                Book.of(
                        "1449373321",
                        "Designing Data-Intensive Applications: The Big Ideas Behind Reliable, Scalable, and Maintainable Systems",
                        "Martin Kleppmann",
                        new BigDecimal("59.99"));

        var book2 =
                Book.of(
                        "0321842685",
                        "Hacker's Delight 2nd Edition",
                        "Henry Warren",
                        new BigDecimal("47.68"));

        var book3 =
                Book.of(
                        "0321349601",
                        "Java Concurrency in Practice",
                        "Brian Goetz, Tim Peierls, Joshua Bloch, Joseph Bowbeer David Holmes",
                        new BigDecimal("28.00"));

        var book4 =
                Book.of(
                        "0134685997",
                        "Effective Java 3rd Edition",
                        "Joshua Bloch",
                        new BigDecimal("43.86"));

        bookRepository.saveAll(List.of(book1, book2, book3, book4));
    }
}
