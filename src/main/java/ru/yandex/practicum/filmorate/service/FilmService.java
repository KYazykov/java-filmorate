package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.PostNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Long filmId, Long userId) {
        checkRequestBodyFilm(filmId, userId);
        log.info("Получен запрос на добавление фильму лайка");
        Film film = filmStorage.getFilmById(filmId);
        film.getLikes().add(userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        checkRequestBodyFilm(filmId, userId);
        log.info("Получен запрос на добавление фильму лайка");
        Film film = filmStorage.getFilmById(filmId);
        film.getLikes().remove(userId);
    }

    public List<Film> findMostPopularFilms(Long count) {
        List<Film> mostPopularFilms = new ArrayList<>(filmStorage.findAllFilms());
        mostPopularFilms.sort((o1, o2) -> {
            final double film1 = o1.getLikes().size();
            final double film2 = o2.getLikes().size();
            return Double.compare(film2, film1);
        });
        List<Film> films = new ArrayList<>();
        if (mostPopularFilms.size() > count) {
            for (int i = 0; i < count; i++) {
                films.add(mostPopularFilms.get(i));
            }
        } else {
            films.addAll(mostPopularFilms);
        }
        return films;
    }

    public void checkRequestBodyFilm(Long filmId, Long userId) {
        if (filmStorage.getFilmById(filmId) == null) {
            log.info("Выявлена ошибка валидации, неверный индентификатор");
            throw new PostNotFoundException("Неверный индентификатор");
        }
        if (userStorage.getUserById(userId) == null) {
            log.info("Выявлена ошибка валидации, пользователь с таким id не найден");
            throw new PostNotFoundException("Пользователь с таким id не найден");
        }
    }
}
