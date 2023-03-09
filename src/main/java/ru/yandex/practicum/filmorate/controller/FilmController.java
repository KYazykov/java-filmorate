package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;
    private final FilmDbStorage filmDbStorage;

    @GetMapping
    public Collection<Film> findAllFilms() {
        return filmDbStorage.findAllFilms();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        inMemoryFilmStorage.checkRequestBodyFilm(film);
        filmDbStorage.addFilm(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        inMemoryFilmStorage.checkRequestBodyFilm(film);
        filmDbStorage.updateFilm(film);
        return film;
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable("id") Long filmId) {
        filmDbStorage.deleteFilm(filmId);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable("id") Long filmId) {
        return filmDbStorage.getFilmById(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(
            @PathVariable("id") Long filmId,
            @PathVariable("userId") Long userId) {
        filmDbStorage.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(
            @PathVariable("id") Long filmId,
            @PathVariable("userId") Long userId) {
        filmDbStorage.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> findMostPopularFilms(
            @RequestParam(defaultValue = "10", required = false) Long count) {
        return filmDbStorage.findMostPopularFilms(count);
    }
}

