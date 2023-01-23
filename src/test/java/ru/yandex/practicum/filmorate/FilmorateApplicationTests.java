package ru.yandex.practicum.filmorate;

import lombok.Builder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Builder
class FilmorateApplicationTests {

    @Test
    void shouldAddFilm() {
        FilmController filmController = new FilmController();
        Film film = Film.builder()
                .name("Назад в будущее")
                .id(1)
                .duration(120)
                .description("Попаданцы")
                .releaseDate(LocalDate.of(1995, 11, 10))
                .build();
        Film film2 = Film.builder()
                .name("Назад в будущее 2")
                .id(2)
                .duration(125)
                .description("Попаданцы 2")
                .releaseDate(LocalDate.of(2005, 1, 11))
                .build();
        filmController.addFilm(film);
        filmController.addFilm(film2);
        List<Film> films = new ArrayList<>();
        films.add(film);
        films.add(film2);
        Assertions.assertEquals(films.toString(), filmController.findAllFilms().toString(), "Добавились не все фильмы");
    }

    @Test
    void shouldUpdateFilm() {
        FilmController filmController = new FilmController();
        Film film = Film.builder()
                .name("Назад в будущее")
                .id(1)
                .duration(120)
                .description("Попаданцы")
                .releaseDate(LocalDate.of(1995, 11, 10))
                .build();
        Film film2 = Film.builder()
                .name("Назад в будущее")
                .id(1)
                .duration(125)
                .description("Попаданцы 2")
                .releaseDate(LocalDate.of(2005, 1, 11))
                .build();
        filmController.addFilm(film);
        filmController.updateFilm(film2);
        List<Film> films = new ArrayList<>();
        films.add(film2);
        Assertions.assertEquals(films.toString(), filmController.findAllFilms().toString(), "Добавились не все фильмы");
    }

    @Test
    void whenDerivedExceptionThrown_thenAssertion_AddFilm() {
        FilmController filmController = new FilmController();
        Film film = Film.builder()
                .name("")
                .id(1)
                .duration(120)
                .description("Попаданцы")
                .releaseDate(LocalDate.of(1995, 11, 10))
                .build();
        Film film2 = Film.builder()
                .name("Назад в будущее 2")
                .id(2)
                .duration(-10)
                .description("Попаданцы 2")
                .releaseDate(LocalDate.of(2005, 1, 11))
                .build();
        Film film3 = Film.builder()
                .name("Назад в будущее")
                .id(3)
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
                .id(4)
                .duration(125)
                .description("Попаданцы 2")
                .releaseDate(LocalDate.of(1005, 1, 11))
                .build();
        Film film5 = Film.builder()
                .name("Назад в будущее")
                .id(4)
                .duration(125)
                .description("Попаданцы 2")
                .releaseDate(LocalDate.of(2005, 1, 11))
                .build();

        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film2);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film3);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film4);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film3);
            filmController.addFilm(film5);
        });
    }

    @Test
    void whenDerivedExceptionThrown_thenAssertion_updateFilm() {
        FilmController filmController = new FilmController();
        Film film = Film.builder()
                .name("")
                .id(1)
                .duration(120)
                .description("Попаданцы")
                .releaseDate(LocalDate.of(1995, 11, 10))
                .build();
        Film film2 = Film.builder()
                .name("Назад в будущее 2")
                .id(2)
                .duration(-10)
                .description("Попаданцы 2")
                .releaseDate(LocalDate.of(2005, 1, 11))
                .build();
        Film film3 = Film.builder()
                .name("Назад в будущее")
                .id(3)
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
                .id(4)
                .duration(125)
                .description("Попаданцы 2")
                .releaseDate(LocalDate.of(1005, 1, 11))
                .build();

        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.updateFilm(film);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.updateFilm(film2);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.updateFilm(film3);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.updateFilm(film4);
        });
    }

    @Test
    void shouldFindAllFilms() {
        FilmController filmController = new FilmController();
        Film film = Film.builder()
                .name("Назад в будущее")
                .id(1)
                .duration(120)
                .description("Попаданцы")
                .releaseDate(LocalDate.of(1995, 11, 10))
                .build();
        Film film2 = Film.builder()
                .name("Назад в будущее 2")
                .id(2)
                .duration(125)
                .description("Попаданцы 2")
                .releaseDate(LocalDate.of(2005, 1, 11))
                .build();

        filmController.addFilm(film);
        filmController.addFilm(film2);
        List<Film> films = new ArrayList<>();
        films.add(film);
        films.add(film2);
        Assertions.assertEquals(films.toString(), filmController.findAllFilms().toString(), "Неправильный показ всех фильмов");
    }

    @Test
    void shouldFindAllFilmsWithoutFilms() {
        FilmController filmController = new FilmController();
        Assertions.assertTrue(filmController.findAllFilms().isEmpty(), "Список фильмов не пустой");
    }

    @Test
    void shouldAddUser() {
        UserController userController = new UserController();
        User user = User.builder()
                .name("Иван")
                .id(1)
                .birthday(LocalDate.of(1995, 11, 10))
                .email("k@bk.ru")
                .login("Ivan")
                .build();
        User user2 = User.builder()
                .name("Сергей")
                .id(2)
                .birthday(LocalDate.of(2015, 11, 10))
                .email("serGey@bk.ru")
                .login("SGey")
                .build();
        userController.addUser(user);
        userController.addUser(user2);
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        Assertions.assertEquals(users.toString(), userController.findAllUsers().toString(),
                "Добавились не все пользователи");
    }

    @Test
    void shouldUpdateUser() {
        UserController userController = new UserController();
        User user = User.builder()
                .name("Иван")
                .id(1)
                .birthday(LocalDate.of(1995, 11, 10))
                .email("k@bk.ru")
                .login("Ivan")
                .build();
        User user2 = User.builder()
                .name("Сергей")
                .id(1)
                .birthday(LocalDate.of(2015, 11, 10))
                .email("k@bk.ru")
                .login("SGey")
                .build();
        userController.addUser(user);
        userController.updateUser(user2);
        List<User> users = new ArrayList<>();
        users.add(user2);
        Assertions.assertEquals(users.toString(), userController.findAllUsers().toString(),
                "Добавились не все пользователи");
    }

    @Test
    void whenDerivedExceptionThrown_thenAssertion_AddUser() {
        UserController userController = new UserController();
        User user = User.builder()
                .name("Иван")
                .id(1)
                .birthday(LocalDate.of(1995, 11, 10))
                .email("")
                .login("Ivan")
                .build();
        User user2 = User.builder()
                .name("Сергей")
                .id(1)
                .birthday(LocalDate.of(2015, 11, 10))
                .email("kbk.ru")
                .login("SGey")
                .build();
        User user3 = User.builder()
                .name("Иван")
                .id(1)
                .birthday(LocalDate.of(1995, 11, 10))
                .email("")
                .login("Ivan D")
                .build();
        User user4 = User.builder()
                .name("Сергей")
                .id(1)
                .birthday(LocalDate.of(2030, 11, 10))
                .email("g@bk.ru")
                .login("SGey")
                .build();
        User user5 = User.builder()
                .name("Сергей")
                .id(1)
                .birthday(LocalDate.of(2015, 11, 10))
                .email("k@bk.ru")
                .login("SGey")
                .build();
        User user6 = User.builder()
                .name("Сергей")
                .id(2)
                .birthday(LocalDate.of(2015, 11, 10))
                .email("k@bk.ru")
                .login("SGey")
                .build();

        Assertions.assertThrows(ValidationException.class, () -> {
            userController.addUser(user);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            userController.addUser(user2);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            userController.addUser(user3);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            userController.addUser(user4);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            userController.addUser(user5);
            userController.addUser(user6);
        });
    }

    @Test
    void whenDerivedExceptionThrown_thenAssertion_updateUser() {
        UserController userController = new UserController();
        User user = User.builder()
                .name("Иван")
                .id(1)
                .birthday(LocalDate.of(1995, 11, 10))
                .email("")
                .login("Ivan")
                .build();
        User user2 = User.builder()
                .name("Сергей")
                .id(1)
                .birthday(LocalDate.of(2015, 11, 10))
                .email("kbk.ru")
                .login("SGey")
                .build();
        User user3 = User.builder()
                .name("Иван")
                .id(1)
                .birthday(LocalDate.of(1995, 11, 10))
                .email("")
                .login("Ivan D")
                .build();
        User user4 = User.builder()
                .name("Сергей")
                .id(1)
                .birthday(LocalDate.of(2030, 11, 10))
                .email("g@bk.ru")
                .login("SGey")
                .build();

        Assertions.assertThrows(ValidationException.class, () -> {
            userController.updateUser(user);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            userController.updateUser(user2);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            userController.updateUser(user3);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            userController.updateUser(user4);
        });
    }

    @Test
    void shouldFindAllUsers() {
        UserController userController = new UserController();
        User user = User.builder()
                .name("Иван")
                .id(1)
                .birthday(LocalDate.of(1995, 11, 10))
                .email("k@bk.ru")
                .login("Ivan")
                .build();
        User user2 = User.builder()
                .name("Сергей")
                .id(2)
                .birthday(LocalDate.of(2015, 11, 10))
                .email("serGey@bk.ru")
                .login("SGey")
                .build();
        userController.addUser(user);
        userController.addUser(user2);
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        Assertions.assertEquals(users.toString(), userController.findAllUsers().toString(),
                "Добавились не все пользователи");
    }

    @Test
    void shouldFindAllUsersWithoutUsers() {
        UserController userController = new UserController();
        Assertions.assertTrue(userController.findAllUsers().isEmpty(), "Список пользователей не пустой");
    }
}
