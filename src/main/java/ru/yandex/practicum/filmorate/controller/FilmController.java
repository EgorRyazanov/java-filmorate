package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.errors.NotFoundException;
import ru.yandex.practicum.filmorate.errors.ValidationError;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.ErrorText;
import ru.yandex.practicum.filmorate.utils.FilmValidation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен запрос на получение всех фильмов");
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Получен запрос на создание фильма");
        if (!FilmValidation.isFilmValid(film)) {
            log.error("Произошла ошибка валидации");
            throw new ValidationError(ErrorText.validationError);
        }
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Запрос на создание фильма завершился успешно");
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Получен запрос на обновление фильма");

        if (!films.containsKey(film.getId())) {
            log.error("Объект не был найден");
            throw new NotFoundException(ErrorText.notFoundError);
        }

        if (!FilmValidation.isFilmValid(film)) {
            log.error("Произошла ошибка валидации");
            throw new ValidationError(ErrorText.validationError);
        }
        films.put(film.getId(), film);
        log.info("Запрос на обновление фильма завершился успешно");
        return film;
    }


    private Integer getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .max(Integer::compare)
                .orElse(0);
        return ++currentMaxId;
    }
}
