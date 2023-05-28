package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    //PUT /users/{id}/friends/{friendId} — добавление в друзья.
    public void addFriends(int id, int frendId) {
        if (userStorage.getUserId(id) != null) {
            if (userStorage.getUserId(frendId) != null) {
                userStorage.getUserId(id).getFriends().add(frendId);
                userStorage.getUserId(frendId).getFriends().add(id);
            } else {
                throw new NotFoundException("пользывателя под id " + frendId + "нет");
            }

        } else {
            throw new NotFoundException("ваш id не верен " + id);
        }

    }

    //DELETE /users/{id}/friends/{friendId} — удаление из друзей.
    public void deleteFriends(int id, int friendId) {
        User bosUser = userStorage.getUserId(id);
        User NotFriend = userStorage.getUserId(friendId);
        if (bosUser != null) {
            if (NotFriend != null) {
                if (userStorage.getUserId(id).getFriends().remove(friendId)) {
                    userStorage.getUserId(friendId).getFriends().remove(id);
                } else {
                    throw new NotFoundException("у вас и так нет этого пользователя в друзьях " + friendId);
                }
            } else {
                throw new NotFoundException("пользователя которого вы хотите удалить нету " + friendId);
            }
        } else {
            throw new NotFoundException("ваш id не верен " + id);
        }

    }

    //GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
    public List<User> getListUser(int id) {
        User user = userStorage.getUserId(id);
        List<User> friendUsers = new ArrayList<>();
        if (user != null) {
            if (user.getFriends().size() == 0) {
                throw new NotFoundException("ваш id не верен " + id);
            }
        }
            return friendUsers = user.getFriends().stream().map(u -> userStorage.getUserId(u)).collect(Collectors.toList());
        }


    //GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
    public List<User> getMutualFriends(int id, int otherId) {
        User bosUser = userStorage.getUserId(id);
        User friend = userStorage.getUserId(otherId);
        List<User> mutualFriends = new ArrayList<>();
        Set<Integer> userFriends;
        if (bosUser != null) {
            if (friend != null) {
                userFriends = new HashSet<>(bosUser.getFriends());
                userFriends.retainAll(friend.getFriends());
            } else {
                throw new NotFoundException("вы указали неверный id пользователя " + id);
            }
        } else {
            throw new NotFoundException("ваш id не верен " + id);
        }
        return mutualFriends = userFriends.stream().map(x -> userStorage.getUserId(x)).collect(Collectors.toList());
    }

}
