package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserStorage;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private final Map<Integer, Set<Integer>> friends = new HashMap<>();
    private final Logger log = LoggerFactory.getLogger(InMemoryUserStorage.class);

    public List<User> getAllUsers() {
        return users.values().stream().toList();
    }

    public User getUserById(Integer id) {
        return this.users.get(id);
    }

    public Set<Integer> getFriends(Integer id) {
        return this.friends.getOrDefault(id, Collections.emptySet());
    }

    public void addFriend(Integer id, Integer friendId) {
        if (friends.containsKey(id)) {
            friends.get(id).add(friendId);
        } else {
            friends.put(id, new HashSet<>(Set.of(friendId)));
        }
    }

    public void deleteFriend(Integer id, Integer friendId) {
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

    public boolean containsUser(Integer id) {
        return users.containsKey(id);
    }

    private Integer getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .max(Integer::compare)
                .orElse(0);
        return ++currentMaxId;
    }
}
