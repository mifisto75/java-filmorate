package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Dao.FriendListDao;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    public UserStorage userStorage;
    public FriendListDao friendListDao;

    public UserService(UserStorage userStorage, FriendListDao friendListDao) {
        this.userStorage = userStorage;
        this.friendListDao = friendListDao;

    }

    //PUT /users/{id}/friends/{friendId} — добавление в друзья.
    public void addFriends(int id, int friendId) {
        userStorage.userExistenceCheck(id);
        boolean status = userStorage.getUserId(friendId).getFriends().contains(id);
        friendListDao.addFriends(id, friendId, status);
    }

    //DELETE /users/{id}/friends/{friendId} — удаление из друзей.
    public void deleteFriends(int id, int friendId) {
        friendListDao.deleteFriends(id, friendId);
    }

    //GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
    public List<User> getUserFriends(int id) { // првевращаем список id заявок в список юзеров
        userStorage.userExistenceCheck(id);
        return friendListDao.checkFienda(id).stream()
                .mapToInt(Integer::valueOf)
                .mapToObj(userStorage::getUserId)
                .collect(Collectors.toList());
    }

    //GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
    public List<User> getMutualFriends(int id, int otherId) {
        List<User> user1 = getUserFriends(id);
        List<User> user2 = getUserFriends(otherId);
        user1.retainAll(user2);
        return user1;
    }


}
