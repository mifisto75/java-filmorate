package ru.yandex.practicum.filmorate.storage.Dao;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewDao {
    Review addReview(Review review); //Добавление нового отзыва.

    Review changeReview(Review review); //Редактирование уже имеющегося отзыва.

    void deleteReview(int id); //Удаление уже имеющегося отзыва.

    Review getReview(int id); //Получение отзыва по идентификатору.

    List<Review> getTopReviewsFilm(int filmId, int count); //Получение всех отзывов по идентификатору фильма,
    // если фильм не указан то все. Если кол-во не указано то 10.

    void addReviewLike(int id, int userId); //пользователь ставит лайк отзыву.

    void addReviewDislike(int id, int userId); //пользователь ставит дизлайк отзыву.

    void deleteReviewLike(int id, int userId); //пользователь удаляет лайк отзыву.

    void deleteReviewDislike(int id, int userId); //пользователь удаляет дизлайк отзыву.

}
