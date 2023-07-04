package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
public class Film {
    private int id;

    private Set<Integer> likes = new HashSet<>();
    @NotBlank(message = "название не может быть пустым")
    private String name;
    @Size(max = 200, message = "превышен лимит в 200 символов")
    private String description;

    private LocalDate releaseDate;
    @Positive(message = "продолжительность не может быть отрицательной")
    private int duration;

    @NotNull(message = "рейтинг не может быть пустым")
    private Mpa mpa;

    private Set<Genre> genres = new HashSet<>();

}
