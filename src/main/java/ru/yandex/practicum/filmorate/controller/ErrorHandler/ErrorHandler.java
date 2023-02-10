package ru.yandex.practicum.filmorate.controller.ErrorHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.PostNotFoundException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;


@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlePostNotFoundException(final PostNotFoundException e) {
        log.error("Ошибка, невозможно найти объект", e);
        return new ErrorResponse(
                e.getMessage()
        );
    }
}
