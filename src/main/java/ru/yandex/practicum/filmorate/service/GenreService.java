package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.errors.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.GenreStorage;

import java.util.List;

@Service
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;
    private final GenreMapper genreMapper;

    public GenreService(GenreStorage genreStorage, GenreMapper genreMapper) {
        this.genreStorage = genreStorage;
        this.genreMapper = genreMapper;
    }

    public List<GenreDto> getAllGenres() {
        log.trace("Получение всех жанров");
        return genreStorage.getAllGenres().stream().map(genreMapper::mapToGenreDto).toList();
    }

    public GenreDto getById(Long id) {
        log.trace("получение жанра по ID");
        return genreStorage.getById(id)
            .map(genreMapper::mapToGenreDto)
            .orElseThrow(NotFoundException::new);
    }
}
