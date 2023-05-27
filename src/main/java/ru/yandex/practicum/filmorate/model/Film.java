package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;


@Data
public class Film {
    private int id;

    private HashSet<Integer> likes = new HashSet<>();
    @NotBlank(message = "название не может быть пустым")
    private String name;
    @Size(max = 200, message = "превышен лимит в 200 символов")
    private String description;

    private LocalDate releaseDate;
    @Positive(message = "продолжительность не может быть отрицательной")
    private int duration;

}
