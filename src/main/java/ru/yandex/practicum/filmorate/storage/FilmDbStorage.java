package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Qualifier("FilmDataBaseImplementation")
@Slf4j
@Repository
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage  {
    private static final String FIND_ALL_QUERY = """
        SELECT f.*, r.id AS rating_id, r.name AS rating_name
        FROM films f
        LEFT JOIN ratings r ON f.rating_id = r.id
        """;
    private static final String FIND_BY_ID_QUERY = """
        SELECT f.*, r.id AS rating_id, r.name AS rating_name
        FROM films f
        LEFT JOIN ratings r ON f.rating_id = r.id
        WHERE f.id = ?
        """;
    private static final String FIND_GENRES_QUERY = """
        SELECT g.id, g.name
        FROM genres g
        JOIN film_genres fg ON g.id = fg.genre_id
        WHERE fg.film_id = ?
        """;
    private static final String DELETE_LIKE_QUERY = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    private static final String INSERT_LIKE = "INSERT INTO likes(user_id, film_id) VALUES (?, ?)";
    private static final String FIND_POPULAR_FILMS_QUERY = """
        SELECT f.*, r.id AS rating_id, r.name AS rating_name
        FROM films f
        LEFT JOIN ratings r ON f.rating_id = r.id
        LEFT JOIN (
            SELECT film_id, COUNT(*) AS likes_count
            FROM likes
            GROUP BY film_id
        ) l ON f.id = l.film_id
        ORDER BY COALESCE(l.likes_count, 0) DESC, f.id
        LIMIT ?
        """;
    private static final String INSERT_FILM_QUERY = "INSERT INTO films(name, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_FILM_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE id = ?";
    private static final String INSERT_FILM_GENRE_QUERY = "INSERT INTO film_genres(film_id, genre_id) VALUES (?, ?)";
    private static final String DELETE_FILM_GENRES_QUERY = "DELETE FROM film_genres WHERE film_id = ?";

    public FilmDbStorage(JdbcClient jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public List<Film> getAllFilms() {
        List<Film> films = this.findMany(FIND_ALL_QUERY);
        for (Film film : films) {
            loadGenres(film);
        }
        return films;
    }

    private void loadGenres(Film film) {
        List<Genre> genres = this.jdbc.sql(FIND_GENRES_QUERY)
            .params(film.getId())
            .query((rs, rowNum) -> {
                Genre genre = new Genre();
                genre.setId(rs.getLong("id"));
                genre.setName(rs.getString("name"));
                return genre;
            })
            .list();
        film.setGenres(genres);
    }

    public Optional<Film> getFilmById(Long id) {
        Optional<Film> filmOpt = this.findOne(FIND_BY_ID_QUERY, id);
        if (filmOpt.isPresent()) {
            Film film = filmOpt.get();
            List<Genre> genres = this.jdbc.sql(FIND_GENRES_QUERY)
                .params(id)
                .query((rs, rowNum) -> {
                    Genre genre = new Genre();
                    genre.setId(rs.getLong("id"));
                    genre.setName(rs.getString("name"));
                    return genre;
                })
                .list();
            film.setGenres(genres);
        }
        return filmOpt;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        this.insert(INSERT_LIKE, userId, filmId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        this.delete(DELETE_LIKE_QUERY, filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(Long count) {
        List<Film> films = this.jdbc.sql(FIND_POPULAR_FILMS_QUERY).param(count).query(mapper).list();
        for (Film film : films) {
            loadGenres(film);
        }
        return films;
    }

    public Film createFilm(Film film) {
        Long ratingId = film.getMpa() != null ? film.getMpa().getId() : null;
        Long filmId = this.insert(INSERT_FILM_QUERY, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), ratingId);

        insertGenres(filmId, film.getGenres());

        return this.getFilmById(filmId).orElseThrow();
    }

    public Film updateFilm(Film film) {
        Long ratingId = film.getMpa() != null ? film.getMpa().getId() : null;
        this.update(
            UPDATE_FILM_QUERY,
            film.getName(),
            film.getDescription(),
            film.getReleaseDate(),
            film.getDuration(),
            ratingId,
            film.getId()
        );

        if (film.getGenres() != null) {
            this.delete(DELETE_FILM_GENRES_QUERY, film.getId());
            insertGenres(film.getId(), film.getGenres());
        }

        return this.getFilmById(film.getId()).orElseThrow();
    }

    public boolean containsFilm(Long id) {
        return this.getFilmById(id).isPresent();
    }

    private void insertGenres(Long filmId, List<Genre> genres) {
        if (genres == null || genres.isEmpty()) {
            return;
        }

        Set<Long> savedGenreIds = new HashSet<>();
        for (Genre genre : genres) {
            if (savedGenreIds.add(genre.getId())) {
                this.update(INSERT_FILM_GENRE_QUERY, filmId, genre.getId());
            }
        }
    }
}
