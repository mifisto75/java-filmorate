package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.Exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;

@Component
public class InMemoryUserStorage implements UserStorage {

    private HashMap<Integer, User> users = new HashMap<>();
    private int nextUserId = 1;


    public ArrayList allUsers() { // получение списка всех пользователей.
        return new ArrayList<>(users.values());
    }

    public User addUser(User user) { // создание пользователя.
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(nextUserId++);
        users.put(user.getId(), user);
        return user;
    }

    public User changeUser(User user) { // обновление пользователя.
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("по вашему id не был найден пользыатель");
        }
        users.put(user.getId(), user);
        return user;
    }

    public User getUserId(int id) { //вернуть юзера по id
        if (!users.containsKey(id)) {
            throw new NotFoundException("по вашему id не был найден пользыатель");
        }
        return users.get(id);
    }

}
