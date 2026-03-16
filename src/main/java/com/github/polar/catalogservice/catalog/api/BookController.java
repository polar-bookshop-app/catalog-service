package com.github.polar.catalogservice.catalog.api;

import com.github.polar.catalogservice.catalog.application.BookService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookResponse> listBooks() {
        return bookService.listBooks();
    }

    @GetMapping("{isbn}")
    public BookResponse getByIsbn(@PathVariable String isbn) {
        return bookService.viewBook(isbn);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse createBook(@Valid @RequestBody BookRequest request) {
        return bookService.addBook(request);
    }

    @DeleteMapping("{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable String isbn) {
        bookService.deleteBook(isbn);
    }

    @PutMapping("{isbn}")
    public BookResponse updateOrCreateBook(
            @PathVariable String isbn, @Valid @RequestBody BookRequest request) {
        return bookService.updateOrCreateBook(isbn, request);
    }
}
