package com.github.polar.catalogservice.catalog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "book")
@EntityListeners(AuditingEntityListener.class)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Book 'ISBN' must be defined")
    @Pattern(regexp = "^(\\d{10})|(\\d{3}-?\\d{10})$", message = "Invalid ISBN format")
    @Column(nullable = false, unique = true)
    private String isbn;

    @NotBlank(message = "Book 'title' must be defined")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Book 'author' must be defined")
    @Column(nullable = false)
    private String author;

    @NotNull(message = "Book 'price' must be defined")
    @Positive(message = "Book 'price' must be positive")
    @Column(nullable = false)
    private BigDecimal price;

    @NotBlank(message = "Book 'publisher' must be defined")
    @Column(nullable = false)
    private String publisher;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant lastModifiedDate;

    @Version
    @Column(nullable = false)
    private int version;

    protected Book() {}

    public Book(String isbn, String title, String author, BigDecimal price, String publisher) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.price = price;
        this.publisher = publisher;
    }

    public static Book of(
            String isbn, String title, String author, BigDecimal price, String publisher) {
        return new Book(isbn, title, author, price, publisher);
    }

    public void updateDetails(String title, String author, BigDecimal price, String publisher) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.publisher = publisher;
    }

    public Long getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
}
