package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exeptions.NotFound;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@RestController
public class UserController {
    private HashMap<Integer, User> users = new HashMap<>();

    private int nextUserId = 1;

    @GetMapping("/users") // получение списка всех пользователей.
    public ArrayList allUsers() {
        log.info("всего пользователей {} " + users.size());
        return  new ArrayList<>(users.values());


    }

    @PostMapping("/users") // создание пользователя.
    public User addUser(@Valid @RequestBody User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(nextUserId++);
        users.put(user.getId(), user);
        log.info("добавлен новый пользователь " + user);
        return user;
    }

    @PutMapping("/users") // обновление пользователя.
    public User changeUser(@Valid @RequestBody User user) throws NotFound {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("обновлен пользователь " + user);
        } else {
            log.warn("ошибка обновления " + user + " не возможно изменить того чего нет");
            throw new NotFound();
        }
        return user;
    }
}
