package com.github.polar.catalogservice.catalog.api;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.io.IOException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
public class BookJsonTest {

    @Autowired private JacksonTester<BookRequest> jackson;

    @Test
    void serializeBook() throws IOException {
        var book =
                new BookRequest(
                        "1111111111",
                        "Build a Large Language Model",
                        "Sebastian Raschka",
                        new BigDecimal("51.67"),
                        "Manning");

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
              "price": 51.67,
              "publisher": "Manning"
            }
            """;

        assertThat(jackson.parse(bookJson))
                .isNotNull()
                .isEqualTo(
                        new BookRequest(
                                "1633437167",
                                "Build a Large Language Model",
                                "Sebastian Raschka",
                                new BigDecimal("51.67"),
                                "Manning"));
    }
}
