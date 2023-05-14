package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exeptions.NotFound;
import ru.yandex.practicum.filmorate.Exeptions.BadRequest;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j

@RestController
public class FilmController {
    private HashMap<Integer, Film> films = new HashMap<>();
    private int nextFilmId = 1;

    @GetMapping("/films") //получение всех фильмов.
    public ArrayList allFilms() {
        log.info("количество фильмов{}" + films.size());
        return new ArrayList(films.values());
    }

    @PostMapping("/films") // добавление фильма.
    public Film addFilm(@Valid @RequestBody Film film) throws BadRequest {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("ошибка при добавлении фильма " + film + " старше «L'Arrivée d'un train en gare de la Ciotat» 1895 , нечего нет ");
            throw new BadRequest();
        } else {
            film.setId(nextFilmId++);
            films.put(film.getId(), film);
            log.info("добавлен новый фильм " + film);
        }
        return film;
    }

    @PutMapping("/films") // обновление фильма.
    public Film changeFilm(@Valid @RequestBody Film film) throws NotFound, BadRequest {
        if (films.containsKey(film.getId())) {
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                log.warn("ошибка при добавлении фильма " + film + " старше «L'Arrivée d'un train en gare de la Ciotat» 1895 , нечего нет ");
                throw new BadRequest();
            } else {
                films.put(film.getId(), film);
                log.info("фильм обновлён " + film);
            }
        } else {
            log.warn("ошибка при обновлении фильма " + film + " не возможно изменить того чего нет");
            throw new NotFound();
        }
        return film;
    }


}
