package ru.yandex.practicum.filmorate.storage.Dao;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Set;

public interface DirectorDao {

    Director createDir(Director dir); //возвращает id директора

    Director getDirById(int id);

    List<Director> getAllDirs();

    Director updateDir(Director dir); //или по id, потом решу

    void deleteDirById(int id);

    void updateDirectors(int filmId, Set<Director> directors);

    void addFilmDirectors(int filmId, Set<Director> dirs);

    Set<Director> getFilmDirectors(int filmId);
}
