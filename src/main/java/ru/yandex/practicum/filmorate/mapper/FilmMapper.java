package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.RatingDto;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public final class FilmMapper {
    public Film mapToFilm(NewFilmRequest request) {
        Film film = new Film();
        film.setName(request.getName());
        film.setDescription(request.getDescription());
        film.setDuration(request.getDuration());
        film.setReleaseDate(request.getReleaseDate());
        if (request.getGenres() != null && !request.getGenres().isEmpty()) {
            film.setGenres(request.getGenres().stream()
                .map(g -> {
                    Genre genre = new Genre();
                    genre.setId(g.getId());
                    return genre;
                })
                .toList());
        }
        if (request.getMpa() != null) {
            Rating rating = new Rating();
            rating.setId(request.getMpa().getId());
            film.setMpa(rating);
        }
        return film;
    }

    public Film mapToFilm(UpdateFilmRequest request) {
        Film film = new Film();
        film.setId(request.getId());
        film.setName(request.getName());
        film.setDescription(request.getDescription());
        film.setDuration(request.getDuration());
        film.setReleaseDate(request.getReleaseDate());
        if (request.getGenres() != null && !request.getGenres().isEmpty()) {
            film.setGenres(request.getGenres().stream()
                .map(g -> {
                    Genre genre = new Genre();
                    genre.setId(g.getId());
                    return genre;
                })
                .toList());
        }
        if (request.getMpa() != null) {
            Rating rating = new Rating();
            rating.setId(request.getMpa().getId());
            film.setMpa(rating);
        }
        return film;
    }

    public FilmDto mapToFilmDto(Film film) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());
        if (film.getGenres() != null) {
            dto.setGenres(film.getGenres().stream()
                .map(g -> {
                    GenreDto genreDto = new GenreDto();
                    genreDto.setId(g.getId());
                    genreDto.setName(g.getName());
                    return genreDto;
                })
                .toList());
        }
        if (film.getMpa() != null) {
            RatingDto ratingDto = new RatingDto();
            ratingDto.setId(film.getMpa().getId());
            ratingDto.setName(film.getMpa().getName());
            dto.setMpa(ratingDto);
        }
        return dto;
    }
}