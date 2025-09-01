package com.github.polar.catalogservice;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.polar.catalogservice.domain.Book;
import com.github.polar.catalogservice.web.ErrorDetails;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CatalogServiceApplicationTests {

    private static final String BASE_URL = "http://localhost:%d/20250525";

    @LocalServerPort int port;

    private WebTestClient client;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToServer().baseUrl(String.format(BASE_URL, port)).build();
    }

    @Test
    void loadContext() {}

    @Test
    void createBookDuplicateShouldFail() {
        var book =
                new Book(
                        "1633437167",
                        "Build a Large Language Model (From Scratch)",
                        "Sebastian Raschka",
                        new BigDecimal("51.67"));

        // create book 1-st request should succeed
        client.post().uri("/books").bodyValue(book).exchange().expectStatus().isCreated();

        // create book 2-nd request should fail
        client.post()
                .uri("/books")
                .bodyValue(book)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody(ErrorDetails.class)
                .value(
                        errorDetails -> {
                            assertThat(errorDetails).isNotNull();
                            assertThat(errorDetails.title()).isEqualTo("Book already exists");
                        });
    }
}
