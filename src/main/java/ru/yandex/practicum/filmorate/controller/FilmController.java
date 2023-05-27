package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j

@RestController
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping("/films") //получение всех фильмов.
    public List allFilms() {
        log.info("вызван метод allFilms - запрос на список всех фильмов");
        return filmStorage.allFilms();
    }

    @GetMapping("/films/{id}") //вернуть фильм по id.
    public Film getFilmId(@PathVariable Integer id) {
        log.info("вызван метод getFilmId - запрос на фильм с id " + id);
        return filmStorage.getFilmId(id);
    }

    @PostMapping("/films") // добавление фильма.
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("вызван метод addFilm - запрос на добавление фильма " + film);
        return filmStorage.addFilm(film);
    }

    @PutMapping("/films") // обновление фильма.
    public Film changeFilm(@Valid @RequestBody Film film) {
        log.info("вызван метод changeFilm - запрос на обновление фильма " + film);
        return filmStorage.changeFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}") //лайк для фильма
    public void likeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("вызван метод likeFilm - запрос на добовление лайк для фильма с id " + id + " от пользывателем c id " + userId);
        filmService.likeFilm(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}") //убрать лайк для фильма
    public void deletLikeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("вызван метод deletLikeFilm - запрос на удаление лайк для фильма с id " + id + " от пользывателем c id " + userId);
        filmService.deletLikeFilm(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> popularFilm(@RequestParam(name = "count", defaultValue = "10") Integer count) { // фильмы по популярности
        log.info("вызван метод popularFilm - запрос на писок фильмов по популярности");
        return filmService.popularFilm(count);
    }
}
