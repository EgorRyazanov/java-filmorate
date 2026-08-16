package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.RatingDto;
import ru.yandex.practicum.filmorate.errors.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.RatingMapper;
import ru.yandex.practicum.filmorate.model.RatingStorage;

import java.util.List;

@Service
@Slf4j
public class RatingService {
    private final RatingStorage ratingStorage;
    private final RatingMapper ratingMapper;

    public RatingService(RatingStorage ratingStorage, RatingMapper ratingMapper) {
        this.ratingMapper = ratingMapper;
        this.ratingStorage = ratingStorage;
    }

    public List<RatingDto> getAllRatings() {
        log.trace("Получение всех рейтингов");
        return ratingStorage.getAllRating().stream().map(ratingMapper::mapToRatingDto).toList();
    }

    public RatingDto getById(Long id) {
        log.trace("получение жанра по ID");
        return ratingStorage.getById(id)
            .map(ratingMapper::mapToRatingDto)
            .orElseThrow(NotFoundException::new);
    }
}
