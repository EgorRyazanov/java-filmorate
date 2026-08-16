package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class UserMapper {
    public User mapToUser(NewUserRequest request) {
        User user = new User();
        user.setName(request.getName() != null ? request.getName() : request.getLogin());
        user.setLogin(request.getLogin());
        user.setEmail(request.getEmail());
        user.setBirthday(request.getBirthday());
        return user;
    }

    public User mapToUser(UpdateUserRequest request) {
        User user = new User();
        user.setId(request.getId());
        user.setName(request.getName() != null ? request.getName() : request.getLogin());
        user.setLogin(request.getLogin());
        user.setEmail(request.getEmail());
        user.setBirthday(request.getBirthday());
        return user;
    }

    public UserDto mapToUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setLogin(user.getLogin());
        dto.setBirthday(user.getBirthday());
        return dto;
    }
}
