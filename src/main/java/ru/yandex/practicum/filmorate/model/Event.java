package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.storage.Dao.impl.EventDaoImpl;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @NotNull(message = "метка времени не может быть пустой")
    private Long timestamp;

    @Positive(message = "id пользователя не может быть отрицательным")
    private int userId;

    @NotNull(message = "тип события не может быть пустым")
    private EventType eventType;

    @NotNull(message = "операция не может быть пустой")
    private OperationType operation;

    @Positive(message = "id события не может быть отрицательным")
    private int eventId;

    @Positive(message = "entityId не может быть отрицательным")
    private int entityId;

    public Event(Long timestamp, int userId, EventType eventType, OperationType operation, int entityId) {
        this.timestamp = timestamp;
        this.userId = userId;
        this.eventType = eventType;
        this.operation = operation;
        this.entityId = entityId;
    }

    @Override
    public String toString() {
        return "{" +
                "\"timestamp\":" + timestamp +
                ", \"userId\":" + userId +
                ", \"eventType\": \"" + eventType.name() +
                "\", \"operation\": \"" + operation.name() +
                "\", \"eventId\":" + eventId +
                ", \"entityId\":" + entityId +
                '}';
    }
    /*
    [
    {
        "timestamp": 123344556,
            "userId": 123,
            "eventType": "LIKE", // одно из значениий LIKE, REVIEW или FRIEND
            "operation": "REMOVE", // одно из значениий REMOVE, ADD, UPDATE
            "eventId": 1234, //primary key
            "entityId": 1234   // идентификатор сущности, с которой произошло событие
    }
] */
}
