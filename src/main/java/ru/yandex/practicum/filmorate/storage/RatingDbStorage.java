package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.RatingStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class RatingDbStorage extends BaseRepository<Rating> implements RatingStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM ratings";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM ratings WHERE id = ?";

    public RatingDbStorage(JdbcClient jdbc, RowMapper<Rating> mapper) {
        super(jdbc, mapper);
    }

    public List<Rating> getAllRating() {
        return this.findMany(FIND_ALL_QUERY);
    }

    public Optional<Rating> getById(Long id) {
        return this.findOne(FIND_BY_ID_QUERY, id);
    }
}
