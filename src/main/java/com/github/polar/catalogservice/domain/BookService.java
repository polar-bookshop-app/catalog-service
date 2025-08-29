package com.github.polar.catalogservice.domain;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> listBooks() {
        return bookRepository.listAllBooks();
    }

    public Book viewBook(String isbn) {
        return bookRepository.findById(isbn).orElseThrow(() -> new BookNotFoundException(isbn));
    }

    public Book addBook(Book book) {
        boolean wasAdded = bookRepository.save(book);

        if (!wasAdded) {
            throw new BookAlreadyExistsException(book.isbn());
        }

        return book;
    }

    public boolean deleteBook(String isbn) {
        return bookRepository.delete(isbn);
    }

    public Book updateOrCreateBook(String isbn, Book book) {

        Optional<Book> maybeExistingBook = bookRepository.findById(isbn);

        if (maybeExistingBook.isPresent()) {
            var updatedBook =
                    new Book(
                            maybeExistingBook.get().isbn(),
                            book.title(),
                            book.author(),
                            book.price());
            bookRepository.save(updatedBook);
            return updatedBook;
        }

        return addBook(book);
    }
}
