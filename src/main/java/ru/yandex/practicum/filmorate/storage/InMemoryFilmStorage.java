package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmStorage;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final Map<Integer, Set<Integer>> likes = new HashMap<>();

    public List<Film> getAllFilms() {
        return films.values().stream().toList();
    }

    public Film getFilmById(Integer id) {
        return this.films.get(id);
    }

    @Override
    public void addLike(Integer id, Integer userId) {
        if (likes.containsKey(id)) {
            likes.get(id).add(userId);
        } else {
            likes.put(id, new HashSet<>(Set.of(userId)));
        }
    }

    @Override
    public void deleteLike(Integer id, Integer userId) {
        if (likes.containsKey(id)) {
            likes.get(id).remove(userId);
        }
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return likes.entrySet().stream()
                .sorted(Comparator.comparingInt((Map.Entry<Integer, Set<Integer>> e) -> e.getValue().size())
                        .reversed())
                .limit(count)
                .map(Map.Entry::getKey)
                .map(this::getFilmById)
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

    public boolean containsFilm(Integer id) {
        return films.containsKey(id);
    }

    private Integer getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .max(Integer::compare)
                .orElse(0);
        return ++currentMaxId;
    }
}
