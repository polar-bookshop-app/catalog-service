package com.github.polar.catalogservice.web;

import java.util.Map;
import org.springframework.http.HttpStatus;

/**
 * Common HTTP error message details.
 *
 * <p>Check: https://datatracker.ietf.org/doc/html/rfc7807 and
 * https://www.rfc-editor.org/rfc/rfc9457.html
 */
public record ErrorDetails(
        String type,
        String title,
        HttpStatus status,
        String detail,
        String instance,
        Map<String, String> context) {}
