package ru.yandex.practicum.filmorate.errors;

public class ValidationError extends RuntimeException {
    public ValidationError(String message) {
        super(message);
    }
}