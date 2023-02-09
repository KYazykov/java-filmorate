package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.PostNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class UserService extends InMemoryUserStorage {


    @Override
    public void addFriend(Long userId, Long friendId) {
        checkRequestBodyUser(userId, friendId);
        log.info("Запрос на добавление друга");
        User user = getUsers().get(userId);
        User friend = getUsers().get(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        checkRequestBodyUser(userId, friendId);
        log.info("Запрос на удаление друга");
        User user = getUsers().get(userId);
        User friend = getUsers().get(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    @Override
    public List<User> findAllFriends(Long userId) {
        if (getUsers().get(userId) == null) {
            log.info("Выявлена ошибка валидации, пользователь с таким id не найден");
            throw new PostNotFoundException("Пользователь с таким id не найден");
        }
        log.info("Запрос на показ всех друзей");
        User user = getUsers().get(userId);
        List<User> friends = new ArrayList<>();
        for (Long id : user.getFriends()) {
            friends.add(getUsers().get(id));
        }
        return friends;
    }

    @Override
    public List<User> findAllMutualFriends(Long userId, Long friendId) {
        checkRequestBodyUser(userId, friendId);
        log.info("Запрос на показ всех общих друзей");
        User user = getUsers().get(userId);
        User anotherUser = getUsers().get(friendId);
        List<User> friends = new ArrayList<>();
        List<User> friendsAnotherUser = new ArrayList<>();
        if (user.getFriends() == null || anotherUser.getFriends() == null) {
            return List.of();
        }
        for (Long id : user.getFriends()) {
            friends.add(getUsers().get(id));
        }
        for (Long id : anotherUser.getFriends()) {
            friendsAnotherUser.add(getUsers().get(id));
        }
        return friends.stream().filter(friendsAnotherUser::contains).collect(toList());
    }

    public void checkRequestBodyUser(Long userId, Long friendId) {
        if (getUsers().get(userId) == null) {
            log.info("Выявлена ошибка валидации, пользователь с таким id не найден");
            throw new PostNotFoundException("Пользователь с таким id не найден");
        }
        if (getUsers().get(friendId) == null) {
            log.info("Выявлена ошибка валидации, добавляемый пользователь с таким id не найден");
            throw new PostNotFoundException("Добавляемый пользователь с таким id не найден");
        }
    }

    @Override
    public Collection<User> findAllUsers() {
        return super.findAllUsers();
    }

    @Override
    public User addUser(User user) {
        return super.addUser(user);
    }

    @Override
    public User updateUser(User user) {
        return super.updateUser(user);
    }

    @Override
    public void deleteUser(Long userId) {
        super.deleteUser(userId);
    }

    @Override
    public User getUserById(Long userId) {
        return super.getUserById(userId);
    }
}
