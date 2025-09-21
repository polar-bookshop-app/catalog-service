package com.github.polar.catalogservice.domain;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional
    public List<Book> listBooks() {
        return toList(bookRepository.findAll());
    }

    private static <T> List<T> toList(Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }

    @Transactional
    public Book viewBook(String isbn) {
        return bookRepository.findByIsbn(isbn).orElseThrow(() -> new BookNotFoundException(isbn));
    }

    @Transactional
    public Book addBook(Book book) {
        if (bookRepository.findByIsbn(book.isbn()).isPresent()) {
            throw new BookAlreadyExistsException(book.isbn());
        }

        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(String isbn) {
        boolean wasDeleted = bookRepository.deleteByIsbn(isbn);

        if (!wasDeleted) {
            LOGGER.warn("Warning: can't delete book with isbn '{}', doesnt exist.", isbn);
        }
    }

    @Transactional
    public Book updateOrCreateBook(String isbn, Book book) {

        Optional<Book> maybeExistingBook = bookRepository.findByIsbn(isbn);

        if (maybeExistingBook.isPresent()) {
            var updatedBook =
                    new Book(
                            maybeExistingBook.get().id(),
                            maybeExistingBook.get().isbn(),
                            book.title(),
                            book.author(),
                            book.price(),
                            book.publisher(),
                            maybeExistingBook.get().createdDate(),
                            maybeExistingBook.get().lastModifiedDate(),
                            maybeExistingBook.get().version());
            bookRepository.save(updatedBook);
            return updatedBook;
        }

        return addBook(book);
    }
}
