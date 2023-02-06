package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    int id = 1;

    @GetMapping
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        checkRequestBodyFilm(film);
        log.info("Получен запрос на добавление фильма.");
        film.setId(id);
        films.put(film.getId(), film);
        id++;
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        checkRequestBodyFilm(film);
        if (!films.isEmpty()) {
            if (!films.containsKey(film.getId())) {
                log.info("Выявлена ошибка валидации, неверный индентификатор");
                throw new ValidationException("Неверный индентификатор");
            }
        }
        log.info("Получен запрос на изменение информации о фильме");
        films.put(film.getId(), film);
        return film;
    }

    private void checkRequestBodyFilm(@Valid @RequestBody Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.info("Выявлена ошибка валидации, название фильма не может быть пустым");
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.info("Выявлена ошибка валидации, описание фильма должно быть не более 200 симоволов");
            throw new ValidationException("Описание фильма должно быть не более 200 симоволов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Выявлена ошибка валидации, дата релиза фильма не может быть раньше 29 декабря 1895 года");
            throw new ValidationException("Дата релиза фильма не может быть раньше 29 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.info("Выявлена ошибка валидации, продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}

