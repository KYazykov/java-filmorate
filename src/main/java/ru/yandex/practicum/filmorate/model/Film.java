package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Film {

    private final Set<Long> likes = new HashSet<>();
    private Long id;
    @Min(1)
    private int duration;
    @NotBlank
    @NotNull
    private String name;
    @Size(min = 1, max = 200)
    private String description;

    private Mpa Mpa;
    private LocalDate releaseDate;

    private List<Genre> genres = new ArrayList<>();

    @PostConstruct
    public void initGenres() {
        genres = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return Objects.equals(id, film.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
