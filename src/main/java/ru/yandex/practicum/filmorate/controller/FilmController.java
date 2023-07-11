package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.Dao.EventDao;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService, EventDao eventDao) {
        this.filmService = filmService;
    }

    @GetMapping("/films") //получение всех фильмов.
    public List<Film> getAllFilms() {
        log.info("вызван метод getAllFilms");
        return filmService.getAllFilms();
    }

    @GetMapping("/films/{id}") //вернуть фильм по id.
    public Film getFilmById(@PathVariable Integer id) {
        log.info("вызван метод getFilmById, id: " + id);
        return filmService.getFilmById(id);
    }

    @PostMapping("/films") // добавление фильма.
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("вызван метод addFilm, film: " + film);
        return filmService.addFilm(film);
    }

    @PutMapping("/films") // обновление фильма.
    public Film changeFilm(@Valid @RequestBody Film film) {
        log.info("вызван метод changeFilm, film: " + film);
        return filmService.changeFilm(film);
    }

    @DeleteMapping("/films/{filmId}") // удаление фильма
    public void deleteFilmById(@PathVariable Integer filmId) {
        log.info("вызван метод deleteFilmById, id: " + filmId);
        filmService.deleteFilmById(filmId);
    }

    @PutMapping("/films/{id}/like/{userId}") //лайк для фильма
    public void addFilmLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("вызван метод addLikeFilm, filmId: " + id + ", userId: " + userId);
        filmService.addFilmLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}") //убрать лайк для фильма
    public void deleteLikeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("вызван метод deleteLikeFilm, filmId: " + id + ", userId: " + userId);
        filmService.deleteLikeFilm(id, userId);
    }

    @GetMapping("/films/popular") // фильмы по популярности
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") @Positive Integer count,
                                      @RequestParam(required = false) Integer genreId,
                                      @RequestParam(required = false) Integer year) {
        log.info("вызван метод getPopularFilms, count: " + count + ", genreId: " + genreId + ", year: " + year);
        return filmService.getPopularFilms(count, genreId, year);
    }

    @GetMapping("/films/common")
    // общие фильмы по популярности
    public List<Film> getCommonFilms(@RequestParam Integer userId, @RequestParam Integer friendId) {
        log.info("вызван метод getCommonFilms - запрос на список общих друзей пользователем c id "
                + userId + " пользователя с id " + friendId);
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/films/director/{directorId}")
    public List<Film> getDirectorFilmsSort(@PathVariable("directorId") @Min(1) int dirId,
                                           @RequestParam(value = "sortBy", defaultValue = "likes") String sortBy) {
        log.info("вызван метод getDirectorFilmsSort, directorId: {}, sortBy: {}", dirId, sortBy);
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
