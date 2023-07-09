package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    List<Film> allFilms();//получение всех фильмов.

    Film addFilm(Film film);// добавление фильма.

    Film changeFilm(Film film); // обновление фильма.

    Film getFilmId(int id);//фильм по id

    void addFilmGenres(int filmId, Set<Genre> genres);

    void updateFilmGenres(int filmId, Set<Genre> genres);

    Set<Genre> getFilmGenres(int filmId);

    List<Film> getCommonFilms(Integer userId, Integer friendId);

    List<Film> getDirectorFilmsSort(int dirId, String sort);

    List<Film> getSearchFilm(String query, String by);
}
