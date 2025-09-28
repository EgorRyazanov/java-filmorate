package ru.yandex.practicum.filmorate.errors;

import lombok.Getter;

@Getter
public record ErrorResponse(String error) {
}