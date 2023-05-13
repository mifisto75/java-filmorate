package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exeptions.NotFound;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
public class UserController {
    private HashMap<Integer, User> users = new HashMap<>();

    private int nextUserId = 1;

    @GetMapping("/users") // получение списка всех пользователей.
    public Collection<User> allUsers() {
        log.info("всего пользывателей {} " + users.size());
        return users.values(); // я тут через колекшон получаю список , тоесть через колекцию HasMap беру только значения без ключей


    }

    @PostMapping("/users") // создание пользователя.
    public User addUser(@Valid @RequestBody User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(nextUserId++);
        users.put(user.getId(), user);
        log.info("добавлен новый пользыватель " + user);
        return user;
    }

    @PutMapping("/users") // обновление пользователя.
    public User changeUser(@Valid @RequestBody User user) throws NotFound {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("обнавлён пользыватель " + user);
        } else {
            log.warn("ошибка обновления " + user + " не возможно изменить того чего нет");
            throw new NotFound();
        }
        return user;
    }
}
