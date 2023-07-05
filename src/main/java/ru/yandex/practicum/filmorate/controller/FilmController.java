package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films") //получение всех фильмов.
    public List allFilms() {
        log.info("вызван метод allFilms - запрос на список всех фильмов");
        return filmService.allFilms();
    }

    @GetMapping("/films/{id}") //вернуть фильм по id.
    public Film getFilmId(@PathVariable Integer id) {
        log.info("вызван метод getFilmId - запрос на фильм с id " + id);
        return filmService.getFilmId(id);
    }

    @PostMapping("/films") // добавление фильма.
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("вызван метод addFilm - запрос на добавление фильма " + film);
        return filmService.addFilm(film);
    }

    @PutMapping("/films") // обновление фильма.
    public Film changeFilm(@Valid @RequestBody Film film) {
        log.info("вызван метод changeFilm - запрос на обновление фильма " + film);
        return filmService.changeFilm(film);
    }

    @DeleteMapping("/films/{filmId}") // удаление фильма
    public void deleteFilm(@PathVariable Integer filmId) {
        log.info("вызван метод deleteFilm - запрос на удаление фильма с id " + filmId);
        filmService.filmStorage.deleteFilm(filmId);
    }

    @PutMapping("/films/{id}/like/{userId}") //лайк для фильма
    public void likeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("вызван метод likeFilm - запрос на добовление лайк для фильма с id " + id + " от пользывателем c id " + userId);
        filmService.likeFilm(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}") //убрать лайк для фильма
    public void deletLikeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("вызван метод deletLikeFilm - запрос на удаление лайк для фильма с id " + id + " от пользывателем c id " + userId);
        filmService.deleteLikeFilm(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> popularFilm(@RequestParam(name = "count", defaultValue = "10") Integer count) { // фильмы по популярности
        log.info("вызван метод popularFilm - запрос на писок фильмов по популярности с count " + count);
        return filmService.popularFilm(count);
    }
}
