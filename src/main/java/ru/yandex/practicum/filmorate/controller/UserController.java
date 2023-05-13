package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exeptions.NotIdExeption;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
@Slf4j
@RestController
public class UserController {
    private HashMap<Integer,User> Users = new HashMap<>() ;

    private int nextUserId = 1 ;

    @GetMapping("/users") // получение списка всех пользователей.
    public Collection<User> allUsers() {
        log.info("всего пользывателей {} "+Users.size());
        return Users.values();


    }
    @PostMapping("/users") // создание пользователя.
    public User addUser(@Valid @RequestBody User user) {
        if (user.getName() == null){
            user.setName(user.getLogin());
        }
        user.setId(nextUserId++);
        Users.put(user.getId(),user);
        log.info("добавлен новый пользыватель "+user);
        return user;
    }
    @PutMapping("/users") // обновление пользователя.
    public User changeUser(@Valid @RequestBody User user) throws NotIdExeption {
        if (Users.containsKey(user.getId())){
            Users.put(user.getId(),user);
            log.info("обнавлён пользыватель "+user);
        }else {
            log.warn("ошибка обновления "+user + " не возможно изменить того чего нет");
            throw new NotIdExeption("not found");
        }
        return user;
    }
}
