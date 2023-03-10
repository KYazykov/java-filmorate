package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.PostNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    private final MpaService mpaService;
    private final GenreService genreService;
    private final UserService userService;


    private Film makeFilm(ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("FILM_ID"));
        film.setDuration(rs.getInt("DURATION"));
        film.setName(rs.getString("NAME"));
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setMpa(new Mpa(rs.getInt("MPA_ID")));
        film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());

        film.setGenres(genreService.getFilmGenres(film.getId()));

        film.setMpa(mpaService.getMpaById(film.getMpa().getId()));

        return film;
    }

    public boolean isFilmExist(long filmId) {
        String sql = "SELECT COUNT(*) FROM FILM WHERE FILM_ID = ? ;";
        int countUser = jdbcTemplate.queryForObject(sql, Integer.class, filmId);

        return countUser > 0;
    }

    private void updateMpa(Film film) {
        film.setMpa(mpaService.getMpaById(film.getMpa().getId()));
    }

    private void updateGenresName(Film film) {
        if (film.getGenres() == null) {
            return;
        }
        List<Genre> genresWithName = new ArrayList<>();
        Set<Integer> doubleId = new HashSet<>();
        for (Genre genre : film.getGenres()) {
            if (!doubleId.contains(genre.getId())) {
                doubleId.add(genre.getId());
                genresWithName.add(genreService.getGenreById(genre.getId()));
            }
        }
        film.setGenres(genresWithName);
    }

    @Override
    public Collection<Film> findAllFilms() {
        String sql = "SELECT * FROM FILM;";
        log.info("Запрос на выдачу всех фильмов");
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film getFilmById(Long filmId) {
        if (isFilmExist(filmId)) {
            String sql = "SELECT * FROM FILM WHERE FILM_ID = ?;";
            List<Film> list = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), filmId);
            log.info("Запрос на выдачу фильма с id: {}", filmId);
            return list.get(0);
        } else {
            log.info(" Фильм с id: {} не найден", filmId);
            throw new PostNotFoundException("Фильм с таким id не найден");
        }
    }

    @Override
    public Film addFilm(Film film) {
        String sqlCreateFilm = "INSERT INTO FILM(" +
                "DURATION, " +
                "NAME, " +
                "DESCRIPTION, " +
                "MPA_ID, " +
                "RELEASE_DATE) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlCreateFilm, new String[]{"FILM_ID"});
            stmt.setInt(1, film.getDuration());
            stmt.setString(2, film.getName());
            stmt.setString(3, film.getDescription());
            stmt.setInt(4, film.getMpa().getId());
            stmt.setDate(5, Date.valueOf(film.getReleaseDate()));
            return stmt;
        }, keyHolder);

        updateMpa(film);
        updateGenresName(film);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        saveGenreToDb(film);
        log.info("Запрос на добавление фильма");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (isFilmExist(film.getId())) {
            String sqlUpdateFilm = "UPDATE FILM SET " +
                    "DURATION = ?, " +
                    "NAME = ?, " +
                    "DESCRIPTION = ?, " +
                    "MPA_ID = ?, " +
                    "RELEASE_DATE = ? " +
                    "WHERE FILM_ID = ?";

            jdbcTemplate.update(sqlUpdateFilm,
                    film.getDuration()
                    , film.getName()
                    , film.getDescription()
                    , film.getMpa().getId()
                    , Date.valueOf(film.getReleaseDate())
                    , film.getId());
            updateMpa(film);
            updateGenresName(film);

            saveGenreToDb(film);
            log.info("Запрос на обновление данных фильма");
            return film;
        } else {
            log.info("Фильм с id не найден");
            throw new PostNotFoundException("Фильм с таким id не найден");
        }
    }

    private void saveGenreToDb(Film film) {
        long filmId = film.getId();
        if (genreService.getFilmGenres(filmId).size() > 0) {
            String sql = "DELETE FROM FILM_GENRES WHERE FILM_ID = ? ;";
            jdbcTemplate.update(sql, filmId);
        }
        if (film.getGenres() != null) {
            final Set<Genre> filmGenres = new HashSet<>(film.getGenres());
            String sql = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
            for (Genre genre : filmGenres) {
                jdbcTemplate.update(sql, filmId, genre.getId());
            }
        }
    }

    @Override
    public void deleteFilm(Long filmId) {
        if (isFilmExist(filmId)) {
            String sql = "DELETE FROM FILM WHERE film_id = ?";
            jdbcTemplate.update(sql, filmId);
            log.info("Запрос на удаление фильма с id: {}", filmId);
        } else {
            log.info(" Фильм с id: {} не найден", filmId);
            throw new PostNotFoundException("Фильм с таким id не найден");
        }
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        if (isFilmExist(filmId) && userService.isUserExist(userId)) {
            String sql = "INSERT INTO LIKES(FILM_ID, USER_ID) VALUES (?, ?)";
            jdbcTemplate.update(sql, filmId, userId);
            log.info("Запрос на добавление лайка фильму с id: {}, пользователем с id: {}", filmId, userId);
        } else {
            log.info("Ошибка при добавлении лайка, фильм с id: {} или пользователь с id: {} не найден", filmId, userId);
            throw new PostNotFoundException("Фильм или пользователь с таким id не найден");
        }
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        if (isFilmExist(filmId) && userService.isUserExist(userId)) {
            String sql = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?;";
            jdbcTemplate.update(sql, filmId, userId);
            log.info("Запрос на удаление лайка фильму с id: {}, пользователем с id: {}", filmId, userId);
        } else {
            log.info("Ошибка при удалении, фильм с id: {} или пользователь с id: {} не найден", filmId, userId);
            throw new PostNotFoundException("Фильм или пользователь с таким id не найден");
        }
    }

    @Override
    public List<Film> findMostPopularFilms(Long count) {
        String sql = "SELECT * " +
                "FROM FILM as F " +
                "WHERE " +
                "FILM_ID IN (SELECT FILM_ID FROM LIKES as L) " +
                "GROUP BY F.FILM_ID " +
                "ORDER BY count(FILM_ID) DESC " +
                "LIMIT ?;";
        if (jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), count).isEmpty()) {
            return new ArrayList<>(findAllFilms());
        }
        log.info("Запрос на выдачу самых популярных фильмов");
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), count);
    }
}
