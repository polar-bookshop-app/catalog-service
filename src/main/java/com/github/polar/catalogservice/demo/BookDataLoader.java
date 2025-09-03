package com.github.polar.catalogservice.demo;

import com.github.polar.catalogservice.domain.Book;
import com.github.polar.catalogservice.domain.BookRepository;
import java.math.BigDecimal;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "catalog.service.testdata.enabled", havingValue = "true")
public class BookDataLoader {

    private final BookRepository bookRepository;

    public BookDataLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadData() {

        bookRepository.save(
                new Book(
                        "1449373321",
                        "Designing Data-Intensive Applications: The Big Ideas Behind Reliable, Scalable, and Maintainable Systems",
                        "Martin Kleppmann",
                        new BigDecimal("59.99")));

        bookRepository.save(
                new Book(
                        "0321842685",
                        "Hacker's Delight 2nd Edition",
                        "Henry Warren",
                        new BigDecimal("47.68")));

        bookRepository.save(
                new Book(
                        "0321349601",
                        "Java Concurrency in Practice",
                        "Brian Goetz, Tim Peierls, Joshua Bloch, Joseph Bowbeer David Holmes",
                        new BigDecimal("28.00")));

        bookRepository.save(
                new Book(
                        "0134685997",
                        "Effective Java 3rd Edition",
                        "Joshua Bloch",
                        new BigDecimal("43.86")));
    }
}
