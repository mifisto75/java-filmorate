package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping("/users") // получение списка всех пользователей.
    public ArrayList allUsers() {
        log.info("вызван метод allUsers - запрос на список всех пользователей");
        return userStorage.allUsers();
    }

    @GetMapping("/users/{id}") //вернуть юзера по id.
    public User getUserId(@PathVariable Integer id) {
        log.info("вызван метод getUserId - запрос на пользователя с ID " + id);
        return userStorage.getUserId(id);
    }

    @PostMapping("/users") // создание пользователя.
    public User addUser(@Valid @RequestBody User user) {
        log.info("вызван метод addUser - запрос на добавление пользователя " + user);
        return userStorage.addUser(user);
    }

    @PutMapping("/users") // обновление пользователя.
    public User changeUser(@Valid @RequestBody User user) throws NotFoundException {
        log.info("вызван метод changeUser - запрос на обновление пользователя " + user);
        return userStorage.changeUser(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}") // добавить в друзья
    public void addFrends(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("вызван метод addFrends - запрос на добовление в друзья пользывателем c id " + id + " пользывателя с id " + friendId);
        userService.addFrends(id, friendId);

    }

    @DeleteMapping("/users/{id}/friends/{friendId}") // удалить из друзей
    public void deleteFrends(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("вызван метод deleteFrends - запрос на удаление из друзей пользывателем c id " + id + " пользывателя с id " + friendId);
        userService.deleteFrends(id, friendId);
    }

    @GetMapping("/users/{id}/friends") // список друзей
    public List<User> getListUser(@PathVariable Integer id) {
        log.info("вызван метод getListUser - запрос на список друзей пользывателя " + id);
        return userService.getListUser(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}") // список общих друзей
    public List<User> getMutualFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("вызван метод getMutualFriends - запрос на список общих друзей пользывателем c id " + id + " пользывателя с id " + otherId);
        return userService.getMutualFriends(id, otherId);
    }
}
