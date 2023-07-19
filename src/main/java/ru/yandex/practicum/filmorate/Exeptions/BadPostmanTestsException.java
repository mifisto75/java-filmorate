package ru.yandex.practicum.filmorate.Exeptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;

@Slf4j
public class BadPostmanTestsException extends DuplicateKeyException {
    public BadPostmanTestsException(String message) {
        super(message);
        log.warn(message);
    }
}
