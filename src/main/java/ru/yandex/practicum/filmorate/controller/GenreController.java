package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@Slf4j
public class GenreController {
    private final GenreService genreService;

    GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<GenreDto> findAll() {
        log.info("Получен запрос на получение всех жанров");
        return this.genreService.getAllGenres();
    }


    @GetMapping("/{filmId}")
    public GenreDto findById(@PathVariable("filmId") Long id) {
        log.info("Получен запрос на получение жанра");
        return this.genreService.getById(id);
    }
}
