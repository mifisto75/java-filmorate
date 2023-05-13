package ru.yandex.practicum.filmorate.Exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WeriOldFilmException extends ResponseStatusException {
    public WeriOldFilmException(final String message) {
        super(HttpStatus.BAD_REQUEST);
    }
}
