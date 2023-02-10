package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.PostNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FilmService implements FilmServiceInterface {

    @Override
    public void addLike(Long filmId, Long userId) {
        checkRequestBodyFilm(filmId, userId);
        log.info("Получен запрос на добавление фильму лайка");
        Film film = InMemoryFilmStorage.getFilms().get(filmId);
        film.getLikes().add(userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        checkRequestBodyFilm(filmId, userId);
        log.info("Получен запрос на добавление фильму лайка");
        Film film = InMemoryFilmStorage.getFilms().get(filmId);
        film.getLikes().remove(userId);
    }

    @Override
    public List<Film> findMostPopularFilms(Long count) {
        List<Film> mostPopularFilms = new ArrayList<>(InMemoryFilmStorage.getFilms().values());
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
        if (!InMemoryFilmStorage.getFilms().containsKey(filmId)) {
            log.info("Выявлена ошибка валидации, неверный индентификатор");
            throw new PostNotFoundException("Неверный индентификатор");
        }
        if (InMemoryUserStorage.getUsers().get(userId) == null) {
            log.info("Выявлена ошибка валидации, пользователь с таким id не найден");
            throw new PostNotFoundException("Пользователь с таким id не найден");
        }
    }
}
