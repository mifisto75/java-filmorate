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
    public void addFriends(int id, int frendId) {
        boolean status = userStorage.getUserId(frendId).getFriends().contains(id);
        friendListDao.addFriends(id, frendId, status);
    }

    //DELETE /users/{id}/friends/{friendId} — удаление из друзей.
    public void deleteFriends(int id, int friendId) {
        friendListDao.deleteFriends(id, friendId);
    }

    //GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
    public List<User> getListUser(int id) { // првевращаем список id заявок в список юзеров
        List<User> friends = friendListDao.chekFienda(id).stream()
                .mapToInt(Integer::valueOf)
                .mapToObj(userStorage::getUserId)
                .collect(Collectors.toList());
        return friends;
    }

    //GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
    public List<User> getMutualFriends(int id, int otherId) {
        List<User> user1 = getListUser(id);
        List<User> user2 = getListUser(otherId);
        user1.retainAll(user2);
        return user1;
    }


}
