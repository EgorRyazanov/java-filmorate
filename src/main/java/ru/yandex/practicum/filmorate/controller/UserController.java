package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен запрос на получение всех пользователей");
        return this.userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User findById(@PathVariable("userId") Integer id) {
        log.info("Получен запрос на получение пользователя");
        return this.userService.getUserById(id);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable("userId") Integer id, @PathVariable("friendId") Integer friendId) {
        log.info("Получен запрос на добавление в друзья");
        this.userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable("userId") Integer id, @PathVariable("friendId") Integer friendId) {
        log.info("Получен запрос на удаление из друзей");
        this.userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable("userId") Integer id) {
        log.info("Получен запрос на получение списка друзей");
        return this.userService.getFriends(id);
    }

    @GetMapping("{userId}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("userId") Integer id, @PathVariable("otherId") Integer otherUserId) {
        log.info("Получен запрос на получение списка общих друзей");
        return this.userService.getCommonFriends(id, otherUserId);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Получен запрос на создание пользователя");
        this.userService.createUser(user);
        log.info("Запрос на создание пользователя завершился успешно");
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("Получен запрос на обновление пользователя");
        this.userService.updateUser(user);
        log.info("Запрос на обновление пользователя завершился успешно");
        return user;
    }
}
