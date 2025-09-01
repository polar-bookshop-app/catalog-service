package com.github.polar.catalogservice.web;

import com.github.polar.catalogservice.domain.BookAlreadyExistsException;
import com.github.polar.catalogservice.domain.BookNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BookControllerAdvice {

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorDetails bookNotFoundException(BookNotFoundException ex) {
        return new ErrorDetails(
                "https://example.com/probs/not-found",
                "Book not found",
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                "/books/" + ex.getIsbn(),
                Collections.emptyMap());
    }

    @ExceptionHandler(BookAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    ErrorDetails bookAlreadyExistsException(BookAlreadyExistsException ex) {
        return new ErrorDetails(
                "https://example.com/probs/already-exists",
                "Book already exists",
                HttpStatus.UNPROCESSABLE_ENTITY,
                ex.getMessage(),
                "/books/",
                Collections.emptyMap());
    }

    static class IndexHolder {
        int idx;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails handleValidationExceptions(MethodArgumentNotValidException ex) {
        var errors = new HashMap<String, String>();
        final IndexHolder undefinedIdx = new IndexHolder();

        ex.getBindingResult()
                .getAllErrors()
                .forEach(
                        error -> {
                            if (error instanceof FieldError fieldError) {
                                String fieldName = fieldError.getField();
                                String errorMessage = error.getDefaultMessage();
                                errors.put(fieldName, errorMessage);
                            } else {
                                errors.put(
                                        "undefined-" + undefinedIdx.idx, error.getDefaultMessage());
                                ++undefinedIdx.idx;
                            }
                        });

        return new ErrorDetails(
                "https://example.com/probs/bad-request",
                "Book bad request",
                HttpStatus.BAD_REQUEST,
                "Book validation failed",
                "/books/",
                errors);
    }
}
