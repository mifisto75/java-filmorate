package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.storage.Dao.EventDao;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.Instant;
import java.util.List;

@Slf4j
@RestController
public class ReviewController {


    private final ReviewService reviewService;
    private final EventDao eventDao;

    @Autowired
    public ReviewController(ReviewService reviewService, EventDao eventDao) {
        this.reviewService = reviewService;
        this.eventDao = eventDao;
    }


    @PostMapping("/reviews") //Добавление нового отзыва.
    public Review addReview(@Valid @RequestBody Review review) {
        log.info("вызван метод addReview " + review);
        Review newReview = reviewService.addReview(review);
        eventDao.addEvent(new Event(Instant.now().toEpochMilli(), newReview.getUserId(),
                "REVIEW", "ADD",
                newReview.getReviewId()));
        return newReview;
    }

    @PutMapping("/reviews") //Редактирование уже имеющегося отзыва.
    public Review changeReview(@Valid @RequestBody Review review) {
        log.info("вызван метод changeReview " + review);
        Review updatedReview = reviewService.changeReview(review);
        eventDao.addEvent(new Event(Instant.now().toEpochMilli(), updatedReview.getUserId(),
                "REVIEW", "UPDATE",
                updatedReview.getReviewId()));
        return updatedReview;
    }

    @DeleteMapping("/reviews/{id}") //Удаление уже имеющегося отзыва.
    public void deleteReview(@PathVariable Integer id) {
        log.info("вызван метод deleteReview " + id);
        Review review = reviewService.getReview(id);
        reviewService.deleteReview(id);
        eventDao.addEvent(new Event(Instant.now().toEpochMilli(), review.getUserId(),
                "REVIEW", "REMOVE",
                review.getReviewId()));
    }

    @GetMapping("/reviews/{id}") //Получение отзыва по идентификатору.
    public Review getReview(@PathVariable Integer id) {
        log.info("вызван метод getReview " + id);
        return reviewService.getReview(id);
    }

    @GetMapping("/reviews")
    //Получение всех отзывов по идентификатору фильма, если фильм не указан то все. Если кол-во не указано то 10.
    public List<Review> getTopReviewsFilm(
            @RequestParam(name = "filmId", defaultValue = "0", required = false) @Positive Integer filmId,
            @RequestParam(name = "count", defaultValue = "10", required = false) @Positive Integer count) {
        log.info("вызван метод getTopReviewsFilm  filmId = " + filmId + " count= " + count);
        return reviewService.getTopReviewsFilm(filmId, count);
    }

    @PutMapping("/reviews/{id}/like/{userId}") //пользователь ставит лайк отзыву.
    public void addReviewLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("вызван метод addReviewLike  id = " + id + " userId= " + userId);
        reviewService.addReviewLike(id, userId);
    }

    @PutMapping("/reviews/{id}/dislike/{userId}") //пользователь ставит дизлайк отзыву.
    public void addReviewDislike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("вызван метод addReviewDislike  id = " + id + " userId= " + userId);
        reviewService.addReviewDislike(id, userId);
    }

    @DeleteMapping("/reviews/{id}/like/{userId}")//пользователь удаляет лайк отзыву.
    public void deleteReviewLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("вызван метод deleteReviewLike  id = " + id + " userId= " + userId);
        reviewService.deleteReviewLike(id, userId);
    }

    @DeleteMapping("/reviews/{id}/dislike/{userId}") //пользователь удаляет дизлайк отзыву.
    public void deleteReviewDislike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("вызван метод deleteReviewDislike  id = " + id + " userId= " + userId);
        reviewService.deleteReviewDislike(id, userId);
    }

}
