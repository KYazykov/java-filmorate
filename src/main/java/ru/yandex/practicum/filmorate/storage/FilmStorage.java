package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {

    Map<Long, Film> getFilms();

    Collection<Film> findAllFilms();

    Film getFilmById(Long filmId);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Long filmId);
}
