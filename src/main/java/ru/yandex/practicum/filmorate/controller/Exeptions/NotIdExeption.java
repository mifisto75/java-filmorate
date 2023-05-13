package ru.yandex.practicum.filmorate.controller.Exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotIdExeption extends ResponseStatusException {
    public NotIdExeption(){
        super(HttpStatus.NOT_FOUND);
    }
}
