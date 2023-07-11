package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.Dao.MpaDao;
import ru.yandex.practicum.filmorate.storage.Dao.impl.GenreDaoImpl;

import javax.validation.ValidationException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;

@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    public static MpaDao mpaDao;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaDao mpaDao) {
        this.jdbcTemplate = jdbcTemplate;
        FilmDbStorage.mpaDao = mpaDao;
    }

    public List<Film> allFilms() {   //получение всех фильмов.
        return new ArrayList<>(jdbcTemplate.query("SELECT * FROM films", new FilmMapper()));


    }

    public Film addFilm(Film film) { // добавление фильма.
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("вы указали неверную дату. фильм не может быть старше 1895.12.28");
        }
        jdbcTemplate.update("INSERT INTO films (name, description, release_date, duration, rating_id )" +
                        " VALUES(?, ?, ?, ?, ?)",
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
        return jdbcTemplate.queryForObject(format("SELECT * FROM films WHERE name='%s' "
                                + "AND description='%s' "
                                + "AND release_date='%s' "
                                + "AND duration= %d "
                                + "AND rating_id=%d",
                        film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId()),
                new FilmMapper());
    }

    public Film changeFilm(Film film) { // обновление фильма.
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("вы указали неверную дату. фильм не может быть старше 1895.12.28");
        }
        jdbcTemplate.update(""
                        + "UPDATE films "
                        + "SET name=?, description=?, release_date=?, duration=?, rating_id=?"
                        + "WHERE film_id=?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        return getFilmId(film.getId());
    }

    public Film getFilmId(int id) { //фильм по id
        try {
            return jdbcTemplate.queryForObject(format("SELECT * FROM films WHERE film_id=%d", id), new FilmMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("не найден фильм по id " + id);
        }
    }

    public void addFilmGenres(int filmId, Set<Genre> genres) {
        for (Genre genre : genres) {
            jdbcTemplate.update("INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)", filmId, genre.getId());
        }
    }

    public void updateFilmGenres(int filmId, Set<Genre> genres) {
        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id=?", filmId);
        addFilmGenres(filmId, genres);
    }

    public Set<Genre> getFilmGenres(int filmId) {
        Set<Genre> genres = new HashSet<>(jdbcTemplate.query(format(""
                + "SELECT f.genre_id, g.name "
                + "FROM film_genres AS f "
                + "LEFT OUTER JOIN genres AS g ON f.genre_id = g.genre_id "
                + "WHERE f.film_id=%d "
                + "ORDER BY g.genre_id", filmId), new GenreDaoImpl.GenreMapper()));
        return genres;
    }

    @Override
    public void deleteFilm(Integer filmId) {
        try {
            jdbcTemplate.update("DELETE FROM films WHERE film_id=?", filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("не верный id пользывателя ");
        }
    }

    @Override
    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        String sql =
                "SELECT f.* FROM films AS f " +
                        "LEFT JOIN film_like_list AS fl ON f.film_id = fl.film_id " +
                        "WHERE fl.film_id IN (SELECT film_id FROM film_like_list WHERE user_id = ?) " +
                        "AND fl.film_id IN (SELECT film_id FROM film_like_list WHERE user_id = ?) " +
                        "GROUP BY fl.film_id " +
                        "ORDER BY COUNT(fl.user_id) DESC";
        return new ArrayList<>(jdbcTemplate.query(sql, new FilmMapper(), userId, friendId));
    }

    //GET /films/director/{directorId}?sortBy=[year,likes]
    public List<Film> getDirectorFilmsSort(int dirId, String sort) {
        List<Film> filmSort = new ArrayList<>();
        if (sort.equals("year")) {
            filmSort = jdbcTemplate.query("SELECT * FROM films WHERE film_id IN " +
                            "(SELECT film_id FROM film_directors WHERE director_id = ?) ORDER BY release_date LIMIT 10",
                    new FilmMapper(), dirId);
        } else if (sort.equals("likes")) {
            filmSort = jdbcTemplate.query("" +
                            "SELECT f.* " +
                            "FROM film_directors AS fd " +
                            "LEFT OUTER JOIN films AS f ON fd.film_id = f.film_id " +
                            "LEFT OUTER JOIN film_like_list AS fl ON f.film_id = fl.film_id " +
                            "WHERE fd.director_id = ? " +
                            "GROUP BY fd.film_id " +
                            "ORDER BY COUNT(fl.user_id) LIMIT 10",
                    new FilmMapper(), dirId);
        } else {
            throw new ru.yandex.practicum.filmorate.Exeptions.ValidationException(String.format("Ошибка вывода фильмов режиссера - sort: %s не существует",
                    sort));
        }
        if (filmSort.isEmpty()) {
            throw new NotFoundException("Нет фильмов по данному режиссеру");
        } else {
            return filmSort;
        }
    }

    private class FilmMapper implements RowMapper<Film> {
        @Override
        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
            Mpa mpa = mpaDao.getMpaId(rs.getInt("rating_id"));

            Film film = new Film();
            film.setId(rs.getInt("film_id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(rs.getInt("duration"));
            film.setMpa(mpa);
            film.setGenres(getFilmGenres(rs.getInt("film_id")));
            return film;
        }
    }
}
