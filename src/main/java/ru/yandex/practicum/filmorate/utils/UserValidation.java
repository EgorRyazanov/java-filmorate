package ru.yandex.practicum.filmorate.utils;

import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;

public class UserValidation {

    public static Boolean isUserValid(User user) {
        return !user.getEmail().isEmpty() && user.getEmail().contains("@") && !user.getLogin().isEmpty() && !user.getLogin().contains(" ") && user.getBirthday().isBefore(LocalDate.now());
    }
}
