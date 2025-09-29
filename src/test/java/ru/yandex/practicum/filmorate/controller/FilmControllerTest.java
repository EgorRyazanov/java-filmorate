package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.errors.NotFoundException;
import ru.yandex.practicum.filmorate.errors.ValidationError;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserStorage;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController controller;
    private UserStorage userStorage;

    @BeforeEach
    void beforeEach() {
        userStorage = new InMemoryUserStorage();
        FilmStorage filmStorage = new InMemoryFilmStorage();
        FilmService service = new FilmService(filmStorage, userStorage);
        controller = new FilmController(service);
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

    @Test
    void shouldFindById() {
        Film created = controller.create(validFilm());
        Film found = controller.findById(created.getId());

        assertEquals(created.getId(), found.getId());
        assertEquals(created.getName(), found.getName());
    }

    @Test
    void shouldAddAndRemoveLike() {
        Film film1 = controller.create(validFilm());
        Film film2 = controller.create(validFilm());
        User user1 = userStorage.createUser(new User("email", "login"));
        User user2 = userStorage.createUser(new User("email", "login"));


        controller.addLike(film1.getId(), user1.getId());
        controller.addLike(film1.getId(), user2.getId());
        controller.addLike(film2.getId(), user1.getId());

        assertEquals(
                List.of(film1.getId(), film2.getId()),
                controller.getPopularFilms(10).stream().map(Film::getId).toList()
        );

        controller.deleteLike(film1.getId(), user2.getId());

        List<Integer> top = controller.getPopularFilms(10).stream().map(Film::getId).toList();
        assertTrue(top.containsAll(List.of(film1.getId(), film2.getId())));
    }

    @Test
    void shouldReturnNotFoundWhenLikeUnknownFilm() {
        assertThrows(NotFoundException.class, () -> controller.addLike(999, 1));
        assertThrows(NotFoundException.class, () -> controller.deleteLike(999, 1));
    }

    @Test
    void shouldReturnPopularFilmsLimitedByCount() {
        Film film1 = controller.create(validFilm());
        Film film2 = controller.create(validFilm());
        controller.create(validFilm());

        User user1 = userStorage.createUser(new User("email", "login"));
        User user2 = userStorage.createUser(new User("email", "login"));

        controller.addLike(film1.getId(), user1.getId());
        controller.addLike(film1.getId(), user2.getId());
        controller.addLike(film2.getId(), user1.getId());

        List<Film> top1 = controller.getPopularFilms(1);
        assertEquals(1, top1.size());
        assertEquals(film1.getId(), top1.getFirst().getId());

        List<Film> top2 = controller.getPopularFilms(2);
        assertEquals(2, top2.size());
        assertEquals(List.of(film1.getId(), film2.getId()),
                top2.stream().map(Film::getId).toList());
    }
}