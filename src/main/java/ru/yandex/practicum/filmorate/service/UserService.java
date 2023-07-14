package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.type.EventType;
import ru.yandex.practicum.filmorate.model.type.OperationType;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Dao.EventDao;
import ru.yandex.practicum.filmorate.storage.Dao.FriendListDao;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;
    private final FriendListDao friendListDao;
    private final EventDao eventDao;


    public UserService(UserStorage userStorage, FriendListDao friendListDao, EventDao eventDao) {
        this.userStorage = userStorage;
        this.friendListDao = friendListDao;
        this.eventDao = eventDao;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Integer id) {
        return userStorage.getUserById(id);
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User changeUser(User user) {
        return userStorage.changeUser(user);
    }

    public void deleteUserById(Integer userId) {
        userStorage.deleteUser(userId);
    }

    //PUT /users/{id}/friends/{friendId} — добавление в друзья.
    public void addFriends(int id, int friendId) {
        userStorage.userExistenceCheck(id);
        boolean status = userStorage.getUserById(friendId).getFriends().contains(id);
        friendListDao.addFriends(id, friendId, status);
        eventDao.addEvent(new Event(Instant.now().toEpochMilli(), id, EventType.FRIEND,
                OperationType.ADD, friendId));
    }

    //DELETE /users/{id}/friends/{friendId} — удаление из друзей.
    public void deleteFriends(int id, int friendId) {
        friendListDao.deleteFriends(id, friendId);
        eventDao.addEvent(new Event(Instant.now().toEpochMilli(), id, EventType.FRIEND,
                OperationType.REMOVE, friendId));
    }

    //GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
    public List<User> getUserFriendsById(int id) { // првевращаем список id заявок в список юзеров
        userStorage.userExistenceCheck(id);
        return friendListDao.checkFienda(id).stream()
                .mapToInt(Integer::valueOf)
                .mapToObj(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    //GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
    public List<User> getMutualFriends(int id, int otherId) {
        List<User> user1 = getUserFriendsById(id);
        List<User> user2 = getUserFriendsById(otherId);
        user1.retainAll(user2);
        return user1;
    }

    public List<Event> getUserEvents(Integer id) {
        return eventDao.getUserEvents(id);
    }

}
