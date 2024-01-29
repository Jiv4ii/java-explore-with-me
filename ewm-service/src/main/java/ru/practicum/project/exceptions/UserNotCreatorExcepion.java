package ru.practicum.project.exceptions;

public class UserNotCreatorExcepion extends RuntimeException{
    public UserNotCreatorExcepion(String message) {
        super(message);
    }
}
