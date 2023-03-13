package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.PostNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Repository
@Slf4j
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(int genreId) {
        String sql = "SELECT * FROM GENRES WHERE GENRE_ID = ? ;";
        List<Genre> genres = jdbcTemplate.query(sql,
                (rs, rowNum) -> new Genre(rs.getInt("GENRE_ID"), rs.getString("NAME"))
                , genreId);
        if (genres.size() > 0) {
            log.info("Запрос на выдачу жанра с id: {}", genreId);
            return genres.get(0);
        }
        log.info("Жанр с id: {} не найден", genreId);
        throw new PostNotFoundException("Жанр не найден");
    }

    @Override
    public List<Genre> getFilmGenres(long filmId) {
        String sql = "SELECT * FROM GENRES WHERE GENRE_ID IN (SELECT GENRE_ID FROM FILM_GENRES WHERE FILM_ID = ?) " +
                " ORDER BY GENRE_ID;";
        log.info("Запрос на выдачу всех жанров фильма с id: {}", filmId);
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new Genre(rs.getInt("GENRE_ID"), rs.getString("NAME"))
                , filmId);
    }

    @Override
    public List<Genre> findAllGenres() {
        String sql = "SELECT * FROM GENRES ORDER BY GENRE_ID;";
        log.info("Запрос на выдачу всех жанров");
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new Genre(rs.getInt("GENRE_ID"), rs.getString("NAME")));
    }
}
