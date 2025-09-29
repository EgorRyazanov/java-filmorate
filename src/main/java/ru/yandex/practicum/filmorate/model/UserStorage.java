package ru.yandex.practicum.filmorate.model;


import java.util.List;
import java.util.Set;

public interface UserStorage {
    /* Получение всех пользователей */
    List<User> getAllUsers();

    /* Создание пользователя */
    User createUser(User user);

    /* Обновление пользователя */
    User updateUser(User user);

    /* Существует ли пользователь */
    boolean containsUser(Integer id);

    /* Получить пользователя по ID */
    User getUserById(Integer id);

    /* Добавить в друзья */
    void addFriend(Integer id, Integer friendId);

    /* Удалить из друзей */
    void deleteFriend(Integer id, Integer friendId);

    /* Получить список друзей пользователя */
    Set<Integer> getFriends(Integer id);
}
