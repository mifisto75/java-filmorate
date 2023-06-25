package ru.yandex.practicum.filmorate.model;


import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
public class User {
    private int id;
    private Set<Integer> friends = new HashSet<>(); //

    @NotBlank(message = "почта не может  быть пустой")
    @Email(message = "не корректно указанная почта")
    private String email;
    @NotBlank(message = "логин не может быть пустым")
    @Pattern(regexp = "^[a-zA-Z0-9а-яА-Я._-]{1,}", message = "логин не может содержать пробыл")
    private String login;
    private String name;

    @NotNull
    @Past(message = "ты что из будущего ?")
    private LocalDate birthday;
}
