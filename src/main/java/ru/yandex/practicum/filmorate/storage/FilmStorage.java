package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Collection<Film> findAllFilms();

    Film getFilmById(Long filmId);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Long filmId);

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Film> findMostPopularFilms(Long count);

}
