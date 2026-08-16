package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<FilmDto> findAll() {
        log.info("Получен запрос на получение всех фильмов");
        return this.filmService.getAllFilms();
    }

    @GetMapping("/{filmId}")
    public FilmDto findById(@PathVariable("filmId") Long id) {
        log.info("Получен запрос на получение фильма");
        return this.filmService.getFilmById(id);
    }


    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable("filmId") Long id, @PathVariable("userId") Long userId) {
        log.info("Получен запрос на добавление лайка");
        this.filmService.addLike(id, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable("filmId") Long id, @PathVariable("userId") Long userId) {
        log.info("Получен запрос на удаление лайка");
        this.filmService.deleteLike(id, userId);
    }


    @GetMapping("/popular")
    public List<FilmDto> getPopularFilms(@RequestParam("count") Long count) {
        log.info("Получен запрос на самые популярные фильмы");
        return this.filmService.getPopularFilms(count);
    }

    @PostMapping
    public FilmDto create(@Valid @RequestBody NewFilmRequest film) {
        log.info("Получен запрос на создание фильма");
        FilmDto createdFilm = this.filmService.createFilm(film);
        log.info("Запрос на создание фильма завершился успешно");
        return createdFilm;
    }

    @PutMapping
    public FilmDto update(@Valid  @RequestBody UpdateFilmRequest film) {
        log.info("Получен запрос на обновление фильма");
        FilmDto updatedFilm = this.filmService.updateFilm(film);
        log.info("Запрос на обновление фильма завершился успешно");
        return updatedFilm;
    }
}
