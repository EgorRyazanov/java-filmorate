package ru.yandex.practicum.filmorate.model;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    /* Получение всех фильмов */
    List<Film> getAllFilms();

    /* Создание фильма */
    Film createFilm(Film film);

    /* Обновление фильма */
    Film updateFilm(Film film);

    /* Существует ли фильм */
    boolean containsFilm(Long id);

    /* Получение фильма по ID */
    Optional<Film> getFilmById(Long id);

    /* Добавить лайк */
    void addLike(Long id, Long userId);

    /* Удалить лайк */
    void deleteLike(Long id, Long userId);

    /* Возвращает список популярных фильмов */
    List<Film> getPopularFilms(Long count);
}
