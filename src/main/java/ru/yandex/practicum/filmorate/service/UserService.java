package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.PostNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    public void addFriend(Long userId, Long friendId) {
        checkRequestBodyUser(userId, friendId);
        log.info("Запрос на добавление друга");
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }


    public void deleteFriend(Long userId, Long friendId) {
        checkRequestBodyUser(userId, friendId);
        log.info("Запрос на удаление друга");
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }


    public List<User> findAllFriends(Long userId) {
        if (userStorage.getUserById(userId) == null) {
            log.info("Выявлена ошибка валидации, пользователь с таким id не найден");
            throw new PostNotFoundException("Пользователь с таким id не найден");
        }
        log.info("Запрос на показ всех друзей");
        User user = userStorage.getUserById(userId);
        List<User> friends = new ArrayList<>();
        for (Long id : user.getFriends()) {
            friends.add(userStorage.getUserById(id));
        }
        return friends;
    }

    public List<User> findAllMutualFriends(Long userId, Long friendId) {
        checkRequestBodyUser(userId, friendId);
        log.info("Запрос на показ всех общих друзей");
        User user = userStorage.getUserById(userId);
        ;
        User anotherUser = userStorage.getUserById(friendId);
        List<User> friends = new ArrayList<>();
        List<User> friendsAnotherUser = new ArrayList<>();
        if (user.getFriends() == null || anotherUser.getFriends() == null) {
            return List.of();
        }
        for (Long id : user.getFriends()) {
            friends.add(userStorage.getUserById(id));
        }
        for (Long id : anotherUser.getFriends()) {
            friendsAnotherUser.add(userStorage.getUserById(id));
        }
        return friends.stream().filter(friendsAnotherUser::contains).collect(toList());
    }

    public void checkRequestBodyUser(Long userId, Long friendId) {
        if (userStorage.getUserById(userId) == null) {
            log.info("Выявлена ошибка валидации, пользователь с таким id не найден");
            throw new PostNotFoundException("Пользователь с таким id не найден");
        }
        if (userStorage.getUserById(friendId) == null) {
            log.info("Выявлена ошибка валидации, добавляемый пользователь с таким id не найден");
            throw new PostNotFoundException("Добавляемый пользователь с таким id не найден");
        }
    }
}
