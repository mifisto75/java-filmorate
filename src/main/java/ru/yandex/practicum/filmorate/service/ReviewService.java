package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.Dao.EventDao;
import ru.yandex.practicum.filmorate.storage.Dao.ReviewDao;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.Instant;
import java.util.List;


@Service
public class ReviewService {

    private ReviewDao reviewDao;
    private UserStorage userStorage;
    private FilmStorage filmStorage;
    private EventDao eventDao;

    public ReviewService(ReviewDao reviewDao, UserStorage userStorage, FilmStorage filmStorage, EventDao eventDao) {
        this.reviewDao = reviewDao;
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.eventDao = eventDao;
    }

    public Review addReview(Review review) { //Добавление нового отзыва.
        filmStorage.getFilmId(review.getFilmId());//проверка на наличие фильма
        userStorage.getUserById(review.getUserId());//проверка на наличие юзера
        Review newReview = reviewDao.addReview(review);
        eventDao.addEvent(new Event(Instant.now().toEpochMilli(), newReview.getUserId(),
                "REVIEW", "ADD",
                newReview.getReviewId()));
        return newReview;
    }

    public Review changeReview(Review review) { //Редактирование уже имеющегося отзыва.
        reviewDao.getReview(review.getReviewId()); //если не будет отзыва, то метод выбросит исключение
        Review updatedReview = reviewDao.changeReview(review);
        eventDao.addEvent(new Event(Instant.now().toEpochMilli(), updatedReview.getUserId(),
                "REVIEW", "UPDATE",
                updatedReview.getReviewId()));
        return updatedReview;
    }

    public void deleteReviewById(int id) { //Удаление уже имеющегося отзыва.
        Review review = reviewDao.getReview(id);
        reviewDao.deleteReview(id);
        eventDao.addEvent(new Event(Instant.now().toEpochMilli(), review.getUserId(),
                "REVIEW", "REMOVE",
                review.getReviewId()));
    }

    public Review getReviewById(int id) { //Получение отзыва по идентификатору.
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
