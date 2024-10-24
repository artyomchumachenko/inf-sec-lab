package ru.mai.is.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ru.mai.is.dto.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public final ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("DataIntegrityViolationException: ", ex);

        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            return new ResponseEntity<>(
                    new ErrorResponse("Ошибка: нарушено уникальное ограничение. Данное значение уже существует в базе данных. " + ex.getLocalizedMessage()),
                    HttpStatus.CONFLICT
            );
        }

        return new ResponseEntity<>(
                new ErrorResponse("Ошибка целостности данных: " + ex.getLocalizedMessage()),
                HttpStatus.CONFLICT
        );
    }
}
