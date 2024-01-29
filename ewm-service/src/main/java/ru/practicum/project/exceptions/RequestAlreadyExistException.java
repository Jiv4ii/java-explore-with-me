package ru.practicum.project.exceptions;

public class RequestAlreadyExistException extends RuntimeException{
    public RequestAlreadyExistException(String message) {
        super(message);
    }
}
