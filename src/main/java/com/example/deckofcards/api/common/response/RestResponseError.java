package com.example.deckofcards.api.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

import java.util.List;

public record RestResponseError(List<Error> errors) {
    public record Error(@JsonIgnore HttpStatus httpStatus, String code, String description) {
    }
}
