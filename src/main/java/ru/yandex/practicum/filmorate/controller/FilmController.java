package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmService filmService;

    @Autowired
    FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен запрос на получение всех фильмов");
        return this.filmService.getAllFilms();
    }

    @GetMapping("/{filmId}")
    public Film findById(@PathVariable("filmId") Integer id) {
        log.info("Получен запрос на получение фильма");
        return this.filmService.getFilmById(id);
    }


    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable("filmId") Integer id, @PathVariable("userId") Integer userId) {
        log.info("Получен запрос на добавление лайка");
        this.filmService.addLike(id, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable("filmId") Integer id, @PathVariable("userId") Integer userId) {
        log.info("Получен запрос на удаление лайка");
        this.filmService.deleteLike(id, userId);
    }


    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam("count") Integer count) {
        log.info("Получен запрос на самые популярные фильмы");
        return this.filmService.getPopularFilms(count);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Получен запрос на создание фильма");
        Film createdFilm = this.filmService.createFilm(film);
        log.info("Запрос на создание фильма завершился успешно");
        return createdFilm;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Получен запрос на обновление фильма");
        this.filmService.updateFilm(film);
        log.info("Запрос на обновление фильма завершился успешно");
        return film;
    }
}
