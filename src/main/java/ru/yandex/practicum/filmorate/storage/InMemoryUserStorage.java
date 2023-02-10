package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.PostNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    static private final Map<Long, User> users = new HashMap<>();
    public long id = 1;
    static public Map<Long, User> getUsers() {
        return users;
    }

    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @Override
    public User getUserById(Long userId) {
        if (!users.containsKey(userId)) {
            log.info("Выявлена ошибка валидации, неверный индентификатор");
            throw new PostNotFoundException("Неверный индентификатор");
        }
        log.info("Запрос на получение данных пользователя");
        return users.get(userId);
    }

    @Override
    public User addUser(User user) {
        checkRequestBodyUser(user);
        if (!users.isEmpty()) {
            for (User user1 : users.values()) {
                if (user1.getEmail().equalsIgnoreCase(user.getEmail())) {
                    log.info("Выявлена ошибка валидации, пользователь с такой электронной почтой уже зарегистрирован");
                    throw new ValidationException("Пользователь с электронной почтой " +
                            user.getEmail() + " уже зарегистрирован.");
                }
            }
        }
        log.info("Запрос на добавление пользователя");
        user.setId(id);
        users.put(user.getId(), user);
        id++;
        return user;
    }

    @Override
    public User updateUser(User user) {
        checkRequestBodyUser(user);
        if (!users.isEmpty()) {
            if (!users.containsKey(user.getId())) {
                log.info("Выявлена ошибка валидации, неверный индентификатор");
                throw new ValidationException("Неверный индентификатор");
            }
        }
        log.info("Запрос на изменение данных пользователя");
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteUser(Long userId) {
        if (!users.containsKey(userId)) {
            log.info("Выявлена ошибка валидации, неверный индентификатор");
            throw new PostNotFoundException("Неверный индентификатор");
        }
        log.info("Запрос на удаление пользователя");
        users.remove(userId);
    }

    public void checkRequestBodyUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.info("Выявлена ошибка валидации, адрес электронной почты не может быть пустым");
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        }
        if (!user.getEmail().contains("@")) {
            log.info("Выявлена ошибка валидации, адрес электронной почты должен иметь @");
            throw new ValidationException("Адрес электронной почты должен иметь @");
        }
        if (!user.getLogin().equals(user.getLogin().trim())) {
            log.info("Выявлена ошибка валидации, логин не должен иметь пробелы");
            throw new ValidationException("Логин не должен иметь пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Выявлена ошибка валидации, дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
