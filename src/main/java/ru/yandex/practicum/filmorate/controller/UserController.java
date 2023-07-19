package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.RecommendationService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;
    private final RecommendationService recommendationService;

    @Autowired
    public UserController(UserService userService, RecommendationService recommendationService) {
        this.userService = userService;
        this.recommendationService = recommendationService;
    }

    @GetMapping("/users") // получение списка всех пользователей.
    public List<User> getAllUsers() {
        log.info("вызван метод getAllUsers");
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}") //вернуть user по id.
    public User getUserById(@PathVariable Integer id) {
        log.info("вызван метод getUserById, id: " + id);
        return userService.getUserById(id);
    }

    @PostMapping("/users") // создание пользователя.
    public User addUser(@Valid @RequestBody User user) {
        log.info("вызван метод addUser, user: " + user);
        return userService.addUser(user);
    }

    @PutMapping("/users") // обновление пользователя.
    public User changeUser(@Valid @RequestBody User user) {
        log.info("вызван метод changeUser, user" + user);
        return userService.changeUser(user);
    }

    @DeleteMapping("/users/{userId}") // удаление пользователя
    public void deleteUserById(@PathVariable Integer userId) {
        log.info("вызван метод deleteUserById, id: " + userId);
        userService.deleteUserById(userId);
    }

    @PutMapping("/users/{id}/friends/{friendId}") // добавить в друзья
    public void addFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("вызван метод addFriends - запрос на добавление в друзья пользователем c id " + id
                + " пользователя с id " + friendId);
        userService.addFriends(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}") // удалить из друзей
    public void deleteFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("вызван метод deleteFriends - запрос на удаление из друзей пользователем c id " + id
                + " пользователя с id " + friendId);
        userService.deleteFriends(id, friendId);
    }

    @GetMapping("/users/{id}/friends") // список друзей
    public List<User> getUserFriendsById(@PathVariable Integer id) {
        log.info("вызван метод getUserFriendsById, id: " + id);
        return userService.getUserFriendsById(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}") // список общих друзей
    public List<User> getMutualFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("вызван метод getMutualFriends - запрос на список общих друзей пользователем c id " + id
                + " пользователя с id " + otherId);
        return userService.getMutualFriends(id, otherId);
    }

    @GetMapping("/users/{id}/recommendations") // рекомендация для пользователя по id
    public List<Film> getFilmRecommendations(@PathVariable int id) {
        log.info("вызван метод getFilmRecommendations для пользователя с id: {}", id);
        return recommendationService.getRecommendedFilms(id);
    }

    @GetMapping("/users/{id}/feed") //вернуть список последних действий пользователя по id.
    public List<Event> getUserFeed(@PathVariable Integer id) {
        log.info("вызван метод getUserFeed - запрос на действия пользователя с ID " + id);
        return userService.getUserEvents(id);
    }
}
