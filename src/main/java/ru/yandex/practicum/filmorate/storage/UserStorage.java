package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;

public interface UserStorage {
    public ArrayList allUsers();// получение списка всех пользователей.
    public User addUser(User user);// создание пользователя.
    public User changeUser(User user);// обновление пользователя.
    public User getUserId(int id);// выдача юзера по id
}
