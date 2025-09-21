package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.errors.NotFoundException;
import ru.yandex.practicum.filmorate.errors.ValidationError;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController controller;

    @BeforeEach
    void BeforeEach() {
        controller = new FilmController();
    }

    private Film validFilm() {
        Film film = new Film("name", LocalDate.of(2010, 7, 16), 148);
        film.setDescription("Description");
        return film;
    }

    @Test
    void shouldCreate() {
        Film created = controller.create(validFilm());

        assertNotNull(created.getId(), "id должен быть присвоен");
        assertEquals(1, created.getId());
        assertEquals("name", created.getName());

        Collection<Film> all = controller.findAll();
        assertEquals(1, all.size(), "фильм должен сохраняться во внутреннем хранилище");
    }

    @Test
    void shouldUpdateId() {
        Film film1 = controller.create(validFilm());
        Film film2 = controller.create(validFilm());

        assertEquals(1, film1.getId());
        assertEquals(2, film2.getId());
        assertNotEquals(film1.getId(), film2.getId());
    }

    @Test
    void shouldReturnValidationErrorWhenEmptyName() {
        Film film = validFilm();
        film.setName("");
        assertThrows(ValidationError.class, () -> controller.create(film));
    }

    @Test
    void shouldReturnValidationErrorWhenLongDescription() {
        Film film = validFilm();
        film.setDescription("x".repeat(201));
        assertThrows(ValidationError.class, () -> controller.create(film));
    }

    @Test
    void shouldReturnValidationErrorWhenInvalidDate() {
        Film film = validFilm();
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        assertThrows(ValidationError.class, () -> controller.create(film));

        film.setReleaseDate(LocalDate.of(1895, 12, 29));
        assertDoesNotThrow(() -> controller.create(film));
    }

    @Test
    void shouldReturnValidationErrorWhenNegativeDuration() {
        Film film = validFilm();
        film.setDuration(0);
        assertThrows(ValidationError.class, () -> controller.create(film));

        film.setDuration(-10);
        assertThrows(ValidationError.class, () -> controller.create(film));
    }

    @Test
    void shouldUpdate() {
        Film created = controller.create(validFilm());
        Integer id = created.getId();

        Film changedFilm = validFilm();
        changedFilm.setId(id);
        changedFilm.setName("Changed");
        Film updated = controller.update(changedFilm);

        assertEquals(id, updated.getId());
        assertEquals("Changed", updated.getName());
    }

    @Test
    void shouldReturnNotFoundErrorWhenUpdate() {
        Film film = validFilm();
        film.setId(999);
        assertThrows(NotFoundException.class, () -> controller.update(film));
    }

    @Test
    void shouldReturnValidationErrorWhenUpdateEmptyName() {
        Film created = controller.create(validFilm());
        Film invalidFilm = validFilm();
        invalidFilm.setId(created.getId());
        invalidFilm.setName("");

        assertThrows(ValidationError.class, () -> controller.update(invalidFilm));
    }

    @Test
    void shouldReturnAllFilms() {
        assertTrue(controller.findAll().isEmpty());

        controller.create(validFilm());
        controller.create(validFilm());

        assertEquals(2, controller.findAll().size());
    }
}
