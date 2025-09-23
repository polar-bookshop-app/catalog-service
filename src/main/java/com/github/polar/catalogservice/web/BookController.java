package com.github.polar.catalogservice.web;

import com.github.polar.catalogservice.domain.Book;
import com.github.polar.catalogservice.domain.BookService;
import jakarta.validation.Valid;
import java.lang.invoke.MethodHandles;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("books")
public class BookController {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
        LOGGER.info("getByIsbn {}", isbn);

        // TODO: below added for testing purpose, to test failure mode
        if ("0321349602".equals(isbn)) {
            throw new IllegalStateException("Invalid ISBN");
        }

        return bookService.viewBook(isbn);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book createBook(@Valid @RequestBody Book book) {
        LOGGER.info("createBook {}", book.isbn());
        return bookService.addBook(book);
    }

    @DeleteMapping("{isbn}")
    public void deleteBook(@PathVariable String isbn) {
        LOGGER.info("deleteBook {}", isbn);
        bookService.deleteBook(isbn);
    }

    @PutMapping("{isbn}")
    public Book updateOrCreateBook(@PathVariable String isbn, @Valid @RequestBody Book book) {
        LOGGER.info("updateOrCreateBook {}", isbn);
        return bookService.updateOrCreateBook(isbn, book);
    }
}
