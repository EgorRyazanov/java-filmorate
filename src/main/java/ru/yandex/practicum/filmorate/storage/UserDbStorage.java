package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Qualifier("UserDataBaseImplementation")
@Slf4j
@Repository
public class UserDbStorage extends BaseRepository<User> implements UserStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_ALL_FRIENDS_QUERY = "SELECT * FROM users WHERE id in (SELECT friend_id FROM friends WHERE user_id = ?)";
    private static final String INSERT_FRIENDSHIP_QUERY = "INSERT INTO friends(user_id, friend_id) VALUES (?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String INSERT_USER_QUERY = "INSERT INTO users(email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private static final String FIND_FRIEND_QUERY = "SELECT COUNT(*) FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friends WHERE user_id = ? and friend_id = ?";

    public UserDbStorage(JdbcClient jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public List<User> getAllUsers() {
        return this.findMany(FIND_ALL_QUERY);
    }

    public Optional<User> getUserById(Long id) {
        return this.findOne(FIND_BY_ID_QUERY, id);
    }

    public User createUser(User user) {
        Long userId = this.insert(INSERT_USER_QUERY, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());

        user.setId(userId);

        return user;
    }

    public User updateUser(User user) {
        update(
            UPDATE_USER_QUERY,
            user.getEmail(),
            user.getLogin(),
            user.getName(),
            user.getBirthday(),
            user.getId()
        );
        return user;
    }

    public boolean containsUser(Long id) {
        return this.getUserById(id).isPresent();
    }

    public Collection<User> getFriends(Long userId) {
        return this.findMany(FIND_ALL_FRIENDS_QUERY, userId);
    }

    public boolean hasFriend(Long userId, Long friendId) {
        Number count = (Number) jdbc.sql(FIND_FRIEND_QUERY)
            .params(userId, friendId)
            .query()
            .singleValue();
        return count.intValue() > 0;

    }

    public void addFriend(Long userId, Long friendId) {
        if (!this.hasFriend(userId, friendId)) {
            this.insert(INSERT_FRIENDSHIP_QUERY, userId, friendId);
        }
    }

    public void deleteFriend(Long userId, Long friendId) {
        this.delete(DELETE_FRIEND_QUERY, userId, friendId);
    }
}
