package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class Mpa {
    private int id;
    private String name;


    public Mpa(int id) {
        this.id = id;
    }
}
