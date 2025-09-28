package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.errors.NotFoundException;
import ru.yandex.practicum.filmorate.errors.ValidationError;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserStorage;
import ru.yandex.practicum.filmorate.utils.UserValidation;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAllUsers() {
        log.trace("Получение всех пользователей");
        return userStorage.getAllUsers();
    }

    public User getUserById(Integer id) {
        log.trace("Получение пользователя по ID");
        return userStorage.getUserById(id);
    }

    public List<User> getFriends(Integer id) {
        log.trace("Получение друзей пользователя");
        if (!userStorage.containsUser(id)) {
            log.error("Объект не найден");
            throw new NotFoundException();
        }

        return userStorage.getFriends(id).stream()
                .map(userStorage::getUserById)
                .toList();
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        log.trace("Получение общих друзья с пользователем");
        return userStorage.getFriends(id).stream()
                .filter(friendId -> userStorage.getFriends(otherId).contains(friendId))
                .map(userStorage::getUserById)
                .toList();
    }

    public void addFriend(Integer id, Integer friendId) {
        log.trace("Добавление в друзья");
        if (!userStorage.containsUser(id) || !userStorage.containsUser(friendId)) {
            log.error("Объекта не было найдено");
            throw new NotFoundException();
        } else {
            userStorage.addFriend(id, friendId);
            userStorage.addFriend(friendId, id);
        }
    }

    public void deleteFriend(Integer id, Integer friendId) {
        log.trace("Удаление из друзей");
        if (!userStorage.containsUser(id) || !userStorage.containsUser(friendId)) {
            log.error("Объекта не было найдено");
            throw new NotFoundException();
        } else {
            userStorage.deleteFriend(id, friendId);
            userStorage.deleteFriend(friendId, id);
        }
    }

    public User createUser(User user) {
        log.trace("Создание пользователя");
        if (!UserValidation.isUserValid(user)) {
            log.error("Произошла ошибка валидации");
            throw new ValidationError();
        }

        return this.userStorage.createUser(user);
    }

    public User updateUser(User user) {
        log.trace("Обновление пользователя");
        if (!this.userStorage.containsUser(user.getId())) {
            log.error("Объект не был найден");
            throw new NotFoundException();
        }

        if (!UserValidation.isUserValid(user)) {
            log.error("Произошла ошибка валидации");
            throw new ValidationError();
        }

        return this.userStorage.updateUser(user);
    }
}
