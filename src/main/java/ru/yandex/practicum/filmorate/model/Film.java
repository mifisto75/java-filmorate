package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;


@Data
public class Film {
    private int id;
    @NotBlank(message = "название не может быть пустым")
    private String name;
    @Size(max = 200, message = "превышен лемит в 200 символов")
    private String description;

    private LocalDate releaseDate;// дата релиза
    @Positive(message = "продолжительнсть не может быть отрицательной")
    private int duration; // продолжительность фильса

}
