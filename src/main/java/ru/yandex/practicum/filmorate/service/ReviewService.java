package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.Dao.ReviewDao;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;


@Service
public class ReviewService {

    public ReviewDao reviewDao;
    private UserStorage userStorage;
    private FilmStorage filmStorage;

    public ReviewService(ReviewDao reviewDao, UserStorage userStorage, FilmStorage filmStorage) {
        this.reviewDao = reviewDao;
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;

    }


    public Review addReview(Review review) { //Добавление нового отзыва.
        filmStorage.getFilmId(review.getFilmId());//проверка на наличие фильма
        userStorage.getUserId(review.getUserId());//проверка на наличие юзера
        return reviewDao.addReview(review);
    }


    public Review changeReview(Review review) { //Редактирование уже имеющегося отзыва.
        reviewDao.getReview(review.getReviewId()); //есле не будет отзыва то метод выбрасит исключение
        return reviewDao.changeReview(review);
    }


    public void deleteReview(int id) { //Удаление уже имеющегося отзыва.
        reviewDao.getReview(id); //есле не будет отзыва то метод выбрасит исключение
        reviewDao.deleteReview(id);
    }


    public Review getReview(int id) { //Получение отзыва по идентификатору.
        return reviewDao.getReview(id);
    }


    public List<Review> getTopReviewsFilm(int filmId, int count) { //Получение всех отзывов по идентификатору фильма
        return reviewDao.getTopReviewsFilm(filmId, count);
    }


    public void addReviewLike(int id, int userId) { //пользователь ставит лайк отзыву.
        reviewDao.getReview(id);
        userStorage.userExistenceCheck(userId);
        reviewDao.addReviewLike(id, userId);
    }


    public void addReviewDislike(int id, int userId) { //пользователь ставит дизлайк отзыву.
        reviewDao.getReview(id);
        userStorage.userExistenceCheck(userId);
        reviewDao.addReviewDislike(id, userId);
    }


    public void deleteReviewLike(int id, int userId) { //пользователь удаляет лайк отзыву.
        reviewDao.getReview(id);
        userStorage.userExistenceCheck(userId);
        reviewDao.deleteReviewLike(id, userId);
    }


    public void deleteReviewDislike(int id, int userId) { //пользователь удаляет дизлайк отзыву.
        reviewDao.getReview(id);
        userStorage.userExistenceCheck(userId);
        reviewDao.deleteReviewDislike(id, userId);
    }

}
