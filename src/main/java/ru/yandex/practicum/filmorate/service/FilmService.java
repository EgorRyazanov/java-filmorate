package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.errors.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmStorage;
import ru.yandex.practicum.filmorate.model.GenreStorage;
import ru.yandex.practicum.filmorate.model.RatingStorage;
import ru.yandex.practicum.filmorate.model.UserStorage;

import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FilmMapper filmMapper;
    private final GenreStorage genreStorage;
    private final RatingStorage ratingStorage;

    public FilmService(@Qualifier("FilmDataBaseImplementation") FilmStorage filmStorage,
                       @Qualifier("UserDataBaseImplementation") UserStorage userStorage,
                       FilmMapper filmMapper,
                       GenreStorage genreStorage,
                       RatingStorage ratingStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.filmMapper = filmMapper;
        this.genreStorage = genreStorage;
        this.ratingStorage = ratingStorage;
    }

    public List<FilmDto> getAllFilms() {
        log.trace("Получение всех фильмов");
        return filmStorage.getAllFilms().stream().map(filmMapper::mapToFilmDto).toList();
    }

    public FilmDto getFilmById(Long id) {
        log.trace("получение фильма по ID");
        return filmStorage.getFilmById(id)
            .map(filmMapper::mapToFilmDto)
            .orElseThrow(NotFoundException::new);
    }


    public void addLike(Long id, Long userId) {
        log.trace("Добавление лайка");
        if (!filmStorage.containsFilm(id) || !userStorage.containsUser(userId)) {
            log.error("Объект не был найден");
            throw new NotFoundException();
        }

        filmStorage.addLike(id, userId);
    }

    public void deleteLike(Long id, Long userId) {
        log.trace("Удаление лайка");
        if (!filmStorage.containsFilm(id) || !userStorage.containsUser(userId)) {
            log.error("Объект не был найден");
            throw new NotFoundException();
        }

        filmStorage.deleteLike(id, userId);
    }

    public List<FilmDto> getPopularFilms(Long count) {
        log.trace("Получение популярных фильмов");
        return filmStorage.getPopularFilms(count).stream().map(filmMapper::mapToFilmDto).toList();
    }

    public FilmDto createFilm(NewFilmRequest newFilm) {
        log.trace("Создание фильма");
        validateGenresAndMpa(newFilm.getGenres(), newFilm.getMpa());
        Film film = this.filmMapper.mapToFilm(newFilm);
        return this.filmMapper.mapToFilmDto(this.filmStorage.createFilm(film));
    }

    public FilmDto updateFilm(UpdateFilmRequest updatedFilm) {
        log.trace("Обновление фильма");
        validateGenresAndMpa(updatedFilm.getGenres(), updatedFilm.getMpa());
        Film film = this.filmMapper.mapToFilm(updatedFilm);
        return this.filmMapper.mapToFilmDto(this.filmStorage.updateFilm(film));
    }

    private void validateGenresAndMpa(List<ru.yandex.practicum.filmorate.dto.GenreDto> genres, ru.yandex.practicum.filmorate.dto.RatingDto mpa) {
        if (genres != null) {
            for (var genre : genres) {
                if (!genreStorage.getById(genre.getId()).isPresent()) {
                    throw new NotFoundException();
                }
            }
        }
        if (mpa != null && !ratingStorage.getById(mpa.getId()).isPresent()) {
            throw new NotFoundException();
        }
    }
}
