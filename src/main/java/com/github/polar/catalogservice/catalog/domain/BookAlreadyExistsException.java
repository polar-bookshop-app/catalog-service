package com.github.polar.catalogservice.catalog.domain;

public class BookAlreadyExistsException extends RuntimeException {

    public BookAlreadyExistsException(String isbn) {
        super(String.format("Book with isbn '%s' already exists", isbn));
    }
}
