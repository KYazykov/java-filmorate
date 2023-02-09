package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.PostNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class FilmService extends InMemoryFilmStorage {
    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();

    @Override
    public void addLike(Long filmId, Long userId) {
        checkRequestBodyFilm(filmId, userId);
        log.info("Получен запрос на добавление фильму лайка");
        Film film = getFilms().get(filmId);
        film.getLikes().add(userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        checkRequestBodyFilm(filmId, userId);
        log.info("Получен запрос на добавление фильму лайка");
        Film film = getFilms().get(filmId);
        film.getLikes().remove(userId);
    }

    @Override
    public List<Film> findMostPopularFilms(Long count) {
        List<Film> mostPopularFilms = new ArrayList<>(getFilms().values());
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

    @Override
    public Collection<Film> findAllFilms() {
        return super.findAllFilms();
    }

    @Override
    public Film addFilm(Film film) {
        return super.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return super.updateFilm(film);
    }

    @Override
    public void deleteFilm(Long filmId) {
        super.deleteFilm(filmId);
    }

    @Override
    public Film getFilmById(Long filmId) {
        return super.getFilmById(filmId);
    }

    public void checkRequestBodyFilm(Long filmId, Long userId) {
        if (!getFilms().containsKey(filmId)) {
            log.info("Выявлена ошибка валидации, неверный индентификатор");
            throw new PostNotFoundException("Неверный индентификатор");
        }
        if (inMemoryUserStorage.getUsers().get(userId) == null) {
            log.info("Выявлена ошибка валидации, пользователь с таким id не найден");
            throw new PostNotFoundException("Пользователь с таким id не найден");
        }
    }
}
