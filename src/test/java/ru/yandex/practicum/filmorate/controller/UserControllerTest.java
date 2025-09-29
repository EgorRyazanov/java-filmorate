package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.errors.NotFoundException;
import ru.yandex.practicum.filmorate.errors.ValidationError;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserStorage;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController controller;

    @BeforeEach
    void beforeEach() {
        UserStorage storage = new InMemoryUserStorage();
        UserService service = new UserService(storage);
        controller = new UserController(service);
    }

    private User validUser() {
        User user = new User("user@example.com", "login");
        user.setName("User Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        return user;
    }

    @Test
    void shouldCreate() {
        User createdUser = controller.create(validUser());

        assertNotNull(createdUser.getId(), "id должен быть присвоен");
        assertEquals(1, createdUser.getId());
        assertEquals("User Name", createdUser.getName());

        var all = controller.findAll();
        assertEquals(1, all.size(), "пользователь должен сохраняться во внутреннем хранилище");
    }

    @Test
    void shouldUpdateId() {
        User user1 = controller.create(validUser());
        User user2 = controller.create(validUser());

        assertEquals(1, user1.getId());
        assertEquals(2, user2.getId());
        assertNotEquals(user1.getId(), user2.getId());
    }

    @Test
    void shouldReturnValidationErrorWhenEmptyEmail() {
        User user = validUser();
        user.setEmail("");
        assertThrows(ValidationError.class, () -> controller.create(user));
    }

    @Test
    void shouldReturnValidationErrorWhenInvalidEmail() {
        User user = validUser();
        user.setEmail("invalid.email.example.com"); // без '@'
        assertThrows(ValidationError.class, () -> controller.create(user));
    }

    @Test
    void shouldReturnValidationErrorWhenEmptyLogin() {
        User user = validUser();
        user.setLogin("");
        assertThrows(ValidationError.class, () -> controller.create(user));
    }

    @Test
    void shouldReturnValidationErrorWhenLoginHasSpaces() {
        User user = validUser();
        user.setLogin("lo gin");
        assertThrows(ValidationError.class, () -> controller.create(user));
    }

    @Test
    void shouldReturnValidationErrorWhenInvalidBirthday() {
        User user = validUser();

        user.setBirthday(LocalDate.now());
        assertThrows(ValidationError.class, () -> controller.create(user));

        user.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ValidationError.class, () -> controller.create(user));

        user.setBirthday(LocalDate.now().minusDays(1));
        assertDoesNotThrow(() -> controller.create(user));
    }

    @Test
    void shouldSetNameToLoginWhenNameIsNullOnCreate() {
        User user = validUser();
        user.setName(null);
        user.setLogin("login123");
        User createdUser = controller.create(user);

        assertEquals("login123", createdUser.getName(), "если name=null, должно подставляться значение login");
    }

    @Test
    void shouldUpdate() {
        User createdUser = controller.create(validUser());
        Integer id = createdUser.getId();

        User changedUser = validUser();
        changedUser.setId(id);
        changedUser.setName("New Name");
        User updatedUser = controller.update(changedUser);

        assertEquals(id, updatedUser.getId());
        assertEquals("New Name", updatedUser.getName());
    }

    @Test
    void shouldReturnNotFoundErrorWhenUpdate() {
        User user = validUser();
        user.setId(999);
        assertThrows(NotFoundException.class, () -> controller.update(user));
    }

    @Test
    void shouldReturnValidationErrorWhenUpdateWithInvalidEmail() {
        User created = controller.create(validUser());

        User invalidUser = validUser();
        invalidUser.setId(created.getId());
        invalidUser.setEmail("no-at-sign");
        assertThrows(ValidationError.class, () -> controller.update(invalidUser));
    }

    @Test
    void shouldSetNameToLoginWhenNameIsNullOnUpdate() {
        User createdUser = controller.create(validUser());

        User updatedUser = validUser();
        updatedUser.setId(createdUser.getId());
        updatedUser.setName(null);
        updatedUser.setLogin("newlogin");
        User updatedUser2 = controller.update(updatedUser);

        assertEquals("newlogin", updatedUser2.getName());
    }

    @Test
    void shouldReturnAllUsers() {
        assertTrue(controller.findAll().isEmpty());

        controller.create(validUser());
        controller.create(validUser());

        assertEquals(2, controller.findAll().size());
    }

    @Test
    void shouldFindById() {
        User created = controller.create(validUser());
        User found = controller.findById(created.getId());

        assertEquals(created.getId(), found.getId());
        assertEquals(created.getEmail(), found.getEmail());
    }

    @Test
    void shouldAddAndDeleteFriend() {
        User user1 = controller.create(validUser());
        User user2 = controller.create(validUser());

        controller.addFriend(user1.getId(), user2.getId());

        var friendsOfU1 = controller.getFriends(user1.getId());
        var friendsOfU2 = controller.getFriends(user2.getId());

        assertEquals(1, friendsOfU1.size());
        assertEquals(user2.getId(), friendsOfU1.getFirst().getId());

        assertEquals(1, friendsOfU2.size());
        assertEquals(user1.getId(), friendsOfU2.getFirst().getId());

        controller.deleteFriend(user1.getId(), user2.getId());

        assertTrue(controller.getFriends(user1.getId()).isEmpty());
        assertTrue(controller.getFriends(user2.getId()).isEmpty());
    }

    @Test
    void shouldReturnNotFoundWhenAddFriendOfUnknownUser() {
        User user1 = controller.create(validUser());
        assertThrows(NotFoundException.class, () -> controller.addFriend(user1.getId(), 999));
        assertThrows(NotFoundException.class, () -> controller.addFriend(999, user1.getId()));
    }

    @Test
    void shouldReturnNotFoundWhenDeleteFriendOfUnknownUser() {
        User user1 = controller.create(validUser());
        assertThrows(NotFoundException.class, () -> controller.deleteFriend(user1.getId(), 999));
        assertThrows(NotFoundException.class, () -> controller.deleteFriend(999, user1.getId()));
    }

    @Test
    void shouldReturnFriends() {
        User user1 = controller.create(validUser());
        User user2 = controller.create(validUser());
        User user3 = controller.create(validUser());

        controller.addFriend(user1.getId(), user2.getId());
        controller.addFriend(user1.getId(), user3.getId());

        var friends = controller.getFriends(user1.getId());
        var ids = friends.stream().map(User::getId).collect(Collectors.toSet());

        assertEquals(Set.of(user2.getId(), user3.getId()), ids);
    }

    @Test
    void shouldThrowNotFoundWhenGetFriendsForUnknownUser() {
        assertThrows(NotFoundException.class, () -> controller.getFriends(999));
    }

    @Test
    void shouldReturnCommonFriends() {
        User user1 = controller.create(validUser());
        User user2 = controller.create(validUser());
        User user3 = controller.create(validUser());
        User user4 = controller.create(validUser());

        controller.addFriend(user1.getId(), user3.getId());
        controller.addFriend(user2.getId(), user3.getId());
        controller.addFriend(user1.getId(), user4.getId());

        var commonAB = controller.getCommonFriends(user1.getId(), user2.getId());
        assertEquals(1, commonAB.size());
        assertEquals(user3.getId(), commonAB.getFirst().getId());
    }
}
