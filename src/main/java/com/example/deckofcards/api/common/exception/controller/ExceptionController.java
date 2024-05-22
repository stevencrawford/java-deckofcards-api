package com.example.deckofcards.api.common.exception.controller;

import com.example.deckofcards.api.common.exception.ApplicationException;
import com.example.deckofcards.api.common.response.RestResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = ApplicationException.class)
    public ResponseEntity<RestResponseError> handleApplication(ApplicationException exception) {
        return response(exception);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<RestResponseError> handleAll(Exception exception) {
        return response(exception);
    }

    private ResponseEntity<RestResponseError> response(ApplicationException exception) {
        var error = exception.getError();
        var restResponseError = new RestResponseError(List.of(error));
        return new ResponseEntity<>(restResponseError, error.httpStatus());
    }

    private ResponseEntity<RestResponseError> response(Exception exception) {
        var error = switch (exception) {
            case IllegalArgumentException e -> new RestResponseError.Error(HttpStatus.BAD_REQUEST, "BAD_REQUEST", e.getMessage());
            // Add more here ...
            default -> new RestResponseError.Error(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", exception.getMessage());
        };
        var restResponseError = new RestResponseError(List.of(error));
        return new ResponseEntity<>(restResponseError, error.httpStatus());
    }

}
