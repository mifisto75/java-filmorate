package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exeptions.ValidationException;
import ru.yandex.practicum.filmorate.Exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Component
public class InMemoryFilmStorage implements FilmStorage {

    private HashMap<Integer, Film> films = new HashMap<>();
    private int nextFilmId = 1;

    @Override
    public List<Film> allFilms() { // получение списка всех фильмов
        return new ArrayList(films.values());
    }

    @Override
    public Film addFilm(Film film) throws ValidationException { // создание фильма.
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("вы указали неверную дату. фильм не может быть старше 1895.12.28");
        } else {
            film.setId(nextFilmId++);
            films.put(film.getId(), film);
        }
        return film;
    }

    @Override
    public Film changeFilm(Film film) throws NotFoundException, ValidationException { // обновление фильма.
        if (films.containsKey(film.getId())) {
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                throw new ValidationException("вы указали неверную дату. фильм не может быть старше 1895.12.28");
            } else {
                films.put(film.getId(), film);
            }
        } else {
            throw new NotFoundException("по вашему id не был найден фильм");
        }
        return film;
    }

    @Override
    public Film getFilmId(int id) { //вернуть фильм по id
        if (!films.containsKey(id)) {
            throw new NotFoundException("не найден фильм по id" + id);
        }
        return films.get(id);
    }
}
