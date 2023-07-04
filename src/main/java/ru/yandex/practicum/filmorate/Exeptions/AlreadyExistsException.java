package ru.yandex.practicum.filmorate.Exeptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlreadyExistsException extends IllegalArgumentException{
    public AlreadyExistsException(String message) {
        super(message);
        log.warn(message);
    }
}
