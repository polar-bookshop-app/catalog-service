package com.github.polar.catalogservice.catalog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.polar.catalogservice.catalog.api.BookRequest;
import com.github.polar.catalogservice.catalog.api.BookResponse;
import com.github.polar.catalogservice.catalog.domain.Book;
import com.github.polar.catalogservice.catalog.domain.BookAlreadyExistsException;
import com.github.polar.catalogservice.catalog.domain.BookNotFoundException;
import com.github.polar.catalogservice.catalog.infrastructure.BookRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock private BookRepository bookRepository;

    @Mock private BookMapper bookMapper;

    @InjectMocks private BookService bookService;

    @Test
    void listBooksReturnsMappedResponses() {
        var first =
                Book.of("1111111111", "First", "Author A", new BigDecimal("10.00"), "Publisher A");
        var second =
                Book.of("2222222222", "Second", "Author B", new BigDecimal("20.00"), "Publisher B");
        var mappedResponses =
                List.of(
                        responseOf(1L, "1111111111", "First"),
                        responseOf(2L, "2222222222", "Second"));

        when(bookRepository.findAll()).thenReturn(List.of(first, second));
        when(bookMapper.toResponseList(List.of(first, second))).thenReturn(mappedResponses);

        var result = bookService.listBooks();

        assertThat(result).isEqualTo(mappedResponses);
        verify(bookRepository).findAll();
        verify(bookMapper).toResponseList(List.of(first, second));
    }

    @Test
    void viewBookWhenFoundReturnsMappedResponse() {
        var isbn = "9780132350884";
        var book =
                Book.of(
                        isbn,
                        "Clean Code",
                        "Robert C. Martin",
                        new BigDecimal("34.99"),
                        "Prentice Hall");
        var mappedResponse = responseOf(10L, isbn, "Clean Code");

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));
        when(bookMapper.toResponse(book)).thenReturn(mappedResponse);

        var result = bookService.viewBook(isbn);

        assertThat(result).isEqualTo(mappedResponse);
        verify(bookRepository).findByIsbn(isbn);
        verify(bookMapper).toResponse(book);
    }

    @Test
    void viewBookWhenMissingThrowsBookNotFoundException() {
        var isbn = "9780132350884";
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.viewBook(isbn))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("Can't find book with isbn '9780132350884'");

        verify(bookRepository).findByIsbn(isbn);
        verify(bookMapper, never()).toResponse(any());
    }

    @Test
    void addBookWhenIsbnAlreadyExistsThrowsBookAlreadyExistsException() {
        var request =
                new BookRequest(
                        "1234567890",
                        "Domain-Driven Design",
                        "Eric Evans",
                        new BigDecimal("54.99"),
                        "Addison-Wesley");
        when(bookRepository.existsByIsbn(request.isbn())).thenReturn(true);

        assertThatThrownBy(() -> bookService.addBook(request))
                .isInstanceOf(BookAlreadyExistsException.class)
                .hasMessage("Book with isbn '1234567890' already exists");

        verify(bookRepository).existsByIsbn(request.isbn());
        verify(bookMapper, never()).toDomain(any());
        verify(bookRepository, never()).save(any());
        verify(bookMapper, never()).toResponse(any());
    }

    @Test
    void addBookWhenIsbnIsNewSavesAndReturnsMappedResponse() {
        var request =
                new BookRequest(
                        "1234567890",
                        "Domain-Driven Design",
                        "Eric Evans",
                        new BigDecimal("54.99"),
                        "Addison-Wesley");
        var toSave =
                Book.of(
                        request.isbn(),
                        request.title(),
                        request.author(),
                        request.price(),
                        request.publisher());
        var saved =
                Book.of(
                        request.isbn(),
                        request.title(),
                        request.author(),
                        request.price(),
                        request.publisher());
        var mappedResponse = responseOf(20L, request.isbn(), request.title());

        when(bookRepository.existsByIsbn(request.isbn())).thenReturn(false);
        when(bookMapper.toDomain(request)).thenReturn(toSave);
        when(bookRepository.save(toSave)).thenReturn(saved);
        when(bookMapper.toResponse(saved)).thenReturn(mappedResponse);

        var result = bookService.addBook(request);

        assertThat(result).isEqualTo(mappedResponse);
        verify(bookRepository).existsByIsbn(request.isbn());
        verify(bookMapper).toDomain(request);
        verify(bookRepository).save(toSave);
        verify(bookMapper).toResponse(saved);
    }

    @Test
    void deleteBookDelegatesToRepository() {
        var isbn = "9780321125217";

        bookService.deleteBook(isbn);

        verify(bookRepository).deleteByIsbn(isbn);
    }

    @Test
    void updateOrCreateBookWhenExistingUpdatesAndSavesBook() {
        var pathIsbn = "9780321125217";
        var request =
                new BookRequest(
                        "0000000000",
                        "Refactoring",
                        "Martin Fowler",
                        new BigDecimal("49.90"),
                        "Addison-Wesley");
        var existingBook =
                Book.of(
                        pathIsbn,
                        "Old title",
                        "Old author",
                        new BigDecimal("40.00"),
                        "Old publisher");
        var savedBook =
                Book.of(
                        pathIsbn,
                        request.title(),
                        request.author(),
                        request.price(),
                        request.publisher());
        var mappedResponse = responseOf(30L, pathIsbn, request.title());

        when(bookRepository.findByIsbn(pathIsbn)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(savedBook);
        when(bookMapper.toResponse(savedBook)).thenReturn(mappedResponse);

        var result = bookService.updateOrCreateBook(pathIsbn, request);

        assertThat(result).isEqualTo(mappedResponse);
        assertThat(existingBook.getIsbn()).isEqualTo(pathIsbn);
        assertThat(existingBook.getTitle()).isEqualTo(request.title());
        assertThat(existingBook.getAuthor()).isEqualTo(request.author());
        assertThat(existingBook.getPrice()).isEqualTo(request.price());
        assertThat(existingBook.getPublisher()).isEqualTo(request.publisher());
        verify(bookRepository).save(existingBook);
        verify(bookMapper, never()).toDomain(any());
        verify(bookMapper).toResponse(savedBook);
    }

    @Test
    void updateOrCreateBookWhenMissingCreatesBookUsingPathIsbn() {
        var pathIsbn = "9780321125217";
        var request =
                new BookRequest(
                        "0000000000",
                        "Refactoring",
                        "Martin Fowler",
                        new BigDecimal("49.90"),
                        "Addison-Wesley");
        var created =
                Book.of(
                        pathIsbn,
                        request.title(),
                        request.author(),
                        request.price(),
                        request.publisher());
        var mappedResponse = responseOf(40L, pathIsbn, request.title());

        when(bookRepository.findByIsbn(pathIsbn)).thenReturn(Optional.empty());
        when(bookMapper.toDomain(any(BookRequest.class))).thenReturn(created);
        when(bookRepository.save(created)).thenReturn(created);
        when(bookMapper.toResponse(created)).thenReturn(mappedResponse);

        var result = bookService.updateOrCreateBook(pathIsbn, request);

        assertThat(result).isEqualTo(mappedResponse);
        verify(bookMapper)
                .toDomain(
                        new BookRequest(
                                pathIsbn,
                                request.title(),
                                request.author(),
                                request.price(),
                                request.publisher()));
        verify(bookRepository).save(created);
        verify(bookMapper).toResponse(created);
    }

    private static BookResponse responseOf(Long id, String isbn, String title) {
        return new BookResponse(
                id,
                isbn,
                title,
                "Author",
                new BigDecimal("9.99"),
                "Publisher",
                Instant.parse("2026-01-01T00:00:00Z"),
                Instant.parse("2026-01-02T00:00:00Z"),
                1);
    }
}
