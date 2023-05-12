package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.Exeptions.NotIdExeption;
import ru.yandex.practicum.filmorate.controller.Exeptions.WeriOldFilmException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
public class FilmController {
    private HashMap<Integer,Film> Films = new HashMap<>();
    private int nextFilmId = 1 ;

    @GetMapping("/films") //получение всех фильмов.
    public Collection<Film> allFilms() {
        log.info("оличество фильмов{}"+ Films.size());
        return Films.values();
    }
    @PostMapping("/films") // добавление фильма.
    public Film addFilm(@Valid @RequestBody Film film) throws WeriOldFilmException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))){
            log.warn("ошибка при добовлении фильма "+film + " старше «L'Arrivée d'un train en gare de la Ciotat» 1895 , нечего нет ");
            throw new WeriOldFilmException("YYYY-MM-dd < 1895-12-28");
        }else {
            film.setId(nextFilmId++);
            Films.put(film.getId(), film);
            log.info("добавлен новый фильм "+film);
        }
        return film;
    }

      @PutMapping("/films") // обновление фильма.
    public Film changeFilm(@Valid @RequestBody Film film) throws NotIdExeption, WeriOldFilmException {
        if (Films.containsKey(film.getId())){
            if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
                log.warn("ошибка при добовлении фильма "+film + " старше «L'Arrivée d'un train en gare de la Ciotat» 1895 , нечего нет ");
                throw new WeriOldFilmException("YYYY-MM-dd < 1895-12-28");
            }else {
                Films.put(film.getId(),film);
                log.info("фильм обновлён "+film);
            }
        }else {
            log.warn("ошибка при обновлении фильма "+film + " не возможно изменить того чего нет");
                throw new NotIdExeption("not found");
            }
        return film;
          }


}
