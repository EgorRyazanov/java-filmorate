package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.errors.NotFoundException;
import ru.yandex.practicum.filmorate.errors.ValidationError;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmStorage;
import ru.yandex.practicum.filmorate.model.UserStorage;
import ru.yandex.practicum.filmorate.utils.FilmValidation;

import java.util.Collection;
import java.util.List;

@Service
public class FilmService {
    private final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> getAllFilms() {
        log.trace("Получение всех фильмов");
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Integer id) {
        log.trace("получение фильма по ID");
        return filmStorage.getFilmById(id);
    }


    public void addLike(Integer id, Integer userId) {
        log.trace("Добавление лайка");
        if (!filmStorage.containsFilm(id) || !userStorage.containsUser(userId)) {
            log.error("Объект не был найден");
            throw new NotFoundException();
        }

        filmStorage.addLike(id, userId);
    }

    public void deleteLike(Integer id, Integer userId) {
        log.trace("Удаление лайка");
        if (!filmStorage.containsFilm(id) || !userStorage.containsUser(userId)) {
            log.error("Объект не был найден");
            throw new NotFoundException();
        }

        filmStorage.deleteLike(id, userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        log.trace("Получение популярных фильмов");
        return filmStorage.getPopularFilms(count);
    }

    public Film createFilm(Film film) {
        log.trace("Создание фильма");
        if (!FilmValidation.isFilmValid(film)) {
            log.error("Произошла ошибка валидации");
            throw new ValidationError();
        }
        return this.filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        log.trace("Обновление фильма");
        if (!FilmValidation.isFilmValid(film)) {
            log.error("Произошла ошибка валидации");
            throw new ValidationError();
        }

        if (!this.filmStorage.containsFilm(film.getId())) {
            log.error("Объект не был найден");
            throw new NotFoundException();
        }
        return this.filmStorage.updateFilm(film);
    }
}
