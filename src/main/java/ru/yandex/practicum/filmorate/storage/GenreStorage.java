package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    Genre getGenreById(int genreId);

    List<Genre> getFilmGenres(long filmId);

    List<Genre> findAllGenres();
}
