package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<UserDto> findAll() {
        log.info("Получен запрос на получение всех пользователей");
        return this.userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto findById(@PathVariable("userId") Long id) {
        log.info("Получен запрос на получение пользователя");
        return this.userService.getUserById(id);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable("userId") Long id, @PathVariable("friendId") Long friendId) {
        log.info("Получен запрос на добавление в друзья");
        this.userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable("userId") Long id, @PathVariable("friendId") Long friendId) {
        log.info("Получен запрос на удаление из друзей");
        this.userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<UserDto> getFriends(@PathVariable("userId") Long id) {
        log.info("Получен запрос на получение списка друзей");
        return this.userService.getFriends(id);
    }

    @GetMapping("{userId}/friends/common/{otherId}")
    public List<UserDto> getCommonFriends(@PathVariable("userId") Long id, @PathVariable("otherId") Long otherUserId) {
        log.info("Получен запрос на получение списка общих друзей");
        return this.userService.getCommonFriends(id, otherUserId);
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody NewUserRequest user) {
        log.info("Получен запрос на создание пользователя");
        UserDto createdUser = this.userService.createUser(user);
        log.info("Запрос на создание пользователя завершился успешно");
        return createdUser;
    }

    @PutMapping
    public UserDto update(@Valid @RequestBody UpdateUserRequest user) {
        log.info("Получен запрос на обновление пользователя");
        UserDto updatedUser = this.userService.updateUser(user);
        log.info("Запрос на обновление пользователя завершился успешно");
        return updatedUser;
    }
}
