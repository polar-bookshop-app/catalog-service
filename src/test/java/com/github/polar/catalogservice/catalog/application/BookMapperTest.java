package com.github.polar.catalogservice.catalog.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.polar.catalogservice.catalog.api.BookRequest;
import com.github.polar.catalogservice.catalog.api.BookResponse;
import com.github.polar.catalogservice.catalog.domain.Book;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

class BookMapperTest {

    private final BookMapper mapper = Mappers.getMapper(BookMapper.class);

    @Test
    void toDomainMapsAllRequestFields() {
        var request =
                new BookRequest(
                        "1234567890",
                        "Refactoring",
                        "Martin Fowler",
                        new BigDecimal("49.90"),
                        "Addison-Wesley");

        var result = mapper.toDomain(request);

        assertThat(result.getIsbn()).isEqualTo("1234567890");
        assertThat(result.getTitle()).isEqualTo("Refactoring");
        assertThat(result.getAuthor()).isEqualTo("Martin Fowler");
        assertThat(result.getPrice()).isEqualByComparingTo("49.90");
        assertThat(result.getPublisher()).isEqualTo("Addison-Wesley");
    }

    @Test
    void toResponseMapsAllBookFields() {
        var book =
                Book.of(
                        "9780134757599",
                        "Clean Architecture",
                        "Robert C. Martin",
                        new BigDecimal("45.00"),
                        "Prentice Hall");
        var createdDate = Instant.parse("2025-01-01T10:15:30Z");
        var lastModifiedDate = Instant.parse("2025-01-02T11:20:40Z");
        ReflectionTestUtils.setField(book, "id", 10L);
        ReflectionTestUtils.setField(book, "createdDate", createdDate);
        ReflectionTestUtils.setField(book, "lastModifiedDate", lastModifiedDate);
        ReflectionTestUtils.setField(book, "version", 3);

        BookResponse response = mapper.toResponse(book);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.isbn()).isEqualTo("9780134757599");
        assertThat(response.title()).isEqualTo("Clean Architecture");
        assertThat(response.author()).isEqualTo("Robert C. Martin");
        assertThat(response.price()).isEqualByComparingTo("45.00");
        assertThat(response.publisher()).isEqualTo("Prentice Hall");
        assertThat(response.createdDate()).isEqualTo(createdDate);
        assertThat(response.lastModifiedDate()).isEqualTo(lastModifiedDate);
        assertThat(response.version()).isEqualTo(3);
    }

    @Test
    void toResponseListMapsEachBook() {
        var first =
                Book.of(
                        "1111111111",
                        "Book One",
                        "Author One",
                        new BigDecimal("10.00"),
                        "Publisher One");
        var second =
                Book.of(
                        "2222222222",
                        "Book Two",
                        "Author Two",
                        new BigDecimal("20.00"),
                        "Publisher Two");

        var responses = mapper.toResponseList(List.of(first, second));

        assertThat(responses).hasSize(2);
        assertThat(responses)
                .extracting(BookResponse::isbn)
                .containsExactly("1111111111", "2222222222");
        assertThat(responses)
                .extracting(BookResponse::title)
                .containsExactly("Book One", "Book Two");
    }
}
