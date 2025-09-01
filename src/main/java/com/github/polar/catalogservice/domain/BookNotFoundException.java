package com.github.polar.catalogservice.domain;

public class BookNotFoundException extends RuntimeException {

    private final String isbn;

    public BookNotFoundException(String isbn) {
        super(String.format("Can't find book with isbn '%s'", isbn));
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }
}
