package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.Dao.DirectorDao;

import java.util.List;

@Slf4j
@Service
public class DirectorService {

    private final DirectorDao directorDao;

    @Autowired
    public DirectorService(DirectorDao directorDao) {
        this.directorDao = directorDao;
    }

    public List<Director> getAllDirs() {
        return directorDao.getAllDirs();
    }

    public Director getDirById(int id) {
        return directorDao.getDirById(id);
    }

    public Director createDir(Director dir) {
        return directorDao.createDir(dir);
    }

    public Director updateDir(Director dir) {
        return directorDao.updateDir(dir);
    }

    public void deleteDirById(int id) {
        directorDao.deleteDirById(id);
    }
}
