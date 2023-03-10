package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public Collection<Genre> findAll() {
        return genreStorage.findAllGenres();
    }

    public List<Genre> getFilmGenres(long filmId) {
        return genreStorage.getFilmGenres(filmId);
    }

    public Genre getGenreById(int genreId) {
        return genreStorage.getGenreById(genreId);
    }
}
