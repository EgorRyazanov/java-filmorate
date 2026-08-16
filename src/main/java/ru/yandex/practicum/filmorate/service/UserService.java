package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.errors.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final UserMapper userMapper;

    public UserService(@Qualifier("UserDataBaseImplementation") UserStorage userStorage, UserMapper userMapper) {
        this.userStorage = userStorage;
        this.userMapper = userMapper;
    }

    public List<UserDto> getAllUsers() {
        log.trace("Получение всех пользователей");
        return userStorage.getAllUsers().stream().map(this.userMapper::mapToUserDto).toList();
    }

    public UserDto getUserById(Long id) {
        log.trace("Получение пользователя по ID");
        return userStorage.getUserById(id)
            .map(this.userMapper::mapToUserDto)
            .orElseThrow(NotFoundException::new);
    }

    public List<UserDto> getFriends(Long id) {
        log.trace("Получение друзей пользователя");
        if (!userStorage.containsUser(id)) {
            log.error("Объект не найден");
            throw new NotFoundException();
        }

        return userStorage.getFriends(id).stream().map(this.userMapper::mapToUserDto).toList();
    }

    public List<UserDto> getCommonFriends(Long id, Long otherId) {
        log.trace("Получение общих друзья с пользователем");
        return userStorage.getFriends(id).stream()
                .filter(friendId -> userStorage.getFriends(otherId).contains(friendId))
                .map(this.userMapper::mapToUserDto)
                .toList();
    }

    public void addFriend(Long id, Long friendId) {
        log.trace("Добавление в друзья");
        if (!userStorage.containsUser(id) || !userStorage.containsUser(friendId)) {
            log.error("Объекта не было найдено");
            throw new NotFoundException();
        }
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(Long id, Long friendId) {
        log.trace("Удаление из друзей");
        if (!userStorage.containsUser(id) || !userStorage.containsUser(friendId)) {
            log.error("Объекта не было найдено");
            throw new NotFoundException();
        }
        userStorage.deleteFriend(id, friendId);
    }

    public UserDto createUser(NewUserRequest newUser) {
        log.trace("Создание пользователя");

        return this.userMapper.mapToUserDto(this.userStorage.createUser(this.userMapper.mapToUser(newUser)));
    }

    public UserDto updateUser(UpdateUserRequest updatedUser) {
        log.trace("Обновление пользователя");
        if (!this.userStorage.containsUser(updatedUser.getId())) {
            log.error("Объект не был найден");
            throw new NotFoundException();
        }

        return this.userMapper.mapToUserDto(this.userStorage.updateUser(this.userMapper.mapToUser(updatedUser)));
    }
}
