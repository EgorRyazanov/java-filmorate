package ru.yandex.practicum.filmorate.model;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    /* Получение всех пользователей */
    List<User> getAllUsers();

    /* Создание пользователя */
    User createUser(User user);

    /* Обновление пользователя */
    User updateUser(User user);

    /* Существует ли пользователь */
    boolean containsUser(Long id);

    /* Получить пользователя по ID */
    Optional<User> getUserById(Long id);

    /* Добавить в друзья */
    void addFriend(Long id, Long friendId);

    /* Удалить из друзей */
    void deleteFriend(Long id, Long friendId);

    /* Получить список друзей пользователя */
    Collection<User> getFriends(Long id);
}
