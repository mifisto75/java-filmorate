package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.Exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;


import java.util.List;
import java.util.stream.Collectors;


@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    //PUT /films/{id}/like/{userId} — пользователь ставит лайк фильму.
    public void likeFilm(int id, int userId) {
        if (filmStorage.getFilmId(id) != null) {
            if (userStorage.getUserId(userId) != null) {
                filmStorage.getFilmId(id).getLikes().add(userId);
            } else {
                throw new NotFoundException("такого пользователя нет. не верный id" + id);
            }
        } else {
            throw new NotFoundException("такого фильма нет. не верный id" + userId);
        }
    }
    //DELETE /films/{id}/like/{userId} — пользователь удаляет лайк.

    public void deleteLikeFilm(int id, int userId) {
        Film film = filmStorage.getFilmId(id);
        if (film != null) {
            if (film.getLikes().remove(userId)) {
            } else {
                throw new NotFoundException("такой пользователь не ставил лайк. не верный id " + id);
            }
        } else {
            throw new NotFoundException("такого фильма нет. не верный id " + userId);
        }
    }

    //GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков. Если значение параметра count не задано, верните первые 10.
    public List<Film> popularFilm(Integer count) {
        if (count < 1) {
            throw new ValidationException("слишком малое число. count должен быть хотябы 1 а не " + count);
        }
        return filmStorage.allFilms().stream().sorted((x, y) -> y.getLikes().size() - x.getLikes().size())
                .limit(count).collect(Collectors.toList());

    }

}
