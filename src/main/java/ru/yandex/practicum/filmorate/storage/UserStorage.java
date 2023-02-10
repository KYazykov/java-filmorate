package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {

    Collection<User> findAllUsers();

    Map<Long, User> getUsers();

    User getUserById(Long userId);

    User addUser(User user);

    User updateUser(User user);

    void deleteUser(Long userId);

}
