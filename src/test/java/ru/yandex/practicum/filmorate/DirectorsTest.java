package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.Dao.DirectorDao;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class DirectorsTest {

    private final DirectorDao directorDao;

    private Director dir;
    private Director dir2;

    @Autowired
    public DirectorsTest(DirectorDao directorDao) {
        this.directorDao = directorDao;
    }

    @BeforeEach
    void dirs() {
        dir = new Director(1, "Director");
        dir2 = new Director(5, "Director2");
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql"})
    void createDirTest() throws Exception {
        Director dirDB = directorDao.createDir(dir2);
        assertEquals(new Director(1, "Director2"), dirDB);
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql"})
    void getDirById() {
        directorDao.createDir(dir);
        assertEquals(dir, directorDao.getDirById(1));
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql"})
    void getAllDirs() {
        directorDao.createDir(dir);
        directorDao.createDir(dir2);
        dir2.setId(2);
        assertEquals(List.of(dir, dir2), directorDao.getAllDirs());
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql"})
    void deleteDir() {
        directorDao.createDir(dir);
        assertEquals(List.of(dir), directorDao.getAllDirs());
        directorDao.deleteDirById(1);
        assertEquals(List.of(), directorDao.getAllDirs());
    }
}
