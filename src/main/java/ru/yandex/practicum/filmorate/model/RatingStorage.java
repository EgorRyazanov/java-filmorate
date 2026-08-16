package ru.yandex.practicum.filmorate.model;

import java.util.List;
import java.util.Optional;

public interface RatingStorage {
    List<Rating> getAllRating();

    Optional<Rating> getById(Long id);
}
