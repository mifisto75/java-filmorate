package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@RestController
public class MpaController {
    private final MpaService mpaService;


    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping("/mpa")
    public List<Mpa> allMpa() {
        log.info("вызван метод allMpa - запрос на список всех MPA");
        return mpaService.mpaDao.allMpa();
    }

    @GetMapping("mpa/{id}")
    public Mpa getMpaId(@PathVariable int id) {
        log.info("вызван метод getMpaId - запрос на MPA c ID " + id);
        return mpaService.mpaDao.getMpaId(id);
    }
}
