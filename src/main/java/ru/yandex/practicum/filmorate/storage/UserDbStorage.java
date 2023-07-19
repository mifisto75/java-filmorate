package ru.yandex.practicum.filmorate.storage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAllUsers() { // получение списка всех пользователей.
        return new ArrayList<>(jdbcTemplate.query("SELECT * FROM users", new UserMapper()));
    }

    @Override
    public User addUser(User user) { // создание пользователя.
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        jdbcTemplate.update("INSERT INTO users (email , login , name , birthday) VALUES (? , ? , ? , ?)",
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        return jdbcTemplate.queryForObject(format("SELECT * FROM users WHERE email='%s'", user.getEmail()), new UserMapper());
    }

    public User changeUser(User user) { // обновление пользователя.
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        jdbcTemplate.update("UPDATE users SET email=?, login=?, name=?, birthday=? WHERE user_id=?",
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return getUserById(user.getId());
    }

    public User getUserById(int id) { // выдача юзера по id
        userExistenceCheck(id);
            User user = jdbcTemplate.queryForObject(format("SELECT * FROM users WHERE user_id=%d", id), new UserMapper());
            return user;
    }

    public void userExistenceCheck(int id) { // приметивная проверка на наличие пользывателя в бд
        try {
            User user = jdbcTemplate.queryForObject(format("SELECT * FROM users WHERE user_id=%d", id), new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("по вашему id " + id + " не был найден пользыатель ");
        }
    }

    @Override
    public void deleteUser(int userId) {
        try {
            jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", userId);
            jdbcTemplate.update("DELETE FROM user_friend_list WHERE from_user_id=? or to_user_id=?", userId, userId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("неверный id пользователя, id: " + userId);
        }
    }

    public static class UserMapper implements RowMapper<User> { //мапер для юзеров
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("user_id"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setName(rs.getString("name"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());
            return user;
        }
    }
}


