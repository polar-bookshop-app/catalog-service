package com.github.polar.catalogservice.domain;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String isbn) {
        super( String.format("Can't find book with isbn '%s'", isbn));
    }
}
