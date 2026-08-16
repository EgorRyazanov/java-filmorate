package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.RatingDto;
import ru.yandex.practicum.filmorate.model.Rating;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public final class RatingMapper {
    public RatingDto mapToRatingDto(Rating film) {
        RatingDto dto = new RatingDto();
        dto.setId(film.getId());
        dto.setName(film.getName());
        return dto;
    }
}