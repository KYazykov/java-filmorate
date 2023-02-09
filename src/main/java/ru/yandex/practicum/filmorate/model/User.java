package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private final Set<Long> friends = new HashSet<>();
    private Long id;

    private final LocalDate birthday;
    private String name;
    @NotBlank
    @NotNull
    private String login;

    public Set<Long> getFriends() {
        return friends;
    }
    @Email
    private String email;
}
