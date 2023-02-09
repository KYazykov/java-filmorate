package ru.yandex.practicum.filmorate;

import lombok.Builder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Builder
class FilmorateApplicationTests {

    @Test
    void shouldAddFilm() {
        InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
        Film film = Film.builder()
                .name("Назад в будущее")
                .id(1L)
                .duration(120)
                .description("Попаданцы")
                .releaseDate(LocalDate.of(1995, 11, 10))
                .build();
        Film film2 = Film.builder()
                .name("Назад в будущее 2")
                .id(2L)
                .duration(125)
                .description("Попаданцы 2")
                .releaseDate(LocalDate.of(2005, 1, 11))
                .build();
        inMemoryFilmStorage.addFilm(film);
        inMemoryFilmStorage.addFilm(film2);
        List<Film> films = new ArrayList<>();
        films.add(film);
        films.add(film2);
        Assertions.assertEquals(films.toString(), inMemoryFilmStorage.findAllFilms().toString(), "Добавились не все фильмы");
        inMemoryFilmStorage.deleteFilm(1L);
        inMemoryFilmStorage.deleteFilm(2L);
    }

    @Test
    void shouldUpdateFilm() {
        InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
        Film film = Film.builder()
                .name("Назад в будущее")
                .id(1L)
                .duration(120)
                .description("Попаданцы")
                .releaseDate(LocalDate.of(1995, 11, 10))
                .build();
        Film film2 = Film.builder()
                .name("Назад в будущее")
                .id(1L)
                .duration(125)
                .description("Попаданцы 2")
                .releaseDate(LocalDate.of(2005, 1, 11))
                .build();
        inMemoryFilmStorage.addFilm(film);
        inMemoryFilmStorage.updateFilm(film2);
        List<Film> films = new ArrayList<>();
        films.add(film2);
        Assertions.assertEquals(films.toString(), inMemoryFilmStorage.findAllFilms().toString(), "Добавились не все фильмы");
        inMemoryFilmStorage.deleteFilm(1L);
    }

    @Test
    void whenDerivedExceptionThrown_thenAssertion_AddFilm() {
        InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
        Film film = Film.builder()
                .name("")
                .id(1L)
                .duration(120)
                .description("Попаданцы")
                .releaseDate(LocalDate.of(1995, 11, 10))
                .build();
        Film film2 = Film.builder()
                .name("Назад в будущее 2")
                .id(2L)
                .duration(-10)
                .description("Попаданцы 2")
                .releaseDate(LocalDate.of(2005, 1, 11))
                .build();
        Film film3 = Film.builder()
                .name("Назад в будущее")
                .id(3L)
                .duration(120)
                .description("Путешествия во времени – самая алогичная и, вместе с тем, поистине неисчерпаемая и невероятно увлекательная тема, бездонная бочка сюжетов для фантастического кинематографа. «Назад в будущее» - один из лучших фантастических фильмов в истории кино, снятых на эту тематику, настоящий культ жанра.\n" +
                        "\n" +
                        "Впрочем, определить жанровую принадлежность этой картины практически нельзя. Это, пожалуй, эталон того, как нужно снимать фильм, в основу сюжета которого легло столько жанров: это и комедия, и фантастика, и драма, и приключения, и даже триллер! Элемент каждого жанра здесь тщательно выверен и очень гармонично вписывается в общую стилистику картины.\n" +
                        "\n" +
                        "«Назад в будущее» - фильм тонкий, умный, глубокий, невероятно продуманный и, вместе с тем, очень легкий, яркий и стильный. Только Земекис мог в такой легкой и изящной форме показать по сути своей очень серьезные вещи: отсутствие")
                .releaseDate(LocalDate.of(1995, 11, 10))
                .build();
        Film film4 = Film.builder()
                .name("Назад в будущее 3")
                .id(4l)
                .duration(125)
                .description("Попаданцы 2")
                .releaseDate(LocalDate.of(1005, 1, 11))
                .build();
        Film film5 = Film.builder()
                .name("Назад в будущее")
                .id(4l)
                .duration(125)
                .description("Попаданцы 2")
                .releaseDate(LocalDate.of(2005, 1, 11))
                .build();

        Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryFilmStorage.addFilm(film);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryFilmStorage.addFilm(film2);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryFilmStorage.addFilm(film3);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryFilmStorage.addFilm(film4);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryFilmStorage.addFilm(film3);
            inMemoryFilmStorage.addFilm(film5);
        });
    }

    @Test
    void whenDerivedExceptionThrown_thenAssertion_updateFilm() {
        InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
        Film film = Film.builder()
                .name("")
                .id(1L)
                .duration(120)
                .description("Попаданцы")
                .releaseDate(LocalDate.of(1995, 11, 10))
                .build();
        Film film2 = Film.builder()
                .name("Назад в будущее 2")
                .id(2L)
                .duration(-10)
                .description("Попаданцы 2")
                .releaseDate(LocalDate.of(2005, 1, 11))
                .build();
        Film film3 = Film.builder()
                .name("Назад в будущее")
                .id(3L)
                .duration(120)
                .description("Путешествия во времени – самая алогичная и, вместе с тем, поистине неисчерпаемая и невероятно увлекательная тема, бездонная бочка сюжетов для фантастического кинематографа. «Назад в будущее» - один из лучших фантастических фильмов в истории кино, снятых на эту тематику, настоящий культ жанра.\n" +
                        "\n" +
                        "Впрочем, определить жанровую принадлежность этой картины практически нельзя. Это, пожалуй, эталон того, как нужно снимать фильм, в основу сюжета которого легло столько жанров: это и комедия, и фантастика, и драма, и приключения, и даже триллер! Элемент каждого жанра здесь тщательно выверен и очень гармонично вписывается в общую стилистику картины.\n" +
                        "\n" +
                        "«Назад в будущее» - фильм тонкий, умный, глубокий, невероятно продуманный и, вместе с тем, очень легкий, яркий и стильный. Только Земекис мог в такой легкой и изящной форме показать по сути своей очень серьезные вещи: отсутствие")
                .releaseDate(LocalDate.of(1995, 11, 10))
                .build();
        Film film4 = Film.builder()
                .name("Назад в будущее 2")
                .id(4l)
                .duration(125)
                .description("Попаданцы 2")
                .releaseDate(LocalDate.of(1005, 1, 11))
                .build();

        Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryFilmStorage.updateFilm(film);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryFilmStorage.updateFilm(film2);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryFilmStorage.updateFilm(film3);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryFilmStorage.updateFilm(film4);
        });
    }

    @Test
    void shouldFindAllFilms() {
        InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
        Film film = Film.builder()
                .name("Назад в будущее")
                .id(1L)
                .duration(120)
                .description("Попаданцы")
                .releaseDate(LocalDate.of(1995, 11, 10))
                .build();
        Film film2 = Film.builder()
                .name("Назад в будущее 2")
                .id(2L)
                .duration(125)
                .description("Попаданцы 2")
                .releaseDate(LocalDate.of(2005, 1, 11))
                .build();

        inMemoryFilmStorage.addFilm(film);
        inMemoryFilmStorage.addFilm(film2);
        List<Film> films = new ArrayList<>();
        films.add(film);
        films.add(film2);
        Assertions.assertEquals(films.toString(), inMemoryFilmStorage.findAllFilms().toString(), "Неправильный показ всех фильмов");
        inMemoryFilmStorage.deleteFilm(1L);
        inMemoryFilmStorage.deleteFilm(2L);
    }

    @Test
    void shouldFindAllFilmsWithoutFilms() {
        InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
        Assertions.assertTrue(inMemoryFilmStorage.findAllFilms().isEmpty(), "Список фильмов не пустой");
    }

    @Test
    void shouldAddUser() {
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        User user = User.builder()
                .name("Иван")
                .id(1L)
                .birthday(LocalDate.of(1995, 11, 10))
                .email("k@bk.ru")
                .login("Ivan")
                .build();
        User user2 = User.builder()
                .name("Сергей")
                .id(2L)
                .birthday(LocalDate.of(2015, 11, 10))
                .email("serGey@bk.ru")
                .login("SGey")
                .build();
        inMemoryUserStorage.addUser(user);
        inMemoryUserStorage.addUser(user2);
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        Assertions.assertEquals(users.toString(), inMemoryUserStorage.findAllUsers().toString(),
                "Добавились не все пользователи");
        inMemoryUserStorage.deleteUser(1L);
        inMemoryUserStorage.deleteUser(2L);
    }

    @Test
    void shouldUpdateUser() {
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        User user = User.builder()
                .name("Иван")
                .id(1L)
                .birthday(LocalDate.of(1995, 11, 10))
                .email("k@bk.ru")
                .login("Ivan")
                .build();
        User user2 = User.builder()
                .name("Сергей")
                .id(1L)
                .birthday(LocalDate.of(2015, 11, 10))
                .email("k@bk.ru")
                .login("SGey")
                .build();
        inMemoryUserStorage.addUser(user);
        inMemoryUserStorage.updateUser(user2);
        List<User> users = new ArrayList<>();
        users.add(user2);
        Assertions.assertEquals(users.toString(), inMemoryUserStorage.findAllUsers().toString(),
                "Добавились не все пользователи");
        inMemoryUserStorage.deleteUser(1L);
    }

    @Test
    void whenDerivedExceptionThrown_thenAssertion_AddUser() {
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        User user = User.builder()
                .name("Иван")
                .id(1L)
                .birthday(LocalDate.of(1995, 11, 10))
                .email("")
                .login("Ivan")
                .build();
        User user2 = User.builder()
                .name("Сергей")
                .id(1L)
                .birthday(LocalDate.of(2015, 11, 10))
                .email("kbk.ru")
                .login("SGey")
                .build();
        User user3 = User.builder()
                .name("Иван")
                .id(1L)
                .birthday(LocalDate.of(1995, 11, 10))
                .email("")
                .login("Ivan D")
                .build();
        User user4 = User.builder()
                .name("Сергей")
                .id(1L)
                .birthday(LocalDate.of(2030, 11, 10))
                .email("g@bk.ru")
                .login("SGey")
                .build();
        User user5 = User.builder()
                .name("Сергей")
                .id(1L)
                .birthday(LocalDate.of(2015, 11, 10))
                .email("k@bk.ru")
                .login("SGey")
                .build();
        User user6 = User.builder()
                .name("Сергей")
                .id(2L)
                .birthday(LocalDate.of(2015, 11, 10))
                .email("k@bk.ru")
                .login("SGey")
                .build();

        Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryUserStorage.addUser(user);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryUserStorage.addUser(user2);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryUserStorage.addUser(user3);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryUserStorage.addUser(user4);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryUserStorage.addUser(user5);
            inMemoryUserStorage.addUser(user6);
        });
        inMemoryUserStorage.deleteUser(1L);
    }

    @Test
    void whenDerivedExceptionThrown_thenAssertion_updateUser() {
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        User user = User.builder()
                .name("Иван")
                .id(1L)
                .birthday(LocalDate.of(1995, 11, 10))
                .email("")
                .login("Ivan")
                .build();
        User user2 = User.builder()
                .name("Сергей")
                .id(1L)
                .birthday(LocalDate.of(2015, 11, 10))
                .email("kbk.ru")
                .login("SGey")
                .build();
        User user3 = User.builder()
                .name("Иван")
                .id(1L)
                .birthday(LocalDate.of(1995, 11, 10))
                .email("")
                .login("Ivan D")
                .build();
        User user4 = User.builder()
                .name("Сергей")
                .id(1L)
                .birthday(LocalDate.of(2030, 11, 10))
                .email("g@bk.ru")
                .login("SGey")
                .build();

        Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryUserStorage.updateUser(user);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryUserStorage.updateUser(user2);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryUserStorage.updateUser(user3);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryUserStorage.updateUser(user4);
        });
    }

    @Test
    void shouldFindAllUsers() {
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        User user = User.builder()
                .name("Иван")
                .id(1L)
                .birthday(LocalDate.of(1995, 11, 10))
                .email("k@bk.ru")
                .login("Ivan")
                .build();
        User user2 = User.builder()
                .name("Сергей")
                .id(2L)
                .birthday(LocalDate.of(2015, 11, 10))
                .email("serGey@bk.ru")
                .login("SGey")
                .build();
        inMemoryUserStorage.addUser(user);
        inMemoryUserStorage.addUser(user2);
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        Assertions.assertEquals(users.toString(), inMemoryUserStorage.findAllUsers().toString(),
                "Добавились не все пользователи");
        inMemoryUserStorage.deleteUser(1L);
        inMemoryUserStorage.deleteUser(2L);
    }

    @Test
    void shouldFindAllUsersWithoutUsers() {
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        Assertions.assertTrue(inMemoryUserStorage.findAllUsers().isEmpty(), "Список пользователей не пустой");
    }
}