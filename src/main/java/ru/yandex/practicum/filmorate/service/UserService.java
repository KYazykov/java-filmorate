package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.PostNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class UserService implements UserServiceInterface {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        checkRequestBodyUser(userId, friendId);
        log.info("Запрос на добавление друга");
        User user = userStorage.getUsers().get(userId);
        User friend = userStorage.getUsers().get(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        checkRequestBodyUser(userId, friendId);
        log.info("Запрос на удаление друга");
        User user = userStorage.getUsers().get(userId);
        User friend = userStorage.getUsers().get(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    @Override
    public List<User> findAllFriends(Long userId) {
        if (userStorage.getUsers().get(userId) == null) {
            log.info("Выявлена ошибка валидации, пользователь с таким id не найден");
            throw new PostNotFoundException("Пользователь с таким id не найден");
        }
        log.info("Запрос на показ всех друзей");
        User user = userStorage.getUsers().get(userId);
        List<User> friends = new ArrayList<>();
        for (Long id : user.getFriends()) {
            friends.add(userStorage.getUsers().get(id));
        }
        return friends;
    }

    @Override
    public List<User> findAllMutualFriends(Long userId, Long friendId) {
        checkRequestBodyUser(userId, friendId);
        log.info("Запрос на показ всех общих друзей");
        User user = userStorage.getUsers().get(userId);
        User anotherUser = userStorage.getUsers().get(friendId);
        List<User> friends = new ArrayList<>();
        List<User> friendsAnotherUser = new ArrayList<>();
        if (user.getFriends() == null || anotherUser.getFriends() == null) {
            return List.of();
        }
        for (Long id : user.getFriends()) {
            friends.add(userStorage.getUsers().get(id));
        }
        for (Long id : anotherUser.getFriends()) {
            friendsAnotherUser.add(userStorage.getUsers().get(id));
        }
        return friends.stream().filter(friendsAnotherUser::contains).collect(toList());
    }

    public void checkRequestBodyUser(Long userId, Long friendId) {
        if (userStorage.getUsers().get(userId) == null) {
            log.info("Выявлена ошибка валидации, пользователь с таким id не найден");
            throw new PostNotFoundException("Пользователь с таким id не найден");
        }
        if (userStorage.getUsers().get(friendId) == null) {
            log.info("Выявлена ошибка валидации, добавляемый пользователь с таким id не найден");
            throw new PostNotFoundException("Добавляемый пользователь с таким id не найден");
        }
    }
}
