package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationDBTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final JdbcTemplate jdbcTemplate;

    private void clearDb() {
        String sql = "DELETE FROM USERS; " +
                " DELETE FROM FILM;";
        jdbcTemplate.update(sql);
    }

    @AfterEach
    void afterEach() {
        clearDb();
    }

    @Test
    public void testAddUser() {
        User user = new User();
        user.setEmail("ivanov@mail.ru");
        user.setLogin("IVANOV");
        user.setName("Vanya");
        user.setBirthday(LocalDate.of(2000, Month.NOVEMBER, 11));
        userStorage.addUser(user);
        User userResult = userStorage.getUserById(1L);

        assertThat(userResult).hasFieldOrPropertyWithValue("email", "ivanov@mail.ru");
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        user.setEmail("ivanov@mail.ru");
        user.setLogin("IVANOV");
        user.setName("Vanya");
        user.setBirthday(LocalDate.of(2000, Month.NOVEMBER, 11));
        User user2 = new User();
        user2.setEmail("petrov@mail.ru");
        user2.setLogin("PETROV");
        user2.setName("Petya");
        user2.setBirthday(LocalDate.of(2010, Month.NOVEMBER, 20));

        userStorage.addUser(user);
        userStorage.addUser(user2);
        User userResult = userStorage.getUserById(user.getId());

        assertThat(userResult)
                .hasFieldOrPropertyWithValue("id", user.getId())
                .hasFieldOrPropertyWithValue("email", "ivanov@mail.ru");
    }

    @Test
    public void updateUser() {
        User user = new User();
        user.setName("Vanya");
        user.setLogin("IVANOV");
        user.setEmail("ivanov@mail.ru");
        user.setBirthday(LocalDate.of(2000, Month.NOVEMBER, 11));
        userStorage.addUser(user);

        user.setName("Faker");

        userStorage.updateUser(user);

        User userResult = userStorage.getUserById(user.getId());

        assertThat(userResult).hasFieldOrPropertyWithValue("name", "Faker");
    }

    @Test
    public void deleteUser() {
        User user = new User();
        user.setEmail("ivanov@mail.ru");
        user.setLogin("IVANOV");
        user.setName("Vanya");
        user.setBirthday(LocalDate.of(2000, Month.NOVEMBER, 11));
        userStorage.addUser(user);

        userStorage.deleteUser(user.getId());
        List<User> users = new ArrayList<>();

        Assertions.assertEquals(users, userStorage.findAllUsers());
    }

    @Test
    public void findAllUser() {
        User user = new User();
        user.setEmail("ivanov@mail.ru");
        user.setLogin("IVANOV");
        user.setName("Vanya");
        user.setBirthday(LocalDate.of(2000, Month.NOVEMBER, 11));
        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("petrov@mail.ru");
        user2.setLogin("PETROV");
        user2.setName("Petya");
        user2.setBirthday(LocalDate.of(2010, Month.FEBRUARY, 20));

        userStorage.addUser(user);
        userStorage.addUser(user2);

        Collection<User> users = List.of(user, user2);

        Assertions.assertEquals(users, userStorage.findAllUsers());
    }

    @Test
    public void testAddFilm() {

        Film film = new Film();
        film.setName("Зеленый слоник");
        film.setDescription("история про дух дружбы");
        film.setDuration(100);
        film.setMpa(new Mpa(1));
        film.setReleaseDate(LocalDate.of(2000, Month.NOVEMBER, 16));


        Film film2 = new Film();
        film2.setName("Евангелион");
        film2.setDescription("Кто бы знал о чем это");
        film2.setDuration(200);
        film2.setReleaseDate(LocalDate.of(2010, Month.JULY, 5));
        film2.setMpa(new Mpa(2));

        filmStorage.addFilm(film);

        Film filmResult = filmStorage.getFilmById(film.getId());

        assertThat(filmResult).hasFieldOrPropertyWithValue("name", "Зеленый слоник");
    }

    @Test
    public void testFindFilmById() {
        Film film = new Film();
        film.setName("Зеленый слоник");
        film.setDescription("история про дух дружбы");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(2000, Month.NOVEMBER, 16));
        film.setMpa(new Mpa(1));

        filmStorage.addFilm(film);

        Film filmResult = filmStorage.getFilmById(film.getId());

        assertThat(filmResult)
                .hasFieldOrPropertyWithValue("id", film.getId())
                .hasFieldOrPropertyWithValue("name", "Зеленый слоник");
    }

    @Test
    public void updateFilm() {

        Film film = new Film();
        film.setName("Зеленый слоник");
        film.setDescription("история про дух дружбы");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(2000, Month.NOVEMBER, 16));
        film.setMpa(new Mpa(1));

        filmStorage.addFilm(film);
        film.setName("Интерстеллар");


        filmStorage.updateFilm(film);

        Film filmResult = filmStorage.getFilmById(film.getId());

        assertThat(filmResult).hasFieldOrPropertyWithValue("name", "Интерстеллар");
    }

    @Test
    public void deleteFilm() {
        Film film = new Film();
        film.setName("Зеленый слоник");
        film.setDescription("история про дух дружбы");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(2000, Month.NOVEMBER, 16));
        film.setMpa(new Mpa(1));

        filmStorage.addFilm(film);
        filmStorage.deleteFilm(film.getId());
        List<Film> empty = new ArrayList<>();

        Assertions.assertEquals(empty, filmStorage.findAllFilms());
    }

    @Test
    public void findAllFilm() {
        Film film = new Film();
        film.setName("Зеленый слоник");
        film.setDescription("история про дух дружбы");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(2000, Month.NOVEMBER, 16));
        film.setMpa(new Mpa(1));


        Film film2 = new Film();
        film2.setName("Евангелион");
        film2.setDescription("Кто бы знал о чем это");
        film2.setDuration(200);
        film2.setReleaseDate(LocalDate.of(2010, Month.JULY, 5));
        film2.setMpa(new Mpa(2));

        filmStorage.addFilm(film);
        filmStorage.addFilm(film2);

        List<String> filmListExpected = List.of(film, film2).stream()
                .map(Object::toString)
                .collect(Collectors.toList());
        List<String> allFilms = new ArrayList<>(filmStorage.findAllFilms()).stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        Assertions.assertEquals(filmListExpected, allFilms);
    }


}