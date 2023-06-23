package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.Dao.MpaDao;


@Service
public class MpaService {
    public MpaDao mpaDao;

    public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    //этот класс нужен для сохранения архетектуры REST
}
