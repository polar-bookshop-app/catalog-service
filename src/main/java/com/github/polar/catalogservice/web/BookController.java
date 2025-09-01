package com.github.polar.catalogservice.web;

import com.github.polar.catalogservice.domain.Book;
import com.github.polar.catalogservice.domain.BookService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> listBooks() {
        return bookService.listBooks();
    }

    @GetMapping("{isbn}")
    public Book getByIsbn(@PathVariable String isbn) {
        return bookService.viewBook(isbn);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book createBook(@Valid @RequestBody Book book) {
        return bookService.addBook(book);
    }

    @DeleteMapping("{isbn}")
    public void deleteBook(@PathVariable String isbn) {
        bookService.deleteBook(isbn);
    }

    @PutMapping("{isbn}")
    public Book updateOrCreateBook(@PathVariable String isbn, @Valid @RequestBody Book book) {
        return bookService.updateOrCreateBook(isbn, book);
    }
}
