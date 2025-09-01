package com.github.polar.catalogservice.web;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.github.polar.catalogservice.domain.Book;
import java.io.IOException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
public class BookJsonTest {

    @Autowired private JacksonTester<Book> jackson;

    @Test
    void serializeBook() throws IOException {
        var book =
                new Book(
                        "1111111111",
                        "Build a Large Language Model",
                        "Sebastian Raschka",
                        new BigDecimal("51.67"));

        var bookJson = jackson.write(book);

        assertThat(bookJson).extractingJsonPathStringValue("@.isbn").isEqualTo("1111111111");
        assertThat(bookJson)
                .extractingJsonPathStringValue("@.title")
                .isEqualTo("Build a Large Language Model");
        assertThat(bookJson)
                .extractingJsonPathStringValue("@.author")
                .isEqualTo("Sebastian Raschka");
        assertThat(bookJson).extractingJsonPathNumberValue("@.price").isEqualTo(51.67);
    }

    @Test
    void deserializeBook() throws IOException {
        var bookJson =
                """
            {
              "isbn": "1633437167",
              "title": "Build a Large Language Model",
              "author": "Sebastian Raschka",
              "price": 51.67
            }
            """;

        assertThat(jackson.parse(bookJson))
                .isNotNull()
                .isEqualTo(
                        new Book(
                                "1633437167",
                                "Build a Large Language Model",
                                "Sebastian Raschka",
                                new BigDecimal("51.67")));
    }
}
