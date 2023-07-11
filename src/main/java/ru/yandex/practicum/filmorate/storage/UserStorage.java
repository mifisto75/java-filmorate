package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

     List<User> getAllUsers();// получение списка всех пользователей.

     User addUser(User user);// создание пользователя.

     User changeUser(User user);// обновление пользователя.

     User getUserById(int id);// выдача юзера по id

    void userExistenceCheck(int id);// проверка наличия в бд

    void deleteUser(int id);//удаление пользователя

    //есть гиганское желание переименовать этот клас как UserDao а имплементирующий его UserDaoIml
    // и раскидать их по пакетам . соответственно и с калсоми фильмов
}
