package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmStorage;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private final Map<Long, Set<Long>> likes = new HashMap<>();

    public List<Film> getAllFilms() {
        return films.values().stream().toList();
    }

    public Optional<Film> getFilmById(Long id) {
        return Optional.of(films.get(id));
    }

    @Override
    public void addLike(Long id, Long userId) {
        if (likes.containsKey(id)) {
            likes.get(id).add(userId);
        } else {
            likes.put(id, new HashSet<>(Set.of(userId)));
        }
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        if (likes.containsKey(id)) {
            likes.get(id).remove(userId);
        }
    }

    @Override
    public List<Film> getPopularFilms(Long count) {
        return likes.entrySet().stream()
                .sorted(Comparator.comparingInt((Map.Entry<Long, Set<Long>> e) -> e.getValue().size())
                        .reversed())
                .limit(count)
                .map(Map.Entry::getKey)
                .map(this::getFilmById)
                .map(Optional::get)
                .toList();
    }

    public Film createFilm(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    public boolean containsFilm(Long id) {
        return films.containsKey(id);
    }

    private Long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .max(Long::compare)
                .orElse(0L);
        return ++currentMaxId;
    }
}
