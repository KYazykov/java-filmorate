package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@NotNull
public class User {
    private final LocalDate birthday;
    private String name;
    private String login;
    private int id;
    @Email
    @NotBlank
    private String email;

}
