package com.example.deckofcards.api.common.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends ApplicationException {

    public EntityNotFoundException(final String message) {
        super(HttpStatus.NOT_FOUND, "NOT_FOUND", message);
    }
}
