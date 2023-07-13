package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.OperationType;
import ru.yandex.practicum.filmorate.storage.Dao.*;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class FilmService {

    private FilmStorage filmStorage;
    private GenreDao genreDao;
    private LikeDao likeDao;
    private MpaDao mpaDao;
    private DirectorDao directorDao;
    private EventDao eventDao;
    private UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, GenreDao genreDao, LikeDao likeDao,
                       MpaDao mpaDao, DirectorDao directorDao, EventDao eventDao, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.genreDao = genreDao;
        this.likeDao = likeDao;
        this.mpaDao = mpaDao;
        this.directorDao = directorDao;
        this.eventDao = eventDao;
        this.userStorage = userStorage;
    }

    public List<Film> getAllFilms() { // все фильмы с заполнеными полями жанр и рейтинг
        List<Film> list = filmStorage.allFilms();
        list.forEach(film -> {
            film.setGenres(filmStorage.getFilmGenres(film.getId()));
            film.setMpa(mpaDao.getMpaById(film.getMpa().getId()));
            film.setDirectors(directorDao.getFilmDirectors(film.getId()));
        });
        return list;
    }

    public Film addFilm(Film film) { //добавить фильм
        Film fil = filmStorage.addFilm(film);
        filmStorage.addFilmGenres(fil.getId(), film.getGenres());
        fil.setGenres(filmStorage.getFilmGenres(fil.getId()));
        directorDao.addFilmDirectors(fil.getId(), film.getDirectors());
        fil.setDirectors(directorDao.getFilmDirectors(fil.getId()));
        return fil;
    }


    public Film changeFilm(Film film) { // обновление фильмов
        Film fil = filmStorage.changeFilm(film);
        filmStorage.updateFilmGenres(fil.getId(), film.getGenres());
        fil.setGenres(filmStorage.getFilmGenres(fil.getId()));
        fil.setMpa(mpaDao.getMpaById(fil.getMpa().getId()));
        directorDao.updateDirectors(fil.getId(), film.getDirectors());
        fil.setDirectors(directorDao.getFilmDirectors(fil.getId()));
        return fil;
    }


    public Film getFilmById(int id) { //фильм по ID
        Film film = filmStorage.getFilmId(id);
        film.setGenres(filmStorage.getFilmGenres(id));
        film.setMpa(mpaDao.getMpaById(film.getMpa().getId()));
        film.setDirectors(directorDao.getFilmDirectors(id));
        return film;
    }


    //PUT /films/{id}/like/{userId} — пользователь ставит лайк фильму.
    public void addFilmLike(int id, int userId) {
        eventDao.addEvent(new Event(Instant.now().toEpochMilli(), userId, EventType.LIKE,
                OperationType.ADD, id));
        likeDao.addLike(id, userId);
    }

    //DELETE /films/{id}/like/{userId} — пользователь удаляет лайк.
    public void deleteLikeFilm(int id, int userId) {
        eventDao.addEvent(new Event(Instant.now().toEpochMilli(), userId, EventType.LIKE,
                OperationType.REMOVE, id));
        likeDao.deleteLike(id, userId);
    }

    //GET /films/popular?count={limit}&genreId={genreId}&year={year}
    // возвращает список из первых count фильмов по количеству лайков
    // если значение параметра count не задано, верните первые 10.
    public List<Film> getPopularFilms(Integer count, Integer genreId, Integer year) {
        List<Film> popularFilmList = new ArrayList<>();
        List<Film> filteredFilmList = getAllFilms();
        List<Integer> filmsId = likeDao.sizeLikeFilmList();
        if (filmsId.size() != 0) {
            for (int filmId : filmsId) {
                popularFilmList.add(filmStorage.getFilmId(filmId));
            }
        } else {
            popularFilmList = getAllFilms().stream()
                    .sorted((x, y) -> y.getId() - x.getId()).collect(Collectors.toList());
        }
        if (year != null) {
            filteredFilmList = filteredFilmList.stream()
                    .filter(film -> film.getReleaseDate().getYear() == year).collect(Collectors.toList());
        }
        if (genreId != null) {
            filteredFilmList = filteredFilmList.stream()
                    .filter(film -> film.getGenres().contains(genreDao.getGenreId(genreId))).collect(Collectors.toList());
        }
        if (year != null || genreId != null) {
            popularFilmList.retainAll(filteredFilmList);
        }
        return popularFilmList.stream()
                .limit(count).collect(Collectors.toList());
    }

    //GET/films/common?userId={userId}&friendId={friendId} - возвращает список общих с другом фильмов с сортировкой по их популярности.
    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        userStorage.userExistenceCheck(userId);
        userStorage.userExistenceCheck(friendId);
        return filmStorage.getCommonFilms(userId, friendId);
    }

    public List<Film> getDirectorFilmsSort(int dirId, String sort) {
        List<Film> films = filmStorage.getDirectorFilmsSort(dirId, sort);
        films.stream().forEach(film -> {
            film.setGenres(filmStorage.getFilmGenres(film.getId()));
            film.setMpa(mpaDao.getMpaById(film.getMpa().getId()));
            film.setDirectors(directorDao.getFilmDirectors(film.getId()));
        });
        return films;
    }

    public List<Film> getFilmsByQuery(String query, String by) {
        if (query.isBlank()) {
            return new ArrayList<>();
        }
        String subString = "%" + query.toLowerCase() + "%";
        return filmStorage.getFilmsByQuery(subString, by).stream()
                .sorted((x, y) -> y.getLikes().size() - x.getLikes().size())
                .collect(Collectors.toList());
    }

    public void deleteFilmById(Integer filmId) {
        filmStorage.deleteFilm(filmId);
    }
}
