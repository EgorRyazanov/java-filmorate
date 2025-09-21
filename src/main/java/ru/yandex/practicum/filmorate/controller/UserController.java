package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.errors.NotFoundException;
import ru.yandex.practicum.filmorate.errors.ValidationError;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.ErrorText;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен запрос на получение всех пользователей");
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Получен запрос на создание пользователя");
        if (!isUserValid(user)) {
            log.error("Произошла ошибка валидации");
            throw new ValidationError(ErrorText.validationError);
        }
        user.setId(getNextId());
        if (user.getName() == null) {
            log.trace("Имя пользователя пустое, будет использоваться логин");
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Запрос на создание пользователя завершился успешно");
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("Получен запрос на обновление пользователя");
        if (!users.containsKey(user.getId())) {
            log.error("Объект не был найден");
            throw new NotFoundException(ErrorText.notFoundError);
        }

        if (!isUserValid(user)) {
            log.error("Произошла ошибка валидации");
            throw new ValidationError(ErrorText.validationError);
        }
        if (user.getName() == null) {
            log.trace("Имя пользователя пустое, будет использоваться логин");
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        log.info("Запрос на обновление пользователя завершился успешно");
        return user;
    }

    private Boolean isUserValid(User user) {
        return !user.getEmail().isEmpty() && user.getEmail().contains("@") && !user.getLogin().isEmpty() && !user.getLogin().contains(" ") && user.getBirthday().isBefore(LocalDate.now());
    }

    private Integer getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .max(Integer::compare)
                .orElse(0);
        return ++currentMaxId;
    }
}
