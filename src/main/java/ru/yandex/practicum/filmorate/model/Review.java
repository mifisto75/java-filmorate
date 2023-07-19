package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {


    private Integer reviewId;
    @NotNull
    private Integer userId;
    @NotNull
    private Integer filmId;
    @NotBlank
    private String content;
    private int useful;
    @NotNull
    private Boolean isPositive;


}
