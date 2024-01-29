package ru.practicum.project.exceptions;

public class DeleteCategoryException extends RuntimeException{
    public DeleteCategoryException(String message) {
        super(message);
    }
}
