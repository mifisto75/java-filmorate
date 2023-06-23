package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;

public interface UserStorage {
    public ArrayList allUsers();// получение списка всех пользователей.

    public User addUser(User user);// создание пользователя.

    public User changeUser(User user);// обновление пользователя.

    public User getUserId(int id);// выдача юзера по id

    //есть гиганское желание переименовать этот клас как UserDao а имплементирующий его UserDaoIml
    // и раскидать их по пакетам . соответственно и с калсоми фильмов
}
