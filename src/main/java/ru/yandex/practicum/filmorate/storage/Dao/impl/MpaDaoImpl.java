package ru.yandex.practicum.filmorate.storage.Dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.Dao.MpaDao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
@Service
@Component
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpaId(int id) {
        try {
            return jdbcTemplate.queryForObject(format("SELECT * FROM mpa_ratings WHERE rating_id=%d", id), new MpaMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("рейтинг не найден не верный id " + id);
        }
    }

    @Override
    public List<Mpa> allMpa() {
        return  new ArrayList<>(jdbcTemplate.query("SELECT * FROM mpa_ratings ORDER BY rating_id", new MpaMapper()));
    }

    private static class MpaMapper implements RowMapper<Mpa> {
        @Override
        public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
            Mpa map = new Mpa();
           map.setId(rs.getInt("rating_id"));
           map.setName(rs.getString("name"));
            return map;
            }
    }
}