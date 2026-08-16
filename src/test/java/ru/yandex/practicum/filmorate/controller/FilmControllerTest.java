package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class FilmControllerTest {

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() {
        jdbcClient.sql("DELETE FROM likes").update();
        jdbcClient.sql("DELETE FROM friends").update();
        jdbcClient.sql("DELETE FROM favourite_films").update();
        jdbcClient.sql("DELETE FROM films").update();
        jdbcClient.sql("DELETE FROM users").update();

        jdbcClient.sql("ALTER TABLE films ALTER COLUMN id RESTART WITH 1").update();
        jdbcClient.sql("ALTER TABLE users ALTER COLUMN id RESTART WITH 1").update();
    }

    private NewFilmRequest validFilmRequest() {
        NewFilmRequest request = new NewFilmRequest();
        request.setName("name");
        request.setDescription("Description");
        request.setReleaseDate(LocalDate.of(2010, 7, 16));
        request.setDuration(148);
        return request;
    }

    @Test
    void shouldCreate() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validFilmRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("name"));
    }

    @Test
    void shouldReturnValidationErrorWhenEmptyName() throws Exception {
        NewFilmRequest film = validFilmRequest();
        film.setName("");

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnValidationErrorWhenLongDescription() throws Exception {
        NewFilmRequest film = validFilmRequest();
        film.setDescription("x".repeat(201));

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnValidationErrorWhenInvalidDate() throws Exception {
        NewFilmRequest film = validFilmRequest();
        film.setReleaseDate(LocalDate.of(1895, 12, 28));

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());

        film.setReleaseDate(LocalDate.of(1895, 12, 29));
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnValidationErrorWhenNegativeDuration() throws Exception {
        NewFilmRequest film = validFilmRequest();
        film.setDuration(0);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());

        film.setDuration(-10);
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }
}
