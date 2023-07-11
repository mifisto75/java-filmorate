package ru.yandex.practicum.filmorate.storage.Dao;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventDao {
    void addEvent(Event event); //Добавление нового события.

    List<Event> getUserEvents(int userId); //Получение всех событий по идентификатору пользователя,
}
