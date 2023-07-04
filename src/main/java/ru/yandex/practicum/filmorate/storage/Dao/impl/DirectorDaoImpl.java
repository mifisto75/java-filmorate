package ru.yandex.practicum.filmorate.storage.Dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.Dao.DirectorDao;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class DirectorDaoImpl implements DirectorDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DirectorDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    public Director createDir(Director dir) {
        String sqlQuery = "INSERT INTO directors (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, dir.getName());
            return stmt;
        }, keyHolder);
        int id = keyHolder.getKey().intValue();
        final Director dirDB = jdbcTemplate.queryForObject("SELECT * FROM directors WHERE id = ?",
                new DirectorMapper(), id);
        log.debug("В базе создан режиссер: {}", dirDB);
        return dirDB;
    }

    public Director getDirById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM directors WHERE id = ?",
                    new DirectorMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Режиссер по id = %d не найден", id));
        }
    }

    public List<Director> getAllDirs() {
        return jdbcTemplate.query("SELECT * FROM directors", new DirectorMapper());
    }

    public Director updateDir(Director dir) {
        int count = jdbcTemplate.update("UPDATE directors SET id = ?, name = ? WHERE id = ?",
                dir.getId(), dir.getName(), dir.getId());
        if (count == 0) {
            throw new NotFoundException(String.format("Ошибка обновления режиссера - id: %d не существует",
                    dir.getId()));
        } else {
            log.debug("В базе обновлен режиссер: {}", dir);
            return getDirById(dir.getId());
        }
    }

    public void deleteDirById(int id) {
        final Director dir = getDirById(id);
        int count = jdbcTemplate.update("DELETE FROM directors WHERE id = ?", id);
        if (count == 0) {
            throw new NotFoundException(String.format("Ошибка удаления режиссера - id: %d не существует", id));
        } else {
            log.debug("В базе удален режиссер: {}", dir);
        }
    }

    public void updateDirectors(int filmId, Set<Director> dirs) {
        jdbcTemplate.update("DELETE FROM film_directors WHERE film_id = ?", filmId);
        addFilmDirectors(filmId, dirs);
    }

    public void addFilmDirectors(int filmId, Set<Director> dirs) {
        for (Director dir : dirs) {
            jdbcTemplate.update("INSERT INTO film_directors (director_id, film_id) VALUES (?, ?)",
                    dir.getId(), filmId);
        }
    }

    public Set<Director> getFilmDirectors(int filmId) {
        return new HashSet<>(jdbcTemplate.query("SELECT * FROM directors WHERE id IN " +
                        "(SELECT director_id FROM film_directors WHERE film_id = ?)",
                new DirectorMapper(), filmId));
    }

    private static class DirectorMapper implements RowMapper<Director> {
        @Override
        public Director mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Director(rs.getInt("id"), rs.getString("name"));
        }
    }


}
