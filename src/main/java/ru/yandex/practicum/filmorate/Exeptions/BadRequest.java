package ru.yandex.practicum.filmorate.Exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BadRequest extends ResponseStatusException {
    public BadRequest() {
        super(HttpStatus.BAD_REQUEST);
    }
}
