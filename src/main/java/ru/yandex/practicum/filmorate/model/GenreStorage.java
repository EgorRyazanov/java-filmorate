package ru.yandex.practicum.filmorate.model;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    List<Genre> getAllGenres();

    Optional<Genre> getById(Long id);
}
