package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String eventType;

    @NotNull(message = "операция не может быть пустой")
    private String operation;

    @Positive(message = "id события не может быть отрицательным")
    private int eventId;

    @Positive(message = "entityId не может быть отрицательным")
    private int entityId;

    public Event(Long timestamp, int userId, String eventType, String operation, int entityId) {
        this.timestamp = timestamp;
        this.userId = userId;
        this.eventType = eventType;
        this.operation = operation;
        this.entityId = entityId;
    }
}
