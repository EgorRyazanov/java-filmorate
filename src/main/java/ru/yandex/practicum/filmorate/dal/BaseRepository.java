package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BaseRepository<T> {
    protected final JdbcClient jdbc;
    protected final RowMapper<T> mapper;

    protected Optional<T> findOne(String query, Object... params) {
        return jdbc.sql(query)
                .params(params)
                .query(mapper)
                .optional();
    }

    protected List<T> findMany(String query, Object... params) {
        return jdbc.sql(query)
                .params(params)
                .query(mapper)
                .list();
    }

    public boolean delete(String query, Object... params) {
        int rowsDeleted = jdbc.sql(query)
                .params(params)
                .update();
        return rowsDeleted > 0;
    }

    protected long insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.sql(query)
                .params(params)
                .update(keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            return key.longValue();
        } else {
            throw new RuntimeException("Не удалось сохранить данные");
        }
    }

    protected void update(String query, Object... params) {
        int rowsUpdated = jdbc.sql(query)
                .params(params)
                .update();
        if (rowsUpdated == 0) {
            throw new RuntimeException("Не удалось обновить данные");
        }
    }
}