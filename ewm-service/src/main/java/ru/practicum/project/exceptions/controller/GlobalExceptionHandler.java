package ru.practicum.project.exceptions.controller;

import lombok.extern.slf4j.Slf4j;
import org.h2.jdbc.JdbcSQLDataException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.project.exceptions.*;

import java.time.DateTimeException;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({UserNotFoundException.class,
            EventNotFoundException.class,
            CategoryNotFoundException.class,
            ClosedEventGetException.class,
            CompilationNotFoundException.class,
            CommentNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(final Exception e) {
        log.info(e.getMessage());
        return Map.of("error", e.getMessage());
    }


    @ExceptionHandler({DateTimeException.class, JdbcSQLDataException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest(final Exception e) {
        log.info(e.getMessage());
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler({RequestWrongStatusException.class,
            MaxLimitException.class,
            DuplicateCategoryNameException.class,
            DuplicateEmailException.class,
            DeleteCategoryException.class,
            EventPublishedException.class,
            RequestAlreadyExistException.class,
            ClosedEventRequestException.class,
            CreatorCantRequestException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleDateTimeException(final Exception e) {
        log.info(e.getMessage());
        return Map.of("error", e.getMessage());
    }


}
