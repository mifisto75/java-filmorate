package ru.yandex.practicum.filmorate.Exeptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationException extends IllegalArgumentException {
    public ValidationException(String message) {
        super(message);
        log.warn(message);

    }
}
