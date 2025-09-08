package com.github.polar.catalogservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.polar.catalogservice.TestcontainersConfiguration;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;

@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJdbcTest
public class BookRepositoryTest {

    @Autowired BookRepository repo;

    @Autowired JdbcAggregateTemplate template;

    @Test
    void findByIsbnNormalCase() {
        template.save(
                new Book(
                        1L,
                        "1449373321",
                        "Designing Data-Intensive Applications: The Big Ideas Behind Reliable, Scalable, and Maintainable Systems",
                        "Martin Kleppmann",
                        new BigDecimal("59.99"),
                        Instant.now(),
                        Instant.now(),
                        0));

        assertThat(repo.findByIsbn("1449373321"))
                .isPresent()
                .get()
                .extracting(Book::isbn)
                .isEqualTo("1449373321");
    }

    @Test
    void findByIsbnNotExistingCase() {
        assertThat(repo.findByIsbn("1449373321")).isEmpty();
    }
}
