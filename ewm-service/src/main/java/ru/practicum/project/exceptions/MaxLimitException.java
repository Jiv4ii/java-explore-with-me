package ru.practicum.project.exceptions;

public class MaxLimitException extends RuntimeException {
    public MaxLimitException(String message) {
        super(message);
    }
}
