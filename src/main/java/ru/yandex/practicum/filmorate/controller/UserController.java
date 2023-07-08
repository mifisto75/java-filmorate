package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.Dao.EventDao;

import javax.validation.Valid;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;
    private final FilmService filmService;
    private final EventDao eventDao;

    @Autowired
    public UserController(UserService userService, FilmService filmService, EventDao eventDao) {
        this.userService = userService;
        this.filmService = filmService;
        this.eventDao = eventDao;
    }

    @GetMapping("/users") // получение списка всех пользователей.
    public ArrayList allUsers() {
        log.info("вызван метод allUsers - запрос на список всех пользователей");
        return userService.userStorage.allUsers();
    }

    @GetMapping("/users/{id}") //вернуть юзера по id.
    public User getUserId(@PathVariable Integer id) {
        log.info("вызван метод getUserId - запрос на пользователя с ID " + id);
        return userService.userStorage.getUserId(id);
    }

    @PostMapping("/users") // создание пользователя.
    public User addUser(@Valid @RequestBody User user) {
        log.info("вызван метод addUser - запрос на добавление пользователя " + user);
        return userService.userStorage.addUser(user);
    }

    @PutMapping("/users") // обновление пользователя.
    public User changeUser(@Valid @RequestBody User user) throws NotFoundException {
        log.info("вызван метод changeUser - запрос на обновление пользователя " + user);
        return userService.userStorage.changeUser(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}") // добавить в друзья
    public void addFrends(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("вызван метод addFrends - запрос на добовление в друзья пользывателем c id " + id
                + " пользывателя с id " + friendId);
        userService.addFriends(id, friendId);
        eventDao.addEvent(new Event(Instant.now().toEpochMilli(), id, "FRIEND",
                "ADD", friendId));
    }

    @DeleteMapping("/users/{id}/friends/{friendId}") // удалить из друзей
    public void deleteFrends(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("вызван метод deleteFrends - запрос на удаление из друзей пользывателем c id " + id
                + " пользывателя с id " + friendId);
        userService.deleteFriends(id, friendId);
        eventDao.addEvent(new Event(Instant.now().toEpochMilli(), id, "FRIEND",
                "REMOVE", friendId));
    }

    @GetMapping("/users/{id}/friends") // список друзей
    public List<User> getListUser(@PathVariable Integer id) {
        log.info("вызван метод getListUser - запрос на список друзей пользывателя " + id);
        return userService.getUserFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}") // список общих друзей
    public List<User> getMutualFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("вызван метод getMutualFriends - запрос на список общих друзей пользывателем c id " + id + " пользывателя с id " + otherId);
        return userService.getMutualFriends(id, otherId);
    }

    @GetMapping("/users/{id}/recommendations") // рекомендация для пользователя по id
    public List<Film> getFilmRecommendations(@PathVariable int id) {
        log.info("вызван метод getFilmRecommendations для пользователя с id: {}", id);
        return filmService.getFilmRecommendations(id);
    }

    @GetMapping("/users/{id}/feed") //вернуть список последних действий пользователя по id.
    public List<Event> getUserFeed(@PathVariable Integer id) {
        log.info("вызван метод getUserFeed - запрос на действия пользователя с ID " + id);
        return eventDao.getUserEvents(id);
    }
}
