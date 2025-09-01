package com.github.polar.catalogservice.web;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.polar.catalogservice.domain.Book;
import com.github.polar.catalogservice.domain.BookAlreadyExistsException;
import com.github.polar.catalogservice.domain.BookNotFoundException;
import com.github.polar.catalogservice.domain.BookService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private BookService bookService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        @Primary
        public BookService mockBookService() {
            return Mockito.mock(BookService.class);
        }
    }

    @BeforeEach
    void beforeEach() {
        Mockito.reset(bookService);
    }

    @Test
    void getByIsbnNormalCase() throws Exception {
        String isbn = "2222222222";

        when(bookService.viewBook(isbn))
                .thenReturn(
                        new Book(
                                isbn,
                                "Build a Large Language Model (From Scratch)",
                                "Sebastian Raschka",
                                new BigDecimal("51.67")));

        mockMvc.perform(get(String.format("/books/%s", isbn)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("@.isbn").value(isbn));

        verify(bookService, times(1)).viewBook(isbn);
    }

    @Test
    void getByIsbnNotExistedBookShouldFail() throws Exception {
        String notExistedIsbn = "2222222222";

        when(bookService.viewBook(notExistedIsbn)).thenThrow(BookNotFoundException.class);

        mockMvc.perform(get(String.format("/books/%s", notExistedIsbn)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("@.title").value("Book not found"));

        verify(bookService, times(1)).viewBook(notExistedIsbn);
    }

    @Test
    void createBookNormalCase() throws Exception {
        String isbn = "3333333333";
        var book =
                new Book(
                        isbn,
                        "Build a Large Language Model",
                        "Sebastian Raschka",
                        new BigDecimal("51.67"));

        when(bookService.addBook(book)).thenReturn(book);

        var bookBodyAsJson =
                """
            {
              "isbn": "3333333333",
              "title": "Build a Large Language Model",
              "author": "Sebastian Raschka",
              "price": 51.67
            }
            """;

        mockMvc.perform(
                        post("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(bookBodyAsJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("@.isbn").value("3333333333"))
                .andExpect(jsonPath("@.title").value("Build a Large Language Model"))
                .andExpect(jsonPath("@.author").value("Sebastian Raschka"))
                .andExpect(jsonPath("@.price").value(new BigDecimal("51.67")));

        verify(bookService, times(1)).addBook(book);
    }

    @Test
    void createBookInvalidIsbnShouldFail() throws Exception {
        var bookBodyAsJson =
                """
            {
              "isbn": "ABC",
              "title": "Build a Large Language Model",
              "author": "Sebastian Raschka",
              "price": 51.67
            }
            """;

        mockMvc.perform(
                        post("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(bookBodyAsJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("@.title").value("Book bad request"));
    }

    @Test
    void createBookDuplicateShouldFail() throws Exception {
        var bookBodyAsJson =
                """
            {
              "isbn": "7777777777",
              "title": "Build a Large Language Model",
              "author": "Sebastian Raschka",
              "price": 51.67
            }
            """;

        var book =
                new Book(
                        "7777777777",
                        "Build a Large Language Model",
                        "Sebastian Raschka",
                        new BigDecimal("51.67"));

        when(bookService.addBook(book)).thenThrow(new BookAlreadyExistsException("7777777777"));

        mockMvc.perform(
                        post("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(bookBodyAsJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("@.title").value("Book already exists"));

        verify(bookService, times(1)).addBook(book);
    }
}
