package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;

public interface UserStorage {
     ArrayList allUsers();// получение списка всех пользователей.

     User addUser(User user);// создание пользователя.

     User changeUser(User user);// обновление пользователя.

     User getUserId(int id);// выдача юзера по id

    void userExistenceCheck(int id);// проверка наличия в бд

    //есть гиганское желание переименовать этот клас как UserDao а имплементирующий его UserDaoIml
    // и раскидать их по пакетам . соответственно и с калсоми фильмов
}
