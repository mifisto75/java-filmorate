package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Dao.GenreDao;
import ru.yandex.practicum.filmorate.storage.Dao.LikeDao;
import ru.yandex.practicum.filmorate.storage.Dao.MpaDao;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class FilmService {
    public FilmStorage filmStorage;
    private UserStorage userStorage;
    private GenreDao genreDao;
    private LikeDao likeDao;
    private MpaDao mpaDao;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage, GenreDao genreDao, LikeDao likeDao, MpaDao mpaDao) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreDao = genreDao;
        this.likeDao = likeDao;
        this.mpaDao = mpaDao;

    }

    public List<Film> allFilms() { // все фильмы с заполнеными полями жанр и рейтинг
        List<Film> list = filmStorage.allFilms();
        list.stream().forEach(film -> {
            film.setGenres(filmStorage.getFilmGenres(film.getId()));
            film.setMpa(mpaDao.getMpaId(film.getMpa().getId()));
        });
        return list;
    }

    public Film addFilm(Film film) { //добавить фильм
        Film fil = filmStorage.addFilm(film);
        filmStorage.addFilmGenres(fil.getId(), film.getGenres());
        fil.setGenres(filmStorage.getFilmGenres(fil.getId()));
        return fil;
    }


    public Film changeFilm(Film film) { // обновление фильмов
        Film fil = filmStorage.changeFilm(film);
        filmStorage.updateFilmGenres(fil.getId(), film.getGenres());
        fil.setGenres(filmStorage.getFilmGenres(fil.getId()));
        fil.setMpa(mpaDao.getMpaId(fil.getMpa().getId()));
        return fil;
    }


    public Film getFilmId(int id) { //фильм по ID
        Film film = filmStorage.getFilmId(id);
        film.setGenres(filmStorage.getFilmGenres(id));
        film.setMpa(mpaDao.getMpaId(film.getMpa().getId()));
        return film;
    }


    //PUT /films/{id}/like/{userId} — пользователь ставит лайк фильму.
    public void likeFilm(int id, int userId) {
        likeDao.addLike(id, userId);
    }

    //DELETE /films/{id}/like/{userId} — пользователь удаляет лайк.
    public void deleteLikeFilm(int id, int userId) {
        likeDao.deleteLike(id, userId);
    }

    //GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков. Если значение параметра count не задано, верните первые 10.
    public List<Film> popularFilm(Integer count) {
        List<Integer> filmsId = likeDao.sizeLikeFilmList(count);
        List<Film> popularFilmList = new ArrayList<>();
        if (filmsId.size() == 0) {

            return allFilms().stream().sorted((x, y) -> y.getLikes().size() - x.getLikes().size())
                    .limit(count).collect(Collectors.toList());
        } else {
            for (int filmId : filmsId) {
                popularFilmList.add(filmStorage.getFilmId(filmId));
            }
        }
        return popularFilmList;
    }

    //GET/films/common?userId={userId}&friendId={friendId} - возвращает список общих с другом фильмов с сортировкой по их популярности.
    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        return filmStorage.getCommonFilms(userId, friendId);
    }

}
