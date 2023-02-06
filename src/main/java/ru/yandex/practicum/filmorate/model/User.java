package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private final LocalDate birthday;
    private String name;
    @NotBlank
    @NotNull
    private String login;
    private int id;
    @Email
    private String email;
}
