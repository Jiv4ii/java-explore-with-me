package ru.practicum.project.exceptions;

public class ClosedEventRequestException extends RuntimeException{
    public ClosedEventRequestException(String message) {
        super(message);
    }
}
