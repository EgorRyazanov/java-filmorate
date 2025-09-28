package ru.yandex.practicum.filmorate.model;


import java.util.List;
import java.util.Set;

public interface UserStorage {
    List<User> getAllUsers();
    User createUser(User user);
    User updateUser(User user);
    boolean containsUser(Integer id);
    User getUserById(Integer id);
    void addFriend(Integer id, Integer friendId);
    void deleteFriend(Integer id, Integer friendId);
    Set<Integer> getFriends(Integer id);
}
