package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.Dao.EventDao;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.Instant;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
public class FilmController {

    private final FilmService filmService;
    private final EventDao eventDao;

    @Autowired
    public FilmController(FilmService filmService, EventDao eventDao) {
        this.filmService = filmService;
        this.eventDao = eventDao;
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
        eventDao.addEvent(new Event(Instant.now().toEpochMilli(), userId, "LIKE",
                "ADD", id));
    }

    @DeleteMapping("/films/{id}/like/{userId}") //убрать лайк для фильма
    public void deletLikeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("вызван метод deletLikeFilm - запрос на удаление лайк для фильма с id " + id + " от пользывателем c id " + userId);
        filmService.deleteLikeFilm(id, userId);
        eventDao.addEvent(new Event(Instant.now().toEpochMilli(), userId, "LIKE",
                "REMOVE", id));
    }

    @GetMapping("/films/popular") // фильмы по популярности
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") @Positive Integer count,
                                      @RequestParam(required = false) Integer genreId,
                                      @RequestParam(required = false) Integer year) {
        log.info("вызван метод getPopularFilms - запрос на список фильмов по популярности " +
                "с count " + count + ", genreId " + genreId + ", year " + year);
        return filmService.getPopularFilms(count, genreId, year);
    }

    @GetMapping("/films/common")
    public List<Film> getCommonFilms(@RequestParam Integer userId, @RequestParam Integer friendId) { // общие фильмы по популярности
        log.info("вызван метод getCommonFilms - - запрос на список общих друзей пользователем c id "
                + userId + " пользователя с id " + friendId);
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/films/director/{directorId}")
    public List<Film> getDirectorFilmsSort(@PathVariable("directorId") @Min(1) int dirId,
                                           @RequestParam(value = "sortBy", defaultValue = "likes") String sortBy) {
        return filmService.getDirectorFilmsSort(dirId, sortBy);
    }

    @GetMapping("/films/search") //фильмы по популярности
    public List<Film> getFilmsByQuery(@RequestParam String query,
                                      @RequestParam String by) {
        log.info("вызван метод getFilmsByQuery - поиск фильмов по названию и/или режиссеру" +
                " с query " + query + " с by " + by);
        return filmService.getFilmsByQuery(query, by);
    }
}
