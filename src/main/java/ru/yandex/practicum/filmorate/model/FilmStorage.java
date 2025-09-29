package ru.yandex.practicum.filmorate.model;

import java.util.List;

public interface FilmStorage {
    /* Получение всех фильмов */
    List<Film> getAllFilms();

    /* Создание фильма */
    Film createFilm(Film film);

    /* Обновление фильма */
    Film updateFilm(Film film);

    /* Существует ли фильм */
    boolean containsFilm(Integer id);

    /* Получение фильма по ID */
    Film getFilmById(Integer id);

    /* Добавить лайк */
    void addLike(Integer id, Integer userId);

    /* Удалить лайк */
    void deleteLike(Integer id, Integer userId);

    /* Возвращает список популярных фильмов */
    List<Film> getPopularFilms(Integer count);
}
