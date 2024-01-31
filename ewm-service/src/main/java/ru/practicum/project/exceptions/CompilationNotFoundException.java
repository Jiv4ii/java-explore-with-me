package ru.practicum.project.exceptions;

public class CompilationNotFoundException extends RuntimeException {
    public CompilationNotFoundException(String message) {
        super(message);
    }
}
