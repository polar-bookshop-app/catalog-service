package com.github.polar.catalogservice.catalog.application;

import com.github.polar.catalogservice.catalog.api.BookRequest;
import com.github.polar.catalogservice.catalog.api.BookResponse;
import com.github.polar.catalogservice.catalog.domain.Book;
import com.github.polar.catalogservice.catalog.domain.BookAlreadyExistsException;
import com.github.polar.catalogservice.catalog.domain.BookNotFoundException;
import com.github.polar.catalogservice.catalog.infrastructure.BookRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Transactional(readOnly = true)
    public List<BookResponse> listBooks() {
        return bookMapper.toResponseList(bookRepository.findAll());
    }

    @Transactional(readOnly = true)
    public BookResponse viewBook(String isbn) {
        Book book =
                bookRepository.findByIsbn(isbn).orElseThrow(() -> new BookNotFoundException(isbn));
        return bookMapper.toResponse(book);
    }

    @Transactional
    public BookResponse addBook(BookRequest request) {
        if (bookRepository.existsByIsbn(request.isbn())) {
            throw new BookAlreadyExistsException(request.isbn());
        }

        Book createdBook = bookRepository.save(bookMapper.toDomain(request));
        return bookMapper.toResponse(createdBook);
    }

    @Transactional
    public void deleteBook(String isbn) {
        bookRepository.deleteByIsbn(isbn);
    }

    @Transactional
    public BookResponse updateOrCreateBook(String isbn, BookRequest request) {
        Book savedBook =
                bookRepository
                        .findByIsbn(isbn)
                        .map(
                                existingBook -> {
                                    existingBook.updateDetails(
                                            request.title(),
                                            request.author(),
                                            request.price(),
                                            request.publisher());
                                    return bookRepository.save(existingBook);
                                })
                        .orElseGet(
                                () ->
                                        bookRepository.save(
                                                bookMapper.toDomain(
                                                        new BookRequest(
                                                                isbn,
                                                                request.title(),
                                                                request.author(),
                                                                request.price(),
                                                                request.publisher()))));

        return bookMapper.toResponse(savedBook);
    }
}
