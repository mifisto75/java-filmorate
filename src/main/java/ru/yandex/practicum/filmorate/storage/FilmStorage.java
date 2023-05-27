package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

public interface FilmStorage {
    public List<Film> allFilms();//получение всех фильмов.
    public Film addFilm(Film film);// добавление фильма.
    public Film changeFilm(Film film); // обновление фильма.
    public Film getFilmId(int id);//фильм по id


}
