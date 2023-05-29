package ru.yandex.practicum.filmorate.Exeptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotFoundException extends IllegalArgumentException {
    public NotFoundException(String message) {
        super(message);
        log.warn(message);
    }
}
