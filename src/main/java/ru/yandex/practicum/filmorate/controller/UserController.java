package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final InMemoryUserStorage inMemoryUserStorage;
    private final UserService userService;
    private final UserDbStorage userDbStorage;

    @GetMapping
    public Collection<User> findAllUsers() {
        return userDbStorage.findAllUsers();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        inMemoryUserStorage.checkRequestBodyUser(user);
        userDbStorage.addUser(user);
        return userDbStorage.getUserById(user.getId());
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        inMemoryUserStorage.checkRequestBodyUser(user);
        userDbStorage.updateUser(user);
        return userDbStorage.getUserById(user.getId());
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long userId) {
        userDbStorage.deleteUser(userId);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long userId) {
        return userDbStorage.getUserById(userId);
    }


    @PutMapping("{id}/friends/{friendId}")
    public void addFriend(
            @PathVariable("id") Long userId,
            @PathVariable("friendId") Long friendId) {
        userDbStorage.addFriend(userId, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(
            @PathVariable("id") Long userId,
            @PathVariable("friendId") Long friendId) {
        userDbStorage.deleteFriend(userId, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> findAllFriends(@PathVariable("id") Long userId) {
        return userDbStorage.findAllFriends(userId);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> findAllMutualFriends(
            @PathVariable("id") Long userId,
            @PathVariable("otherId") Long friendId) {
        return userDbStorage.findAllMutualFriends(userId, friendId);
    }
}

