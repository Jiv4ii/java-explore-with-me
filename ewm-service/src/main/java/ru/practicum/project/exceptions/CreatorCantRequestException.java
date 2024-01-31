package ru.practicum.project.exceptions;

public class CreatorCantRequestException extends RuntimeException {
    public CreatorCantRequestException(String message) {
        super(message);
    }
}
