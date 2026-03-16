package com.github.polar.catalogservice.catalog.api;

import java.math.BigDecimal;
import java.time.Instant;

public record BookResponse(
        Long id,
        String isbn,
        String title,
        String author,
        BigDecimal price,
        String publisher,
        Instant createdDate,
        Instant lastModifiedDate,
        int version) {}
