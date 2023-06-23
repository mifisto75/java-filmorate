package ru.yandex.practicum.filmorate.storage.Dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.Dao.GenreDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static java.lang.String.format;

@Service
@Component
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreId(int id) {
        try {
            return jdbcTemplate.queryForObject(format("SELECT * FROM genres WHERE genre_id=%d", id), new GenreMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("жанр не найден  не верный id " + id);
        }
    }

    @Override
    public List<Genre> allGenre() {
        return jdbcTemplate.query("SELECT * FROM genres ORDER BY genre_id", new GenreMapper());
    }


    public static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            Genre genre = new Genre();
            genre.setId(rs.getInt("genre_id"));
            genre.setName(rs.getString("name"));
            return genre;
        }
    }
}

