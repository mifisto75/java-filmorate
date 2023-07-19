package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
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

    private Set<Director> directors = new HashSet<>();

}
