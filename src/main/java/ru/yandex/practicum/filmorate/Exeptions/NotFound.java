package ru.yandex.practicum.filmorate.Exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFound extends ResponseStatusException {
    public NotFound() {
        super(HttpStatus.NOT_FOUND);
    }
}
