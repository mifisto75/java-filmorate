package ru.yandex.practicum.filmorate.storage.Dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.storage.Dao.LikeDao;


import javax.validation.ValidationException;
import java.util.List;
import static java.lang.String.format;

@Service
@Component
public class LikeDaoImpl implements LikeDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(int filmId, int userId) {
        try {
            jdbcTemplate.update("INSERT INTO film_like_list (film_id, user_id)VALUES (?, ?)", filmId, userId);
        } catch (
                EmptyResultDataAccessException e) {
            throw new NotFoundException("не верный  id пользывателя или фильма");
        }
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        try {
            int i = jdbcTemplate.queryForObject(format(
                    "SELECT film_id + user_id FROM film_like_list WHERE film_id=%d AND user_id=%d", filmId, userId), Integer.class);
            //клас костыль который выбрасывает исключение NotFoundException если в базе пусто
        } catch (
                EmptyResultDataAccessException e) {
            throw new NotFoundException("нечего удалять");
        }
        jdbcTemplate.update("DELETE FROM film_like_list WHERE film_id=? AND user_id=?", filmId, userId);
    }

    @Override
    public List<Integer> sizeLikeFilmList() {
        return jdbcTemplate.queryForList("SELECT film_id FROM (SELECT film_id, COUNT(user_id) as count_users " +
                "FROM film_like_list GROUP BY film_id) as subquery ORDER BY count_users DESC", Integer.class);
    }
}
