package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Film {
    @NotBlank
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int id;
    private int duration;

}
