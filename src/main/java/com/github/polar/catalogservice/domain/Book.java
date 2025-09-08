package com.github.polar.catalogservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

public record Book(
        @Id Long id,
        @NotBlank(message = "Book 'ISBN' must be defined")
                @Pattern(regexp = "^(\\d{10})|(\\d{3}-?\\d{10})$", message = "Invalid ISBN format")
                String isbn,
        @NotBlank(message = "Book 'title' must be defined") String title,
        @NotBlank(message = "Book 'author' must be defined") String author,
        @NotNull(message = "Book 'price' must be defined")
                @Positive(message = "Book 'price' must be positive")
                BigDecimal price,
        @CreatedDate Instant createdDate,
        @LastModifiedDate Instant lastModifiedDate,
        @Version int version) {

    public static Book of(String isbn, String title, String author, BigDecimal price) {
        return new Book(null, isbn, title, author, price, null, null, 0);
    }
}
