package ru.practicum.project.exceptions;

public class RequestWrongStatusException extends RuntimeException {
    public RequestWrongStatusException(String message) {
        super(message);
    }
}
