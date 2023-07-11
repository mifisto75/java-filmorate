package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping //Получение всех режиссеров
    public List<Director> getAllDirs() {
        log.debug("Получен запрос GET (getAllDirs)");
        final List<Director> dirs = directorService.getAllDirs();
        log.debug("Получен ответ GET (getAllDirs) dirs: {}", dirs);
        return dirs;
    }

    @GetMapping(value = "/{id}") //Получение режиссера по id
    public Director getDirById(@PathVariable("id") @Min(1) int id) {
        log.debug("Получен запрос GET (getDirById)");
        final Director dir = directorService.getDirById(id);
        log.debug("Получен ответ GET (getDirById) dir: {}", dir);
        return dir;
    }

    @PostMapping //Создание режиссера
    public Director createDir(@RequestBody @Valid Director dir) {
        log.debug("Получен запрос POST (createDir)");
        final Director dirDB = directorService.createDir(dir);
        log.debug("Получен ответ POST (createDir) dir: {}", dirDB);
        return dirDB;
    }

    @PutMapping //Изменение режиссера
    public Director updateDir(@RequestBody @Valid Director dir) {
        log.debug("Получен запрос PUT (updateDir)");
        final Director dirDB = directorService.updateDir(dir);
        log.debug("Получен ответ PUT (updateDir) dir: {}", dirDB);
        return dirDB;
    }

    @DeleteMapping(value = "/{id}") //Удаление режиссёра
    public void deleteDirById(@PathVariable("id") @Min(1) int id) {
        log.debug("Получен запрос DELETE (deleteDirById)");
        directorService.deleteDirById(id);
        log.debug("Режиссер успешно удален");
    }
}
