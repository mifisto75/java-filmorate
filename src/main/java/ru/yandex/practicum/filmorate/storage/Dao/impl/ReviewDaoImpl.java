package ru.yandex.practicum.filmorate.storage.Dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.Dao.ReviewDao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@Component
public class ReviewDaoImpl implements ReviewDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    @Override
    public Review addReview(Review review) { //Добавление нового отзыва.
        jdbcTemplate.update("INSERT INTO reviews (content, is_positive, user_id, film_id)" +
                        " VALUES(?, ?, ?, ?)",
                review.getContent(), review.getIsPositive(), review.getUserId(), review.getFilmId());
        return jdbcTemplate.queryForObject(format("SELECT * FROM reviews WHERE content='%s' "
                                + "AND user_id= %d "
                                + "AND film_id=%d",
                        review.getContent(), review.getUserId(), review.getFilmId()),
                new ReviewMapper(jdbcTemplate));
    }

    @Override
    public Review changeReview(Review review) { //Редактирование уже имеющегося отзыва.
        jdbcTemplate.update(""
                        + "UPDATE reviews "
                        + "SET content=?, is_positive=?"
                        + "WHERE review_id=?",
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId());
        return getReview(review.getReviewId());
    }

    @Override
    public void deleteReview(int id) { //Удаление уже имеющегося отзыва.
        jdbcTemplate.update("DELETE FROM review_like_list WHERE review_id=? ", id);
        jdbcTemplate.update("DELETE FROM reviews WHERE review_id=? ", id);
    }

    @Override
    public Review getReview(int id) { //Получение отзыва по идентификатору.
        try {
            return jdbcTemplate.queryForObject(format("SELECT * FROM reviews WHERE review_id=%d", id),
                    new ReviewMapper(jdbcTemplate));
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("не найден отзыв по id " + id);
        }
    }

    @Override
    public List<Review> getTopReviewsFilm(int filmId, int count) { //Получение всех отзывов по идентификатору фильма
        if (filmId != 0) {
            return new ArrayList<Review>(jdbcTemplate.query(format("SELECT * FROM reviews WHERE film_id=%d ",
                    filmId), new ReviewMapper(jdbcTemplate))).stream()
                    .sorted(Comparator.comparing(Review::getUseful).reversed())
                    .limit(count)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<Review>(jdbcTemplate.query("SELECT * FROM reviews ",
                    new ReviewMapper(jdbcTemplate))).stream()
                    .sorted(Comparator.comparing(Review::getUseful).reversed())
                    .limit(count)
                    .collect(Collectors.toList());
        }
    }


    @Override
    public void addReviewLike(int id, int userId) { //пользователь ставит лайк отзыву.
        jdbcTemplate.update("INSERT INTO review_like_list (review_id, user_id , is_positive)VALUES (?, ? ,TRUE)", id, userId);
    }

    @Override
    public void addReviewDislike(int id, int userId) { //пользователь ставит дизлайк отзыву.
        jdbcTemplate.update("INSERT INTO review_like_list (review_id, user_id , is_positive)VALUES (?, ? , FALSE)", id, userId);
    }

    @Override
    public void deleteReviewLike(int id, int userId) { //пользователь удаляет лайк отзыву.
        jdbcTemplate.update("DELETE FROM review_like_list WHERE review_id=? AND user_id=?", id, userId);
    }

    @Override
    public void deleteReviewDislike(int id, int userId) { //пользователь удаляет дизлайк отзыву.
        jdbcTemplate.update("DELETE FROM review_like_list WHERE review_id=? AND user_id=?", id, userId);

    }

    private static class ReviewMapper implements RowMapper<Review> {
        private final JdbcTemplate jdbcTemplate;

        private ReviewMapper(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }

        @Override
        public Review mapRow(ResultSet rs, int rowNum) throws SQLException {


            Review review = new Review();
            review.setReviewId(rs.getInt("review_id"));
            review.setUserId(rs.getInt("user_id"));
            review.setFilmId(rs.getInt("film_id"));
            review.setContent(rs.getString("content"));
            review.setIsPositive(rs.getBoolean("is_positive"));
            try {
                Integer getUseful = jdbcTemplate.queryForObject(format(
                        "SELECT SUM(CASE WHEN is_positive = true THEN 1 ELSE -1 END) AS count_value " +
                                " FROM review_like_list WHERE review_id= %d " +
                                "GROUP BY review_id", review.getReviewId()), Integer.class);
                review.setUseful(getUseful);//есле скрипт не найдёт лакйов то вылетит исключение где итогом будет 0
            } catch (EmptyResultDataAccessException e) {
                review.setUseful(0);
            }
            return review;
        }
    }
}

