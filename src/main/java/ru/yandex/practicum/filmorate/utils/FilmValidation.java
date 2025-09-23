package ru.yandex.practicum.filmorate.utils;

import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;

public class FilmValidation {
    public static Boolean isFilmValid(Film film) {
        return !film.getName().isEmpty() && film.getDescription().length() <= 200 && film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28)) && film.getDuration() > 0;
    }
}
