package ru.yandex.practicum.filmorate.model;

import java.util.List;

public interface FilmStorage {
    List<Film> getAllFilms();
    Film createFilm(Film film);
    Film updateFilm(Film film);
    boolean containsFilm(Integer id);
    Film getFilmById(Integer id);
    void addLike(Integer id, Integer userId);
    void deleteLike(Integer id, Integer userId);
    List<Film> getPopularFilms(Integer count);
}
