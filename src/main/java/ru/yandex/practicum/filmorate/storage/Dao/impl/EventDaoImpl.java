package ru.yandex.practicum.filmorate.storage.Dao.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.type.EventType;
import ru.yandex.practicum.filmorate.model.type.OperationType;
import ru.yandex.practicum.filmorate.storage.Dao.EventDao;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import static java.lang.String.format;

@Service
public class EventDaoImpl implements EventDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    @Autowired
    public EventDaoImpl(JdbcTemplate jdbcTemplate, UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    @Override
    public void addEvent(Event event) {
        userStorage.userExistenceCheck(event.getUserId());
        jdbcTemplate.update("INSERT INTO events (time_stamp, user_id, event_type, operation, entity_id)" +
                        " VALUES(?, ?, ?, ?, ?)",
                event.getTimestamp(), event.getUserId(), event.getEventType().name(),
                event.getOperation().name(), event.getEntityId());
    }

    @Override
    public List<Event> getUserEvents(int userId) {
        userStorage.userExistenceCheck(userId);
        return jdbcTemplate.query(format("SELECT * FROM events WHERE user_id='%d'", userId),
                new EventMapper());
    }

    private class EventMapper implements RowMapper<Event> {
        @Override
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
            Event event = new Event();
            event.setTimestamp(rs.getLong("time_stamp"));
            event.setUserId(rs.getInt("user_id"));
            event.setEventType(strToEventType(rs.getString("event_type")));
            event.setOperation(strToOperationType(rs.getString("operation")));
            event.setEventId(rs.getInt("event_id"));
            event.setEntityId(rs.getInt("entity_id"));
            return event;
        }
    }

    public static EventType strToEventType(String strEventType) {
        switch (strEventType) {
            case "LIKE":
                return EventType.LIKE;
            case "REVIEW":
                return EventType.REVIEW;
            case "FRIEND":
                return EventType.FRIEND;
            default:
                throw new IllegalArgumentException("Неверный тип события: " + strEventType);
        }
    }

    public static OperationType strToOperationType(String strOperationType) {
        switch (strOperationType) {
            case "REMOVE":
                return OperationType.REMOVE;
            case "ADD":
                return OperationType.ADD;
            case "UPDATE":
                return OperationType.UPDATE;
            default:
                throw new IllegalArgumentException("Неверный тип операции: " + strOperationType);
        }
    }
}
