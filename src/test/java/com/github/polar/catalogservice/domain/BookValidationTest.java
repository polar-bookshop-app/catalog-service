package com.github.polar.catalogservice.domain;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class BookValidationTest {

    private static Validator validator;

    @BeforeAll
    static void beforeAll() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void validBook() {
        var book =
                Book.of(
                        "1633437167",
                        "Build a Large Language Model (From Scratch)",
                        "Sebastian Raschka",
                        new BigDecimal("51.67"),
                        "Manning");

        Set<ConstraintViolation<Book>> violations = validator.validate(book);

        assertThat(violations).isEmpty();
    }

    @Test
    void bookWithNegativePriceShouldFail() {
        var book =
                Book.of(
                        "1633437167",
                        "Build a Large Language Model (From Scratch)",
                        "Sebastian Raschka",
                        new BigDecimal("-51.67"),
                        "Manning");

        Set<ConstraintViolation<Book>> violations = validator.validate(book);

        assertThat(violations).hasSize(1);

        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Book 'price' must be positive");
    }

    @Test
    void bookWithIncorrectIsbnShouldFail() {
        var book =
                Book.of(
                        "1633437167ABC",
                        "Build a Large Language Model (From Scratch)",
                        "Sebastian Raschka",
                        new BigDecimal("51.67"),
                        "Manning");

        Set<ConstraintViolation<Book>> violations = validator.validate(book);

        assertThat(violations).hasSize(1);

        assertThat(violations.iterator().next().getMessage()).isEqualTo("Invalid ISBN format");
    }
}
