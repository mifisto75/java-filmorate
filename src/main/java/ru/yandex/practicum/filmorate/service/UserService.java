package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    //PUT /users/{id}/friends/{friendId} — добавление в друзья.
    public void addFrends(int id, int frendId) {
        if (userStorage.getUserId(id) != null) {
            if (userStorage.getUserId(frendId) != null) {
                userStorage.getUserId(id).getFreands().add(frendId);
                userStorage.getUserId(frendId).getFreands().add(id);
            } else {
                throw new NotFoundException("пользывателя под id " + frendId + "нет");
            }

        } else {
            throw new NotFoundException("ваш id не верен " + id);
        }

    }

    //DELETE /users/{id}/friends/{friendId} — удаление из друзей.
    public void deleteFrends(int id, int frendId) {
        if (userStorage.getUserId(id) != null) {
            if (userStorage.getUserId(frendId) != null) {
                if (userStorage.getUserId(id).getFreands().contains(frendId)) {
                    userStorage.getUserId(id).getFreands().remove(frendId);
                    userStorage.getUserId(frendId).getFreands().remove(id);
                } else {
                    throw new NotFoundException("у вас и так нет этого пользователя в друзьях " + frendId);
                }
            } else {
                throw new NotFoundException("пользователя которого вы хотите удалить нету " + frendId);
            }

        } else {
            throw new NotFoundException("ваш id не верен " + id);
        }

    }

    //GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
    public List<User> getListUser(int id) {
        List<User> ferndUser = new ArrayList<>();
        if (userStorage.getUserId(id) != null) {
            if (userStorage.getUserId(id).getFreands() != null) {
                for (Integer users : userStorage.getUserId(id).getFreands()) {
                    ferndUser.add(userStorage.getUserId(users));
                }
            }
        } else {
            throw new NotFoundException("ваш id не верен " + id);
        }
        return ferndUser;
    }

    //GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
    public List<User> getMutualFriends(int id, int otherId) {
        List<User> mutualFriends = new ArrayList<>();
        if (userStorage.getUserId(id) != null) {
            if (userStorage.getUserId(otherId) != null) {
                Set<Integer> userFrends = new HashSet<>(userStorage.getUserId(id).getFreands());
                userFrends.retainAll(userStorage.getUserId(otherId).getFreands());
                for (Integer user : userFrends) {
                    mutualFriends.add(userStorage.getUserId(user));
                }
            } else {
                throw new NotFoundException("вы указали неверный id пользователя " + id);
            }

        } else {
            throw new NotFoundException("ваш id не верен " + id);
        }
        return mutualFriends;
    }

}
