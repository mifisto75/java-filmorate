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
        return reviewService.addReview(review);
    }

    @PutMapping("/reviews") //Редактирование уже имеющегося отзыва.
    public Review changeReview(@Valid @RequestBody Review review) {
        return reviewService.changeReview(review);
    }

    @DeleteMapping("/reviews/{id}") //Удаление уже имеющегося отзыва.
    public void deleteReview(@PathVariable Integer id) {
        reviewService.deleteReview(id);
    }

    @GetMapping("/reviews/{id}") //Получение отзыва по идентификатору.
    public Review getReview(@PathVariable Integer id) {
        return reviewService.getReview(id);
    }

    @GetMapping("/reviews")
    //Получение всех отзывов по идентификатору фильма, если фильм не указан то все. Если кол-во не указано то 10.
    public List<Review> getTopReviewsFilm(
            @RequestParam(name = "filmId", defaultValue = "0", required = false) @Positive Integer filmId,
            @RequestParam(name = "count", defaultValue = "10", required = false) @Positive Integer count) {
        return reviewService.getTopReviewsFilm(filmId, count);
    }

    @PutMapping("/reviews/{id}/like/{userId}") //пользователь ставит лайк отзыву.
    public void addReviewLike(@PathVariable Integer id, @PathVariable Integer userId) {
        reviewService.addReviewLike(id, userId);
    }

    @PutMapping("/reviews/{id}/dislike/{userId}") //пользователь ставит дизлайк отзыву.
    public void addReviewDislike(@PathVariable Integer id, @PathVariable Integer userId) {
        reviewService.addReviewDislike(id, userId);
    }

    @DeleteMapping("/reviews/{id}/like/{userId}")//пользователь удаляет лайк отзыву.
    public void deleteReviewLike(@PathVariable Integer id, @PathVariable Integer userId) {
        reviewService.deleteReviewLike(id, userId);
    }

    @DeleteMapping("/reviews/{id}/dislike/{userId}") //пользователь удаляет дизлайк отзыву.
    public void deleteReviewDislike(@PathVariable Integer id, @PathVariable Integer userId) {
        reviewService.deleteReviewDislike(id, userId);
    }

}
