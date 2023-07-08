package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Dao.DirectorDao;
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
    private RecommendationService recommendationService;
    private DirectorDao directorDao;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage, GenreDao genreDao, LikeDao likeDao,
                       MpaDao mpaDao, DirectorDao directorDao, RecommendationService recommendationService) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreDao = genreDao;
        this.likeDao = likeDao;
        this.mpaDao = mpaDao;
        this.directorDao = directorDao;
        this.recommendationService = recommendationService;
    }

    public List<Film> allFilms() { // все фильмы с заполнеными полями жанр и рейтинг
        List<Film> list = filmStorage.allFilms();
        list.stream().forEach(film -> {
            film.setGenres(filmStorage.getFilmGenres(film.getId()));
            film.setMpa(mpaDao.getMpaId(film.getMpa().getId()));
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
        fil.setMpa(mpaDao.getMpaId(fil.getMpa().getId()));
        directorDao.updateDirectors(fil.getId(), film.getDirectors());
        fil.setDirectors(directorDao.getFilmDirectors(fil.getId()));
        return fil;
    }


    public Film getFilmId(int id) { //фильм по ID
        Film film = filmStorage.getFilmId(id);
        film.setGenres(filmStorage.getFilmGenres(id));
        film.setMpa(mpaDao.getMpaId(film.getMpa().getId()));
        film.setDirectors(directorDao.getFilmDirectors(id));
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

    //GET /films/popular?count={limit}&genreId={genreId}&year={year}
    // возвращает список из первых count фильмов по количеству лайков
    // если значение параметра count не задано, верните первые 10.
    public List<Film> popularFilm(Integer count, Integer genreId, Integer year) {
        if (count < 1) {
            throw new ValidationException("слишком малое число. count должен быть хотя бы 1 а не " + count);
        }
        List<Film> popularFilmList = new ArrayList<>();
        List<Film> filteredFilmList = allFilms();
        List<Integer> filmsId = likeDao.sizeLikeFilmList();
        if (filmsId.size() != 0) {
            for (int filmId : filmsId) {
                popularFilmList.add(filmStorage.getFilmId(filmId));
            }
        } else {
            popularFilmList = allFilms().stream()
                    .sorted((x, y) -> y.getId() - x.getId()).collect(Collectors.toList());
            ;
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

    public List<Film> getFilmRecommendations(int userId) {
        return recommendationService.getRecommendedFilms(userId);
    }

    //GET/films/common?userId={userId}&friendId={friendId} - возвращает список общих с другом фильмов с сортировкой по их популярности.
    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        return filmStorage.getCommonFilms(userId, friendId);
    }

    public List<Film> getDirectorFilmsSort(int dirId, String sort) {
        List<Film> films = filmStorage.getDirectorFilmsSort(dirId, sort);
        films.stream().forEach(film -> {
            film.setGenres(filmStorage.getFilmGenres(film.getId()));
            film.setMpa(mpaDao.getMpaId(film.getMpa().getId()));
            film.setDirectors(directorDao.getFilmDirectors(film.getId()));
        });
        return films;
    }
}
