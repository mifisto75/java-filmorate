package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
public class ReviewController {


    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @PostMapping("/reviews") //Добавление нового отзыва.
    public Review addReview(@Valid @RequestBody Review review) {
        log.info("вызван метод addReview, review: " + review);
        return reviewService.addReview(review);
    }

    @PutMapping("/reviews") //Редактирование уже имеющегося отзыва.
    public Review changeReview(@Valid @RequestBody Review review) {
        log.info("вызван метод changeReview, review: " + review);
        return reviewService.changeReview(review);
    }

    @DeleteMapping("/reviews/{id}") //Удаление уже имеющегося отзыва.
    public void deleteReviewById(@PathVariable Integer id) {
        log.info("вызван метод deleteReviewById, id: " + id);
        reviewService.deleteReviewById(id);
    }

    @GetMapping("/reviews/{id}") //Получение отзыва по идентификатору.
    public Review getReviewById(@PathVariable Integer id) {
        log.info("вызван метод getReviewById, id: " + id);
        return reviewService.getReviewById(id);
    }

    @GetMapping("/reviews")
    //Получение всех отзывов по идентификатору фильма, если фильм не указан то все. Если кол-во не указано, то 10.
    public List<Review> getTopReviewsFilm(
            @RequestParam(name = "filmId", defaultValue = "0", required = false) @Positive Integer filmId,
            @RequestParam(name = "count", defaultValue = "10", required = false) @Positive Integer count) {
        log.info("вызван метод getTopReviewsFilm, filmId = " + filmId + ", count= " + count);
        return reviewService.getTopReviewsFilm(filmId, count);
    }

    @PutMapping("/reviews/{id}/like/{userId}") //пользователь ставит лайк отзыву.
    public void addReviewLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("вызван метод addReviewLike, id = " + id + ", userId= " + userId);
        reviewService.addReviewLike(id, userId);
    }

    @PutMapping("/reviews/{id}/dislike/{userId}") //пользователь ставит дизлайк отзыву.
    public void addReviewDislike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("вызван метод addReviewDislike, id = " + id + ", userId= " + userId);
        reviewService.addReviewDislike(id, userId);
    }

    @DeleteMapping("/reviews/{id}/like/{userId}")//пользователь удаляет лайк отзыву.
    public void deleteReviewLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("вызван метод deleteReviewLike, id = " + id + ", userId= " + userId);
        reviewService.deleteReviewLike(id, userId);
    }

    @DeleteMapping("/reviews/{id}/dislike/{userId}") //пользователь удаляет дизлайк отзыву.
    public void deleteReviewDislike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("вызван метод deleteReviewDislike, id = " + id + ", userId= " + userId);
        reviewService.deleteReviewDislike(id, userId);
    }

}
