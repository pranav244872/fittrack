package com.pranav244872.fitness_tracker.exception;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.dao.DataIntegrityViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Maps DB column names to user-friendly field names
    private static final Map<String, String> FIELD_DISPLAY_NAMES = Map.of(
        "username", "Username",
        "email", "Email",
        "name", "Name"
    );

    // Matches PostgreSQL detail: Key (column)=(value) already exists.
    private static final Pattern DUPLICATE_KEY_PATTERN =
            Pattern.compile("Key \\(([^)]+)\\)=\\(([^)]+)\\) already exists");

    // returns 404 when something doesnt exist ex: category
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn("Resource Not Found: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }


    // return 400 when illegal argument ex: negative duration or duplicate username
    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Illegal Argument: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // return 404 for missing static resources
    @ExceptionHandler(NoResourceFoundException.class)
    public ProblemDetail handleNoResourceFound(NoResourceFoundException ex) {
        log.warn("Resource Not Found: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // return 409 for database constraint violations (duplicate key, etc.)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.warn("Data Integrity Violation: {}", ex.getMessage());

        Throwable rootCause = ex.getMostSpecificCause();
        String rootMessage = rootCause != null ? rootCause.getMessage() : "";

        if (rootMessage.contains("duplicate key value")) {
            Matcher matcher = DUPLICATE_KEY_PATTERN.matcher(rootMessage);
            if (matcher.find()) {
                String column = matcher.group(1);  // e.g. "username"
                String value = matcher.group(2);   // e.g. "pranav"
                String fieldName = FIELD_DISPLAY_NAMES.getOrDefault(column, column);

                String detail = String.format("%s '%s' is already taken", fieldName, value);
                log.warn("Duplicate key: {}={}", column, value);
                return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, detail);
            }
            // Duplicate key but couldn't parse the details — keep it generic
            return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "A duplicate entry already exists.");
        }

        // Non-duplicate constraint violation (e.g. NOT NULL)
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "A data constraint was violated.");
    }

    // fallback for unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleAllExceptions(Exception ex) {
        log.error("Unhandled exception occurred: {}", ex.getMessage(), ex);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
    }
}
