package ru.yandex.practicum.filmorate.Exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotIdExeption extends ResponseStatusException {
    public NotIdExeption(final String message) {
        super(HttpStatus.NOT_FOUND);
    }
}
