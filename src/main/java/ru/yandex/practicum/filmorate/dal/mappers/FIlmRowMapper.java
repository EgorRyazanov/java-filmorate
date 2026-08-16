package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;

@Component
public class FIlmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        Film film = new Film();
        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setDuration(rs.getInt("duration"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());

        long ratingId = rs.getLong("rating_id");
        if (!rs.wasNull()) {
            Rating rating = new Rating();
            rating.setId(ratingId);
            String ratingName = rs.getString("rating_name");
            if (ratingName != null) {
                rating.setName(ratingName);
            }
            film.setMpa(rating);
        }

        return film;
    }
}
