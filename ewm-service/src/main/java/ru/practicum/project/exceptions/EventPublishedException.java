package ru.practicum.project.exceptions;

public class EventPublishedException extends RuntimeException {
    public EventPublishedException(String message) {
        super(message);
    }
}
