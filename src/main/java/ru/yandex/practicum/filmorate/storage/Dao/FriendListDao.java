package ru.yandex.practicum.filmorate.storage.Dao;


import java.util.List;

public interface FriendListDao {
    void addFriends(int id, int frendId, boolean status); //добавить в друзья

    List<Integer> chekFienda(int id); // проверяе кто отправил пользвавтелю запрос на дружбу

    void deleteFriends(int id, int friendId); // удалить из друзей


}
