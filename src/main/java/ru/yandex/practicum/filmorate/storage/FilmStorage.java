package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;


import java.util.List;

public interface FilmStorage {
    public List<Film> allFilms();//получение всех фильмов.

    public Film addFilm(Film film);// добавление фильма.

    public Film changeFilm(Film film); // обновление фильма.

    public Film getFilmId(int id);//фильм по id


}
