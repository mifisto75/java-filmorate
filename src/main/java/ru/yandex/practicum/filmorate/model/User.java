package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private int id;
    @NotBlank(message = "почта не может  быть пустой")
    @Email(message = "не коректо указанная почта")
    private String email;
    @NotBlank(message = "логин не может быть пустым")
    @Pattern(regexp = "^[a-zA-Z0-9а-яА-Я._-]{1,50}", message = "логин не может содержать пробыл и быть длиней чем 50 символов")
    private String login; // логин пользователя
    private String name; // имя для отображения

    @Past(message = "ты что из будущего ?")
    private LocalDate birthday; // дата рождение
}
