package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.PostNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    static private final Map<Long, Film> films = new HashMap<>();
    public FilmService filmService;
    long id = 1;

    public Map<Long, Film> getFilms() {
        return films;
    }

    @Override
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    @Override
    public Film getFilmById(Long filmId) {
        if (!films.containsKey(filmId)) {
            log.info("Выявлена ошибка валидации, неверный индентификатор");
            throw new PostNotFoundException("Неверный индентификатор");
        }
        log.info("Получен запрос на получение информации о фильмe");
        return films.get(filmId);
    }

    @Override
    public Film addFilm(Film film) {
        checkRequestBodyFilm(film);
        log.info("Получен запрос на добавление фильма.");
        film.setId(id);
        films.put(film.getId(), film);
        id++;
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
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

    @Override
    public void deleteFilm(Long filmId) {
        if (!films.containsKey(filmId)) {
            log.info("Выявлена ошибка валидации, неверный индентификатор");
            throw new PostNotFoundException("Неверный индентификатор");
        }
        log.info("Получен запрос на удаление фильма");
        films.remove(filmId);
    }

    public void checkRequestBodyFilm(Film film) {
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

    @Override
    public void addLike(Long filmId, Long userId) {
        filmService.addLike(filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        filmService.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> findMostPopularFilms(Long count) {
        return filmService.findMostPopularFilms(count);
    }
}
