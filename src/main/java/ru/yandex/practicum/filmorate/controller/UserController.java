package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    int id = 1;

    @GetMapping
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.info("Выявлена ошибка валидации, адрес электронной почты не может быть пустым");
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        }
        if (!users.isEmpty()) {
            for (User user1 : users.values()) {
                if (user1.getEmail().equals(user.getEmail())) {
                    log.info("Выявлена ошибка валидации, пользователь с такой электронной почтой уже зарегистрирован");
                    throw new ValidationException("Пользователь с электронной почтой " +
                            user.getEmail() + " уже зарегистрирован.");
                }
            }
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
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        log.info("Запрос на добавление пользователя");
        user.setId(id);
        users.put(user.getId(), user);
        id++;
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
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
        if (!users.isEmpty()) {
            if (!users.containsKey(user.getId())) {
                log.info("Выявлена ошибка валидации, неверный индентификатор");
                throw new ValidationException("Неверный индентификатор");
            }
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Выявлена ошибка валидации, дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        log.info("Запрос на изменение данных пользователя");
        users.put(user.getId(), user);
        return user;
    }
}

