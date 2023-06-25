package ru.yandex.practicum.filmorate.storage.Dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.FriendList;
import ru.yandex.practicum.filmorate.storage.Dao.FriendListDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@Component
public class FriendListDaoImpl implements FriendListDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendListDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addFriends(int id, int friendId, boolean status) {
        try {
            jdbcTemplate.update("INSERT INTO user_friend_list (from_user_id, to_user_id, boolean_status) "
                    + "VALUES(?, ?, ?)", friendId, id, status);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("не верный id польщывателя " + id + " или " + friendId);
        }
    }

    public List<Integer> checkFienda(int id) {
        try {
            List<Integer> checkFiendaList = jdbcTemplate.query(format("SELECT from_user_id, to_user_id, boolean_status "
                            + "FROM user_friend_list WHERE to_user_id=%d", id), new FriendMapper())
                    .stream()
                    .map(FriendList::getFromUserId)//выбираем только тех кто отправил нам запрос
                    .collect(Collectors.toList());
            return checkFiendaList;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("не верный id пользывателя " + id);
        }
    }

    public void deleteFriends(int id, int friendId) {
        try {
            jdbcTemplate.update("DELETE FROM user_friend_list WHERE from_user_id=? "
                    + "AND to_user_id=?", friendId, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("не верный id пользывателя ");
        }
    }


    private static class FriendMapper implements RowMapper<FriendList> {
        @Override
        public FriendList mapRow(ResultSet rs, int rowNum) throws SQLException {
            FriendList friendList = new FriendList();
            friendList.setFromUserId(rs.getInt("from_user_id"));
            friendList.setToUserId(rs.getInt("to_user_id"));
            friendList.setStatus(rs.getBoolean("boolean_status"));
            return friendList;
        }
    }
}
