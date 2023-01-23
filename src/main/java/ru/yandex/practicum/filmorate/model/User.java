package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {
    private final LocalDate birthday;
    private String name;
    private String login;
    private int id;
    private String email;

}
