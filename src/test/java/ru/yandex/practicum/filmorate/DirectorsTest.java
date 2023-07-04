package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.Dao.DirectorDao;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class DirectorsTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DirectorDao directorDao;

    private Director dir;
    private Director dir2;

    @BeforeEach
    void dirs() {
        dir = new Director(1, "Director");
        dir2 = new Director(2, "Director");
    }

    @Test
    void createDirTest() throws Exception {
        Director dirDB = directorDao.createDir(dir2);
        assertEquals(dir, dirDB);
    }

    @Test
    void getDirById() {
        directorDao.createDir(dir);
        assertEquals(dir, directorDao.getDirById(1));
    }

    @Test
    void getAllDirs() {
        directorDao.createDir(dir);
        directorDao.createDir(dir2);
        assertEquals(List.of(dir, dir2), directorDao.getAllDirs());
    }

    @Test
    void deleteDir() {
        directorDao.createDir(dir);
        assertEquals(List.of(dir), directorDao.getAllDirs());
        directorDao.deleteDirById(1);
        assertEquals(List.of(), directorDao.getAllDirs());
    }
}
