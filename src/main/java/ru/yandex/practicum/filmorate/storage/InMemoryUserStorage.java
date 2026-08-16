package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserStorage;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Logger log = LoggerFactory.getLogger(InMemoryUserStorage.class);

    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Set<Long>> friends = new HashMap<>();

    public List<User> getAllUsers() {
        return users.values().stream().toList();
    }

    public Optional<User> getUserById(Long id) {
        return Optional.of(this.users.get(id));
    }

    public Collection<User> getFriends(Long userId) {
        Collection<Long> userFriendIds = this.friends.get(userId);
        return userFriendIds.stream().map(
            users::get
        ).toList();
    }

    public void addFriend(Long id, Long friendId) {
        if (friends.containsKey(id)) {
            friends.get(id).add(friendId);
        } else {
            friends.put(id, new HashSet<>(Set.of(friendId)));
        }
    }

    public void deleteFriend(Long id, Long friendId) {
        if (friends.containsKey(id)) {
            friends.get(id).remove(friendId);
        }
    }

    public User createUser(User user) {
        user.setId(getNextId());
        if (user.getName() == null) {
            log.trace("Имя пользователя пустое, будет использоваться логин");
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) {
        if (user.getName() == null) {
            log.trace("Имя пользователя пустое, будет использоваться логин");
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    private Long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .max(Long::compare)
                .orElse(0L);
        return ++currentMaxId;
    }

    public boolean containsUser(Long id) {
        return users.containsKey(id);
    }
}
