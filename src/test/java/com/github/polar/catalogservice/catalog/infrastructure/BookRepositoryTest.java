package com.github.polar.catalogservice.catalog.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.polar.catalogservice.TestcontainersConfiguration;
import com.github.polar.catalogservice.catalog.domain.Book;
import com.github.polar.catalogservice.config.DataConfig;
import java.math.BigDecimal;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;

@TestMethodOrder(MethodOrderer.Random.class)
@Import({TestcontainersConfiguration.class, DataConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class BookRepositoryTest {

    @Autowired BookRepository repo;

    @Test
    void findByIsbnNormalCase() {
        repo.save(
                Book.of(
                        "1449373321",
                        "Designing Data-Intensive Applications: The Big Ideas Behind Reliable, Scalable, and Maintainable Systems",
                        "Martin Kleppmann",
                        new BigDecimal("59.99"),
                        "Manning"));

        assertThat(repo.findByIsbn("1449373321"))
                .isPresent()
                .get()
                .extracting(Book::getIsbn)
                .isEqualTo("1449373321");
    }

    @Test
    void findByIsbnNotExistingCase() {
        assertThat(repo.findByIsbn("1449373321")).isEmpty();
    }

    @Test
    void saveBookWithExistingIsbnShouldFail() {
        repo.save(
                Book.of(
                        "1449373321",
                        "Designing Data-Intensive Applications: The Big Ideas Behind Reliable, Scalable, and Maintainable Systems",
                        "Martin Kleppmann",
                        new BigDecimal("59.99"),
                        "Manning"));

        try {
            repo.saveAndFlush(
                    Book.of(
                            "1449373321",
                            "Designing Data-Intensive Applications: The Big Ideas Behind Reliable, Scalable, and Maintainable Systems",
                            "Martin Kleppmann",
                            new BigDecimal("59.99"),
                            "Manning"));
        } catch (Exception ex) {
            assertThat(ex)
                    .isInstanceOfAny(
                            DataIntegrityViolationException.class, JpaSystemException.class);
            return;
        }

        throw new AssertionError("Expected duplicate ISBN to fail");
    }

    @Test
    void deleteByIsbnNotExisted() {
        assertThat(repo.deleteByIsbn("1449373321")).isZero();
    }

    @Test
    void deleteByIsbnExisted() {
        repo.save(
                Book.of(
                        "1449373321",
                        "Designing Data-Intensive Applications: The Big Ideas Behind Reliable, Scalable, and Maintainable Systems",
                        "Martin Kleppmann",
                        new BigDecimal("59.99"),
                        "Manning"));

        assertThat(repo.deleteByIsbn("1449373321")).isEqualTo(1);
    }
}
